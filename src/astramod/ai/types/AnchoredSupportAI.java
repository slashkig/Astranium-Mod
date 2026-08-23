package astramod.ai.types;

import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.Teams.BlockPlan;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.ConstructBuild;

import static mindustry.Vars.*;

/** Uses elements from BuilderAI and RepairAI. */
public class AnchoredSupportAI extends AnchoredAI {
	public @Nullable Unit assisting;
	public @Nullable Teamc enemy;
	public @Nullable BlockPlan lastPlan;

	public static float retreatDelay = Time.toSeconds * 2f;
	public float fleeRange;
	public boolean alwaysFlee = false;

	boolean found = false;
	Building damagedTarget;
	float retreatTimer;

	public AnchoredSupportAI() { }

	public AnchoredSupportAI(boolean alwaysFlee) {
		this.alwaysFlee = alwaysFlee;
	}

	@Override public void init() {
		super.init();
		fleeRange = boundRadius / 2f;
	}

	@Override public void updateVisuals() {
		if (target == null) super.updateVisuals();
	}

	@Override public void updateMovement() {
		unit.updateBuilding = true;
		boolean moving = false;

		if (assisting != null && !assisting.isValid()) assisting = null;

		if (unit.buildPlan() == null && damagedTarget == null || alwaysFlee) {
			if (timer.get(timerTarget4, 40)) {
				enemy = target(unit.x, unit.y, fleeRange, true, true);
			}

			// Fly away from enemy when not doing anything, but only after a delay
			if (((retreatTimer += Time.delta) >= retreatDelay || alwaysFlee) && enemy != null) {
				unit.clearBuilding();
				super.updateMovement();
				moving = true;
			}
		}

		// Try to follow and mimic someone
		if (assisting != null) {
			retreatTimer = 0f;

			// Validate follower
			if (!assisting.isValid() || anchor.dst(assisting) > boundRadius || !assisting.activelyBuilding()) {
				assisting = null;
				unit.plans.clear();
				return;
			}

			// Set to follower's first build plan, whatever that is
			unit.plans.clear();
			unit.plans.addFirst(assisting.buildPlan());
			lastPlan = null;
			moveTo(assisting.buildPlan(), unit.type.buildRange * 0.9f, 25f);
		} else if (unit.buildPlan() != null) {
			if (!alwaysFlee) retreatTimer = 0f;
			// Approach plan if building
			BuildPlan req = unit.buildPlan();

			// Clear break plan if another player is breaking something
			if (!req.breaking && timer.get(timerTarget2, 40f)) {
				for (Player player : Groups.player) {
					if (player.isBuilder() && player.unit().activelyBuilding() && player.unit().buildPlan().samePos(req) && player.unit().buildPlan().breaking) {
						unit.plans.removeFirst();
						// Remove from list of plans
						unit.team.data().plans.remove(p -> p.x == req.x && p.y == req.y);
						return;
					}
				}
			}

			boolean valid = !(lastPlan != null && lastPlan.removed) &&
				((req.tile() != null && req.tile().build instanceof ConstructBuild cons && cons.current == req.block) ||
				(req.breaking ? Build.validBreak(unit.team(), req.x, req.y) : Build.validPlace(req.block, unit.team(), req.x, req.y, req.rotation)));

			if (valid) {
				// Move toward the plan
				float range = Math.min(unit.type.buildRange - unit.type.hitSize * 2f, BuilderAI.buildRadius);
				moveTo(req.tile(), range, 20f);
				moving = !unit.within(req.tile(), range);
			} else {
				// Discard invalid plan
				unit.plans.removeFirst();
				lastPlan = null;
			}
		} else {
			// Follow someone and help them build
			if (timer.get(timerTarget2, 20f)) {
				found = false;

				Units.nearby(unit.team, anchor.x, anchor.y, boundRadius, u -> {
					if (found) return;

					if (u.canBuild() && u != unit && u.activelyBuilding()) {
						BuildPlan plan = u.buildPlan();

						Building build = world.build(plan.x, plan.y);
						if (build instanceof ConstructBuild cons) {
							float dist = Math.min(cons.dst(unit) - unit.type.buildRange, 0);

							// Make sure you can reach the plan in time
							if (dist / unit.speed() < cons.buildCost * 0.9f) {
								assisting = u;
								found = true;
							}
						}
					}
				});
			}

			if (assisting == null) {
				// Find new plan
				if (!unit.team.data().plans.isEmpty()) {
					var blocks = unit.team.data().plans;
					BlockPlan block = blocks.first();

					// Check if it's already been placed
					if (world.tile(block.x, block.y) != null && world.tile(block.x, block.y).block() == block.block) {
						blocks.removeFirst();
					} else if (Build.validPlace(block.block, unit.team(), block.x, block.y, block.rotation) &&
					(!alwaysFlee || !nearEnemy(block.x, block.y)) && anchor.dst(block.x * tilesize, block.y * tilesize) < boundRadius) {
						lastPlan = block;
						// Add build plan
						unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, block.block, block.config));
						// Shift build plan to tail so next unit builds something else
						blocks.addLast(blocks.removeFirst());
					} else {
						// Shift head of queue to tail, try something else next time
						blocks.addLast(blocks.removeFirst());
					}
				}

				if (unit.buildPlan() == null) {
					// Repair blocks
					if (target instanceof Building) {
						boolean shoot = false;

						if (target.within(unit, unit.type.range)) {
							unit.aim(target);
							shoot = true;
						}

						unit.controlWeapons(shoot);
					} else if (target == null) {
						unit.controlWeapons(false);
					}

					if (target != null && target instanceof Building b && b.team == unit.team) {
						if (unit.type.circleTarget) {
							circleAttack(unit.type.circleTargetRadius);
						} else if (!target.within(unit, unit.type.range * 0.65f)) {
							moveTo(target, unit.type.range * 0.65f, 10f);
						}

						if (!unit.type.circleTarget) {
							unit.lookAt(target);
						}
					} else {
						// Return to anchor
						super.updateMovement();
					}
				}
			}
		}
		if (!unit.type.flying) {
            unit.updateBoosting(unit.type.boostWhenBuilding || moving || unit.floorOn().isDuct || unit.floorOn().damageTaken > 0f || unit.floorOn().isDeep());
		}
	}

	@Override public void updateTargeting() {
		if (timer.get(timerTarget, 15f)) {
			damagedTarget = Units.findDamagedTile(unit.team, unit.x, unit.y);
			if (damagedTarget instanceof ConstructBuild || damagedTarget != null && damagedTarget.dst(anchor) > boundRadius) damagedTarget = null;
		}

		if (damagedTarget == null) {
			super.updateTargeting();
		} else {
			target = damagedTarget;
		}
	}

	@Override public boolean shouldFire() {
		return !(unit.controller() instanceof CommandAI ai) || ai.shouldFire();
	}

	protected boolean nearEnemy(int x, int y) {
		return Units.nearEnemy(unit.team, x * tilesize - fleeRange / 2f, y * tilesize - fleeRange / 2f, fleeRange, fleeRange);
	}
}