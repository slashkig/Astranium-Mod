package astramod.ai.types;

import mindustry.entities.*;
import mindustry.gen.*;

public class AnchoredProtectorAI extends AnchoredAI {
	public Unit closestUnit;

	@Override public void updateMovement() {
		Building anchor = anchor();
		if (closestUnit != null && !closestUnit.dead()) {
			moveTo(closestUnit, unit.type.range * 0.8f, 25f);
			unit.controlWeapons(true);
			if (unit.dst(closestUnit) < unit.type.range) unit.aimLook(closestUnit);
			else unit.lookAt(closestUnit);
		} else {
			closestUnit = Units.closestEnemy(unit.team, anchor.x, anchor.y, boundRadius, u -> u.checkTarget(unit.type.targetAir, unit.type.targetGround));
			super.updateMovement();
		}
	}

	@Override public boolean shouldShoot() {
		return closestUnit != null && unit.dst(closestUnit) < unit.type.range;
	}
}