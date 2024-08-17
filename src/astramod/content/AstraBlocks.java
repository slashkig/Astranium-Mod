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
	public static Block oreTestium, oreHematite, oreNeodymium, ironForge, magnetiteSynthesizer, blastFurnace, vacuumChamber, ironDrill, hematiteWall, hematiteWallLarge, ironWall, ironWallLarge, steelWall, steelWallLarge, testblaster;

	public static void load() {
		Log.info("Loading blocks");

		oreTestium = new OreBlock(AstraItems.testium);

		oreHematite = new OreBlock(AstraItems.hematite);

		oreNeodymium = new OreBlock(AstraItems.neodymium);
		
		ironForge = new GenericCrafter("iron-forge") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.hematite, 20, Items.lead, 10));
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItems.iron, 1);
			craftTime = 40f;
			size = 2;
			hasPower = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			squareSprite = false;

			consumeItems(ItemStack.with(AstraItems.hematite, 2));
			consumePower(0.50f);
		}};

		magnetiteSynthesizer = new GenericCrafter("magnetite-synthesizer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 50, Items.silicon, 30, Items.lead, 40));
			craftEffect = Fx.pulverizeMedium;
			outputItem = new ItemStack(AstraItems.magnetite, 1);
			craftTime = 60f;
			size = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(AstraItems.hematite, 2, Items.graphite, 1));
			consumePower(1.2f);
		}};
		
		blastFurnace = new GenericCrafter("blast-furnace") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 80, Items.graphite, 40, Items.silicon, 60, Items.titanium, 30));
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItems.steel, 1);
			craftTime = 70f;
			size = 3;
			hasPower = hasItems = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
			squareSprite = false;

			consumeItems(ItemStack.with(AstraItems.iron, 3, Items.coal, 2));
			consumePower(2.5f);
		}};

		vacuumChamber = new GenericCrafter("vacuum-chamber") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.steel, 300, Items.silicon, 200, Items.metaglass, 250, Items.phase-fabric, 80));
			health = 600;
			armor = 5f;
			craftEffect = Fx.formsmoke;
			outputItem = new ItemStack(AstraItems.aerogel, 2);
			craftTime = 360f;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
			liquidCapacity = 100f;

			consumeItems(ItemStack.with(AstraItems.phase-fabric, 4, Items.silicon, 8));
			consumeLiquid(Liquids.cryofluid, 0.4f);
			consumePower(8f);
		}};
		
		ironDrill = new Drill("iron-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.iron, 25, Items.copper, 25, Items.graphite, 20));
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
		}};
		
		hematiteWallLarge = new Wall("hematite-wall-large") {{
			requirements(Category.defense, ItemStack.mult(hematiteWall.requirements, 4));
			health = 90 * 16;
			size = 2;
		}};

		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.iron, 6));
			health = 120 * 4;
			armor = 2f;
		}};
		
		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, ItemStack.mult(ironWall.requirements, 4));
			health = 120 * 16;
			armor = 2f;
			size = 2;
		}};

		platedTitaniumWall = new Wall("plated-titanium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.titanium, 6, Items.copper, 6));
			health = 160 * 4;
			armor = 4f;
		}};
		
		platedTitaniumWallLarge = new Wall("plated-titanium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedTitaniumWall.requirements, 4));
			health = 160 * 16;
			armor = 4f;
			size = 2;
		}};

		platedPlastaniumWall = new Wall("plated-plastanium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.plastanium, 6, Items.lead, 8));
			health = 175 * 4;
			armor = 2f;
			insulated = true;
			absorbLasers = true;
		}};
		
		platedPlastaniumWallLarge = new Wall("plated-plastanium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedPlastaniumWall.requirements, 4));
			health = 175 * 16;
			armor = 2f;
			size = 2;
			insulated = true;
			absorbLasers = true;
		}};
		
		steelWall = new Wall("steel-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.steel, 8));
			health = 220 * 4;
			armor = 8f;
		}};
		
		steelWallLarge = new Wall("steel-wall-large") {{
			requirements(Category.defense, ItemStack.mult(steelWall.requirements, 4));
			health = 220 * 16;
			armor = 8f;
			size = 2;
		}};

		platedThoriumWall = new Wall("plated-thorium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.thorium, 6, AstraItems.magnetite, 4));
			health = 200 * 4;
			armor = 6f;
		}};
		
		platedThoriumWallLarge = new Wall("plated-thorium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedThoriumWall.requirements, 4));
			health = 200 * 16;
			armor = 6f;
			size = 2;
		}};
		
		testblaster = new ItemTurret("testblaster") {{
			requirements(Category.turret, ItemStack.with(AstraItems.testium, 1000));
			ammo(
				AstraItems.testium, new BasicBulletType(10f, 10000000) {{
					lifetime = 100f;
					width = 20f;
					height = 25f;
					pierceCap = 100;
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

			maxAmmo = 1000;
			recoil = 2.5f;
			reload = 6f;
			rotateSpeed = 5f;
			range = 300f;
			size = 2;
			health = 100000000;
			shootY = 7.5f;
			ammoUseEffect = Fx.none;
			shootSound = Sounds.missile;

			limitRange();
		}};
	}
}
