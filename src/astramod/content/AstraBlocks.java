package astramod.content;

import mindustry.world.Block;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.storage.*;
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
import astramod.classes.blocks.production.*;
import astramod.classes.draw.*;

public class AstraBlocks {
	public static Block oreTestium, oreHematite, oreLithium, oreNeodymium, wallOreLithium,
		ironFurnace, blastFurnace, castIronPress, hydraulicPress, castIronSmelter, purificationSmelter, castIronKiln, magnetiteSynthesizer, cryofluidBlender, plastaniumCompressor, plastaniumFabricator, steelForge, ferrofluidMixer, phaseWeaver, phaseLoom, surgeArcFurnace, vacuumChamber,
		compactDrill, ironDrill, augerDrill,
		hematiteWall, hematiteWallLarge, ironWall, ironWallLarge, platedTitaniumWall, platedTitaniumWallLarge, platedPlastaniumWall, platedPlastaniumWallLarge, steelWall, steelWallLarge, platedThoriumWall, platedThoriumWallLarge, platedSurgeWall, platedSurgeWallLarge, platedPhaseWall, platedPhaseWallLarge,
		hematiteConveyor, ironConveyor, bulkConveyor, surgeBulkConveyor, ironJunction, ironRouter, ironOverflowGate, ironUnderflowGate, ironSorter, invertedIronSorter,
		coreNode,
		testblaster;

	public static void load() {
		Log.info("Loading blocks");

		oreTestium = new OreBlock(AstraItems.testium) {{ variants = 1; }};

		oreHematite = new OreBlock(AstraItems.hematite);

		oreLithium = new OreBlock(AstraItems.lithium);

		oreNeodymium = new OreBlock(AstraItems.neodymium);

		wallOreLithium = new OreBlock("ore-wall-lithium", AstraItems.lithium) {{ wallOre = true; }};

		ironFurnace = new GenericCrafter("iron-furnace") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.hematite, 40, Items.lead, 10));
			size = 2;
			hasPower = hasItems = true;

			consumeItem(AstraItems.hematite, 2);
			consumePower(0.40f);
			craftTime = 50f;
			outputItem = new ItemStack(AstraItems.iron, 1);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		blastFurnace = new GenericCrafter("blast-furnace") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 100,
				AstraItems.lithium, 50,
				Items.titanium, 80,
				Items.graphite, 70
			));
			scaledHealth = 55;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.hematite, 8, Items.pyratite, 2));
			consumePower(2.8f);
			craftTime = 60f;
			outputItem = new ItemStack(AstraItems.iron, 5);

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
			craftTime = 120f;
			outputItem = new ItemStack(Items.graphite, 2);

			craftEffect = Fx.pulverizeMedium;
		}};

		hydraulicPress = new GenericCrafter("hydraulic-press") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 120,
				Items.graphite, 100,
				Items.metaglass, 90,
				Items.plastanium, 75,
				Items.silicon, 50
			));
			scaledHealth = 60;
			size = 3;
			hasPower = hasLiquids = hasItems = true;
			itemCapacity = 20;
			liquidCapacity = 20f;

			consumeItem(Items.coal, 7);
			consumeLiquid(Liquids.oil, 0.15f);
			consumePower(1.6f);
			craftTime = 75f;
			outputItem = new ItemStack(Items.graphite, 4);

			drawer = new DrawMulti(new DrawPistons() {{ sinMag = 1f; lenOffset = -1f; }}, new DrawDefault());
			craftEffect = Fx.pulverizeMedium;
		}};

		castIronSmelter = new GenericCrafter("cast-iron-smelter") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 60, Items.copper, 30, Items.graphite, 20));
			scaledHealth = 45;
			size = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.coal, 2, Items.sand, 4));
			consumePower(0.70f);
			craftTime = 200f / 3;
			outputItem = new ItemStack(Items.silicon, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffef99")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		purificationSmelter = new GenericCrafter("purification-smelter") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 125,
				Items.silicon, 60,
				Items.graphite, 85,
				Items.titanium, 100,
				AstraItems.lithium, 70
			));
			scaledHealth = 60;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(Items.sand, 5, Items.graphite, 2));
			consumeItem(Items.pyratite, 2).boost();
			consumePower(4.6f);
			craftTime = 50f;
			outputItem = new ItemStack(Items.silicon, 5);

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

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat());
			ambientSound = Sounds.electricHum;
			craftEffect = Fx.smeltsmoke;
		}};

		cryofluidBlender = new GenericCrafter("cryofluid-blender") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 90, Items.metaglass, 60, Items.titanium, 40));
			scaledHealth = 50;
			size = 2;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 30f;

			consumeItem(Items.titanium, 2);
			consumeLiquid(Liquids.water, 0.3f);
			consumePower(1.4f);
			craftTime = 160f;
			outputLiquid = new LiquidStack(Liquids.cryofluid, 0.3f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water),
				new DrawLiquidTile(Liquids.cryofluid) {{ drawLiquidLight = true; }},
				new DrawDefault()
			);
			lightLiquid = Liquids.cryofluid;
		}};

		plastaniumCompressor = new GenericCrafter("plastanium-compressor") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.iron, 100,
				Items.lead, 100,
				Items.silicon, 50,
				AstraItems.magnetite, 70
			));
			scaledHealth = 60;
			size = 2;
			hasPower = hasItems = hasLiquids = true;
			liquidCapacity = 60f;

			consumeItem(Items.titanium, 4);
			consumeLiquid(Liquids.oil, 0.3f);
			consumePower(3f);
			craftTime = 90f;
			outputItem = new ItemStack(Items.plastanium, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFade());
			craftEffect = Fx.formsmoke;
			updateEffect = Fx.plasticburn;
		}};

		plastaniumFabricator = new GenericCrafter("plastanium-fabricator") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 140,
				Items.silicon, 120,
				Items.plastanium, 100,
				AstraItems.lithium, 90,
				AstraItems.neodymium, 40
			));
			scaledHealth = 70;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 20;
			liquidCapacity = 100f;

			consumeItems(ItemStack.with(Items.titanium, 9, AstraItems.lithium, 3));
			consumeLiquid(Liquids.oil, 0.8f);
			consumePower(8f);
			craftTime = 90f;
			outputItem = new ItemStack(Items.plastanium, 6);

			drawer = new DrawMulti(new DrawDefault(), new DrawFade());
			craftEffect = Fx.formsmoke;
			updateEffect = Fx.plasticburn;
		}};

		steelForge = new GenericCrafter("steel-forge") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.iron, 120,
				Items.graphite, 80,
				Items.silicon, 60,
				Items.titanium, 60
			));
			scaledHealth = 55;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.iron, 3, Items.coal, 4));
			consumePower(4f);
			craftTime = 200f / 3;
			outputItem = new ItemStack(AstraItems.steel, 1);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat());
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		ferrofluidMixer = new GenericCrafter("ferrofluid-mixer") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 150,
				Items.metaglass, 140,
				Items.plastanium, 80,
				AstraItems.magnetite, 125,
				Items.graphite, 100
			));
			scaledHealth = 65;
			size = 3;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 40f;

			consumeItem(AstraItems.magnetite, 1);
			consumeLiquid(Liquids.oil, 0.25f);
			consumePower(4.5f);
			craftTime = 60f;
			outputLiquid = new LiquidStack(AstraFluids.ferrofluid, 0.25f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.oil),
				new DrawLiquidTile(AstraFluids.ferrofluid),
				new DrawRegion("-spinner", 1, true),
				new DrawDefault()
			);
		}};

		phaseWeaver = new GenericCrafter("phase-weaver") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 180,
				Items.silicon, 160,
				AstraItems.lithium, 150,
				AstraItems.magnetite, 100,
				Items.thorium, 120
			));
			scaledHealth = 60;
			size = 2;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.thorium, 8, Items.sand, 16));
			consumePower(6f);
			craftTime = 180f;
			outputItem = new ItemStack(Items.phaseFabric, 2);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawWeave(), new DrawDefault());
			ambientSound = Sounds.techloop;
			craftEffect = Fx.smeltsmoke;
		}};

		phaseLoom = new GenericCrafter("phase-loom") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 260,
				Items.silicon, 280,
				AstraItems.lithium, 200,
				Items.thorium, 180,
				AstraItems.neodymium, 140,
				Items.phaseFabric, 120
			));
			scaledHealth = 70;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 40;

			consumeItems(ItemStack.with(Items.thorium, 12, Items.sand, 24));
			consumePower(15f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.phaseFabric, 3);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawMultiWeave() {{ glowColor = new Color(1f, 0.4f, 0.4f, 0.8f); }},
				new DrawDefault()
			);
			ambientSound = Sounds.techloop;
			craftEffect = Fx.smeltsmoke;
		}};

		surgeArcFurnace = new GenericCrafter("surge-arc-furnace") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 220,
				Items.silicon, 160,
				Items.graphite, 180,
				AstraItems.lithium, 120,
				Items.thorium, 100
			));
			scaledHealth = 65;
			armor = 2f;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.copper, 8, AstraItems.lithium, 4, Items.titanium, 4, Items.silicon, 6));
			consumePower(5f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.surgeAlloy, 2);
			
			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		vacuumChamber = new GenericCrafter("vacuum-chamber") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 320,
				AstraItems.steel, 280,
				Items.silicon, 200,
				Items.metaglass, 260,
				Items.phaseFabric, 190,
				AstraItems.neodymium, 175
			));
			scaledHealth = 75;
			armor = 5f;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 40;
			liquidCapacity = 200f;

			consumeItems(ItemStack.with(Items.phaseFabric, 4, Items.silicon, 8));
			consumeLiquid(Liquids.cryofluid, 1.2f);
			consumePower(12f);
			craftTime = 360f;
			outputItem = new ItemStack(AstraItems.aerogel, 2);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.cryofluid) {{ drawLiquidLight = true; }},
				new DrawDefault()
			);
			ambientSound = Sounds.electricHum;
			updateEffect = Fx.plasticburn; // Not sure why it's called that
			craftEffect = Fx.smeltsmoke;
		}};

		compactDrill = new Drill("compact-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.hematite, 14, Items.lead, 6));
			size = 2;

			liquidBoostIntensity = 1.4f;
			consumeLiquid(Liquids.water, 0.04f).boost();
			drillTime = 500;
			tier = 2;
			hardnessDrillMultiplier = 75f;
		}};

		ironDrill = new MultiCoolantDrill("iron-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.iron, 25, Items.copper, 25, Items.graphite, 20));
			size = 3;
			hasPower = true;
			liquidCapacity = 7.5f;

			liquidBoostIntensity = 1.5f;
			consumePower(0.5f);
			consumeLiquidBoosts(0.07f, Liquids.water, 1.5f, AstraFluids.ferrofluid, 1.8f);
			drillTime = 360;
			tier = 3;
			hardnessDrillMultiplier = 65f;

			rotateSpeed = 3f;
			updateEffect = Fx.pulverizeMedium;
			drillEffect = Fx.mineBig;
		}};

		augerDrill = new MultiCoolantDrill("auger-drill") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.steel, 35,
				AstraItems.magnetite, 25,
				Items.graphite, 30,
				Items.titanium, 20
			));
			scaledHealth = 55;
			size = 3;
			hasPower = true;
			liquidCapacity = 10f;

			liquidBoostIntensity = 1.6f;
			consumePower(1.2f);
			consumeLiquidBoosts(0.1f, Liquids.water, 1.6f, AstraFluids.ferrofluid, 1.95f);
			drillTime = 260;
			tier = 4;
			hardnessDrillMultiplier = 55f;

			rotateSpeed = 4.5f;
			drawRim = true;
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
			requirements(Category.defense, ItemStack.with(Items.thorium, 6, AstraItems.iron, 4));
			health = 205 * 4;
			armor = 6f;
			auraDamage = 2f;
			auraRadius = 15f;
		}};

		platedThoriumWallLarge = new AuraWall("plated-thorium-wall-large", Color.purple) {{
			requirements(Category.defense, ItemStack.mult(platedThoriumWall.requirements, 4));
			health = 205 * 16;
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

		platedPhaseWall = new Wall("plated-phase-wall") {{
			requirements(Category.defense, ItemStack.with(Items.phaseFabric, 8, AstraItems.magnetite, 6));
			health = 190 * 4;
			armor = 10f;
			chanceDeflect = 16f;
			flashHit = true;
		}};

		platedPhaseWallLarge = new Wall("plated-phase-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedPhaseWall.requirements, 4));
			health = 190 * 16;
			armor = 10f;
			size = 2;
			chanceDeflect = 16f;
			flashHit = true;
		}};

		hematiteConveyor = new Conveyor("hematite-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.hematite, 1));
			health = 40;
			speed = 0.05f;
			displayedSpeed = 7f;
		}};

		ironConveyor = new Conveyor("iron-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.lead, 1));
			health = 60;
			speed = 0.1f;
			displayedSpeed = 14f;
			buildCostMultiplier = 1.5f;
		}};

		bulkConveyor = new StackConveyor("bulk-conveyor") {{
			requirements(Category.distribution, ItemStack.with(Items.plastanium, 1, Items.titanium, 1, Items.silicon, 1));
			health = 90;
			speed = 0.07f;
			itemCapacity = 10;
		}};

		surgeBulkConveyor = new StackConveyor("surge-bulk-conveyor") {{
			requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 1, AstraItems.magnetite, 1, Items.thorium, 1));
			health = 140;
			speed = 0.06f;
			itemCapacity = 20;
		}};

		ironJunction = new Junction("iron-junction") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2));
			health = 70;
			buildCostMultiplier = 5f;
			speed = 15;
			capacity = 4;
		}};

		((Conveyor)hematiteConveyor).junctionReplacement = ironJunction;
		((Conveyor)ironConveyor).junctionReplacement = ironJunction;

		ironRouter = new Router("iron-router") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 3));
			health = 100;
			buildCostMultiplier = 2f;
		}};

		ironOverflowGate = new OverflowGate("iron-overflow-gate"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.lead, 2));
			health = 70;
			buildCostMultiplier = 2f;
		}};

		ironUnderflowGate = new OverflowGate("iron-underflow-gate"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.lead, 2));
			health = 70;
			buildCostMultiplier = 2f;
			invert = true;
		}};

		ironSorter = new Sorter("iron-sorter"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.graphite, 1));
			health = 70;
			buildCostMultiplier = 3f;
		}};

		invertedIronSorter = new Sorter("inverted-iron-sorter"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.graphite, 1));
			health = 70;
			buildCostMultiplier = 3f;
			invert = true;
		}};

		coreNode = new CoreBlock("core-node") {{
			requirements(Category.effect, ItemStack.with(AstraItems.iron, 1000, Items.graphite, 500, Items.lead, 500));
			health = 2000;
			size = 4;
			itemCapacity = 5000;
			alwaysUnlocked = true;
			isFirstTier = true;

			unitType = UnitTypes.alpha;
			unitCapModifier = 10;
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
