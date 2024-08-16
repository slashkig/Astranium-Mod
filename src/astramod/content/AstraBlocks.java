package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.graphics.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.content.*;
import arc.util.Log;
import arc.graphics.*;
import astramod.content.AstraItems;

public class AstraBlocks {
	public static Block oreTestium, oreHematite, ironForge, blastFurnace, ironDrill, hematiteWall, hematiteWallLarge, testblaster;

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

		ironDrill = new Drill("iron-drill") {{
			requirements(Category.production, ItemStack.with(Items.copper, 25, Items.graphite, 20, AstraItems.iron, 25));
			drillTime = 320;
			size = 3;
			hasPower = true;
			tier = 3;
			updateEffect = Fx.pulverizeMedium;
			drillEffect = Fx.mineBig;
			
			consumePower(0.8f);
			consumeLiquid(Liquids.water, 0.07f).boost();
		}};
		
		hematiteWall = new Wall("hematite-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.hematite, 6));
			health = 90 * 4;
			envDisabled |= Env.scorching;
		}};
		
		hematiteWallLarge = new Wall("hematite-wall-large") {{
			requirements(Category.defense, ItemStack.mult(hematiteWall.requirements, 4));
			health = 90 * 16;
			size = 2;
			envDisabled |= Env.scorching;
		}};

		testblaster = new ItemTurret("testblaster") {{
			requirements(Category.turret, ItemStack.with(AstraItems.testium, 1000));
			ammo(
				AstraItems.testium, new BasicBulletType(10f, 100000) {{
					lifetime = 100f;
					width = 20f;
					height = 25f;
					pierceCap = 10;
					ammoMultiplier = 10;
					frontColor = Color.valueOf("ee00ee");
					backColor = Color.valueOf("ff22ff");
				}}
			);

			drawer = new DrawTurret() {{
				parts.add(new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					under = true;
					moveY = -5f;
				}});
			}};

			maxAmmo = 500;
			recoil = 2.5f;
			reload = 6f;
			rotateSpeed = 5f;
			range = 300f;
			size = 2;
			health = 1000000;
			shootY = 7.5f;
			ammoUseEffect = Fx.none;
			shootSound = Sounds.missile;

			limitRange();
		}};
	}
}