package astramod.content;

import mindustry.type.Item;
import arc.graphics.Color;
import arc.util.Log;

public class AstraItems {
	public static Item testium, hematite, iron, lithium, magnetite, steel, neodymium, aerogel;

	public static void load() {
		Log.info("Loading items");

		testium = new Item("testium", Color.valueOf("ff00ff")) {{
			hardness = 1;
			description = "Test item for Mindustry";
		}};
		
		hematite = new Item("hematite", Color.valueOf("802f0c")) {{
			hardness = 1;
			description = "Unrefined iron ore. Used in basic construction.";
			cost = 0.6f;
		}};
		
		iron = new Item("iron", Color.valueOf("404059")) {{
			description = "Smelted iron ore. Used for construction of many types of buildings.";
			cost = 0.9f;
		}};

		lithium = new Item("lithium", Color.valueOf("e40808")) {{
			hardness = 3;
			description = "A lightweight but highly reactive metal used in advanced electrical equipment.";
			cost = 1.2f;
			flammability = 1.8f;
			explosiveness = 0.65f;
		}};
		
		magnetite = new Item("magnetite", Color.valueOf("696969")) {{
			description = "A ferromagnetic form of iron, used in energy generation and high-grade machinery.";
			cost = 1.1f;
		}};

		steel = new Item("steel", Color.valueOf("cfcfcf")) {{
			description = "A durable and versitile carbon-iron alloy used in advanced construction.";
			cost = 1.2f;
		}};

		neodymium = new Item("neodymium", Color.valueOf("c0954c")) {{
			hardness = 5;
			description = "A rare and powerful heavy metal.  Crucial for high-performance magnets, advanced electronic components, and precision hardware.";
			cost = 1.5f;
			radioactivity = 0.05f;
		}};
		
		aerogel = new Item("aerogel", Color.valueOf("e8ffff")) {{
			description = "A synthetic ultralight substance. Used in thermal insulation, advanced photocatalysis, and serves as a powerful catalyst in defensive structures.";
			cost = 1.2f;
		}};
	}
}
