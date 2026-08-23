package astramod.ai.types;

import mindustry.entities.*;
import mindustry.gen.*;

public class AnchoredAttackerAI extends AnchoredAI {
	@Override public void updateVisuals() {
		if (target != null) {
			faceTarget();
		} else super.updateVisuals();
	}

	@Override public void updateMovement() {
		if (target != null) {
			moveTo(target, unit.range() * 0.8f, 25f);
		} else super.updateMovement();
	}

	@Override public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
		return Units.closestEnemy(unit.team, anchor.x, anchor.y, boundRadius, u -> u.checkTarget(unit.type.targetAir, unit.type.targetGround));
	}
}