package astramod.ai.types;

import mindustry.ai.UnitStance;
import mindustry.gen.*;

public class FollowShootAI extends FollowAI {
	public PosTeam targeter;

	public FollowShootAI() {
		targeter = PosTeam.create();
	}

	@Override public void updateVisuals() {
		if (followShooting()) {
			unit.lookAt(targeter);
		} else if (target != null) {
			faceTarget();
		} else super.updateVisuals();
	}

	@Override public void updateMovement() {
		if (followShooting()) {
			targeter.set(following.aimX(), following.aimY());
			if (!unit.inRange(targeter) || unit.within(targeter, unit.range() / 2f)) moveTo(targeter, unit.type.range * 0.9f, 25f);
		} else {
			super.updateMovement();
		}
	}

	@Override public boolean shouldFire() {
		return !hasStance(UnitStance.holdFire) && (unit.inRange(targeter) || !followShooting());
	}

	@Override public boolean shouldShoot() {
		return super.shouldShoot() && (followShooting() || target != null);
	}

	@Override public Teamc findTarget(float x, float y, float range, boolean air, boolean ground) {
		return followShooting() ? targeter : super.findTarget(x, y, range, air, ground);
	}

	public boolean followShooting() {
		return following != null && following.isShooting();
	}
}