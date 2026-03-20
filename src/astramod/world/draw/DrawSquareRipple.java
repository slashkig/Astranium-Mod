package astramod.world.draw;

import static mindustry.Vars.tilesize;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.DrawBlock;

public class DrawSquareRipple extends DrawBlock {
	public Color color;
	public float minStroke = 0.2f, strokeMag = 2f;

	@Override public void draw(Building build) {
		float f = 1f - (Time.time / 100f) % 1f;
		float s = build.block.size * tilesize / 2f;

		Draw.color(color);
		Lines.stroke((strokeMag * f + minStroke) * build.warmup());
		Lines.square(build.x, build.y, Math.min(1f + (1f - f) * s, s));
		Draw.color();
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[0];
	}
}
