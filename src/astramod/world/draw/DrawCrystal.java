package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.gen.Building;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawCrystal extends DrawBlock {
	public String suffix = "-crystal";
	public TextureRegion crystalRegion;

	public DrawCrystal() {
	}

	@Override public void load(Block block) {
		crystalRegion = Core.atlas.find(block.name + suffix);
	}

	@Override public void draw(Building build) {
		Draw.alpha(build.efficiency());
		Draw.rect(crystalRegion, build.x, build.y);
		Draw.color();
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] { crystalRegion };
	}
}