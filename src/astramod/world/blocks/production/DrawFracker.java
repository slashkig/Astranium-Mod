package astramod.world.blocks.production;

import arc.audio.*;
import arc.graphics.g2d.*;
import mindustry.world.draw.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.Fracker;

/** Fracker that can use custom drawers. */
public class DrawFracker extends Fracker {
	public DrawBlock drawer = new DrawDefault();
	public Effect consumeEffect = Fx.none;
	public Sound consumeSound = Sounds.drillImpact;

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
			if (efficiency > 0 && accumulator >= itemUseTime) {
				consumeEffect.at(x, y);
				consumeSound.at(x, y);
			}
			super.updateTile();
			totalProgress += delta() * efficiency;
		}

		@Override public void draw() {
			drawer.draw(this);
		}
	}
}