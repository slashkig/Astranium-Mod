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
	public TextureRegion heatRegion;
	public float alphaMag = 0f, alphaScl = 3f;
	public float maxAlpha = 1f;

	@Override public void load(Block block) {
		heatRegion = Core.atlas.find(block.name + "-heat");
	}

	@Override public void draw(Building build) {
		if (build.warmup() > 0f) {
			Draw.z(Layer.blockAfterCracks);

			if(alphaMag > 0f) Draw.alpha(build.warmup() * maxAlpha * (1f - alphaMag + Mathf.absin(Time.time, alphaScl, alphaMag)));
			else Draw.alpha(build.warmup() * maxAlpha);
			Draw.rect(heatRegion, build.x, build.y);
			Draw.color();
		}
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[0];
	}
}