package astramod.ai.types;

import arc.math.*;
import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.gen.*;
import astramod.math.MathUtil;

public class GroundBacklineAI extends GroundAI {
	public float followRange = -1f;
	public Unit tank;

	public GroundBacklineAI(float followRange) {
		this.followRange = followRange;
	}

	public GroundBacklineAI() { }

	@Override public void init() {
		if (followRange < 0f) {
			followRange = 40f + unit.hitSize * 2f;
		}
	}

	@Override public void updateMovement() {
		if (timer.get(timerTarget2, tank == null ? 40f : 90f)) {
			tank = Units.closest(unit.team, unit.x, unit.y, followRange, u -> u.controller() instanceof GroundTankAI);
		}

		if (Units.invalidateTarget(tank, null, unit.x, unit.y, followRange)) {
			tank = null;
			super.updateMovement();
		} else {
			float heading = tank.rotation();
			float bearing = tank.angleTo(unit);
			float angle = Angles.angleDist(heading, bearing);
			if (angle > 120f) {
				super.updateMovement();
			} else if (angle < 45f && unit.dst(tank) < unit.hitSize + tank.hitSize) {
				moveTo(Tmp.v1.set(tank).trns(heading + 90f * MathUtil.angleBearing(heading, bearing), followRange), 0f);
			} else if (!unit.moving()) {
				unit.lookAt(heading);
			}
		}
	}

	@Override public float prefSpeed() {
		return tank == null ? super.prefSpeed() : Math.min(tank.speed(), unit.speed());
	}
}