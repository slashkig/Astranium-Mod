package astramod.entities.part;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.part.*;
import mindustry.graphics.*;
import astramod.math.*;

public class FirePart extends DrawPart {
	protected static final Rand rand = new Rand();

	public float minProgress = 0.5f;
	public int particleCount = 10;
	public float particleSize = 1f, particleLife = 30f, particleDist = 4f;
	public float particleSizeChange = -0.25f;
	public float angleRange = 45f;
	public float distOffset = 0f;
	public PartProgress progress = PartProgress.warmup;
	public Interp moveInterp = Interp.linear;
	public Interp angleInterp = new CubicPeakInterp(0.4f, 0.5f);
	public Color startColor = Pal.lightFlame, endColor = Pal.darkFlame;
	public float startAlpha = 1f, endAlpha = 0.75f;
	public float layer = Layer.turret + 1;

	@Override public void load(String name) { }

	@Override public void draw(PartParams params) {
		float p = progress.getClamp(params);
		if (p > minProgress) {
			rand.setSeed(Point2.pack((int)params.x, (int)params.y));
			float base = Time.time / particleLife;
			Draw.z(layer);

			float fin, dist, angle;
			for (int i = 0; i < particleCount; i++) {
				fin = (rand.random(2f) + base) % 1f;
				dist = distOffset - particleDist * moveInterp.apply(fin);
				angle = params.rotation + rand.range(angleRange) * angleInterp.apply(fin);

				Draw.color(startColor, endColor, fin);
				Draw.alpha(Mathf.lerp(startAlpha, endAlpha, fin) * p);
				Fill.circle(
					params.x + Angles.trnsx(angle, dist),
					params.y + Angles.trnsy(angle, dist),
					particleSize + particleSizeChange * fin
				);
			}

			Draw.reset();
		}
	}
}