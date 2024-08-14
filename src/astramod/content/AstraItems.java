package astramod.content;

import mindustry.type.Item;
import arc.graphics.Color;
import arc.util.Log;

public class AstraItems {
	public static Item testium;

	public static void load() {
		Log.info("Loading testmod items");

		testium = new Item("testium", Color.valueOf("ff00ff")) {{
			hardness = 1;
			description = "Test item for Mindustry";
		}};
	}
}