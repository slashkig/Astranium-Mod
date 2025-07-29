package astramod.world.blocks.production;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class LaserBore extends BeamDrill {
	public int baseSpeedTier = 2;
	public float hardnessMult = 0;

	public TextureRegion crystalRegion;

	public LaserBore(String name) {
		super(name);
		group = BlockGroup.drills;
	}

	@Override public void init() {
		super.init();

		// Dynamic output based on item hardness
		for (Item item : content.items()) {
			if (item.hardness <= tier && item.hardness != baseSpeedTier) {
				drillMultipliers.put(item, 1f - hardnessMult * (item.hardness - baseSpeedTier));
			}
		}
	}

	@Override public void load() {
		super.load();
		crystalRegion = Core.atlas.find(name + "-crystal");
	}

	public class LaserBoreBuild extends BeamDrillBuild {
		@Override public void draw() {
			super.draw();
			if (crystalRegion.found()) {
				Draw.alpha(warmup());
				Draw.z(Layer.block);
				Draw.rect(crystalRegion, x, y);
				Draw.reset();
			}
		}

		@Override public float warmup() {
			return warmup;
		}
	}
}