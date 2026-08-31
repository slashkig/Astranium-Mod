package astramod.ai.types;

import mindustry.ai.types.*;

public class GroundRangerAI extends GroundAI {
	@Override public void updateMovement() {
		if (target == null || !target.isAdded() || !unit.within(target, unit.range() * 0.9f)) {
			super.updateMovement();
		} else {
			faceTarget();
		}
	}
}