package astramod.content;

import arc.graphics.*;
import arc.util.Log;
import mindustry.type.*;
import mindustry.content.*;

public class AstraFluids {
	public static Liquid steam, helium, ferrofluid, plasma;
	
	public static void load() {
		Log.info("Loading fluids");

		steam = new Liquid("steam", Color.grays(0.9f)) {{
			gas = true;
			temperature = 0.7f;
		}};

		ferrofluid = new Liquid("ferrofluid", Color.valueOf("220000")) {{
			effect = StatusEffects.none; // TODO need to add status effect
			coolant = true;
			barColor = Color.valueOf("440202");
			viscosity = 0.25f;
			heatCapacity = 0.7f;
			flammability = 0.05f;
			temperature = 0.4f;
			boilPoint = 0.7f;
			gasColor = Color.grays(0.1f);
		}};

		helium = new Liquid("helium", Color.valueOf("fff596")) {{
			gas = true;
			temperature = 0.4f;
			flammability = 0;
		}};

		plasma = new Liquid("plasma", Color.valueOf("7722ed")) {{
			gas = true;
			temperature = 2f;
			explosiveness = 0.5f;
			lightColor = Color.valueOf("d8bfd8").a(0.5f);
		}};
	}
}