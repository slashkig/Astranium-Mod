package astramod.content;

import arc.util.Log;
import arc.graphics.*;
import mindustry.content.*;
import mindustry.type.*;

public class AstraItems {
	public static Item testium, hematite, iron, lithium, magnetite, steel, crystals, neodymium, aerogel, astranium;

	public static void load() {
		Log.info("Loading items");

		testium = new Item("testium", Color.valueOf("ff00ff"));

		hematite = new Item("hematite", Color.valueOf("802f0c")) {{
			hardness = 1;
			cost = 0.6f;
			UnitTypes.mono.mineItems.add(this);
			UnitTypes.poly.mineItems.add(this);
			UnitTypes.mega.mineItems.add(this);
		}};

		iron = new Item("iron", Color.valueOf("404059")) {{
			cost = 0.9f;
		}};

		lithium = new Item("lithium", Color.valueOf("e40808")) {{
			hardness = 3;
			cost = 1.2f;
			flammability = 1.8f;
			explosiveness = 0.65f;
			charge = 0.15f;
			UnitTypes.mega.mineItems.add(this);
		}};

		magnetite = new Item("magnetite", Color.valueOf("696969")) {{
			cost = 1.1f;
		}};

		steel = new Item("steel", Color.valueOf("cfcfcf")) {{
			cost = 1.2f;
		}};

		crystals = new Item("crystals", Color.valueOf("bf2851")) {{
			hardness = 4;
			cost = 1.5f;
			charge = 0.5f;
		}};

		neodymium = new Item("neodymium", Color.valueOf("c0954c")) {{
			hardness = 5;
			cost = 1.3f;
			radioactivity = 0.1f;
		}};

		aerogel = new Item("aerogel", Color.valueOf("4da6ff")) {{
			cost = 1.4f;
		}};

		astranium = new Item("astranium", Color.valueOf("8142ff")) {{
			cost = 1.6f;
			charge = 0.3f;
			radioactivity = 0.05f;
		}};

		Items.graphite.hardness = 2;
	}
}
