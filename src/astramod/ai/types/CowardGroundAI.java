package astramod.ai.types;

import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

public class CowardGroundAI extends RangerGroundAI {
	public boolean fleeing = false;

	@Override public void updateMovement() {
		if (fleeing || target instanceof Unitc r && unit.within(target, r.range())) {
			Building core = unit.closestCore();
			if (core != null && !unit.within(core, unit.range())) {
				moveTo(core, unit.range());
			}
			fleeing = target instanceof Unitc r && unit.within(target, r.range() * 1.2f);
		} else super.updateMovement();
	}

	@Override public void updateTargeting() {
		updateWeapons();
	}

	@Override public Teamc target(float x, float y, float range, boolean air, boolean ground) {
		return Units.closestTarget(unit.team, x, y, range, u -> u.checkTarget(air, ground) && u.hasWeapons(), b -> ground && b.block.flags.contains(BlockFlag.turret));
	}
}