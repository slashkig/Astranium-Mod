package astramod.ai.types;

import mindustry.Vars;
import mindustry.ai.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.logic.Ranged;

/** Unit must implement {@link BuildingTetherc}. */
public class AnchoredAI extends AIController {
	public float boundRadius;
	protected Building anchor;

	@Override public void init() {
		if (unit instanceof BuildingTetherc bt && bt.building() != null) {
			anchor(bt.building());
		} else {
			unit.kill();
		}
	}

	public void anchor(Building build) {
		anchor = build;
		if (build instanceof Ranged b) {
			boundRadius = b.range();
		}
	}

	@Override public void updateMovement() {
		if (anchor != null && unit.dst(anchor) > Vars.tilesize) {
			moveTo(anchor, 1f);
		}
	}
	
	@Override public boolean shouldFire() {
		return target != null && unit.inRange(target) && !hasStance(UnitStance.holdFire);
	}

	@Override public boolean invalid(Teamc target) {
		return super.invalid(target) || anchor.dst(target) > boundRadius * 1.1f && !hasStance(UnitStance.pursueTarget);
	}

	@Override public boolean isLogicControllable() {
		return false;
	}
}