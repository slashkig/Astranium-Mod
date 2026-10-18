package astramod.ai.types;

import arc.func.Prov;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.units.*;
import mindustry.gen.Healthc;
import astramod.entities.abilities.ActivatedAbility;

public class AbilityAI extends AIController {
	public Prov<AIController> baseAI = GroundAI::new;
	public ActivatedAbility ability;

	@Override public void init() {
		ability = (ActivatedAbility)Structs.find(unit.abilities, ActivatedAbility.class::isInstance);
	}

	@Override public void updateUnit() {
		super.updateUnit();

		updateTargeting();
		if (ability.canActivate(unit) && ability.shouldActivate(unit, target)) {
			ability.activateNet(unit);
		}
	}

	@Override public void updateTargeting() {
		if (retarget()) {
			target = ability.selectTarget(this);
		}

		noTargetTime += Time.delta;

		if (!ability.validTarget(unit, target)) {
			if (target instanceof Healthc h && !h.isValid()) {
				targetInvalidated();
			}
			target = null;
		} else {
			noTargetTime = 0f;
		}
	}

	@Override public AIController fallback() {
		return baseAI.get();
	}

	@Override public boolean useFallback() {
		return true;
	}
}