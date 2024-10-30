package astramod.world.draw;

import arc.util.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Vec2;
import mindustry.gen.*;
import mindustry.world.Block;
import mindustry.world.draw.*;
import astramod.graphics.*;

public class DrawPlasmaBall extends DrawBlock {
	public Color color = AstraPal.plasmaGlowPurple;
	public float lifetime = 10f;

	public int particles = 15;
	public float particleLength = 6f;
	public float particleWidth = 1f;
	public int particleSegments = 4;
	public float particleAngleDev = 45f;
	public float particleLenDev = 0.5f;

	public float ballAlpha = 0.8f;
	public float ballRad = 1.5f;
	public float ballMag = 0.25f;
	public float ballInMult = 0.6f;

	private float counter = 0f;
	private float prevTime = 0f;

	@Override public void draw(Building build) {
		if (build.warmup() > 0f) {
			rand.setSeed(build.id + (long)counter);
			if (prevTime + lifetime < Time.time) {
				counter = counter > particles ? 0 : counter + Time.delta;
				prevTime = Time.time;
			}

			for (int i = 0; i < (int)(particles * build.warmup()); i++) {
				float fin = 1f - ((rand.random(2f) + (Time.time / lifetime)) % 1f);
				float fout = 1f - fin;
				float lenLeft = particleLength;
				float angle = rand.random(360f);
				float posX = build.x;
				float posY = build.y;
				Vec2 next = new Vec2(posX, posY);

				Lines.stroke(particleWidth * fout);
				Draw.color(color, Color.white, fin);

				for (int seg = particleSegments; seg > 0; seg--) {
					float segLen = seg == 1 ? lenLeft : (lenLeft / seg) * (1f + rand.range(particleLenDev));
					lenLeft -= segLen;
					angle = (angle + rand.range(particleAngleDev)) % 360f;
					next = next.trns(angle, segLen).add(posX, posY);

					Lines.line(posX, posY, next.x, next.y, false);

					posX = next.x;
					posY = next.y;
				}
			}

			Draw.color(color);
			Draw.alpha(ballAlpha * build.warmup());
			Fill.circle(build.x, build.y, (ballRad + Mathf.absin(Time.time, lifetime, ballMag)) * build.warmup());
			Draw.color(1f, 1f, 1f, build.warmup());
			Fill.circle(build.x, build.y, (ballRad * ballInMult + Mathf.absin(Time.time, lifetime, ballMag * ballInMult)) * build.warmup());

			Draw.reset();
		}
	}
	
	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[0];
	}
}