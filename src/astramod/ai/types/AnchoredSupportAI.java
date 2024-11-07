package astramod.ai.types;

import arc.struct.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.Teams.BlockPlan;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock.ConstructBuild;

import static mindustry.Vars.*;

public class AnchoredSupportAI extends AnchoredAI {
	public @Nullable Unit following;
	public @Nullable Teamc enemy;
	public @Nullable BlockPlan lastPlan;

	public static float retreatDelay = Time.toSeconds * 2f;
	public float fleeRange;
	public boolean alwaysFlee = false;

	boolean found = false;
	Building damagedTarget;
	float retreatTimer;

	public AnchoredSupportAI(float bound) {
		super(bound);
		fleeRange = bound / 2f;
	}

	public AnchoredSupportAI(float bound, boolean alwaysFlee) {
		this(bound);
		this.alwaysFlee = alwaysFlee;
	}

	@Override public void updateMovement() {
		Building anchor = anchor();

		if (target != null && shouldShoot()) {
			unit.lookAt(target);
		}

		unit.updateBuilding = true;

		if (unit.buildPlan() == null && damagedTarget == null || alwaysFlee) {
			if (timer.get(timerTarget4, 40)) {
				enemy = target(unit.x, unit.y, fleeRange, true, true);
			}

			// Fly away from enemy when not doing anything, but only after a delay
			if ((retreatTimer += Time.delta) >= retreatDelay || alwaysFlee) {
				if (enemy != null) {
					unit.clearBuilding();
					super.updateMovement();
				}
			}
		}

		// Try to follow and mimic someone
		if (following != null) {
			retreatTimer = 0f;

			// Validate follower
			if (!following.isValid() || anchor.dst(following) > boundRadius || !following.activelyBuilding()) {
				following = null;
				unit.plans.clear();
				return;
			}

			// Set to follower's first build plan, whatever that is
			unit.plans.clear();
			unit.plans.addFirst(following.buildPlan());
			lastPlan = null;
			moveTo(following.buildPlan(), unit.type.buildRange * 0.9f, 25f);
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
				moveTo(req.tile(), unit.type.buildRange * 0.9f, 25f);
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
								following = u;
								found = true;
							}
						}
					}
				});
			}

			if (following == null) {
				// Find new plan
				if (!unit.team.data().plans.isEmpty()) {
					Queue<BlockPlan> blocks = unit.team.data().plans;
					BlockPlan block = blocks.first();

					// Check if it's already been placed
					if (world.tile(block.x, block.y) != null && world.tile(block.x, block.y).block().id == block.block) {
						blocks.removeFirst();
					} else if (Build.validPlace(content.block(block.block), unit.team(), block.x, block.y, block.rotation) &&
					(!alwaysFlee || !nearEnemy(block.x, block.y)) && anchor.dst(block.x * tilesize, block.y * tilesize) < boundRadius) {
						lastPlan = block;
						// Add build plan
						unit.addBuild(new BuildPlan(block.x, block.y, block.rotation, content.block(block.block), block.config));
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
							circleAttack(120f);
						} else if (!target.within(unit, unit.type.range * 0.65f)) {
							moveTo(target, unit.type.range * 0.65f);
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
	}

	@Override public void updateTargeting() {
		if (timer.get(timerTarget, 15)) {
			damagedTarget = Units.findDamagedTile(unit.team, unit.x, unit.y);
			if (damagedTarget instanceof ConstructBuild || damagedTarget != null && damagedTarget.dst(anchor()) > boundRadius) damagedTarget = null;
		}

		if (damagedTarget == null) {
			super.updateTargeting();
		} else {
			target = damagedTarget;
		}
	}

	protected boolean nearEnemy(int x, int y) {
		return Units.nearEnemy(unit.team, x * tilesize - fleeRange / 2f, y * tilesize - fleeRange / 2f, fleeRange, fleeRange);
	}

	@Override public AIController fallback() {
		return unit.type.flying ? new FlyingAI() : new GroundAI();
	}

	@Override public boolean useFallback() {
		return state.rules.waves && unit.team == state.rules.waveTeam && !unit.team.rules().rtsAi;
	}

	@Override public boolean shouldShoot() {
		return !unit.isBuilding() && unit.type.canAttack;
	}
}