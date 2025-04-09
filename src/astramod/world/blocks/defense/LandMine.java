package astramod.world.blocks.defense;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.*;
import astramod.graphics.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class LandMine extends Block {
	public float explodePower = 50f;
	public float explodeRadius = 2.5f;
	public float explodeFire = 0f;
	public float knockback = 0f;

	public StatusEffect status = StatusEffects.none;
	public float statusDuration = 240f;

	public int numLightning = 0;
	public float lightningDamage = 20f;
	public int lightningLength = 10;
	public Color lightningColor = Pal.surge;

	public @Nullable BulletType bullet;
	public int shots = 0;
	public float shotInaccuracy = 0f;

	public float teamAlpha = 0.3f;

	public LandMine(String name) {
		super(name);
		update = false;
		destructible = true;
		solid = false;
		targetable = false;
		underBullets = true;
		squareSprite = false;
		hasShadow = false;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.detonation, AstraStatValues.mine(this, 0));
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id] } : new TextureRegion[] { region };
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		if (tile.floor().isLiquid) return false;

		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof LandMineBuild) return false;
		}
		return true;
	}

	@Override public boolean canReplace(Block other) {
		return other.alwaysReplace || !other.privileged && other != this && other instanceof LandMine;
	}

	@Override public void changePlacementPath(Seq<Point2> points, int rotation) {
		if (points.size > 1) {
			Point2 origin = points.first(), point;
			float maxDist = points.peek().dst(origin);
			for (int i = 1; i < points.size; i++) {
				point = points.get(i).add(Geometry.d4x(rotation) * i * size, Geometry.d4y(rotation) * i * size);
				if (point.dst(origin) > maxDist) {
					points.removeRange(i, points.size - 1);
					return;
				}
			}
		}
	}

	@Override public int minimapColor(Tile tile) {
		return AstraPal.teamFaded[tile.team().id].rgba();
	}

	public class LandMineBuild extends Building {
		@Override public void drawCracks() { }

		@Override public void unitOn(Unit unit) {
			if (enabled && unit.team != team) triggered();
		}

		@Override public void damage(float damage) {
			super.damage(damage / 2f);
		}

		@Override public void control(LAccess type, double p1, double p2, double p3, double p4) {
			if (type == LAccess.shoot && p3 == 1) triggered();
			super.control(type, p1, p2, p3, p4);
		}

		@Override public void control(LAccess type, Object p1, double p2, double p3, double p4) {
			if (type == LAccess.shootp && p2 == 1) triggered();
			super.control(type, p1, p2, p3, p4);
		}

		public void triggered() {
			for (int i = 0; i < numLightning; i++) {
				Lightning.create(team, lightningColor, lightningDamage, x, y, Mathf.random(360f), lightningLength);
			}

			if (bullet != null) {
				for(int i = 0; i < shots; i++){
					bullet.create(this, x, y, (360f / shots) * i + Mathf.random(shotInaccuracy));
				}
			}

			Damage.dynamicExplosion(x, y, explodeFire, explodePower, 0f, explodeRadius, true, true, team);

			float radius = explodeRadius * tilesize;
			if (knockback != 0) {
				Units.nearbyEnemies(team, x, y, radius, unit -> {
					unit.apply(status, statusDuration);
					float dist = unit.dst(this) / radius;
					Tmp.v3.set(unit).sub(this).nor().scl(knockback * 80f * (1f - (knockback > 0 ? dist : Mathf.pow(dist * 2f - 1f, 2))));
					unit.impulse(Tmp.v3);
				});
			} else if (status != StatusEffects.none) {
				Units.nearbyEnemies(team, x, y, radius, unit -> unit.apply(status, statusDuration));
			}

			kill();
		}

		@Override public void onDestroyed() {
			if (createRubble && !floor().solid) {
				Effect.rubble(x, y, size);
			}
		}
	}
}