package astramod.ai.types;

import mindustry.ai.types.*;
import mindustry.entities.*;
import astramod.entities.UnitUtil;

public class GroundTankAI extends GroundAI {
	@Override public void faceTarget() {
		if (!unit.type.omniMovement && !unit.moving()) {
			if (!Units.invalidateTarget(target, unit, unit.range()) && UnitUtil.targetPriority(target) >= 0f) {
				unit.lookAt(target);
			}
		} else super.faceTarget();
	}

	public float weight() {
		return unit.health;
	}
}