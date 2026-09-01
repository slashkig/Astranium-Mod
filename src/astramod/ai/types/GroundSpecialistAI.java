package astramod.ai.types;

import arc.func.*;
import arc.math.geom.*;
import mindustry.Vars;
import mindustry.ai.types.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.meta.*;

public class GroundSpecialistAI extends GroundAI {
	protected static final Vec2 moveToVec = new Vec2();

	public Boolf<Building> targetFilter;
	public float detectionRange = 100f;

	public GroundSpecialistAI(Boolf<Building> targetFilter, float detectionRange) {
		this.targetFilter = targetFilter;
		this.detectionRange = detectionRange;
	}

	public GroundSpecialistAI(Boolf<Building> filter) {
		targetFilter = filter;
	}

	@Override public void updateMovement() {
		if (target != null) {
			boolean move = true;
			float engageRange = unit.range() * 0.9f;
			boolean withinAttackRange = unit.within(target, engageRange);
			Building targetBuild = Vars.world.buildWorld(target.x(), target.y());

			moveToVec.set(target);

			if (targetBuild != null && !unit.type.circleTarget && unit.within(targetBuild, 0.9f * targetBuild.block.size * Vars.tilesize / 2f)) {
				move = false;
			}

			if (!unit.isFlying()) {
				boolean unreachable = false;

				if (withinAttackRange) {
					move = true;
				} else {
					var result = Vars.controlPath.getPathPosition(unit, new Vec2(target.x(), target.y()));

					unreachable = result.unreachable;
					move &= result.move;
					if (result.move) moveToVec.set(result.dest);
				}

				if (unit.team.isAI() && unreachable) {
					targetInvalidated();
					return;
				}
			}

			if (move) {
				if (unit.type.circleTarget) {
					circleAttack(unit.type.circleTargetRadius);
				} else {
					moveTo(moveToVec, withinAttackRange || unit.isFlying() ? engageRange : 0f, unit.isFlying() ? 40f : 100f, false, null, true);
				}
			}

			if (unit.isFlying() && move && !(unit.type.circleTarget && !unit.type.omniMovement) && !withinAttackRange) {
				unit.lookAt(target);
			} else {
				faceTarget();
			}
		} else {
			targetInvalidated();
			super.updateMovement();
		}
	}

	@Override public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
		return Units.findEnemyTile(unit.team, x, y, detectionRange, b ->
			targetFilter.get(b) && (unit.isFlying() || !unit.isPathImpassable(World.toTile(b.x), World.toTile(b.y)))
			|| b.block.flags.contains(BlockFlag.core) && unit.within(b, range * 1.2f)
		);
	}
}