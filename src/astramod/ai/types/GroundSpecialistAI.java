package astramod.ai.types;

import arc.func.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.ai.types.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.meta.*;
import astramod.ai.AstraPathfinding;

public class GroundSpecialistAI extends GroundAI {
	protected static final Vec2 moveToVec = new Vec2();

	public Boolf<Building> targetFilter;
	public float detectionRange;
	@Nullable protected Teamc moveTarget;

	public GroundSpecialistAI(Boolf<Building> targetFilter, float detectionRange) {
		this.targetFilter = targetFilter;
		this.detectionRange = detectionRange;
	}

	@Override public void updateMovement() {
		if (moveTarget != null) {
			float engageRange = unit.type.circleTarget ? unit.type.circleTargetRadius * 1.2f : unit.range() * 0.8f;
			if (unit.within(moveTarget, engageRange)) {
				target = moveTarget;

				if (unit.type.circleTarget) {
					circleAttack(unit.type.circleTargetRadius);
				}
			} else {
				AstraPathfinding.setPositionField(unit, moveTarget);
				pathfind(AstraPathfinding.fieldPosition);
			}

			faceTarget();
		} else {
			targetInvalidated();
			super.updateMovement();
		}
	}

	@Override public Teamc findMainTarget(float x, float y, float range, boolean air, boolean ground) {
		moveTarget = Units.findEnemyTile(unit.team, x, y, detectionRange, b ->
			targetFilter.get(b) && (unit.isFlying() || !unit.isPathImpassable(World.toTile(b.x), World.toTile(b.y)))
			|| b.block.flags.contains(BlockFlag.core) && unit.within(b, range + Vars.tilesize * 2f)
		);
		return moveTarget != null && unit.within(moveTarget, range) ? moveTarget : findTarget(x, y, range, air, ground);
	}
}