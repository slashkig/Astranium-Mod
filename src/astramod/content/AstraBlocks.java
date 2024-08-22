package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.distribution.*;
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
import arc.audio.*;
import astramod.content.AstraItems;
import astramod.classes.blocks.defense.*;

public class AstraBlocks {
	public static Block oreTestium, oreHematite, oreLithium, oreNeodymium,
		ironFurnace, castIronPress, castIronSmelter, castIronKiln, magnetiteSynthesizer, plastaniumCompressor, steelForge, phaseWeaver, surgeArcFurnace, vacuumChamber,
		compactDrill, ironDrill,
		hematiteWall, hematiteWallLarge, ironWall, ironWallLarge, platedTitaniumWall, platedTitaniumWallLarge, platedPlastaniumWall, platedPlastaniumWallLarge, steelWall, steelWallLarge, platedThoriumWall, platedThoriumWallLarge, platedSurgeWall, platedSurgeWallLarge, platedPhaseWall, platedPhaseWallLarge,
		ironConveyor,
		testblaster;

	public static void load() {
		Log.info("Loading blocks");

		oreTestium = new OreBlock(AstraItems.testium);

		oreHematite = new OreBlock(AstraItems.hematite);

		oreLithium = new OreBlock(AstraItems.lithium);

		oreNeodymium = new OreBlock(AstraItems.neodymium);

		ironFurnace = new GenericCrafter("iron-furnace") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.hematite, 40, Items.lead, 10));
			size = 2;
			hasPower = hasItems = true;

			consumeItem(AstraItems.hematite, 2);
			consumePower(0.40f);
			craftTime = 40f;
			outputItem = new ItemStack(AstraItems.iron, 1);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronPress = new GenericCrafter("cast-iron-press") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 40, Items.lead, 25, Items.copper, 10));
			scaledHealth = 45;
			size = 2;
			hasItems = true;

			consumeItem(Items.coal, 4);
			consumePower(0.25f);
			craftTime = 140f;
			outputItem = new ItemStack(Items.graphite, 2);

			craftEffect = Fx.pulverizeMedium;
		}};

		castIronSmelter = new GenericCrafter("cast-iron-smelter") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 60, Items.copper, 30, Items.graphite, 20));
			scaledHealth = 45;
			size = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.coal, 2, Items.sand, 4));
			consumePower(0.70f);
			craftTime = 70f;
			outputItem = new ItemStack(Items.silicon, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronKiln = new GenericCrafter("cast-iron-kiln") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 60, Items.lead, 40, Items.copper, 30));
			scaledHealth = 45;
			size = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.lead, 3, Items.sand, 3));
			consumePower(0.60f);
			craftTime = 80f;
			outputItem = new ItemStack(Items.metaglass, 3);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		magnetiteSynthesizer = new GenericCrafter("magnetite-synthesizer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 80, Items.silicon, 40, Items.graphite, 50));
			scaledHealth = 50;
			size = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(AstraItems.hematite, 2, Items.graphite, 1));
			consumePower(1.2f);
			craftTime = 60f;
			outputItem = new ItemStack(AstraItems.magnetite, 1);

			ambientSound = Sounds.electricHum;
			craftEffect = Fx.smeltsmoke;
		}};

		plastaniumCompressor = new GenericCrafter("plastanium-compressor") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 100, Items.silicon, 70, AstraItems.lithium, 60, AstraItems.magnetite, 50));
			scaledHealth = 60;
			size = 2;
			hasPower = hasItems = hasLiquids = true;
			liquidCapacity = 60f;

			consumeItem(Items.titanium, 4);
			consumeLiquid(Liquids.oil, 0.3f);
			consumePower(3f);
			craftTime = 100f;
			outputItem = new ItemStack(Items.plastanium, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFade());
			craftEffect = Fx.formsmoke;
			updateEffect = Fx.plasticburn;
		}};

		steelForge = new GenericCrafter("steel-forge") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 120, Items.graphite, 80, Items.silicon, 60, Items.titanium, 60));
			scaledHealth = 55;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.iron, 3, Items.coal, 2));
			consumePower(4f);
			craftTime = 70f;
			outputItem = new ItemStack(AstraItems.steel, 1);

			drawer = new DrawDefault();
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		phaseWeaver = new GenericCrafter("phase-weaver") {{
			requirements(Category.crafting, ItemStack.with(
				 AstraItems.steel, 180,
				 Items.silicon, 120,
				 AstraItems.lithium, 100,
				 AstraItems.magnetite, 100,
				 Items.thorium, 80
			));
			scaledHealth = 60;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.thorium, 8, Items.sand, 18));
			consumePower(6f);
			craftTime = 210f;
			outputItem = new ItemStack(Items.phaseFabric, 2);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawWeave(), new DrawDefault());
			ambientSound = Sounds.techloop;
			craftEffect = Fx.smeltsmoke;
		}};

		surgeArcFurnace = new GenericCrafter("surge-arc-furnace") {{
			requirements(Category.crafting, ItemStack.with(
				 AstraItems.steel, 200,
				 Items.silicon, 180,
				 Items.graphite, 150,
				 AstraItems.lithium, 120,
				 Items.thorium, 100
			));
			scaledHealth = 65;
			armor = 2f;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.copper, 4, AstraItems.lithium, 2, Items.titanium, 2, Items.silicon, 3));
			consumePower(5f);
			craftTime = 70f;
			outputItem = new ItemStack(Items.surgeAlloy, 1);
			
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		vacuumChamber = new GenericCrafter("vacuum-chamber") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 300,
				AstraItems.steel, 280,
				Items.silicon, 200,
				Items.metaglass, 250,
				Items.phaseFabric, 150,
				AstraItems.neodymium, 120
			));
			scaledHealth = 70;
			armor = 5f;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 30;
			liquidCapacity = 100f;

			consumeItems(ItemStack.with(Items.phaseFabric, 4, Items.silicon, 8));
			consumeLiquid(Liquids.cryofluid, 0.5f);
			consumePower(12f);
			craftTime = 360f;
			outputItem = new ItemStack(AstraItems.aerogel, 2);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawLiquidTile(Liquids.cryofluid){{drawLiquidLight = true;}}, new DrawDefault());
			ambientSound = Sounds.electricHum;
			craftEffect = Fx.plasticburn; // Not sure why it's called that
		}};

		compactDrill = new Drill("compact-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.hematite, 14, Items.copper, 6));
			size = 2;

			consumeLiquid(Liquids.water, 0.04f).boost();
			consumeLiquid(AstraFluids.ferrofluid, 0.03f).boost();
			drillTime = 500;
			tier = 2;
		}};

		ironDrill = new Drill("iron-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.iron, 25, Items.copper, 25, Items.graphite, 20));
			size = 3;
			hasPower = true;

			consumePower(0.5f);
			consumeLiquid(Liquids.water, 0.07f).boost();
			consumeLiquid(AstraFluids.ferrofluid, 0.06f).boost();
			drillTime = 360;
			tier = 3;

			updateEffect = Fx.pulverizeMedium;
			drillEffect = Fx.mineBig;
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
			requirements(Category.defense, ItemStack.with(Items.titanium, 6, Items.graphite, 2));
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
			requirements(Category.defense, ItemStack.with(Items.plastanium, 6, Items.metaglass, 4));
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

		platedThoriumWall = new AuraWall("plated-thorium-wall", Color.purple) {{
			requirements(Category.defense, ItemStack.with(Items.thorium, 6, AstraItems.magnetite, 4));
			health = 200 * 4;
			armor = 6f;
			auraDamage = 2f;
			auraRadius = 15f;
		}};

		platedThoriumWallLarge = new AuraWall("plated-thorium-wall-large", Color.purple) {{
			requirements(Category.defense, ItemStack.mult(platedThoriumWall.requirements, 4));
			health = 200 * 16;
			armor = 6f;
			size = 2;
			auraDamage = 8f;
			auraRadius = 25f;
		}};

		platedSurgeWall = new Wall("plated-surge-wall") {{
			requirements(Category.defense, ItemStack.with(Items.surgeAlloy, 8, AstraItems.lithium, 6));
			health = 280 * 4;
			armor = 16f;
			lightningChance = 0.075f;
			lightningDamage = 45f;
		}};

		platedSurgeWallLarge = new Wall("plated-surge-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedSurgeWall.requirements, 4));
			health = 280 * 16;
			armor = 16f;
			size = 2;
			lightningChance = 0.075f;
			lightningDamage = 45f;
		}};

		platedPhaseWall = new ProjectorWall("plated-phase-wall", 1.6f) {{
			requirements(Category.defense, ItemStack.with(Items.phaseFabric, 8, Items.plastanium, 6, Items.silicon, 5));
			health = 190 * 4;
			armor = 10f;
			consumePower(0.05f);
			shieldHealth = 150f;
			breakCooldown = 1500f;
			regenSpeed = 0.5f;
			chanceDeflect = 10f;
			flashHit = true;
			absorbLasers = absorbLightning = true;
		}};

		platedPhaseWallLarge = new ProjectorWall("plated-phase-wall-large", 3.2f) {{
			requirements(Category.defense, ItemStack.mult(platedPhaseWall.requirements, 4));
			health = 190 * 16;
			armor = 10f;
			size = 2;
			consumePower(0.2f);
			shieldHealth = 600f;
			breakCooldown = 1200f;
			chanceDeflect = 10f;
			flashHit = true;
			absorbLasers = absorbLightning = true;
		}};

		ironConveyor = new Conveyor("iron-conveyor"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.copper, 1));
			health = 60;
			speed = 0.05f;
			displayedSpeed = 7f;
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
