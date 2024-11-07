package astramod.ai.types;

import static mindustry.Vars.state;

import arc.util.*;
import mindustry.ai.types.*;
import mindustry.entities.units.*;
import mindustry.gen.*;

public abstract class FollowAI extends AIController {
	public @Nullable Unit following;
	public float followRange = 40f;

	@Override public void updateMovement() {
		if (following != null) {
			moveTo(following, following.type.hitSize + unit.type.hitSize / 2f + followRange);
		}

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
		following = closest == null ? null : closest.unit();
	}

	@Override public AIController fallback() {
		return unit.type.flying ? new FlyingAI() : new GroundAI();
	}

	@Override public boolean useFallback() {
		return state.rules.waves && unit.team == state.rules.waveTeam && !unit.team.rules().rtsAi;
	}
}