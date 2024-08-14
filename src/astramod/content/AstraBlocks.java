package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.content.Fx;
import astramod.content.AstraItems;
import arc.util.Log;

public class AstraBlocks {
	public static Block oreTestium, ironForge;

	public static void load() {
		Log.info("Loading blocks");

		oreTestium = new OreBlock(AstraItems.testium);

		ironForge = new GenericCrafter("iron-forge") {{
			requirements(Catergory.crafting, with(AstraItems.hematite, 20));
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItem.iron, 1);
			craftTime = 60f;
			size = 2;
			hasPower = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));

			consumeItems(with(AstraItems.hematite, 2));
			consumePower(0.60f);
		}};
	}
}
