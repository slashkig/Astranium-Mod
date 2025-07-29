package astramod.world.blocks.production;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import astramod.content.AstraFluids;

/** Draws liquid tanks for plasma drills. */
public class PlasmaDrill extends MultiCoolantDrill {
	public TextureRegion liquidRegion;
	public TextureRegion crystalRegion;

	public PlasmaDrill(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		liquidRegion = Core.atlas.find(name + "-liquid");
		crystalRegion = Core.atlas.find(name + "-crystal");
	}

	public class PlasmaDrillBuild extends MultiCoolantDrillBuild {
		@Override public void draw() {
			super.draw();
			Drawf.liquid(liquidRegion, x, y, liquids.get(AstraFluids.plasma) / liquidCapacity, AstraFluids.plasma.color);
			if (crystalRegion.found()) {
				Draw.alpha(warmup());
				Draw.z(Layer.block);
				Draw.rect(crystalRegion, x, y);
				Draw.reset();
			}
		}
	}
}