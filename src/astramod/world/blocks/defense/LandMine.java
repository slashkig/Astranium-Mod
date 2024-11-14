package astramod.world.blocks.defense;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.LAccess;
import mindustry.world.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class LandMine extends Block {
	public float explodePower = 50f;
	public float explodeRadius = 2.5f;
	public float explodeFire = 0f;

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
		stats.add(Stat.damage, Mathf.floor(explodePower / 11f) * explodePower / 2f);
		stats.add(AstraStat.incendivity, explodeFire);
		stats.add(Stat.range, explodeRadius, StatUnit.blocks);

		if (bullet != null) {
			stats.add(Stat.ammo, StatValues.ammo(ObjectMap.of(this, bullet)));
		}
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		if (tile.floor().isLiquid) return false;

		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof LandMineBuild) return false;
		}
		return true;
	}

	@Override public int minimapColor(Tile tile) {
		return tile.team().color.cpy().a(0.5f).rgba();
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
			if (bullet != null) {
				for(int i = 0; i < shots; i++){
					bullet.create(this, x, y, (360f / shots) * i + Mathf.random(shotInaccuracy));
				}
			}

			Damage.dynamicExplosion(x, y, explodeFire, explodePower, 0f, explodeRadius, true, true, team);
			kill();
		}

		@Override public void onDestroyed() {
			if (createRubble && !floor().solid) {
				Effect.rubble(x, y, size);
			}
		}
	}
}