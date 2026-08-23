package astramod.ai.types;

import arc.struct.*;
import arc.util.*;
import mindustry.entities.Units;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class FollowShieldAI extends FollowAI {
	public static final ObjectMap<Unit, Seq<Teamc>> targetTracker = new ObjectMap<>();

	@Nullable public ShieldArcAbility shieldAbility;
	public boolean useUnitShield = true;
	public float defendRadius;

	public FollowShieldAI(float radius) {
		defendRadius = radius;
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

	@Override public void updateVisuals() {
		if (target != null)	{
			unit.lookAt(target);
		} else if (following != null) {
			if (following.isShooting()) unit.lookAt(following.aimX(), following.aimY());
			else if (!targetTracker.get(following).isEmpty()) unit.lookAt(targetTracker.get(following).first());
			else super.updateVisuals();
		} else {
			super.updateVisuals();
		}
	}

	@Override public void updateMovement() {
		if (following != null && following.isValid()) {
			// Low shield, move to back to recharge
			if ((useUnitShield ? unit.shield : shieldAbility.data) < 1f) {
				moveTo(Tmp.v1.set(following).add(Tmp.v2.trns(following.rotation + 180f, followRange * 2f)), following.hitSize(), 50f, false, null);
			// Protect from target enemy
			} else if (target != null) {
				moveTo(Tmp.v1.set((target.x() + following.x()) / 2f, (target.y() + following.y()) / 2f), 1f, 25f, false, null);
			// Protect from direction being shot at
			} else if (following.isShooting()) {
				moveTo(Tmp.v1.set((following.x() + following.aimX()) / 2f, (following.y() + following.aimY()) / 2f), following.hitSize(), 25f, false, null);
			} else {
				super.updateMovement();
			}
		} else {
			super.updateMovement();
		}
	}
	@Override public void updateTargeting() {
		if (following != null && retarget()) {
			Teamc t = findMainTarget(following.x, following.y, defendRadius, true, true);
			if (t != target) {
				targetTracker.get(following).remove(target);
				target = t;
				if (t != null) {
					targetTracker.get(following).add(t);
				}
			}
		}
	}

	@Override public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
		return following == null ? null : Units.closestTarget(unit.team, x, y, range,
			u -> u.hasWeapons() && (u == target || !targetTracker.get(following).contains(u)), b -> b.block.attacks);
	}

	@Override public void follow(Unit unit) {
		if (unit == null && following != null) {
			if (!following.isValid()) {
				targetTracker.remove(following);
			} else if (target != null) {
				targetTracker.get(following).remove(target);
			}
			target = null;
		} else if (unit != null && !targetTracker.containsKey(unit)) {
			targetTracker.put(unit, new Seq<>());
		}
		super.follow(unit);
	}
}
