package astramod.ai.types;

import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class AnchoredShieldAI extends AnchoredAI {
	public Unit targetUnit;
	@Nullable public ShieldArcAbility shieldAbility;
	public boolean useUnitShield = true;

	@Override public void init() {
		super.init();
		for (Ability ability : unit.abilities) {
			if (ability instanceof ShieldArcAbility shieldAbility) {
				this.shieldAbility = shieldAbility;
				useUnitShield = false;
				break;
			}
		}
	}

	@Override public void updateMovement() {
		Building anchor = anchor();
		if (targetUnit != null && !targetUnit.dead() && targetUnit.isShooting()) {
			if ((useUnitShield ? unit.shield : shieldAbility.data) > 1f) {
				if (unit.dst(targetUnit.aimX, targetUnit.aimY) < 5f || unit.dst(targetUnit) < 5f) {
					moveTo(Tmp.v1.set(targetUnit).add(Tmp.v2.trns(targetUnit.angleTo(anchor), unit.type.range)), 1f, 25f, false, null);
				} else {
					moveTo(Tmp.v1.set((targetUnit.x + targetUnit.aimX) / 2, (targetUnit.y + targetUnit.aimY) / 2), 1f, 25f, false, null);
				}
				unit.lookAt(targetUnit);
			} else super.updateMovement();
		} else {
			targetUnit = Units.closestEnemy(unit.team, anchor.x, anchor.y, boundRadius, u -> u.isShooting());
			super.updateMovement();
		}
	}
}