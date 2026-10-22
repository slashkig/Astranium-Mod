package astramod.content;

import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.graphics.*;

public class AstraFx {
	public static final Rand rand = new Rand();
	public static final Vec2 tmp = new Vec2();

	public static final Effect

	pulverizePurple = new Effect(40f, e -> {
		Angles.randLenVectors(e.id, 5, 3f + e.fin() * 8f, (x, y) -> {
			Draw.color(AstraPal.plasmaGlowPurple, Pal.stoneGray, e.fin());
			Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45f);
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

	emberPyraFlame = new Effect(40f, 80f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkerGray, e.fin());
		Draw.alpha(e.fout() * 4f);

		Angles.randLenVectors(e.id, 12, e.finpow() * 90f, e.rotation, 20f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.7f + e.fin() * 5f);
			Drawf.light(e.x, e.y, e.fin() * 6f, Pal.lightPyraFlame, e.fout() + 1f);
		});
	}),

	emberCoalFlame = new Effect(40f, 80f, e -> {
		Draw.color(Pal.lightFlame, Pal.darkFlame, Pal.darkerGray, e.fin());
		Draw.alpha(e.fout() * 2f);

		Angles.randLenVectors(e.id, 12, e.finpow() * 75f, e.rotation, 25f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 1.2f + e.fin() * 1.5f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});
	}),

	applyShield = new Effect(20f, e -> {
		Draw.color(e.color);
		Draw.alpha(e.fslope());
		Icon.defense.draw(e.x - 4f, e.y - 4f, 8f, 8f);
	}),

	magnetized = new Effect(60f, e -> {
		Draw.color(AstraFluids.ferrofluid.color);
		Draw.alpha(e.fout());
		Angles.randLenVectors(e.id, 1, 2f + e.foutpow() * 20f, (x, y) -> {
			Fill.poly(e.x + x, e.y + y, 6, 1.5f);
		});
	}).layer(Layer.effect + 1f).followParent(true).rotWithParent(true),

	radiate = new Effect(30f, e -> {
		Draw.color(e.color, Color.white, e.fin());
		Lines.stroke(0.2f + 0.8f * e.fout());
		rand.setSeed(e.id);

		tmp.trns(e.data instanceof Position pos ? pos.angleTo(e.x, e.y) : rand.random(360f),
			rand.random(2f + e.fin() * 16f));
		Lines.line(e.x + tmp.x * 0.5f, e.y + tmp.y * 0.5f, e.x + tmp.x, e.y + tmp.y);
	}),

	charged1 = new Effect(40f, e -> {
		Draw.color(AstraPal.crystalBack);
		Angles.randLenVectors(e.id, 2, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});
	}),

	charged2 = new Effect(40f, e -> {
		Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fout());
		Angles.randLenVectors(e.id, 2, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});

		e.scaled(15f, c -> {
			rand.setSeed(e.id);
			Seq<Vec2> lines = new Seq<>();
			float sparkX = e.x, sparkY = e.y;
			for (int i = 0; i < 3; i++) {
				lines.add(new Vec2(sparkX + rand.range(4f), sparkY + rand.range(4f)));
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

	charged3 = new Effect(40f, e -> {
		Draw.color(AstraPal.crystalGlow, AstraPal.crystalFront, AstraPal.crystalBack, e.fout());
		Angles.randLenVectors(e.id, 4, 1f + e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fslope() * 4f);
		});

		e.scaled(25f, c -> {
			rand.setSeed(e.id);
			Seq<Vec2> lines = new Seq<>();
			float sparkX = e.x, sparkY = e.y;
			for (int i = 0; i < 5; i++) {
				lines.add(new Vec2(sparkX + rand.range(8f), sparkY + rand.range(8f)));
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
		if (!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.3f;
		float length = (radius / 2f + e.finpow() * radius * 1.25f) * e.fin();

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
		if (!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.4f;
		float length = (radius / 2f + e.finpow() * radius * 1.25f) * e.fin();

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
		if (!(e.data instanceof Unit unit)) return;

		float radius = unit.hitSize() * 1.6f;
		float length = (radius / 2f + e.finpow() * radius * 1.25f) * e.fin();

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

	shootArbalest = new Effect (12f, e -> {
		float w = 5f * e.fout();
		float l = 12f * e.fout();
		Draw.color(Color.white, Pal.lightOrange, e.fin());
		Drawf.tri(e.x, e.y, w, 15f, e.rotation);
		Drawf.tri(e.x, e.y, w, 6f, e.rotation + 180f);
		for (int i : Mathf.signs) {
			Drawf.tri(e.x, e.y, w, l, e.rotation + 90f * i);
		}
		Drawf.light(e.x, e.y, 20f, Pal.lightOrange, e.fout() * 0.7f);
	}),

	hitSniper = new Effect(18f, e -> {
		Draw.color(Color.white, Pal.lightOrange, e.fin());
		e.scaled(7f, s -> {
			Lines.stroke(0.5f + s.fout());
			Lines.circle(e.x, e.y, s.fin() * 5f);
		});

		Lines.stroke(0.5f + e.fout());
		Angles.randLenVectors(e.id, 5, e.fin() * 15f, (x, y) -> {
			float ang = Mathf.angle(x, y);
			Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
		});
		Drawf.light(e.x, e.y, 20f, Pal.lightOrange, 0.6f * e.fout());

		float w = 4f * e.fout();
		float l = 15f;
		Draw.color(Color.white, Pal.lightOrange, e.fin());
		Drawf.tri(e.x, e.y, w, l, e.rotation);
		Drawf.tri(e.x, e.y, w, l, e.rotation + 180f);
		Drawf.light(e.x, e.y, 20f, Pal.lightOrange, e.foutpowdown() * 0.7f);
	}),

	shootMeissa = new MultiEffect(
		new Effect(12f, e -> {
			Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.fin());
			Lines.stroke(0.5f + e.fout());
			Lines.circle(e.x, e.y, e.fin() * 5f);
		}),
		energyFlare(AstraPal.crystalFront, AstraPal.crystalBack)
	),

	saiphCharge = new Effect(30f, e -> {
		Draw.color(AstraPal.crystalFront, AstraPal.crystalLazerLight, e.fout());

		Lines.stroke(0.2f + e.fout() * 2f);
		Angles.randLenVectors(e.id, 14, 1f + 20f * e.fout(), e.rotation, 120f, (x, y) -> {
			Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), e.fslope() * 3f + 1f);
			Drawf.light(e.x, e.y, 12f, AstraPal.astraFront, 0.6f * e.fout());
		});

		float fade = e.fin();
		Draw.color(AstraPal.crystalRed, AstraPal.crystalBack, fade);
		Fill.circle(e.x, e.y, fade * 3f);

		Draw.color(AstraPal.crystalBack, AstraPal.crystalFront, fade);
		Fill.circle(e.x, e.y, fade * 2f);
		Drawf.light(e.x, e.y, 16f, AstraPal.crystalBack, 0.8f * fade);
	}),

	saiphShoot = new MultiEffect(
		new Effect(9f, 80f, e -> {
			Draw.color(AstraPal.crystalFront, AstraPal.crystalBack, e.finpow());
			Lines.stroke(e.fout() * 2f + 0.2f);
			Lines.circle(e.x, e.y, e.fin() * 22f);
		}),
		energyFlare(AstraPal.crystalLazerLight, AstraPal.crystalFront, 2f)
	),

	hitLiquid = new Effect(16, e -> {
		Draw.color(e.color, 1f);

		Angles.randLenVectors(e.id, 5, 1f + e.fin() * 15f, e.rotation, 60f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f);
		});
	}).layer(Layer.effect + 1f),

	shootLiquid = new Effect(15f, 80f, e -> {
		Draw.color(e.color, 1f);

		Angles.randLenVectors(e.id, 2, e.finpow() * 15f, e.rotation, 11f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.5f + e.fout() * 2.5f);
		});
	}).layer(Layer.effect + 1f),

	shootGas = new Effect(15f, 80f, e -> {
		Draw.color(e.color);
		Draw.alpha(e.fout() * 2f);

		Angles.randLenVectors(e.id, 1, e.finpow() * 15f, e.rotation, 11f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.5f + e.fin() * 2.5f);
		});
	}),

	magneticMine = new Effect ( 40f, e -> {
		float rad = AstraBlocks.magneticMine.destroyBullet.splashDamageRadius;
		e.scaled(30, i -> {
			Draw.color(AstraPal.astraBack, AstraPal.astraFront, AstraPal.astraBack, i.fin());
			Lines.stroke(6f * i.fslope());
			Lines.poly(e.x, e.y, 6, 2f + i.fout() * rad, i.fout() * 3 * 60f);;
		});

		rand.setSeed(e.id);
		Draw.color(AstraPal.astraFront);
		Angles.randLenVectors(e.id, 20, rad * e.fout(), (x, y) -> {
			Fill.poly(e.x + x, e.y + y, 6, e.fslope() * 3f + rand.random(0f, 5f), rand.random(0f, 180f) * e.fslope());
			Drawf.light(e.x, e.y, 16f, AstraPal.astraFront, 0.6f * e.fout());
		});
	}),

	blastMine = new Effect (40f, e -> {
		float rad = AstraBlocks.blastMine.destroyBullet.splashDamageRadius;
		e.scaled(18f, i -> {
			Draw.color(Pal.blastAmmoFront, Pal.blastAmmoBack, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		Draw.color(Color.gray);
		Angles.randLenVectors(e.id, 13, rad * 1.2f * e.finpow(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 6f * e.fout());
		});

		Draw.color(Pal.blastAmmoFront, Pal.blastAmmoBack, e.finpow());
		Lines.stroke(3f * e.fout());
		Angles.randLenVectors(e.id + 1, 10, 1f + rad * 1.5f * e.fin(), (x, y) -> {
			Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 8f * e.fout());
			Drawf.light(e.x, e.y, 20f, Pal.blastAmmoFront, 0.6f * e.fout());
		});
	}),

	giantMine = new Effect (60f, e -> {
		float rad = AstraBlocks.giantMine.destroyBullet.splashDamageRadius;
		e.scaled(26f, i -> {
			Draw.color(Pal.blastAmmoFront, Pal.blastAmmoBack, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		rand.setSeed(e.id);
		Draw.color(Color.gray);
		Angles.randLenVectors(e.id, 18, rad * 1.2f * e.finpow(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, rand.random(6f, 10f) * e.fout());
		});

		Draw.color(Pal.blastAmmoFront, Pal.blastAmmoBack, e.finpow());
		Lines.stroke(5f * e.fout());
		Angles.randLenVectors(e.id + 1, 16, 1f + rad * 1.5f * e.fin(), (x, y) -> {
			Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 10f * e.fout());
			Drawf.light(e.x, e.y, 20f, Pal.blastAmmoFront, 0.6f * e.fout());
		});

		Draw.color(AstraPal.fireBulletFront, Pal.blastAmmoFront, e.finpow());
		Angles.randLenVectors(e.id + 1, 8, 1f + rad * 1.5f * e.fin(), (x, y) -> {
			Drawf.tri(e.x, e.y, 12f * e.fout(), (rad / 1.5f), rand.random(0f, 360f));
			Drawf.light(e.x, e.y, 20f, AstraPal.fireBulletFront, 0.6f * e.fout());
		});
	}),

	fragMine = new Effect (40f, e -> {
		float rad = AstraBlocks.fragMine.destroyBullet.splashDamageRadius + 24f;
		e.scaled(18f, i -> {
			Draw.color(Pal.plastaniumFront, Pal.plastaniumBack, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		Draw.color(Color.gray);
		Angles.randLenVectors(e.id, 13, rad * 1.2f * e.finpow(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 5f * e.fout());
		});


		e.scaled(20f, c -> {
			rand.setSeed(c.id);
			Draw.color(Pal.plastaniumFront, Pal.plastaniumBack, c.finpow());
			Angles.randLenVectors(c.id + 1, 10, 1f + (rad / 2f) * 1.5f * c.fin(), (x, y) -> {
				Fill.square(c.x + x, c.y + y, c.fout() * 4f, rand.random(0f, 180f));
				Drawf.light(c.x, c.y, 16f, Pal.plastaniumFront, 0.6f * c.fout());
			});
		});
	}),

	largeFragMine = new Effect (60f, e -> {
		float rad = AstraBlocks.largeFragMine.destroyBullet.splashDamageRadius + 25f;
		e.scaled(26f, i -> {
			Draw.color(Pal.plastaniumFront, Pal.plastaniumBack, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		rand.setSeed(e.id);
		Draw.color(Color.gray);
		Angles.randLenVectors(e.id, 15, rad * 1.2f * e.finpow(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, rand.random(6f, 12f) * e.fout());
		});

		e.scaled(20f, c -> {
			rand.setSeed(c.id);
			Draw.color(Pal.plastaniumFront, Pal.plastaniumBack, c.fin());
			Lines.stroke(5f * c.fout());
			Angles.randLenVectors(c.id + 1, 14, 1f + (rad / 2f) * c.fin(), (x, y) -> {
				Fill.square(c.x + x, c.y + y, c.fout() * 8f, rand.random(0f, 180f));
				Drawf.light(c.x, c.y, 20f, Pal.blastAmmoFront, 0.6f * c.fout());
			});
		});
	}),

	surgeMine = new Effect (40f, e -> {
		float rad = AstraBlocks.surgeMine.destroyBullet.splashDamageRadius;
		e.scaled(15f, i -> {
			Draw.color(Pal.surgeAmmoFront, Pal.surgeAmmoBack, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		Draw.color(Pal.surgeAmmoFront);
		Lines.stroke(4f * e.fout());
		Angles.randLenVectors(e.id + 1, 16, 1f + rad * 1.5f * e.fin(), (x, y) -> {
			Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 12f * e.fout());
			Drawf.light(e.x, e.y, 20f, Pal.blastAmmoFront, 0.6f * e.fout());
		});
	}),

	incendiaryMine = new Effect (120, e -> {
		float rad = AstraBlocks.incendiaryMine.destroyBullet.splashDamageRadius + 12f;
		e.scaled(18f, i -> {
			Draw.color(Pal.lightPyraFlame, Pal.lightPyraFlame, Pal.darkPyraFlame, i.fin());
			Lines.stroke(8f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * rad);
		});

		rand.setSeed(e.id);
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkerGray, e.fin());
		Angles.randLenVectors(e.id + 1, 15, 1f + rad * 1.5f * e.fout(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 4f);
			Drawf.light(e.x, e.y, 16f, Pal.lightPyraFlame, 0.6f * e.fout());
		});
	}),

	navalExplosion = new Effect (25, e -> {
		float rad = AstraBlocks.navalMine.destroyBullet.splashDamageRadius;
		Draw.color(AstraPal.fireBulletFront, Pal.gray, AstraPal.fireBulletFront, e.fslope());
		Lines.stroke(8f * e.fslope());
		Lines.circle(e.x, e.y, 3f + e.fslope() * rad * 1.3f);
	}),

	navalMineShockwave = new Effect(120, e -> {
		float rad = AstraBlocks.navalMine.destroyBullet.splashDamageRadius;
		Draw.color(AstraPal.waterWaveLightest, AstraPal.waterWaveLight, AstraPal.waterWave, e.fin());
		Draw.alpha(e.fout());
		Lines.stroke(8f * e.fout());
		Lines.circle(e.x, e.y, 50f * e.fin());

		rand.setSeed(e.id);
		Draw.color(Color.white, AstraPal.waterWaveLightest, AstraPal.waterWave, e.fin());
		Draw.alpha(e.fout());
		Angles.randLenVectors(e.id, 16, rad * 1.5f * e.fin(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, rand.random(6f, 20f) * e.fout());
		});
	}),

	navalMine = new MultiEffect(navalMineShockwave, navalExplosion),

	fireShootShotgun = new Effect(20f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkerGray, e.finpow());
		Angles.randLenVectors(e.id, 2, e.finpow() * 25f, e.rotation, 7f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});

		e.scaled(12f, c -> {
			Draw.color(Pal.lighterOrange, Pal.lightOrange, c.fin());
			float w = 6f * c.fout();
			Drawf.tri(e.x, e.y, w, 15f * c.fout(), c.rotation);
			Drawf.tri(e.x, e.y, w, 3f * c.fout(), c.rotation + 180f);
		});
	}),

	fireShoot = new Effect(25f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkerGray, e.finpow());
		Angles.randLenVectors(e.id, 6, e.finpow() * 25f, e.rotation, 7f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});

		e.scaled(12f, c -> {
			Draw.color(Pal.lighterOrange, Pal.lightOrange, c.fin());
			float w = 1f + 5 * c.fout();
			Drawf.tri(e.x, e.y, w, 15f * c.fout(), c.rotation);
			Drawf.tri(e.x, e.y, w, 3f * c.fout(), c.rotation + 180f);
		});
	}),

	fireShootLarge = new Effect(25f, e -> {
		Draw.color(Color.lightGray, Color.gray, e.fin());
		Angles.randLenVectors(e.id, 6, e.finpow() * 19f, e.rotation, 10f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
		});

		Draw.color(Pal.lightPyraFlame, Pal.darkerGray, e.finpow());
		Angles.randLenVectors(e.id, 10, e.finpow() * 25f, e.rotation, 18f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});

		e.scaled(12f, c -> {
			Draw.color(Pal.lighterOrange, Pal.lightOrange, c.fin());
			float w = 1f + 5 * c.fout();
			Drawf.tri(e.x, e.y, w, 15f * c.fout(), c.rotation);
			Drawf.tri(e.x, e.y, w, 3f * c.fout(), c.rotation + 180f);
		});
	}),

	fireBulletTrailSmall = new Effect(25f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkerGray, e.fin());
		rand.setSeed(e.id);

		float len = rand.random(0.3f, 0.8f);
		float angle = rand.random(360f);
		tmp.trns(angle, e.fin() * 10f * len);

		Fill.circle(e.x + tmp.x, e.y + tmp.y, 0.3f + e.fout() * rand.random(0.5f, 1f));
		Drawf.light(e.x, e.y, 20f, Pal.lightPyraFlame, 0.8f * e.foutpowdown());
	}).layer(Layer.bullet - 0.01f),

	fireBulletTrail = new Effect(25f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkerGray, e.fin());
		rand.setSeed(e.id);

		float len = rand.random(0.3f, 0.8f);
		float angle = rand.random(360f);
		tmp.trns(angle, e.fin() * 10f * len);

		Fill.circle(e.x + tmp.x, e.y + tmp.y, 0.4f + e.fout() * rand.random(1f, 2f));
		Drawf.light(e.x, e.y, 20f, Pal.lightPyraFlame, 0.8f * e.foutpowdown());
	}).layer(Layer.bullet - 0.01f),

	fireBulletHit = new Effect(20, e -> {
		rand.setSeed(e.id);
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());
		Angles.randLenVectors(e.id, 5, e.fin() * 8f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.foutpowdown() * 2f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});
	}),

	fireBulletHitLarge = new Effect(25, e -> {
		rand.setSeed(e.id);
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, e.fin());
		Angles.randLenVectors(e.id, 6, e.fin() * 18f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.foutpowdown() * 4f + 0.2f);
			Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
		});
	}),

	iceBulletTrail = new Effect(100f, e -> {
		Draw.color(AstraPal.freezeMist, AstraPal.freezeFront, e.finpowdown());
		rand.setSeed(e.id);

		float len = rand.random(0.3f, 0.8f);
		float angle = rand.random(360f);
		tmp.trns(angle, e.fin() * 10f * len);

		Draw.alpha(rand.random(0.3f, 0.8f) * e.fout());
		Fill.circle(e.x + tmp.x, e.y + tmp.y, 0.4f + e.foutpowdown() * rand.random(2f, 4f));
		Drawf.light(e.x, e.y, 20f, AstraPal.freezeBack, 0.8f * e.foutpowdown());
	}),

	iceShoot = new Effect(60f, e -> {
		rand.setSeed(e.id);
		Draw.color(AstraPal.freezeMist, AstraPal.freezeFront, e.finpowdown());
		Draw.alpha(rand.random(0.3f, 0.8f) * e.fout());
		Angles.randLenVectors(e.id, 12, e.finpow() * 30f, e.rotation, 30f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.foutpowdown() * rand.random(3f, 5f));
		});

		e.scaled(40f, icicle -> {
			rand.setSeed(e.id);
			Draw.color(AstraPal.freezeFront, AstraPal.freezeBack, icicle.foutpowdown());
			Angles.randLenVectors(e.id, 5, 40f * e.finpow(), e.rotation, 30, (x, y) -> {
				Fill.poly(e.x + x, e.y + y, 5, icicle.foutpowdown() * rand.random(1f, 3f), rand.random(0f, 180f));
				Drawf.light(e.x, e.y, 20f, AstraPal.freezeMist, 0.6f * icicle.fout());
			});
		});

		e.scaled(30, c -> {
			Draw.color(AstraPal.freezeFront, AstraPal.freezeBack, e.fout());
			float triW = 6f;
			for (int i : Mathf.signs) {
				Drawf.tri(e.x, e.y, 6f * c.fout(), 9f * c.fout(), e.rotation + 40f * i);
			}
			Drawf.tri(e.x, e.y, triW * c.fout(), 15f * c.fout(), e.rotation);
			Drawf.tri(e.x, e.y, triW * c.fout(), 7f * c.fout(), e.rotation + 180f);

			Draw.color(AstraPal.freezeMist, AstraPal.freezeFront, e.fout());
			Drawf.tri(e.x, e.y, triW * c.fout(), 10f * c.fout(), e.rotation);
			Drawf.tri(e.x, e.y, triW * c.fout(), 4f * c.fout(), e.rotation + 180f);
		});
	}),

	icyBurst = new Effect(100f, e -> {
		float rad = 16f;
		rand.setSeed(e.id);
		e.scaled(60f, i -> {
			Draw.color(AstraPal.freezeFront, AstraPal.freezeBack, e.fin());
			Lines.stroke(6f * i.fout());
			Lines.circle(e.x, e.y, 2f + i.fin() * 10);
		});

		Draw.color(AstraPal.freezeMist, AstraPal.freezeFront, e.finpowdown());
		Draw.alpha(rand.random(0.3f, 0.8f) * e.fout());
		Angles.randLenVectors(e.id, 14, rad * 1.2f * e.finpow(), (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.foutpowdown() * rand.random(3f, 6f));
		});

		int points = 6;
		float triW = 6f;
		float offset = Mathf.randomSeed(e.id, 360f);
		for(int i = 0; i < points; i++){
			float angle = i* 360f / points + offset;
			Draw.color(AstraPal.freezeFront, AstraPal.freezeBack, e.fin());
			Drawf.tri(e.x , e.y, triW * e.foutpowdown(), 10f * e.foutpowdown(), angle + 180f);
			Draw.color(AstraPal.freezeMist, AstraPal.freezeFront, e.fin());
			Drawf.tri(e.x , e.y, triW * e.foutpowdown(), 6f * e.foutpowdown(), angle + 180f);
		}

		Draw.color(AstraPal.freezeFront, AstraPal.freezeBack, e.fin());
		Lines.stroke(3f * e.fout());
		Angles.randLenVectors(e.id + 1, 5, 1f + rad * 1.5f * e.fin(), (x, y) -> {
			Fill.poly(e.x + x, e.y + y, 5, e.foutpowdown() * rand.random(1f, 4f), rand.random(0f, 180f));
			Drawf.light(e.x, e.y, 20f, AstraPal.freezeMist, 0.6f * e.fout());
		});
	}),

	// TODO: Chemical Burn Effects need more testing. This is all just theory for now.
	chemicalBurn = new Effect(35f, e -> {
		e.scaled(15f, c -> {
			rand.setSeed(c.id);
			Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, c.fout());
			Angles.randLenVectors(e.id, rand.random(1, 3), 15f * c.fin(), (x, y) -> {
				Fill.circle(e.x + x, e.y + y, 0.2f + c.foutpowdown() * rand.random(1f, 2f));
				Drawf.light(e.x, e.y, 3f, AstraPal.lightChemFire, c.fin() + 0.8f);
			});
		});
		Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, AstraPal.darkerChemFire, e.fin());

		Angles.randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.1f + e.fout() * 1.4f);
		});
	}),

	chemicalBurnShoot = new Effect(25f, e -> {
		Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, AstraPal.darkerChemFire, e.fin());
		Angles.randLenVectors(e.id, 6, e.finpow() * 25f, e.rotation, 7f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
			Drawf.light(e.x, e.y, 3f, AstraPal.lightChemFire, e.fout() + 0.8f);
		});

		e.scaled(12f, c -> {
			Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, c.fin());
			float w = 1f + 5 * c.fout();
			Drawf.tri(e.x, e.y, w, 15f * c.fout(), c.rotation);
			Drawf.tri(e.x, e.y, w, 3f * c.fout(), c.rotation + 180f);
		});
	}),

	chemicalBurnShootLarge = new Effect(20f, e -> {
		Draw.color(Color.lightGray, Color.gray, e.finpow());
		Angles.randLenVectors(e.id, 5, e.finpow() * 19f, e.rotation, 10f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
		});

		Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, AstraPal.darkerChemFire, e.fin());
		Angles.randLenVectors(e.id, 6, e.finpow() * 25f, e.rotation, 18f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
			Drawf.light(e.x, e.y, 3f, AstraPal.lightChemFire, e.fout() + 0.8f);
		});

		e.scaled(12f, c -> {
			Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, c.fin());
			float w = 1f + 5 * c.fout();
			Drawf.tri(e.x, e.y, w, 15f * c.fout(), c.rotation);
			Drawf.tri(e.x, e.y, w, 3f * c.fout(), c.rotation + 180f);
		});
	}),

	chemicalBurnTrail = new Effect(25f, e -> {
		e.scaled(15f, c -> {
			rand.setSeed(c.id);
			Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, c.fout());
			Angles.randLenVectors(e.id, rand.random(1, 3), 15f * c.fin(), (x, y) -> {
				Fill.circle(e.x + x, e.y + y, 0.2f + c.foutpowdown() * rand.random(1f, 2f));
				Drawf.light(e.x, e.y, 3f, AstraPal.lightChemFire, c.fin() + 0.8f);
			});
		});
		Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, AstraPal.darkerChemFire, e.fin());
		rand.setSeed(e.id);

		tmp.trns(rand.random(360f), e.fin() * 10f * rand.random(0.3f, 0.8f));

		Fill.circle(e.x + tmp.x, e.y + tmp.y, 0.4f + e.fout() * rand.random(1f, 2f));
		Drawf.light(e.x, e.y, 20f, Pal.lightPyraFlame, 0.8f * e.foutpowdown());
	}).layer(Layer.bullet - 0.01f),

	chemicalBurnHitLarge = new Effect(25, e -> {
		e.scaled(20f, c -> {
			rand.setSeed(c.id);
			Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, c.fout());
			Angles.randLenVectors(e.id, rand.random(3, 8), 30f * c.fin(), (x, y) -> {
				Fill.circle(e.x + x, e.y + y, c.foutpowdown() * rand.random(1.8f, 3.3f));
				Drawf.light(e.x, e.y, 4f, AstraPal.lightChemFire, c.fin() + 0.8f);
			});
		});

		rand.setSeed(e.id);
		Draw.color(AstraPal.lightChemFire, AstraPal.darkChemFire, e.fin());
		Angles.randLenVectors(e.id, 6, e.fin() * 14f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, e.foutpowdown() * 4f + 0.2f);
			Drawf.light(e.x, e.y, 3f, AstraPal.lightChemFire, e.fout() + 0.8f);
		});
	}),

	// TODO: Accidental creation, gotta save and refine it for later.
	magneticTrail = new Effect(15f, e -> {
		rand.setSeed(e.id);
		Draw.color(AstraPal.astraBack, AstraPal.astraFront, e.fin());
		Angles.randLenVectors(e.id, rand.random(3), e.foutpowdown() * 25f, 18f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 0.2f + e.fin() * rand.random(1.5f, 3f));
			Drawf.light(e.x, e.y, 3f, AstraPal.astraFront, e.fout() + 0.8f);
		});
	});

	/** See {@link #colorFlare(BasicBulletType, float)}. */
	public static Effect colorFlare(BasicBulletType b) {
		return colorFlare(b, 1f);
	}

	/** See {@link #colorFlare(BasicBulletType, float)}. */
	public static Effect colorFlare(BasicBulletType b, float scale) {
		return colorFlare(b.frontColor, scale);
	}

	/** See {@link #colorFlare(BasicBulletType, float)}. */
	public static Effect colorFlare(Color color) {
		return colorFlare(color, 1f);
	}

	/** Creates a single-color muzzle flare. */
	public static Effect colorFlare(Color color, float scale) {
		return new Effect(12f + scale, e -> {
			float width = 6f * scale * e.fout();
			Draw.color(color);
			Drawf.tri(e.x, e.y, width, 15f * scale * e.fout(), e.rotation);
			Drawf.tri(e.x, e.y, width, 5f * scale * e.fout(), e.rotation + 180f);
			Drawf.light(e.x, e.y, 20f, color, e.fout() * 0.7f);
		});
	}

	/** See {@link #energyFlare(Color, Color, float)}. */
	public static Effect energyFlare(BasicBulletType b) {
		return energyFlare(b, 1f);
	}

	/** See {@link #energyFlare(Color, Color, float)}. */
	public static Effect energyFlare(BasicBulletType b, float scale) {
		return energyFlare(b.frontColor, b.backColor, scale);
	}

	/** See {@link #energyFlare(Color, Color, float)}. */
	public static Effect energyFlare(Color lightColor, Color darkColor) {
		return energyFlare(lightColor, darkColor, 1f);
	}

	/** Creates a muzzle flare with two colors. Mostly for energy weapons. */
	public static Effect energyFlare(Color lightColor, Color darkColor, float scale) {
		return new Effect((12f + scale), e -> {
			float w = (6f * scale) * e.fout();
			Draw.color(darkColor);
			Drawf.tri(e.x, e.y, w, 15f * scale * e.fout(), e.rotation);

			Draw.color(lightColor);
			Drawf.tri(e.x, e.y, w, 7f * scale * e.fout(), e.rotation);
			Drawf.tri(e.x, e.y, w, 5f * scale * e.fout(), e.rotation + 180f);
			Drawf.light(e.x, e.y, 20f, lightColor, e.fout() * 0.7f);
		});
	}

	/** See {@link #triFlare(Color, Color, float)}. */
	public static Effect triFlare(BasicBulletType b) {
		return triFlare(b, 1f);
	}

	/** See {@link #triFlare(Color, Color, float)}. */
	public static Effect triFlare(BasicBulletType b, float scale) {
		return triFlare(b.frontColor, b.backColor, scale);
	}

	/** See {@link #triFlare(Color, Color, float)}. */
	public static Effect triFlare(Color lightColor, Color darkColor) {
		return triFlare(lightColor, darkColor, 1f);
	}

	/** Creates a muzzle flare with 3 points. */
	public static Effect triFlare(Color lightColor, Color darkColor, float scale) {
		return new Effect((14f + scale), e -> {
			Draw.color(darkColor, e.fout());
			float w = (6f * scale) * e.fout();
			for (int i : Mathf.signs) {
				Drawf.tri(e.x, e.y, 6f * scale * e.fout(), (9f * scale) * e.fout(), e.rotation + 45f * i);
			}
			Drawf.tri(e.x, e.y, w, 15f * scale * e.fout(), e.rotation);

			Draw.color(lightColor);
			Drawf.tri(e.x, e.y, w, 7f * scale * e.fout(), e.rotation);
			Drawf.tri(e.x, e.y, w, 5f * scale * e.fout(), e.rotation + 180f);
			Drawf.light(e.x, e.y, 20f, lightColor, e.fout() * 0.7f);
		});
	}

	// TODO make dynamicExplosion default values scale to splashDamageRadius

	/** See {@link #dynamicExplosion(Color, Color, float, float, float, int, int, boolean)}. */
	public static Effect dynamicExplosion(BasicBulletType b) {
		return dynamicExplosion(b, 10, b.splashDamageRadius <= 20f ? 5 : 10);
	}

	/** See {@link #dynamicExplosion(Color, Color, float, float, float, int, int, boolean)}. */
	public static Effect dynamicExplosion(BasicBulletType b, int bitDensity, int smokeDensity) {
		return dynamicExplosion(b.frontColor, b.backColor, b.splashDamageRadius, 40f, 8f, bitDensity, smokeDensity, b.fragBullet != null);
	}

	/** See {@link #dynamicExplosion(Color, Color, float, float, float, int, int, boolean)}. */
	public static Effect dynamicExplosion(Color lightColor, Color darkColor, float rad) {
		return dynamicExplosion(lightColor, darkColor, rad, false);
	}

	/** See {@link #dynamicExplosion(Color, Color, float, float, float, int, int, boolean)}. */
	public static Effect dynamicExplosion(Color lightColor, Color darkColor, float rad, boolean squareBits) {
		return dynamicExplosion(lightColor, darkColor, rad, 40f, 8f, 10, rad <= 20f ? 5 : 12, squareBits);
	}

	/** See {@link #dynamicExplosion(Color, Color, float, float, float, int, int, boolean)}. */
	public static Effect dynamicExplosion(Color lightColor, Color darkColor, float rad, int bitDensity, int smokeDensity, boolean squareBits) {
		return dynamicExplosion(lightColor, darkColor, rad, 40f, 8f, bitDensity, smokeDensity, squareBits);
	}

	/** Scales to bullet splash radius.
	 * @param circleSize - used for wave stroke and smoke size.
	 * @param bitDensity - amount of sparks/squares.
	 * @param smokeDensity - amount of smoke particles.
	 * @param squareBits - if true then bits are squares, otherwise sparks.
	 * */
	public static Effect dynamicExplosion(Color lightColor, Color darkColor, float rad, float lifetime, float circleSize, int bitDensity, int smokeDensity, boolean squareBits) {
		return new Effect ( lifetime, e -> {
			boolean cond = rad >= 40f;
			float waveLife = cond ? (15f + Mathf.floor(rad / 20f)) : 12f;
			e.scaled(waveLife, i -> {
				Draw.color(lightColor, darkColor, i.fin());
				Lines.stroke(circleSize * i.fout());
				Lines.circle(e.x, e.y, 2f + i.fin() * rad);
			});

			if (smokeDensity > 0) {
				rand.setSeed(e.id);
				Draw.color(Color.gray);
				Angles.randLenVectors(e.id, smokeDensity, rad * 1.2f * e.finpow(), (x, y) -> {
					Fill.circle(e.x + x, e.y + y, rand.random(circleSize, circleSize * 1.5f) * e.fout());
				});
			}

			float bitLife = cond ? (20f + Mathf.floor(rad / 20f)) : 20f;
			e.scaled(bitLife, c -> {
				if (squareBits) {
					rand.setSeed(c.id);
					Draw.color(lightColor, darkColor, c.fin());
					Angles.randLenVectors(c.id + 1, bitDensity, 1f + rad * 1.5f * c.fin(), (x, y) -> {
						Fill.square(c.x + x, c.y + y, c.fout() * 5f, rand.random(0f, 180f));
						Drawf.light(c.x, c.y, 20f, lightColor, 0.6f * c.fout());
					});
				} else {
					Draw.color(lightColor, darkColor, c.fin());
					Lines.stroke(3f * c.fout());
					Angles.randLenVectors(c.id + 1, bitDensity, 1f + rad * 1.5f * c.fin(), (x, y) -> {
						Lines.lineAngle(c.x + x, c.y + y, Mathf.angle(x, y), 8f * c.fout());
						Drawf.light(c.x, c.y, 20f, lightColor, 0.6f * c.fout());
					});
				}
			});
		});
	}

	/** See {@link #dynamicBurst(Color, Color, float, int)}. */
	public static Effect dynamicBurst(BasicBulletType b) {
		return dynamicBurst(b.frontColor, b.backColor, 1);
	}

	/** See {@link #dynamicBurst(Color, Color, float, int)}. */
	public static Effect dynamicBurst(BasicBulletType b, float scale) {
		return dynamicBurst(b.frontColor, b.backColor, scale);
	}

	/** See {@link #dynamicBurst(Color, Color, float, int)}. */
	public static Effect dynamicBurst(Color light, Color dark, float scale) {
		float rad = 12f * scale;
		return dynamicBurst(light, dark, rad <= 24f ? 20f : 20f + (rad / 6f), rad, Math.round(6f * scale));
	}

	/** Non scaling explosion effect. Used for non-splash damage explosions. */
	public static Effect dynamicBurst(Color light, Color dark, float lifetime, float rad, int bitDensity) {
		return new Effect(lifetime, e -> {
			e.scaled((lifetime * 0.75f), i -> {
				Draw.color(light, dark, i.fin());
				Lines.stroke(6f * i.fout());
				Lines.circle(e.x, e.y, 2f + i.fin() * rad);
			});

			Draw.color(light);
			Lines.stroke(3f * e.fout());
			Angles.randLenVectors(e.id + 1, bitDensity, 1f + rad * 1.5f * e.fin(), (x, y) -> {
				Lines.lineAngle(e.x + x, e.y + y, Mathf.angle(x, y), 8f * e.fin());
				Drawf.light(e.x, e.y, 16f, light, 0.6f * e.fout());
			});
		});
	}

	public static Effect smokeScreen(float smokeRad) {
		return new Effect(140, 200f, b -> {
			float intensity = 6.8f;
			float baseLifetime = 80f + intensity * 11f;
			b.lifetime = 50f + intensity * 65f;

			Draw.color(AstraPal.smokeScreen);
			Draw.alpha(0.8f);
			for (int i = 0; i < 4; i++) {
				Fx.rand.setSeed(b.id * 2L + i);
				float lenScl = Fx.rand.random(0.4f, 1f);
				final int fi = i - 1;
				b.scaled(b.lifetime * lenScl, e -> Angles.randLenVectors(e.id + fi, e.fin(Interp.pow10Out), (int)(2.9f * intensity), smokeRad * intensity, (x, y, in, out) -> {
					float fout = e.fout(Interp.pow5Out) * Fx.rand.random(0.5f, 1f);
					float rad = fout * ((2f + intensity) * 2.35f);

					Fill.circle(e.x + x, e.y + y, rad);
				}));
			}

			b.scaled(baseLifetime, e -> {
				Draw.color(AstraPal.smokeScreen, Color.white, e.fin());
				e.scaled(5 + intensity * 2f, i -> {
					Lines.stroke((3.1f + intensity / 5f) * i.fout());
					Lines.circle(e.x, e.y, (3f + i.fin() * 14f) * intensity);
				});
			});
		}).layer(Layer.effect + 1f);
	}

	/** Railgun bolt pierce effect.
	 * @param boltWaveWidth - side triangle width.
	 * @param boltWaveLen - side triangle length.
	 * @param waveSize - shockwave size.
	 * */
	public static Effect boltPierce(BasicBulletType bolt, float boltWaveWidth, float boltWaveLen, float waveSize , int sparkCount) {
		return new Effect(24f, e -> {
			e.scaled(10f, b -> {
				Draw.color(bolt.frontColor, b.fin());
				Lines.stroke(b.fout() * 3f + 0.2f);
				Lines.circle(b.x, b.y, b.fin() * waveSize);
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

	/** See {@link #railgunShoot(BasicBulletType, float, float, float, float)}. */
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

	/** See {@link #mortarShoot(BasicBulletType, int, int, float, float, boolean)}. */
	public static Effect mortarShoot(BasicBulletType b) {
		return mortarShoot(b, 16, 7, 22f, 20f, true);
	}

	public static Effect mortarShoot(BasicBulletType b, int smokeDensity, int squareDensity, float smokeRange, float muzzleFlareLen, boolean withShockwave) {
		return new MultiEffect(
			new Effect(35f, e -> {
				Draw.color(Pal.lightOrange, Color.gray, e.fin());
				Angles.randLenVectors(e.id, smokeDensity, e.finpow() * 29f, e.rotation, smokeRange, (x, y) -> {
					Fill.circle(e.x + x, e.y + y, e.fout() * 4f + 0.1f);
				});

				rand.setSeed(e.id);
				Draw.color(b.frontColor, b.backColor, e.fin());
				Angles.randLenVectors(e.id, squareDensity, 35f * e.finpow(), e.rotation, smokeRange * 1.5f, (x, y) -> {
					Fill.square(e.x + x, e.y + y, e.fout() * 3.2f, rand.random(0f, 180f));
					Drawf.light(e.x, e.y, 12f, b.frontColor, 0.6f * e.fout());
				});

				if (withShockwave) {
					e.scaled( 10f, c -> {
						Draw.color(b.frontColor, b.backColor, e.fin());
						float circleSize = muzzleFlareLen * 2f;
						Lines.stroke(c.fout() * 2f + 0.2f);
						Lines.circle(e.x, e.y, e.fin() * circleSize);
					});
				}
			}),
			colorFlare(b.frontColor, 2f)
		);
	}

	/** See {@link #boltTurretShoot(BasicBulletType, boolean, boolean, boolean)}. */
	public static Effect boltTurretShoot(BasicBulletType b) {
		return boltTurretShoot(b, false, false);
	}

	public static Effect boltTurretShoot(BasicBulletType b, boolean withFire, boolean withShockwave) {
		return new MultiEffect(
			new Effect(25f, e -> {
				boolean specialEffects = withFire || (b.fragBullet != null);
				int smokeDensity = specialEffects ? 7 : 12;
				float smokeRange = specialEffects ? 5f : 8f;

				Draw.color(Color.lightGray, Color.gray, e.fin());
				Angles.randLenVectors(e.id, smokeDensity, e.finpow() * 19f, e.rotation, smokeRange, (x, y) -> {
					Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
				});

				if (withFire) {
					Draw.color(Pal.lightPyraFlame, Pal.lightPyraFlame, Pal.darkerGray, e.fin());
					Angles.randLenVectors(e.id, 10, e.finpow() * 25f, e.rotation, 18f, (x, y) -> {
						Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.2f);
						Drawf.light(e.x, e.y, 3f, Pal.lightFlame, e.fout() + 0.8f);
					});
				}

				if (b.fragBullet != null) {
						e.scaled(20, c -> {
						rand.setSeed(c.id);
						Draw.color(b.frontColor, b.backColor, c.fin());
						Angles.randLenVectors(c.id, 4, 28f * c.fin(), c.rotation, 14 * 1.5f, (x, y) -> {
							Fill.square(c.x + x, c.y + y, c.fout() * 2f, rand.random(0f, 180f));
							Drawf.light(c.x, c.y, 16f, b.frontColor, c.fout() * 0.8f + 0.1f);
						});
					});
				}

				if (withShockwave) {
					e.scaled(7f, c -> {
						Draw.color(b.frontColor, b.backColor, c.fin());
						Lines.stroke(c.fout() * 3f + 0.2f);
						Lines.circle(c.x, c.y, c.fin() * 10f);
					});
				}
			}),
			colorFlare(b.frontColor)
		);
	}

	public static Effect hitColor(BasicBulletType b) {
		return new Effect(15, e -> {
			Draw.color(b.frontColor, b.backColor, e.fin());
			e.scaled(7f, s -> {
				Lines.stroke(0.5f + s.fout());
				Lines.circle(e.x, e.y, s.fin() * 5f);
			});
			rand.setSeed(e.id);
			Draw.color(b.frontColor, b.backColor, e.fin());
			if (b.fragBullet != null) {
				Angles.randLenVectors(e.id + 1, 12, e.fin() * 15f, (x, y) -> {
					Fill.square(e.x + x, e.y + y, e.fout() * 2f, rand.random(0f, 180f));
					Drawf.light(e.x, e.y, 20f, b.frontColor, 0.6f * e.fout());
				});
				Drawf.light(e.x, e.y, 20f, b.frontColor, 0.6f * e.fout());
			} else {
				Lines.stroke(0.5f + e.fout());
				Angles.randLenVectors(e.id, 5, e.fin() * 15f, (x, y) -> {
					float ang = Mathf.angle(x, y);
					Lines.lineAngle(e.x + x, e.y + y, ang, e.fout() * 3 + 1f);
				});
				Drawf.light(e.x, e.y, 20f, b.frontColor, 0.6f * e.fout());
			}
		});
	}
}