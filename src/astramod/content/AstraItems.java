package astramod.content;

import mindustry.type.Item;
import arc.graphics.Color;
import arc.util.Log;

public class AstraItems {
	public static Item testium, hematite, iron, steel;

	public static void load() {
		Log.info("Loading items");

		testium = new Item("testium", Color.valueOf("ff00ff")) {{
			hardness = 1;
			description = "Test item for Mindustry";
		}};
		
		hematite = new Item("hematite", Color.valueOf("802f0c")) {{
			hardness = 1;
			description = "Unrefined iron ore. Used in basic construction.";
			cost = 0.7f;
		}};
		
		iron = new Item("iron", Color.valueOf("5f5f6f")) {{
			description = "Smelted iron ore. Used in simple industrial buildings.";
			cost = 1f;
		}};

		steel = new Item("steel", Color.valueOf("cfcfcf")) {{
			description = "A carbon-iron alloy used to construct strong defenses and industries.";
			cost = 1.2f;
		}};

		aerogel = new Item("aerogel", Color.valueOf("e8ffff")) {{
			description = "A synthetic ultralight substance. Used in thermal insulation, advanced photocatalysis, and serves as a powerful catalyst in defensive structures.";
			cost = 1.2f;
		}};
	}
}
