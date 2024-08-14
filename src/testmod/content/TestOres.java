package testmod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import testmod.content.TestItems;
import arc.util.Log;

public class TestOres {
	public static Block oreTestium;

	public static void load() {
		Log.info("Loading testmod ores");

		oreTestium = new OreBlock(TestItems.testium);
	}
}