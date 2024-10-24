package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

/** Draws an pump cycle with 4 phases: still -> up -> rotate -> down */
public class DrawVerticalPump extends DrawBlock {
	public float cycleTime = 60f;
	public float maxScale = 1.5f;
	public boolean rotate = true;
	/** Speed of the down phase in proportion to the other phases. */
	public float downTime = 1f;

	public Effect downEffect = Fx.none;
	public String suffix = "-top";
	public TextureRegion topRegion;

	@Override public void load(Block block) {
		topRegion = Core.atlas.find(block.name + suffix);
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] { topRegion };
	}

	@Override public void draw(Building build) {
		float progress = (3f + downTime) * (build.totalProgress() % cycleTime) / cycleTime;
		if (progress <= 1f) {
			// Still phase
			Draw.rect(topRegion, build.x, build.y);
		} else if (progress <= 2f) {
			// Up phase
			float scl = Mathf.lerp(1f, maxScale, progress - 1f);
			Draw.scl(scl, scl);
			Draw.rect(topRegion, build.x, build.y);
		} else if (progress <= 3f) {
			// Rotate phase
			Draw.scl(maxScale, maxScale);
			if (rotate) Drawf.spinSprite(topRegion, build.x, build.y, Mathf.lerp(0f, 90f, progress - 2f));
			else Draw.rect(topRegion, build.x, build.y);
		} else {
			// Down phase
			float scl = Mathf.lerp(maxScale, 1f, (progress - 3f) / downTime);
			if (downEffect != Fx.none && Mathf.equal(scl, 1f, 0.025f)) {
				downEffect.at(build.x, build.y);
			}
			Draw.scl(scl, scl);
			Draw.rect(topRegion, build.x, build.y);
		}
		Draw.scl();
	}
}