package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import mindustry.graphics.*;
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
				Draw.draw(Layer.blockOver, () -> {
					AstraShaders.phase.region = openRegion;
					AstraShaders.phase.alpha = openAlpha;

					Draw.shader(AstraShaders.phase);
					Draw.rect(openRegion, x, y);
					Draw.shader();
				});
			} else {
				Draw.rect(region, x, y);
			}
		}
	}
}