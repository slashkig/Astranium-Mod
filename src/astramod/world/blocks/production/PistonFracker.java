package astramod.world.blocks.production;

import arc.Core;
import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.world.draw.*;
import mindustry.world.blocks.production.Fracker;

// Fracker with pistons instead of a rotator
public class PistonFracker extends Fracker {
	public DrawPistons pistons = new DrawPistons();
	public TextureRegion bottomRegion;

	public PistonFracker(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		bottomRegion = Core.atlas.find(name + "-bottom");
		pistons.load(this);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] { region, pistons.iconRegion };
	}

	public class PistonFrackerBuild extends FrackerBuild {
		@Override public void updateTile() {
			super.updateTile();
			totalProgress += warmup * Time.delta;
		}

		@Override public void draw() {
			Draw.rect(bottomRegion, x, y);
			Draw.rect(region, x, y);
			Draw.z(Layer.blockCracks);
			drawCracks();
			Draw.z(Layer.blockAfterCracks);

			Drawf.liquid(liquidRegion, x, y, liquids.get(result) / liquidCapacity, result.color);
			pistons.draw(this);
		}
	}
}