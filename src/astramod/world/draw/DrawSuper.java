package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.*;

public class DrawSuper extends DrawBlock {
	public TextureRegion previewRegion;

	@Override public void load(Block block) {
		previewRegion = Core.atlas.find(block.name + "-preview", block.region);
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] { previewRegion };
	}

	@Override public void draw(Building build) {
		((SuperDrawable)build).drawSuper();
	}
}
