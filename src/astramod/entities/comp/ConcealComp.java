package astramod.entities.comp;

import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.*;

@EntityComponent
abstract class ConcealComp implements Teamc {
	@Import float x, y, hitSize;
	@Import Team team;

	float concealment;

	@Override @Replace(value = 1f) public boolean inFogTo(Team viewer) {
		if (team == viewer) return false;
		else if (concealment > 0f && !(self() instanceof Unitc u && u.isFlying())) return true;
		else if (!Vars.state.rules.fog) return false;
		else if (hitSize <= 16f) return !Vars.fogControl.isVisible(viewer, x, y);
		else {
			// For large hitsizes, check around the unit instead
			float trns = hitSize / 2f;
			for (var p : Geometry.d8) {
				if (Vars.fogControl.isVisible(viewer, x + p.x * trns, y + p.y * trns)) {
					return false;
				}
			}
			return true;
		}
	}

	@Override public void update() {
		concealment = Mathf.maxZero(concealment - Time.delta);
	}
}