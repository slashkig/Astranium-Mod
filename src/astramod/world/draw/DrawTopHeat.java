package astramod.world.draw;

import arc.*;
import arc.graphics.g2d.*;
import arc.util.*;
import arc.math.Mathf;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.DrawBlock;

public class DrawTopHeat extends DrawBlock {
	public TextureRegion heat;
	public boolean fluctuate = false;

	@Override public void load(Block block) {
		heat = Core.atlas.find(block.name + "-heat");
	}

	@Override public void draw(Building build) {
		if (build.warmup() > 0f) {
			Draw.z(Layer.blockAfterCracks);

			if(fluctuate) {
				float s = 0.3f;
				float ts = 0.6f;
				Draw.alpha(build.warmup() * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
			}
			else {
				Draw.alpha(build.warmup());
			}
			Draw.rect(heat, build.x, build.y);

			Draw.color();
		}
	}
}