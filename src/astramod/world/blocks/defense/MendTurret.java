package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import astramod.graphics.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.units.*;

import static mindustry.Vars.*;

public class MendTurret extends RepairTurret {
	public float retargetTime = 30f;
	public float rotateSpeed = 0.5f;
	public float targetingArc = 30f;

	public MendTurret(String name) {
		super(name);
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		drawPotentialLinks(x, y);
		drawOverlay(x * tilesize + offset, y * tilesize + offset, rotation);

		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, repairRadius, AstraPal.mend);
		indexer.eachBlock(player.team(), x * tilesize + offset, y * tilesize + offset, repairRadius, other -> true, other -> Drawf.selected(other, Tmp.c1.set(AstraPal.mend).a(Mathf.absin(4f, 1f))));
	}

	public void drawBeam(MendTurretBuild build) {
		drawBeam(build.x, build.y, build.rotation, length, build.id, world.build(build.target), build.team, build.strength, pulseStroke, pulseRadius, beamWidth,
			build.lastEnd, build.offset, laserColor, laserTopColor, laser, laserEnd, laserTop, laserTopEnd);
	}

	public class MendTurretBuild extends RepairPointBuild {
		public int target = -1;

		@Override public void updateTile() {
			float multiplier = 1f + (acceptCoolant ? liquids.current().heatCapacity * coolantMultiplier * optionalEfficiency : 0);
			Building build = null;

			if (target != -1) {
				build = world.build(target);
				if (build == null || build.dead() || build.dst(this) - build.block.size * tilesize / 2f > repairRadius || build.health() >= build.maxHealth()) {
					target = -1;
				}
			}

			if (target == -1) offset.setZero();

			boolean healed = false;

			if (build != null && efficiency > 0) {
				float angle = Angles.angle(x, y, build.x + offset.x, build.y + offset.y);
				if (Angles.angleDist(angle, rotation) < targetingArc) {
					healed = true;
					build.heal(repairSpeed * strength * edelta() * multiplier);
					build.recentlyHealed();
					if (timer(timerEffect, 40f / multiplier)) Fx.healBlockFull.at(build.x, build.y, build.block.size, AstraPal.mend, build.block);
				}
				rotation = Mathf.slerpDelta(rotation, angle, rotateSpeed * efficiency * timeScale);
			}

			strength = Mathf.lerpDelta(strength, healed ? 1f : 0f, 0.08f * Time.delta);

			if (timer(timerTarget, retargetTime)) selectTarget();
		}

		@Override public void drawSelect() {
			indexer.eachBlock(this, range(), other -> other != this, other -> Drawf.selected(other, Tmp.c1.set(AstraPal.mend).a(Mathf.absin(4f, 1f))));
			Drawf.dashCircle(x, y, range(), AstraPal.mend);
		}

		public void selectTarget() {
			// Anonymous helper class for lambda
			final var bestTarget = new Object() {
				Building build = world.build(target);
				float weight = target == -1 ? -1f : 0.5f + targetWeight(build);
			};

			indexer.eachBlock(this, range(), b -> b.damaged() && !b.isHealSuppressed(), build -> {
				float weight = targetWeight(build);
				if (bestTarget.weight < weight) {
					bestTarget.weight = weight;
					bestTarget.build = build;
				}				
			});
			target = bestTarget.build != null ? bestTarget.build.pos() : -1;
		}

		public float targetWeight(Building build) {
			return 2f - build.dst(this) / range() - build.health / build.maxHealth;
		}

		@Override public void draw() {
			Draw.rect(baseRegion, x, y);

			Draw.z(Layer.turret);
			Drawf.shadow(region, x - (size / 2f), y - (size / 2f), rotation - 90);
			Draw.rect(region, x, y, rotation - 90);

			drawBeam(this);
		}

		@Override public boolean shouldConsume() {
			return target != -1 && enabled;
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.i(target);
		}

		public void read(Reads read, byte revision) {
			super.read(read, revision);
			target = read.i();
		}
	}
}