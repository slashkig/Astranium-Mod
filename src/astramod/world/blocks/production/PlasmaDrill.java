package astramod.world.blocks.production;

import arc.Core;
import arc.util.Time;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import mindustry.graphics.*;
import astramod.content.AstraFluids;

/** Draws liquid tanks for plasma drills. */
public class PlasmaDrill extends MultiCoolantDrill {
	public TextureRegion liquidRegion;
	public TextureRegion crystalRegion;
	public TextureRegion blurRegion;
	public float blurThresh = 0.9f;

	public PlasmaDrill(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		liquidRegion = Core.atlas.find(name + "-liquid");
		crystalRegion = Core.atlas.find(name + "-crystal");
		blurRegion = Core.atlas.find(name + "-rotator-blur");
	}

	public class PlasmaDrillBuild extends MultiCoolantDrillBuild {
		@Override public void draw() {
			float s = 0.3f;
			float ts = 0.6f;

			Draw.rect(region, x, y);
			Draw.z(Layer.blockCracks);
			drawDefaultCracks();

			Draw.z(Layer.blockAfterCracks);
			if (drawRim) {
				Draw.color(heatColor);
				Draw.alpha(warmup * ts * (1f - s + Mathf.absin(Time.time, 3f, s)));
				Draw.blend(Blending.additive);
				Draw.rect(rimRegion, x, y);
				Draw.blend();
				Draw.color();
			}

			if (drawSpinSprite) {
				Drawf.spinSprite(drawBlur() ? blurRegion : rotatorRegion, x, y, timeDrilled * rotateSpeed);
			} else {
				Draw.rect(drawBlur() ? blurRegion : rotatorRegion, x, y, timeDrilled * rotateSpeed);
			}

			Draw.rect(topRegion, x, y);

			Drawf.liquid(liquidRegion, x, y, liquids.get(AstraFluids.plasma) / liquidCapacity, AstraFluids.plasma.color);
			if (crystalRegion.found()) {
				Draw.alpha(warmup());
				Draw.z(Layer.block);
				Draw.rect(crystalRegion, x, y);
				Draw.reset();
			}

			if (dominantItem != null && drawMineItem) {
				Draw.color(dominantItem.color);
				Draw.rect(itemRegion, x, y);
				Draw.color();
			}
		}

		public boolean drawBlur() {
			return blurRegion.found() && warmup >= blurThresh;
		}
	}
}