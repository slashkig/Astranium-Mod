package astramod.world.draw;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.Interp.*;
import arc.util.*;
import mindustry.world.Block;
import mindustry.world.draw.*;
import mindustry.graphics.*;
import mindustry.gen.*;

public class DrawEmitSmoke extends DrawBlock {
	public Color color = Color.valueOf("f2d585");

	public float alpha = 0.5f;
	public int particles = 30;
	public float particleLife = 70f, particleRad = 7f, addSizeMult = 0.2f, particleSize = 3f, fadeMargin = 0.4f, rotateScl = 3f;
	public Interp particleInterp = new PowIn(1.5f);
	public Blending blending = Blending.normal;
	public float layer = Layer.blockAdditive;

	@Override public void draw(Building build) {
		if (build.warmup() > 0f) {
			float a = alpha * build.warmup();
			float z = Draw.z();
			Draw.blend(blending);
			Draw.color(color);
			if (layer > 0) Draw.z(layer);

			rand.setSeed(build.id);
			for (int i = 0; i < particles; i++) {
				float fin = 1f - ((rand.random(2f) + (Time.time / particleLife)) % 1f);
				float fout = 1f - fin;
				float angle = rand.random(360f) + (Time.time / rotateScl) % 360f;
				float len = particleRad * particleInterp.apply(fout);
				Draw.alpha(a * (1f - Mathf.curve(fout, 1f - fadeMargin)));
				Fill.circle(
					build.x + Angles.trnsx(angle, len),
					build.y + Angles.trnsy(angle, len),
					particleSize * (addSizeMult + fout) * build.warmup()
				);
			}

			Draw.blend();
			Draw.reset();
			Draw.z(z);
		}
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[0];
	}
}