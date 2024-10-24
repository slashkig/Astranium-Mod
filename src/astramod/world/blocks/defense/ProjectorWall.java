package astramod.world.blocks.defense;

import arc.*;
import arc.func.*;
import arc.util.*;
import arc.util.io.*;
import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.entities.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.defense.*;
import mindustry.logic.Ranged;
import astramod.content.*;

import static mindustry.Vars.*;

/** Hybrid between force projector and shield wall. */
public class ProjectorWall extends Wall {
	public float shieldHealth = 900f;
	public float breakCooldown = 60f * 10f;
	public float regenSpeed = 2f;

	public float shieldSize;
	public int sides = 8;
	public float shieldRotation = 22.5f;
	public float fullRadius;

	public boolean absorbLightning = false;

	public Effect absorbEffect = Fx.absorb;
	public Effect shieldBreakEffect = AstraFx.octShieldBreak;
	public TextureRegion glowRegion;

	protected static ProjectorWallBuild paramEntity;
	protected static Effect paramEffect;
	protected static final Cons<Bullet> shieldConsumer = bullet -> {
		if (bullet.team != paramEntity.team && bullet.type.absorbable && Intersector.isInRegularPolygon(((ProjectorWall)(paramEntity.block)).sides, paramEntity.x, paramEntity.y, paramEntity.realRadius(), ((ProjectorWall)(paramEntity.block)).shieldRotation, bullet.x, bullet.y)) {
			bullet.absorb();
			paramEffect.at(bullet);
			paramEntity.hit = 1f;
			paramEntity.shield -= bullet.damage;
		}
	};

	public ProjectorWall(String name, float shieldRange) {
		super(name);

		update = true;
		shieldSize = shieldRange;
		fullRadius = tilesize * (size / 2f + shieldSize);
		hasPower = consumesPower = conductivePower = true;
	}

	@Override public void init() {
		updateClipRadius(fullRadius + 3f);
		super.init();
	}

	@Override public void load() {
		super.load();
		glowRegion = Core.atlas.find(name + "-glow");
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(Stat.shieldHealth, shieldHealth);
		stats.add(Stat.range, shieldSize, StatUnit.blocks);
		stats.add(Stat.cooldownTime, (int)(breakCooldown / 60f), StatUnit.seconds);
	}

	@Override public void setBars() {
		super.setBars();
		addBar("shield", (ProjectorWallBuild entity) -> new Bar("stat.shieldhealth", Pal.accent, () -> entity.broken() ? 0f : entity.shield / (shieldHealth)).blink(Color.white));
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Draw.color(Pal.gray);
		Lines.stroke(3f);
		Lines.poly(x * tilesize + offset, y * tilesize + offset, sides, fullRadius, shieldRotation);
		Draw.color(player.team().color);
		Lines.stroke(1f);
		Lines.poly(x * tilesize + offset, y * tilesize + offset, sides, fullRadius, shieldRotation);
		Draw.color();
	}

	public class ProjectorWallBuild extends WallBuild implements Ranged {
		public float shield = shieldHealth;
		public float breakTimer;
		public float radscl, warmup;

		public float range() {
			return realRadius();
		}

		@Override public void onRemoved() {
			float radius = realRadius();
			if (!broken() && radius > 1f) Fx.forceShrink.at(x, y, radius, team.color);
			super.onRemoved();
		}

		@Override public void pickedUp() {
			super.pickedUp();
			radscl = warmup = 0f;
		}

		@Override public boolean inFogTo(Team viewer) {
			return false;
		}

		@Override public void updateTile() {
			radscl = Mathf.lerpDelta(radscl, broken() ? 0f : warmup, 0.05f);
			warmup = Mathf.lerpDelta(warmup, efficiency, 0.1f);

			if (breakTimer > 0) {
				breakTimer -= Time.delta;
			}

			if (breakTimer <= 0) {
				shield = Mathf.clamp(shield + regenSpeed * edelta(), 0f, shieldHealth);
			}

			if (shield <= 0 && !broken()) {
				shield = 0;
				breakTimer = breakCooldown;
				shieldBreakEffect.at(x, y, realRadius(), team.color);
				if (team != state.rules.defaultTeam) {
					Events.fire(EventType.Trigger.forceProjectorBreak);
				}
			}

			if (hit > 0f) {
				hit -= 1f / 5f * Time.delta;
			}

			deflectBullets();
		}

		public void deflectBullets() {
			float realRadius = realRadius();

			if (realRadius > 0 && !broken()) {
				paramEntity = this;
				paramEffect = absorbEffect;
				Groups.bullet.intersect(x - realRadius, y - realRadius, realRadius * 2f, realRadius * 2f, shieldConsumer);
			}
		}

		public float realRadius() {
			return fullRadius * radscl;
		}

		@Override public float warmup() {
			return warmup;
		}

		public boolean broken() {
			return breakTimer > 0 || !canConsume();
		}

		@Override public boolean isInsulated() {
			return absorbLightning;
		}

		@Override public void draw() {
			super.draw();

			if (warmup > 0.001f && glowRegion.found()) {
				Draw.color(team.color);
				Draw.alpha(warmup);
				Draw.z(Layer.blockOver);
				Draw.rect(glowRegion, x, y);
			}
			
			if (!broken()) {
				float radius = realRadius();

				if (radius > 0.001f) {
					Draw.color(team.color, Color.white, Mathf.clamp(hit));

					if (renderer.animateShields) {
						Draw.z(Layer.shields + 0.001f * hit);
						Draw.alpha(1f);
						Fill.poly(x, y, sides, radius, shieldRotation);
					} else {
						Draw.z(Layer.shields);
						Lines.stroke(1.5f);
						Draw.alpha(0.09f + Mathf.clamp(0.08f * hit));
						Fill.poly(x, y, sides, radius, shieldRotation);
						Draw.alpha(1f);
						Lines.poly(x, y, sides, radius, shieldRotation);
					}
				}
			}

			Draw.reset();
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.f(shield);
			write.f(radscl);
			write.f(warmup);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			shield = read.f();
			radscl = read.f();
			warmup = read.f();
		}
	}
}