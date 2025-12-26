package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
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
		drawBeam(build.x, build.y, build.rotation, length, build.id, build.target, build.team, build.strength, pulseStroke, pulseRadius, beamWidth,
			build.lastEnd, build.offset, laserColor, laserTopColor, laser, laserEnd, laserTop, laserTopEnd);
	}

	public class MendTurretBuild extends RepairPointBuild {
		public Building target;

		@Override public void updateTile() {
			float multiplier = 1f + (acceptCoolant ? liquids.current().heatCapacity * coolantMultiplier * optionalEfficiency : 0);

			if (target != null && (target.dead() || target.dst(this) - target.block.size * tilesize / 2f > repairRadius || target.health() >= target.maxHealth())) {
				target = null;
			}

			if (target == null) offset.setZero();

			boolean healed = false;

			if (target != null && efficiency > 0) {
				float angle = Angles.angle(x, y, target.x + offset.x, target.y + offset.y);
				if (Angles.angleDist(angle, rotation) < targetingArc) {
					healed = true;
					target.heal(repairSpeed * strength * edelta() * multiplier);
					target.recentlyHealed();
					if (timer(timerEffect, 40f / multiplier)) Fx.healBlockFull.at(target.x, target.y, target.block.size, AstraPal.mend, target.block);
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
				Building build = target;
				float weight = target == null ? -1f : 0.5f + targetWeight(target);
			};

			indexer.eachBlock(this, range(), b -> b.damaged() && !b.isHealSuppressed(), build -> {
				float weight = targetWeight(build);
				if (bestTarget.weight < weight) {
					bestTarget.weight = weight;
					bestTarget.build = build;
				}				
			});
			target = bestTarget.build;
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
			return target != null && enabled;
		}
	}
}