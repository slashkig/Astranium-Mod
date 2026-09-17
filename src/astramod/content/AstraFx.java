package astramod.content;

import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import astramod.entities.bullet.BoltBulletType;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.graphics.*;

import static arc.graphics.g2d.Draw.alpha;
import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.lineAngle;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

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
		for(int i = 0; i < 6; i++){
			Fx.v.trns(Fx.rand.random(360f), Fx.rand.random(e.finpow() * 14f)).add(e.x, e.y);
			Fill.circle(Fx.v.x, Fx.v.y, Fx.rand.random(1.4f, 3.4f));
		}
	}).layer(Layer.bullet - 1f),

	oilSmoke = new Effect(180f, e -> {
		float length = 3f + e.finpow() * 20f;
		Fx.rand.setSeed(e.id);
		for(int i = 0; i < 13; i++){
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

	shootMediumFlame = new Effect(35f, 80f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkFlame, e.fin());
		Draw.alpha(0.8f + 0.2f * e.fout());

		Angles.randLenVectors(e.id, 12, e.finpow() * 90f, e.rotation, 20f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.5f + e.fout() * 2f);
		});
	}),

	shootWideFlame = new Effect(34f, 80f, e -> {
		Draw.color(Pal.lightFlame, Pal.darkFlame, Pal.darkerGray, e.fin());
		Draw.alpha(0.7f + 0.3f * e.foutpow());

		Angles.randLenVectors(e.id, 12, e.finpow() * 75f, e.rotation, 25f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 1.2f + e.fin() * 1.5f);
		});
	}),

	applyShield = new Effect(20f, e -> {
		Draw.color(e.color);
		Draw.alpha(e.fslope());
		Icon.defense.draw(e.x - 4f, e.y - 4f, 8f, 8f);
	}),

	attractMetalParticles = new Effect(60f, e -> {
		Draw.color(Pal.darkestMetal);
		Draw.alpha(e.fout());

		Angles.randLenVectors(e.id, 1, 2f + e.foutpow() * 20f, (x, y) -> {
			Fill.poly(e.x + x, e.y + y, 6, 1.5f);
		});
	}) {{ layer += 1f; }},

	radiate = new Effect(30f, e -> {
		Draw.color(e.color, Color.white, e.fin());
		Lines.stroke(0.2f + 0.8f * e.fout());
		Mathf.rand.setSeed(e.id);

		tmp.trns(e.data instanceof Position pos ? pos.angleTo(e.x, e.y) : Mathf.random(360f),
			Mathf.random(2f + e.fin() * 16f));
		Lines.line(e.x + tmp.x * 0.5f, e.y + tmp.y * 0.5f, e.x + tmp.x, e.y + tmp.y);
	}),

	charged = new Effect(40f, e -> {
		Draw.color(e.color);

		Angles.randLenVectors(e.id, 2, 1f + e.fin() * 8f, (x, y) -> {
			Fill.square(e.x + x, e.y + y, e.fslope() * 1.8f, 45f);
			Drawf.light(e.x + x, e.y + y, e.fslope() * 4f, e.color, e.fslope() * 0.5f);
		});
	}),

	overcharged1 = new Effect(20f, e -> {
		Draw.color(e.color);
		Lines.stroke(e.fout());

		Lines.circle(e.x, e.y, 2f + 8f * e.fin());
	}),

	overcharged2 = new Effect(30f, e -> {
		Draw.color(e.color);
		Lines.stroke(2f * e.fout());

		Lines.circle(e.x, e.y, 2f + 14f * e.fin());
	}),

	overcharged3 = new Effect(40f, e -> {
		Draw.color(e.color);
		Lines.stroke(3f * e.fout());

		Lines.circle(e.x, e.y, 2f + 20f * e.fin());
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

	public static void setSparks(ExplosionEffect effect, Color sparkColor, int sparks, float sparkRad, float sparkLen, float sparkStroke) {
		effect.sparkColor = sparkColor;
		effect.sparks = sparks;
		effect.sparkRad = sparkRad;
		effect.sparkLen = sparkLen;
		effect.sparkStroke = sparkStroke;
	}
	public static void setWave(ExplosionEffect effect, Color waveColor, float waveLife, float radius, float stroke) {
		effect.waveColor = waveColor;
		effect.waveLife = waveLife;
		effect.waveRad = radius;
		effect.waveStroke = stroke;
	}
	public static void setWave(ExplosionEffect effect, Color waveColor, float radius, float stroke) {
		effect.waveColor = waveColor;
		effect.waveRad = radius;
		effect.waveStroke = stroke;
	}

	public static ExplosionEffect dynamicBurstSmall(BasicBulletType b) {
		return new ExplosionEffect() {{
			lifetime = 9f;
			smokeSize = 0f;
			smokeSizeBase = 0f;
			setWave(this, b.backColor, 12f, 6f);
			setSparks(this, b.frontColor, 6, 15f, 3f, 3f);
		}};
	}
	public static ExplosionEffect dynamicBurstSmall(Color waveColor, Color sparkColor) {
		return new ExplosionEffect() {{
			lifetime = 9f;
			smokeSize = 0f;
			smokeSizeBase = 0f;
			setWave(this, waveColor, 8f, 12f, 6f);
			setSparks(this, sparkColor, 6, 15f, 3f, 3f);
		}};
	}

	public static ExplosionEffect dynamicBurstLarge(BasicBulletType b) {
		return new ExplosionEffect() {{
			lifetime = 15f;
			smokeSize = 0f;
			smokeSizeBase = 0f;
			setWave(this, b.backColor, 12f, 25f, 12f);
			setSparks(this, b.frontColor, 12, 30, 5, 6);
		}};
	}
	public static ExplosionEffect dynamicBurstLarge(Color waveColor, Color sparkColor) {
		return new ExplosionEffect() {{
			lifetime = 15f;
			smokeSize = 0f;
			smokeSizeBase = 0f;
			setWave(this, waveColor, 12f, 25f, 12f);
			setSparks(this, sparkColor, 12, 30, 5, 6);
		}};
	}

	public static ExplosionEffect dynamicExplosion(BasicBulletType b) {
		return new ExplosionEffect() {{
			waveRad = b.splashDamageRadius;
			smokeRad = sparkRad = b.splashDamageRadius * 1.5f;
			waveColor = b.frontColor;
			sparkColor = b.backColor;
		}};
	}

	public static Effect smokeScreen(float smokeRad, Color smokeColor) {
		return new Effect(140, 200f, b -> {
			float intensity = 6.8f;
			float baseLifetime = 80f + intensity * 11f;
			b.lifetime = 50f + intensity * 65f;

			color(smokeColor);
			alpha(0.8f);
			for(int i = 0; i < 4; i++){
				Fx.rand.setSeed(b.id * 2 + i);
				float lenScl = Fx.rand.random(0.4f, 1f);
				int fi = i;
				b.scaled(b.lifetime * lenScl, e -> {
					randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2.9f * intensity), smokeRad * intensity, (x, y, in, out) -> {
						float fout = e.fout(Interp.pow5Out) * Fx.rand.random(0.5f, 1f);
						float rad = fout * ((2f + intensity) * 2.35f);

						Fill.circle(e.x + x, e.y + y, rad);
						Drawf.light(e.x + x, e.y + y, rad * 2.5f, AstraPal.magnetFront, 0.5f);
					});
				});
			}

			b.scaled(baseLifetime, e -> {
				Draw.color(AstraPal.magnetBack, AstraPal.magnetFront, e.fin());
				e.scaled(5 + intensity * 2f, i -> {
					stroke((3.1f + intensity/5f) * i.fout());
					Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
					Drawf.light(e.x, e.y, i.fin() * 14f * 2f * intensity, Color.white, 0.9f * e.fout());
				});
			});
		});
	}

	public static Effect boltPierce(BoltBulletType bolt, float boltWaveWidth, float boltWaveLen, int sparkCount) {
		return new Effect(24f, e -> {
			e.scaled(10f, b -> {
				color(bolt.frontColor, bolt.frontColor, b.fin());
				stroke(b.fout() * 3f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * 20f);
			});

			color(bolt.backColor);

			for(int i : Mathf.signs){
				Drawf.tri(e.x, e.y, boltWaveWidth * e.fout(), boltWaveLen, e.rotation + 90f * i);
			}
			color(bolt.frontColor, e.color, e.fin());
			stroke(e.fout() * 1.3f + 0.7f);

			randLenVectors(e.id, sparkCount, 41f * e.fin(), e.rotation, 10f, (x, y) -> {
				lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 10f + 0.5f);
			});
		});
	}
	public static Effect railgunShoot(BoltBulletType bolt, float widthSide, float lenSide, float widthFront, float lenFront) {
		return new Effect(24f, e -> {
			e.scaled(10f, b -> {
				color(Color.white, bolt.backColor, b.fin());
				stroke(b.fout() * 3f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * 50f);
			});

			color(bolt.backColor);

			for(int i : Mathf.signs){
				Drawf.tri(e.x, e.y, widthSide * e.fout(), lenSide, e.rotation + 90f * i);
				Drawf.tri(e.x, e.y, widthFront * e.fout(), lenFront, e.rotation + 20f * i);
			}

			Drawf.light(e.x, e.y, 180f, bolt.backColor, 0.9f * e.fout());
		});
	}
	public static Effect railgunShoot(BoltBulletType bolt, float width, float len) {
		return new Effect(24f, e -> {
			e.scaled(10f, b -> {
				color(Color.white, bolt.backColor, b.fin());
				stroke(b.fout() * 3f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * 50f);
			});

			color(bolt.backColor);

			for(int i : Mathf.signs){
				Drawf.tri(e.x, e.y, width * e.fout(), len, e.rotation + 90f * i);
				Drawf.tri(e.x, e.y, width * e.fout(), len, e.rotation + 20f * i);
			}

			Drawf.light(e.x, e.y, 180f, bolt.backColor, 0.9f * e.fout());
		});
	}
}