package astramod.world.draw;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.DrawBlock;

public class DrawTopHeat extends DrawBlock {
	public TextureRegion heat;

	@Override public void load(Block block) {
		heat = Core.atlas.find(block.name + "-heat");
	}

	@Override public void draw(Building build) {
		if (build.warmup() > 0f) {
			Draw.z(Layer.block + 0.01f);

			Draw.alpha(build.warmup());
			Draw.rect(heat, build.x, build.y);

			Draw.color();
		}
	}
}