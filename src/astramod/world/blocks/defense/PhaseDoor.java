package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.Vars;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.*;
import astramod.graphics.*;

public class PhaseDoor extends AutoDoor {
	public float openAlpha = 0.5f;

	public PhaseDoor(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		if (!openRegion.found()) openRegion = region;
	}
	
	public class PhaseDoorBuild extends AutoDoorBuild {
		@Override public void draw() {
			if (open) {
				Draw.alpha(openAlpha);
				Draw.rect(openRegion, x, y);
				Draw.color(AstraPal.teamFaded[team.id]);
				Draw.z(Layer.shields);
				Fill.poly(x, y, 4, Vars.tilesize * Mathf.sqrt2, 45f);
				Draw.reset();
			} else {
				Draw.rect(region, x, y);
			}
		}
	}
}
