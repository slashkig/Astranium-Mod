package astramod.ai.types;

import arc.func.Prov;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.world.*;
import astramod.entities.UnitUtil;

public class RaiderAI extends AIController {
	public Prov<AIController> groundAI = GroundBacklineAI::new;

	@Nullable protected Tile landingTarget;

	@Override public void updateMovement() {
		boolean ignoreTarget = Units.invalidateTarget(target, unit, unit.range() * 1.2f) || target instanceof Building b && b.block.priority < 0f;
		unit.updateBoosting(ignoreTarget);

		if (unit.isGrounded()) {
			if (ignoreTarget && (fallback != null || (fallback = fallback()) != null)) {
				if (fallback.unit() != unit) fallback.unit(unit);
				fallback.updateMovement();
			} else {
				if (UnitUtil.targetPriority(target) >= 0f) moveTo(target, unit.range() * 0.8f, 0f);
				faceTarget();
			}
		} else if (!ignoreTarget) {
			if (timer.get(timerTarget2, landingTarget == null ? 40f : 90f)) {
				landingTarget = UnitUtil.closestEmptyTile(unit, target, unit.range());
			}
			if (landingTarget != null) {
				if (!UnitUtil.isEmptyTile(landingTarget)) {
					timer.reset(timerTarget2, -1f);
				} else {
					moveTo(landingTarget, 0f, 20f, true, null, true);
				}
			} else {
				moveToCore();
			}
		} else {
			moveToCore();
		}
	}

	public void moveToCore() {
		moveTo(unit.closestEnemyCore(), unit.range() * 0.8f, 20f);
	}

	@Override public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
		return unit.isFlying() ? Units.closestTarget(unit.team, x, y, range,
			u -> u.checkTarget(air, ground) && u.type.targetPriority >= 0f,
			t -> ground && (unit.type.targetUnderBlocks || !t.block.underBullets) && t.block.priority >= 0f
		) : super.findMainTarget(x, y, range, air, ground);
	}

	@Override public AIController fallback() {
		return groundAI.get();
	}

	@Override public boolean useFallback() {
		return !unit.type.canBoost || unit.isGrounded() && unit.healthf() < 0.5f;
	}
}