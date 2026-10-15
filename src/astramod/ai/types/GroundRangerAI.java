package astramod.ai.types;

import mindustry.entities.*;
import astramod.entities.UnitUtil;

public class GroundRangerAI extends GroundBacklineAI {
	@Override public void updateMovement() {
		if (Units.invalidateTarget(target, unit, unit.range() * 0.8f) || UnitUtil.targetPriority(target) < 0f) {
			super.updateMovement();
		} else {
			faceTarget();
		}
	}
}