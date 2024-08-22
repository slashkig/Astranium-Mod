package astramod.content;

import mindustry.type.Liquid;
import mindustry.content.StatusEffects;
import arc.graphics.Color;

public class AstraFluids {
	public static Liquid ferrofluid;
	
	public static void load() {
		ferrofluid = new Liquid("ferrofluid", Color.valueOf("220000")) {{
			effect = StatusEffects.none; // Need to add status effect
			viscosity = 0.25f;
			heatCapacity = 0.65f;
			flammability = 0.6f;
			explosiveness = 0.2f;
			boilPoint = 0.7f;
			gasColor = Color.grays(0.1f);
		}};
	}
}
