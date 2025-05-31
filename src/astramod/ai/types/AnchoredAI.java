package astramod.ai.types;

import mindustry.Vars;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.logic.Ranged;

/** Unit must implement {@link BuildingTetherc}. */
public abstract class AnchoredAI extends AIController {
	public float boundRadius;
	private Building anchor;

	@Override public void init() {
		if (unit instanceof BuildingTetherc bt && bt.building() != null) {
			anchor(bt.building());
		}
	}

	public Building anchor() {
		return anchor;
	}

	public void anchor(Building build) {
		anchor = build;
		if (build instanceof Ranged b) {
			boundRadius = b.range();
		}
	}

	@Override public void updateMovement() {
		if (unit.dst(anchor) > Vars.tilesize) {
			moveTo(anchor, 0.1f);
		}
	}
}