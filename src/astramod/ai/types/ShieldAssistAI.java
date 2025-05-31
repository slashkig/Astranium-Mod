package astramod.ai.types;

import arc.util.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class ShieldAssistAI extends FollowAI {
	public PosTeam targeter;
	@Nullable public ShieldArcAbility shieldAbility;
	public boolean useUnitShield = true;

	public ShieldAssistAI() {
		targeter = PosTeam.create();
	}

	@Override public void init() {
		for (Ability ability : unit.abilities) {
			if (ability instanceof ShieldArcAbility shieldAbility) {
				this.shieldAbility = shieldAbility;
				useUnitShield = false;
				break;
			}
		}
	}

	@Override public void updateMovement() {
		if (following != null && !following.dead() && following.isShooting()) {
			if ((useUnitShield ? unit.shield : shieldAbility.data) > 10f) {
				targeter.set(following.aimX(), following.aimY());
				if (unit.dst(targeter) > unit.type.range || unit.dst(targeter) < unit.type.range / 2f) moveTo(targeter, unit.type.range * 0.9f, 25f);
				unit.lookAt(targeter);
			} else {
				moveTo(Tmp.v1.set(following).add(Tmp.v2.trns(following.rotation + 180, followRange * 2)), 1f, 50f, false, null);
			}
		} else {
			super.updateMovement();
		}
	}
}
