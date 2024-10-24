package astramod.world.blocks.production;

import arc.util.*;
import arc.graphics.g2d.*;
import mindustry.world.draw.*;
import mindustry.world.blocks.production.Fracker;

/** Fracker that can use custom drawers. */
public class DrawFracker extends Fracker {
	public DrawBlock drawer = new DrawDefault();

	public DrawFracker(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		drawer.load(this);
	}

	@Override public TextureRegion[] icons() {
		return drawer.icons(this);
	}

	public class DrawFrackerBuild extends FrackerBuild {
		@Override public void updateTile() {
			super.updateTile();
			totalProgress += warmup * Time.delta;
		}

		@Override public void draw() {
			drawer.draw(this);
		}
	}
}