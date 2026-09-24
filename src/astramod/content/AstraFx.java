package astramod.content;

import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.graphics.*;

public class AstraFx {
	public static final Vec2 tmp = new Vec2();

	public static final Effect

	pulverizePurple = new Effect(40f, e -> {
		Angles.randLenVectors(e.id, 5, 3f + e.fin() * 8f, (x, y) -> {
			Draw.color(AstraPal.plasmaGlowPurple, Pal.stoneGray, e.fin());
			Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45);
		});
	}),

	steamGenerate = new Effect(100f, e -> {
		Draw.color(AstraFluids.steam.color);
		Draw.alpha(e.fslope() * 0.8f);

		Fx.rand.setSeed(e.id);
		for (int i = 0; i < 6; i++) {
			Fx.v.trns(Fx.rand.random(360f), Fx.rand.random(e.finpow() * 14f)).add(e.x, e.y);
			Fill.circle(Fx.v.x, Fx.v.y, Fx.rand.random(1.4f, 3.4f));
		}
	}).layer(Layer.bullet - 1f),

	oilSmoke = new Effect(180f, e -> {
		float length = 3f + e.finpow() * 20f;
		Fx.rand.setSeed(e.id);
		for (int i = 0; i < 13; i++) {
			Fx.v.trns(Fx.rand.random(360f), Fx.rand.random(length));
			float sizer = Fx.rand.random(1.3f, 3.7f);

			e.scaled(e.lifetime * Fx.rand.random(0.5f, 1f), b -> {
				Draw.color(Color.grays(0.3f), b.fslope());

				Fill.circle(e.x + Fx.v.x, e.y + Fx.v.y, sizer + b.fslope() * 1.2f);
			});
		}
	}).startDelay(30f),

	octShieldBreak = new Effect(40f, e -> {
		Draw.color(e.color);
		Lines.stroke(3f * e.fout());
		Lines.poly(e.x, e.y, 8, e.rotation + e.fin(), 22.5f);
	}),

	colorLaser = new Effect(8f, e -> {
		Color color = e.data instanceof Teamc t ? t.team().color : e.color;
		Draw.color(Color.white, color, e.fin());
		Lines.stroke(0.5f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 5f);

		Drawf.light(e.x, e.y, 23f, color, e.fout() * 0.7f);
	}),

	sonicPulse = new Effect(12f, e -> {
		Draw.color(Color.white, AstraPal.sonicShotBack, e.fin());
		Lines.stroke(0.5f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 3f);

		Drawf.light(e.x, e.y, 23f, AstraPal.sonicShotBack, e.fout() * 0.7f);
	}),

	sonicHit = new Effect(20f, e -> {
		Draw.color(Color.white, AstraPal.sonicShotBack, e.fin());
		Lines.stroke(0.1f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 8f);
	}),

	emberCoalFlame = new Effect(35f, 80f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkFlame, e.fin());
		Draw.alpha(0.8f + 0.2f * e.fout());

		Angles.randLenVectors(e.id, 12, e.finpow() * 90f, e.rotation, 20f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.5f + e.fin() * 4f);
			Drawf.light(e.x, e.y, 6f, Pal.lightPyraFlame, e.fout() + 0.3f);
		});
	}),

	emberPyraFlame = new Effect(34f, 80f, e -> {
		Draw.color(Pal.lightFlame, Pal.darkFlame, Pal.darkerGray, e.fin());
		Draw.alpha(0.7f + 0.3f * e.fout());

		Angles.randLenVectors(e.id, 12, e.finpow() * 75f, e.rotation, 25f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 1.2f + e.fin() * 1.5f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.6f);
		});
	}),

	applyShield = new Effect(20f, e -> {
		Draw.color(e.color);
		Draw.alpha(e.fslope());
		Icon.defense.draw(e.x - 4f, e.y - 4f, 8f, 8f);
	}),

	attractMetalParticles = new Effect(60f, e -> {
		Draw.color(AstraFluids.ferrofluid.color);
		Draw.alpha(e.fout());

		e.scaled(60f, b -> {
			Draw.color(AstraFluids.ferrofluid.color, e.fin());
			Lines.stroke(3.5f * b.fslope());
			Lines.circle(b.x, b.y, 32f * b.fout());
		});

		Angles.randLenVectors(e.id, 1, 2f + e.foutpow() * 20f, (x, y) -> {
			Fill.poly(e.x + x, e.y + y, 6, 1.5f);
		});
	}).layer(Layer.effect + 1f).followParent(true).rotWithParent(true),

	radiate = new Effect(30f, e -> {
		Draw.color(e.color, Color.white, e.fin());
		Lines.stroke(0.2f + 0.8f * e.fout());
		Mathf.rand.setSeed(e.id);

		tmp.trns(e.data instanceof Position pos ? pos.angleTo(e.x, e.y) : Mathf.random(360f),
			Mathf.random(2f + e.fin() * 16f));
		Lines.line(e.x + tmp.x * 0.5f, e.y + tmp.y * 0.5f, e.x + tmp.x, e.y + tmp.y);
	}),

	charged1 = new Effect(40f, e -> {
		Draw.color(AstraPal.crystalBack);
		Angles.randLenVectors(e.id, 2, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});
	}),
	charged2 = new Effect(30f, e -> {
		Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fout());
		Angles.randLenVectors(e.id, 2, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});
		e.scaled(15f, c -> {
			Seq<Vec2> lines = new Seq<>();
			float sparkX = e.x, sparkY = e.y;
			for (int i = 0; i < 3; i++) {
				lines.add(new Vec2(sparkX + Mathf.range(4f), sparkY + Mathf.range(4f)));
			}

			Lines.stroke(3f * e.fout());
			Draw.color(AstraPal.crystalGlow, AstraPal.crystalFront, e.fin());
			for (int i = 0; i < lines.size - 1; i++) {
				Vec2 cur = lines.get(i);
				Vec2 next = lines.get(i + 1);

				Lines.line(cur.x, cur.y, next.x, next.y, false);
			}

			for (Vec2 p : lines) {
				Fill.circle(p.x, p.y, Lines.getStroke() / 2f);
			}
		});
	}),
	charged3 = new Effect(30f, e -> {
		Draw.color(AstraPal.crystalGlow, AstraPal.crystalFront, AstraPal.crystalBack, e.fout());
		Angles.randLenVectors(e.id, 4, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});
		e.scaled(25f, c -> {
			Seq<Vec2> lines = new Seq<>();
			float sparkX = e.x, sparkY = e.y;
			for (int i = 0; i < 5; i++) {
				lines.add(new Vec2(sparkX + Mathf.range(8f), sparkY + Mathf.range(8f)));
			}

			Lines.stroke(3f * e.fout());
			Draw.color(Color.white, AstraPal.crystalGlow, e.fin());
			for (int i = 0; i < lines.size - 1; i++) {
				Vec2 cur = lines.get(i);
				Vec2 next = lines.get(i + 1);

				Lines.line(cur.x, cur.y, next.x, next.y, false);
			}

			for (Vec2 p : lines) {
				Fill.circle(p.x, p.y, Lines.getStroke() / 2f);
			}
		});
	}),

	overcharged1 = new Effect(20f, e -> {
		if(!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.3f;
		float length = (radius/2f + e.finpow() * radius * 1.25f) * e.fin();

		e.scaled(16f, c -> {
			Draw.color(AstraPal.crystalFront, 0.9f);
			Lines.stroke(c.fout() * 2f + 0.5f);

			Angles.randLenVectors(e.id, (int)(radius * 1.2f) + 3, length, (x, y) -> {
				Lines.lineAngle(c.x + x, c.y + y, Mathf.angle(x, y), e.fslope() * 10f + 0.5f);
			});
		});

		Draw.color(AstraPal.crystalBack, e.fin());
		Lines.stroke(e.fout() + 2f);
		Lines.circle(e.x, e.y, radius * e.fin());
	}),

	overcharged2 = new Effect(20f, e -> {
		if(!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.4f;
		float length = (radius/2f + e.finpow() * radius * 1.25f) * e.fin();

		e.scaled(16f, c -> {
			Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fin());
			Lines.stroke(c.fout() * 2f + 0.5f);

			Angles.randLenVectors(e.id, (int)(radius * 1.2f), length, (x, y) -> {
				Lines.lineAngle(c.x + x, c.y + y, Mathf.angle(x, y), e.fslope() * 10f + 0.5f);
			});
		});

		Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fin());
		Lines.stroke(e.fout() + 3f);
		Lines.circle(e.x, e.y, radius * e.fin());
	}),

	overcharged3 = new Effect(20f, e -> {
		if(!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.6f;
		float length = (radius/2f + e.finpow() * radius * 1.25f) * e.fin();

		e.scaled(18f, c -> {
			Draw.color(AstraPal.crystalGlow, AstraPal.crystalFront, e.fin());
			Lines.stroke(c.fout() * 2f + 0.5f);

			Angles.randLenVectors(e.id, (int)(radius * 1.2f), length, (x, y) -> {
				Lines.lineAngle(c.x + x, c.y + y, Mathf.angle(x, y), e.fslope() * 10f + 0.5f);
			});
		});

		Draw.color(AstraPal.crystalGlow, AstraPal.crystalFront, AstraPal.crystalBack, e.fin());
		Lines.stroke(e.fout() + 4f);
		Lines.circle(e.x, e.y, radius * e.fin());
	}),

	hitCrystal = new Effect(8, e -> {
		Draw.color(AstraPal.crystalFront, AstraPal.crystalShoot, e.fin());
		Lines.stroke(0.5f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 5f);

		Drawf.light(e.x, e.y, 23f, Pal.heal, e.fout() * 0.7f);
	}),

	shootCrystal = new Effect(8, e -> {
		Draw.color(AstraPal.crystalShoot);
		float w = 1f + 5 * e.fout();
		Drawf.tri(e.x, e.y, w, 17f * e.fout(), e.rotation);
		Drawf.tri(e.x, e.y, w, 4f * e.fout(), e.rotation + 180f);
	}),

	crystalCharge = new Effect(30f, e -> {
		Draw.color(AstraPal.crystalLazerLight);

		Angles.randLenVectors(e.id, 14, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
			Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
		});
	}),

	crystalChargeBall = new Effect(60f, e -> {
		float margin = 1f - Mathf.curve(e.fin(), 0.9f);
		float fin = Math.min(margin, e.fin());

		Draw.color(AstraPal.crystalRed);
		Fill.circle(e.x, e.y, fin * 3f);

		Draw.color();
		Fill.circle(e.x, e.y, fin * 2f);
	}),

	crystalShockwave = new Effect(9f, 80f, e -> {
		Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fin());
		Lines.stroke(e.fout() * 2f + 0.2f);
		Lines.circle(e.x, e.y, e.fin() * 22f);
	});

	public static Effect dynamicExplosion(BasicBulletType b) {
		return dynamicExplosion(b, 10, 13, false);
	}
	public static Effect dynamicExplosion(BasicBulletType b, boolean squareBits) {
		return dynamicExplosion(b, 10, 13, squareBits);
	}

	public static Effect dynamicExplosion(BasicBulletType b, int bitDensity, int smokeDensity, boolean squareBits) {
		return new Effect ( 15f, e -> {
			e.scaled(12f, i -> {
				Draw.color(b.frontColor, b.backColor, e.fin());
				Lines.stroke(6f * i.fout());
				Lines.circle(e.x, e.y, 2f + i.fin() * b.splashDamageRadius);
			});

			Draw.color(Color.gray, e.fin());
			Angles.randLenVectors(e.id, smokeDensity, b.splashDamageRadius * 1.2f * e.finpow(), (x, y) -> {
				Fill.circle(e.x + x, e.y + y, e.fout() * 6f + 0.5f);
			});

			if (squareBits) {
				Mathf.rand.setSeed(e.id);
				Draw.color(b.frontColor, b.backColor, e.fin());
				Angles.randLenVectors(e.id, bitDensity, b.splashDamageRadius * 0.8f * e.finpow(), (x, y) -> {
					Fill.square(e.x + x, e.y + y, e.fout() * 3f, Mathf.rand.random(0f, 180f));
					Drawf.light(e.x, e.y, 16f, b.frontColor, 0.6f * e.fout());
				});
			} else {
				Draw.color(b.frontColor);
				Lines.stroke(1f);
				Angles.randLenVectors(e.id + 1, bitDensity, 1f + b.splashDamageRadius * 1.5f * e.finpow(), (x, y) -> {
					Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout());
					Drawf.light(e.x, e.y, 16f, b.frontColor, 0.6f * e.fout());
				});
			}
		});
	}

	public static Effect dynamicBurst(BasicBulletType b, boolean isLarge) {
		return dynamicBurst(b.frontColor, b.backColor, isLarge);
	}
	public static Effect dynamicBurst(Color lightClr, Color darkClr, boolean isLarge) {
		float rad = isLarge ? 25f : 12;
		int bitDensity = isLarge ? 12 : 6;
		return dynamicBurst(lightClr, darkClr, rad, bitDensity);
	}
	public static Effect dynamicExplosionMassive(Color lightClr, Color darkClr) {
		return dynamicBurst(lightClr, darkClr, 35f, 18);
	}

	public static Effect dynamicBurst(Color lightClr, Color darkClr, float rad, int bitDensity) {
		return new Effect ( 15f, e -> {
			e.scaled(15f, i -> {
				Draw.color(lightClr, darkClr, e.fin());
				Lines.stroke(6f * i.fout());
				Lines.circle(e.x, e.y, 2f + i.fin() * rad);
			});

			Draw.color(lightClr);
			Lines.stroke(1f);
			Angles.randLenVectors(e.id + 1, bitDensity, 1f + rad * 1.5f * e.finpow(), (x, y) -> {
				Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 1f + e.fout());
				Drawf.light(e.x, e.y, 16f, lightClr, 0.6f * e.fout());
			});
		});
	}

	public static Effect smokeScreen(float smokeRad, Color smokeColor) {
		return new Effect(140, 200f, b -> {
			float intensity = 6.8f;
			float baseLifetime = 80f + intensity * 11f;
			b.lifetime = 50f + intensity * 65f;

			Draw.color(smokeColor);
			Draw.alpha(0.8f);
			for (int i = 0; i < 4; i++) {
				Fx.rand.setSeed(b.id * 2 + i);
				float lenScl = Fx.rand.random(0.4f, 1f);
				int fi = i;
				b.scaled(b.lifetime * lenScl, e -> {
					Angles.randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), smokeRad * intensity, (x, y, in, out) -> {
						float fout = e.fout(Interp.pow5Out) * Fx.rand.random(0.5f, 1f);
						float rad = fout * ((2f + intensity) * 2.35f);

						Fill.circle(e.x + x, e.y + y, rad);
						Drawf.light(e.x + x, e.y + y, rad * 2.5f, smokeColor, 0.5f);
					});
				});
			}

			b.scaled(baseLifetime, e -> {
				Draw.color(smokeColor, Color.white, e.fin());
				e.scaled(5 + intensity * 2f, i -> {
					Lines.stroke((3.1f + intensity / 5f) * i.fout());
					Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
					Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
				});
			});
		});
	}

	public static Effect boltPierce(BasicBulletType bolt, float boltWaveWidth, float boltWaveLen, float circleSize , int sparkCount) {
		return new Effect(24f, e -> {
			e.scaled(10f, b -> {
				Draw.color(bolt.frontColor, b.fin());
				Lines.stroke(b.fout() * 3f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * circleSize);
			});

			Draw.color(bolt.backColor);
			for (int i : Mathf.signs) {
				Drawf.tri(e.x, e.y, boltWaveWidth * e.fout(), boltWaveLen, e.rotation + 90f * i);
			}

			Draw.color(bolt.frontColor, e.color, e.fin());
			Lines.stroke(e.fout() * 1.3f + 0.7f);
			Angles.randLenVectors(e.id, sparkCount, 41f * e.fin(), e.rotation, 10f, (x, y) -> {
				Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 10f + 0.5f);
			});
		});
	}

	public static Effect railgunShoot(BasicBulletType bolt, float width, float len) {
		return railgunShoot(bolt, width, len, width, len);
	}
	public static Effect railgunShoot(BasicBulletType bolt, float widthSide, float lenSide, float widthFront, float lenFront) {
		return new Effect(24f, e -> {
			e.scaled(24f, b -> {
				Draw.color(bolt.frontColor, bolt.backColor, e.fin());
				Lines.stroke(e.fout() * 1.3f + 0.7f);
				Angles.randLenVectors(e.id, 10, 41f * e.fin(), e.rotation, 10f, (x, y) -> {
					Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 16f + 0.5f);
				});
			});

			Draw.color(bolt.backColor);
			for (int i : Mathf.signs) {
				Drawf.tri(e.x, e.y, widthSide * e.fout(), lenSide, e.rotation + 40f * i);
			}

			Draw.color(bolt.frontColor);
			for (int i : Mathf.signs) {
				Drawf.tri(e.x, e.y, widthFront * e.fout(), lenFront, e.rotation + 20f * i);
				Drawf.tri(e.x, e.y, widthFront, (lenFront * 0.4f) * e.fout(), e.rotation + 180f);
			}

			e.scaled(10f, b -> {
				Draw.color(bolt.frontColor, bolt.backColor, e.fin());
				Lines.stroke(b.fout() * 2f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * 30f);
			});

			Drawf.light(e.x, e.y, 180f, bolt.backColor, 0.9f * e.fout());
		});
	}


	public static Effect mortarShoot(ArtilleryBulletType b) {
		return mortarShoot(b, 20, 7, 22f, 20f, true);
	}
	public static Effect mortarShoot(ArtilleryBulletType b, int smokeDensity, int squareDensity, float smokeRange, float muzzleFlareLen, boolean withShockwave) {
		return new Effect(35f, e -> {
			Draw.color(Pal.lightOrange, Color.gray, e.fin());
			Angles.randLenVectors(e.id, smokeDensity, e.finpow() * 29f, e.rotation, smokeRange, (x, y) -> {
				Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.1f);
			});

			Mathf.rand.setSeed(e.id);
			Draw.color(b.frontColor, b.backColor, e.fin());
			Angles.randLenVectors(e.id, squareDensity, 35f * e.finpow(), e.rotation, smokeRange * 1.5f, (x, y) -> {
				Fill.square(e.x + x, e.y + y, e.fout() * 3.2f, Mathf.rand.random(0f, 180f));
				Drawf.light(e.x, e.y, 16f, b.frontColor, 0.6f * e.fout());
			});

			Draw.color(Pal.lightOrange,Color.gray, e.fin());
			Angles.randLenVectors(e.id, smokeDensity / 2, e.finpow() * 29f, e.rotation, smokeRange, (x, y) -> {
				Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.1f);
			});

			/*float w = 1.2f + 5 * e.fout();*/
			e.scaled(15f, c -> {
				float w = (muzzleFlareLen / 2.5f) * e.fout();
				Draw.color(b.frontColor, b.backColor, e.fin());
				Drawf.tri(e.x, e.y, w, muzzleFlareLen * e.fout(), e.rotation);
				Drawf.tri(e.x, e.y, w, (muzzleFlareLen * 0.2f)* e.fout(), e.rotation + 180f);
			});

			if (withShockwave) {
				e.scaled( 10f, c -> {
					Draw.color(b.frontColor, b.backColor, e.fin());
					float circleSize = muzzleFlareLen * 2f;
					Lines.stroke(c.fout() * 2f + 0.2f);
					Lines.circle(e.x, e.y, e.fin() * circleSize);
				});
			}
		});
	}
}