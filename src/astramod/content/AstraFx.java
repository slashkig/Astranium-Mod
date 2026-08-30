package astramod.content;

import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
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

		tmp.trns(e.data instanceof Position pos ? pos.angleTo(e.x, e.y) : Mathf.random(360f), Mathf.random(2f + e.fin() * 16f));
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
	});
}