package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

/** Draws an pump cycle with 4 phases: still -> up -> rotate -> down */
public class DrawVerticalPump extends DrawBlock {
	public float cycleTime = 60f;
	public float maxScale = 1.5f;
	public boolean rotate = true;

	public String suffix = "-top";
	public TextureRegion topRegion;

	@Override public void load(Block block) {
		topRegion = Core.atlas.find(block.name + suffix);
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] { topRegion };
	}

	@Override public void draw(Building build) {
		float progress = 4f * (build.totalProgress() % cycleTime) / cycleTime;
		if (progress <= 1f) {
			Draw.rect(topRegion, build.x, build.y);
		} else if (progress <= 2f) {
			float scl = Mathf.lerp(1f, maxScale, progress - 1f);
			Draw.scl(scl, scl);
			Draw.rect(topRegion, build.x, build.y);
		} else if (progress <= 3f) {
			Draw.scl(maxScale, maxScale);
			if (rotate) Drawf.spinSprite(topRegion, build.x, build.y, Mathf.lerp(0f, 90f, progress - 2f));
			else Draw.rect(topRegion, build.x, build.y);
		} else if (progress <= 4f) {
			float scl = Mathf.lerp(maxScale, 1f, progress - 3f);
			Draw.scl(scl, scl);
			Draw.rect(topRegion, build.x, build.y);
		}
		Draw.scl();
	}
}