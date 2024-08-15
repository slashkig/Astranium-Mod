package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.draw.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.content.*;
import astramod.content.AstraItems;
import arc.util.Log;
import arc.graphics.*;

public class AstraBlocks {
	public static Block oreTestium, oreHematite, ironForge, blastFurnace, hematiteWall, hematiteWallLarge;

	public static void load() {
		Log.info("Loading blocks");

		oreTestium = new OreBlock(AstraItems.testium);

		oreHematite = new OreBlock(AstraItems.hematite);
		
		ironForge = new GenericCrafter("iron-forge") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.hematite, 20));
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItems.iron, 1);
			craftTime = 60f;
			size = 2;
			hasPower = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			squareSprite = false;

			consumeItems(ItemStack.with(AstraItems.hematite, 2));
			consumePower(0.60f);
		}};

		blastFurnace = new GenericCrafter("blast-furnace") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 80, Items.graphite, 40, Items.silicon, 60, Items.titanium, 30));
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItems.steel, 1);
			craftTime = 100f;
			size = 3;
			hasPower = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
			squareSprite = false;

			consumeItems(ItemStack.with(AstraItems.iron, 3, Items.coal, 2));
			consumePower(2.5f);
		}};

		hematiteWall = new Wall("hematite-wall"){{
            		requirements(Category.defense, ItemStack.with(AstraItems.hematite, 6));
            		health = 90 * wallHealthMultiplier;
            		envDisabled |= Env.scorching;
        	}};
		
		hematiteWallLarge = new Wall("hematite-wall-large") {{
            		requirements(Category.defense, ItemStack.mult(hematiteWall.requirements, 4));
            		health = 90 * 4 * wallHealthMultiplier;
			size = 2;
            		envDisabled |= Env.scorching;
        	}};
	}
}
