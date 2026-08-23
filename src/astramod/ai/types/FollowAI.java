package astramod.ai.types;

import arc.util.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import astramod.ai.*;

import static mindustry.Vars.state;

public abstract class FollowAI extends AIController {
	public @Nullable Unit following;
	public float followRange = 40f;

	@Override public void updateMovement() {
		if (following != null) {
			moveTo(following, following.type.hitSize + unit.type.hitSize / 2f + followRange);
		}

		if (timer.get(timerTarget2, following == null ? 40f : 90f) && (!hasStance(AstraUnitStance.lockFollow) || following == null || !following.isValid())) {
			float minDst = Float.MAX_VALUE;
			Player closest = null;

			for (Player player : Groups.player) {
				if (!player.dead() && player.team() == unit.team) {
					float dst = player.dst2(unit);
					if (dst < minDst) {
						closest = player;
						minDst = dst;
					}
				}
			}
			follow(closest == null ? null : closest.unit());
		}
	}

	@Override public AIController fallback() {
		return unit.team.isAI() && unit.team.rules().prebuildAi ? new PrebuildAI() : unit.type.flying ? new FlyingAI() : new GroundAI();
	}

	@Override public boolean useFallback() {
		return unit.team.isAI() && unit.team.rules().prebuildAi || state.rules.waves && unit.team == state.rules.waveTeam && !unit.team.rules().rtsAi;
	}

	@Override public boolean shouldFire() {
		return !hasStance(UnitStance.holdFire) && target != null && unit.inRange(target);
	}

	@Override public boolean shouldShoot() {
		return !unit.isBuilding() && unit.type.canAttack;
	}

	public void follow(Unit unit) {
		following = unit;
	}
}