package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import astramod.content.AstraItems;
import arc.util.Log;

public class AstraBlocks {
	public static Block oreTestium;

	public static void load() {
		Log.info("Loading ores");

		oreTestium = new OreBlock(AstraItems.testium);
	}
}