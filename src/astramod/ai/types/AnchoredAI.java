package astramod.ai.types;

import mindustry.Vars;
import mindustry.entities.units.*;
import mindustry.gen.*;

public abstract class AnchoredAI extends AIController {
	public float boundRadius = 100f;

	public AnchoredAI(float bound) {
		boundRadius = bound;
	}

	public Building anchor() {
		return ((BuildingTetherc)unit).building();
	}

	@Override public void updateMovement() {
		Building anchor = anchor();
		if (unit.dst(anchor) > Vars.tilesize) {
			moveTo(anchor, 0.1f);
		}
	}
}