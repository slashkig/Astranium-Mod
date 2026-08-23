package astramod.ai.types;

import arc.struct.Seq;
import arc.util.*;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class AnchoredShieldAI extends AnchoredAI {
	@Nullable public ShieldArcAbility shieldAbility;
	public boolean useUnitShield = true;

	protected static final Seq<Teamc> targets = new Seq<>();
	@Nullable public Building protectTarget;

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

	@Override public void updateVisuals() {
		if (target != null) {
			unit.lookAt(target);
		} else super.updateVisuals();
	}

	@Override public void updateMovement() {
		if (target instanceof Unit enemy && (useUnitShield ? unit.shield : shieldAbility.data) > 1f) {
			// Raycast for the first block in line of fire of the enemy
			if (timer.get(timerTarget2, 40f)) {
				protectTarget = null;
				Tmp.v1.set(enemy).add(Tmp.v2.trns(enemy.angleTo(enemy.aimX(), enemy.aimY()), enemy.range()));
				World.raycastEachWorld(enemy.x(), enemy.y(), Tmp.v1.x, Tmp.v1.y, (x, y) -> {
					Building build = Vars.world.build(x, y);
					if (build != null && build.team == unit.team && (!build.block.underBullets || build == Vars.world.buildWorld(enemy.aimX(), enemy.aimY()))) {
						protectTarget = build;
						return true;
					} else return false;
				});
			}

			// Move to the midpoint between enemy and block, otherwise shadow the enemy outside of its range
			if (enemy.isShooting() && protectTarget != null) {
				Tmp.v1.set((enemy.x() + protectTarget.x()) / 2f, (enemy.y() + protectTarget.y()) / 2f);
			} else {
				Tmp.v1.set(enemy).add(Tmp.v2.trns(enemy.angleTo(anchor), Math.min(Math.max(unit.range(), enemy.range() * 1.2f), anchor.dst(enemy) * 0.9f)));
			}
			moveTo(Tmp.v1, 1f, 25f, false, null);			
		} else super.updateMovement();
	}
	

	@Override public void updateTargeting() {
		if (super.retarget()) {
			Teamc t = target(anchor.x, anchor.y, boundRadius, true, true);
			if (t != target) {
				targets.remove(target);
				target = t;
				if (t != null) {
					targets.add(t);
					timer.reset(timerTarget2, -1f);
				}
			}
		}

		noTargetTime += Time.delta;

		if (super.invalid(target)) {
			if (target instanceof Healthc h && !h.isValid()) {
				targetInvalidated();
			}
			if (target != null) {
				targets.remove(target);
				protectTarget = null;
				target = null;
			}
		} else {
			noTargetTime = 0f;
			super.updateTargeting();
		}
	}

	@Override public Teamc target(float x, float y, float range, boolean air, boolean ground) {
		return Units.closestEnemy(unit.team, x, y, range, u -> u.hasWeapons() && (u == target || !targets.contains(u)));
	}

	@Override public boolean retarget() {
		return false;
	}

	@Override public boolean invalid(Teamc target) {
		return false;
	}
}