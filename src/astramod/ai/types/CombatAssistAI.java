package astramod.ai.types;

import mindustry.gen.*;

public class CombatAssistAI extends FollowAI {
	public PosTeam targeter;

	public CombatAssistAI() {
		targeter = PosTeam.create();
	}

	@Override public void updateMovement() {
		if (following != null && !following.dead() && following.isShooting() && unit.hasWeapons()) {
			targeter.set(following.aimX(), following.aimY());
			if (unit.dst(targeter) > unit.type.range || unit.dst(targeter) < unit.type.range / 2f) moveTo(targeter, unit.type.range * 0.9f, 25f);
			unit.controlWeapons(true);
			unit.aimLook(targeter);
		} else {
			super.updateMovement();
		}
	}

	@Override public boolean shouldShoot() {
		return following != null && following.isShooting() && unit.dst(targeter) < unit.type.range;
	}
}