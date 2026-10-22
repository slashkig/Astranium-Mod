package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import mindustry.Vars;
import mindustry.world.blocks.defense.*;
import astramod.graphics.*;

public class PhaseDoor extends AutoDoor {
	public float openAlpha = 0.6f;

	public PhaseDoor(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		if (!openRegion.found()) openRegion = region;
	}

	@Override public void init() {
		super.init();
		if (flashHit && openAlpha < 1f) {
			drawCached = false;
		}
	}
	
	public class PhaseDoorBuild extends AutoDoorBuild {
		@Override public void draw() {
			if (open) {
				if (Vars.renderer.animateShields) {
					DrawUtil.drawPhase(openRegion, x, y, openAlpha);
				} else {
					Draw.alpha(openAlpha);
					Draw.rect(openRegion, x, y);
					Draw.reset();
				}
			} else {
				Draw.rect(region, x, y);
			}
		}
	}
}