package astramod.content;

import arc.math.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;
import astramod.graphics.*;

public class AstraFx {
	public static final Effect

	pulverizePurple = new Effect(40, e -> {
		Angles.randLenVectors(e.id, 5, 3f + e.fin() * 8f, (x, y) -> {
			Draw.color(AstraPal.plasmaGlowPurple, Pal.stoneGray, e.fin());
			Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45);
		});
	}),

	steamGenerate = new Effect(100, e -> {
		Draw.color(AstraFluids.steam.color);
		Draw.alpha(e.fslope() * 0.8f);

		Fx.rand.setSeed(e.id);
		for(int i = 0; i < 6; i++){
			Fx.v.trns(Fx.rand.random(360f), Fx.rand.random(e.finpow() * 14f)).add(e.x, e.y);
			Fill.circle(Fx.v.x, Fx.v.y, Fx.rand.random(1.4f, 3.4f));
		}
	}).layer(Layer.bullet - 1f),
	
	oilSmoke = new Effect(180, e -> {
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

	octShieldBreak = new Effect(40, e -> {
		Draw.color(e.color);
		Lines.stroke(3f * e.fout());
		Lines.poly(e.x, e.y, 8, e.rotation + e.fin(), 22.5f);
	}),

	coreLaser = new Effect(8, e -> {
		Draw.color(Pal.bulletYellow, Pal.bulletYellowBack, e.fin());
		Lines.stroke(0.5f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 5f);

		Drawf.light(e.x, e.y, 23f, Pal.bulletYellowBack, e.fout() * 0.7f);
	}),

	superLaser = new Effect(8, e -> {
		Draw.color(Color.white, AstraPal.testPink, e.fin());
		Lines.stroke(0.5f + e.fout());
		Lines.circle(e.x, e.y, e.fin() * 5f);

		Drawf.light(e.x, e.y, 23f, AstraPal.testPink, e.fout() * 0.7f);
	}),
	
	shootMediumFlame = new Effect(35f, 80f, e -> {
		Draw.color(Pal.lightPyraFlame, Pal.darkPyraFlame, Pal.darkFlame, e.fin());

		Angles.randLenVectors(e.id, 12, e.finpow() * 90f, e.rotation, 20f, (x, y) -> {
			Fill.circle(e.x + x, e.y + y, 1.5f + e.fout() * 1f);
		});
	});
}