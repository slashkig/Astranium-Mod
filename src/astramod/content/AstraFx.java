package astramod.content;

import arc.math.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.entities.*;
import astramod.graphics.*;

public class AstraFx {
	public static final Effect pulverizePurple = new Effect(40, e -> {
		Angles.randLenVectors(e.id, 5, 3f + e.fin() * 8f, (x, y) -> {
			Draw.color(AstraPal.plasmaGlowPurple, Pal.stoneGray, e.fin());
			Fill.square(e.x + x, e.y + y, e.fout() * 2f + 0.5f, 45);
		});
	});

	public static final Effect superLaser = new Effect(8, e -> {
        Draw.color(Color.white, AstraPal.testPink, e.fin());
        Lines.stroke(0.5f + e.fout());
        Lines.circle(e.x, e.y, e.fin() * 5f);

        Drawf.light(e.x, e.y, 23f, AstraPal.testPink, e.fout() * 0.7f);
    });
}