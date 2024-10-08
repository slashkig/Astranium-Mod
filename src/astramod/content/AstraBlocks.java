package astramod.content;

import arc.util.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.world.Block;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import mindustry.entities.Units;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.part.DrawPart.*;
import mindustry.entities.pattern.*;
import mindustry.graphics.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.content.*;
import astramod.graphics.*;
import astramod.world.draw.*;
import astramod.world.meta.*;
import astramod.world.blocks.defense.*;
import astramod.world.blocks.distribution.*;
import astramod.world.blocks.environment.*;
import astramod.world.blocks.liquid.AstraLiquidBridge;
import astramod.world.blocks.power.*;
import astramod.world.blocks.production.*;
import astramod.world.blocks.storage.*;

@SuppressWarnings("unused")
public class AstraBlocks {
	public static Block
		hardstone, hardstoneWall,
		oreTestium, oreHematite, oreLithium, oreErythronite, oreNeodymium, wallOreCopper, wallOreLead, wallOreLithium, erythronicHardstoneWall,
		ironFurnace, blastFurnace, castIronPress, hydraulicPress, castIronSmelter, purificationSmelter, castIronKiln, castIronMixer, formulationMixer, magnetiteSynthesizer, explosivesRefinery, cryofluidBlender, cryofluidProcessor, plastaniumCompressor, plastaniumFabricator, steelForge, steelFoundry, ferrofluidMixer, plasmaEnergizer, phaseWeaver, phaseLoom, surgeArcFurnace, surgeArcCrucible, vacuumChamber, astraniumForge,
		windTurbine, windTurbineLarge,
		compactDrill, ironDrill, augerDrill, plasmaDrill, excavationDrill, compactBore, laserBore, pulseBore, frackingDrill,
		compactPump, turbinePump, jetstreamPump,
		hematiteWall, hematiteWallLarge, ironWall, ironWallLarge, ironDoor, platedTitaniumWall, platedTitaniumWallLarge, platedPlastaniumWall, platedPlastaniumWallLarge, steelWall, steelWallLarge, platedThoriumWall, platedThoriumWallLarge, platedSurgeWall, platedSurgeWallLarge, platedPhaseWall, platedPhaseWallLarge, aerotechWall, aerotechWallLarge, astraniumWall, astraniumWallLarge,
		hematiteConveyor, ironConveyor, durasteelConveyor, platedSteelConveyor, bulkConveyor, surgeBulkConveyor, surgeBulkJunction, surgeBulkRouter,
		ironJunction, ironBridge, ironRouter, ironDistributor, ironOverflowGate, ironUnderflowGate, ironSorter, invertedIronSorter, platedJunction, platedBridge, platedRouter, platedDistributor, platedOverflowGate, platedUnderflowGate, platedSorter, invertedPlatedSorter,
		crudePipeline, wavePipeline, jetPipeline, crystalPipeline, waveJunction, waveBridge, waveRouter, crystalJunction, crystalBridge, crystalRouter,
		ironTank, steelTank, crystalTank,
		coreNode, coreHub,
		dart, viper,
		omegafactory, uberwall, superRouter, testblaster;

	public static void load() {
		Log.info("Loading blocks");

		// region ENVIRONMENT

		hardstone = new Floor("hardstone") {{ variants = 4; }};

		hardstoneWall = new AstraStaticWall("hardstone-wall") {{
			variants = 3; largeVariants = 2;
			hardstone.asFloor().wall = this;
		}};

		// region ORES

		oreTestium = new OreBlock(AstraItems.testium) {{ variants = 1; }};

		oreHematite = new OreBlock(AstraItems.hematite);

		oreLithium = new OreBlock(AstraItems.lithium);

		oreNeodymium = new OreBlock(AstraItems.neodymium);

		wallOreCopper = new OreBlock("ore-wall-copper", Items.copper) {{ wallOre = true; variants = 4; }};

		wallOreLead = new OreBlock("ore-wall-lead", Items.lead) {{ wallOre = true; variants = 4; }};

		wallOreLithium = new OreBlock("ore-wall-lithium", AstraItems.lithium) {{ wallOre = true; }};

		erythronicHardstoneWall = new AstraStaticWall("erythronite-hardstone-wall") {{
			itemDrop = AstraItems.crystals;
			variants = 3; largeVariants = 2;
		}};

		// region CRAFTERS

		ironFurnace = new GenericCrafter("iron-furnace") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.hematite, 40, Items.lead, 10));
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;

			consumeItem(AstraItems.hematite, 2);
			consumePower(0.40f);
			craftTime = 50f;
			outputItem = new ItemStack(AstraItems.iron, 1);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(AstraPal.ironSmoke));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		blastFurnace = new GenericCrafter("blast-furnace") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 100,
				AstraItems.lithium, 60,
				Items.titanium, 80,
				Items.graphite, 75
			));
			scaledHealth = 55;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.hematite, 8, Items.pyratite, 2));
			consumePower(2.8f);
			craftTime = 60f;
			outputItem = new ItemStack(AstraItems.iron, 6);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawEmitSmoke() {{
				color = AstraPal.ironSmoke;
				particles = 20;
				particleLife = 90f;
				particleRad = 6f;
			}});
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronPress = new GenericCrafter("cast-iron-press") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 40, Items.lead, 25, Items.copper, 10));
			scaledHealth = 45;
			size = 2;
			fogRadius = 2;
			hasItems = true;

			consumeItem(Items.coal, 4);
			consumePower(0.25f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.graphite, 2);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{
				sinMag = 2f;
				sinScl = 2f * (15f / Mathf.PI);
				sinOffset = 30f;
				lenOffset = -2f;
			}}, new DrawDefault());
			craftEffect = Fx.pulverizeMedium;
		}};

		hydraulicPress = new MultiLiquidCrafter("hydraulic-press") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 120,
				Items.graphite, 100,
				AstraItems.magnetite, 60,
				Items.plastanium, 75,
				Items.silicon, 50
			));
			scaledHealth = 60;
			size = 3;
			fogRadius = 3;
			hasPower = hasLiquids = hasItems = true;
			itemCapacity = 20;
			liquidCapacity = 20f;

			consumeItem(Items.coal, 8);
			consumeLiquidsMulti(0.15f, Liquids.water, 0.75f, Liquids.oil, 1.5f);
			consumePower(2.2f);
			craftTime = 75f;
			outputItem = new ItemStack(Items.graphite, 5);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{
				sinMag = 2.5f;
				sinScl = (5f / 6f) * (15f / Mathf.PI);
				sinOffset = 12.5f;
				lenOffset = -2.5f;
			}}, new DrawDefault());
			craftEffect = Fx.pulverizeMedium;
		}};

		castIronSmelter = new GenericCrafter("cast-iron-smelter") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 60, Items.copper, 30, Items.graphite, 20));
			scaledHealth = 45;
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.coal, 2, Items.sand, 4));
			consumePower(0.70f);
			craftTime = 200f / 3;
			outputItem = new ItemStack(Items.silicon, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(AstraPal.siliconSmoke));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		purificationSmelter = new BoostableCrafter("purification-smelter") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 125,
				Items.silicon, 60,
				Items.graphite, 90,
				Items.titanium, 100,
				AstraItems.lithium, 75
			));
			scaledHealth = 60;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(Items.sand, 5, Items.graphite, 2));
			consumeItemBoost(Items.pyratite, 2, 0.8f);
			consumePower(5f);
			craftTime = 50f;
			outputItem = new ItemStack(Items.silicon, 5);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawEmitSmoke() {{
				color = AstraPal.siliconSmoke;
				particles = 20;
				particleLife = 90f;
				particleRad = 6f;
			}});
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronKiln = new GenericCrafter("cast-iron-kiln") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 60, Items.lead, 40, Items.copper, 30));
			scaledHealth = 45;
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.lead, 3, Items.sand, 3));
			consumePower(0.60f);
			craftTime = 80f;
			outputItem = new ItemStack(Items.metaglass, 3);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(AstraPal.glassSmoke));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronMixer = new GenericCrafter("cast-iron-mixer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 50, Items.lead, 30, Items.graphite, 25));
			scaledHealth = 45;
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(AstraItems.hematite, 2, Items.coal, 4, Items.sand, 3));
			consumePower(0.40f);
			craftTime = 150f;
			outputItem = new ItemStack(Items.pyratite, 2);

			drawer = new DrawMulti(new DrawDefault());
			craftEffect = Fx.smeltsmoke;
		}};

		formulationMixer = new GenericCrafter("formulation-mixer") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 100,
				Items.plastanium, 50,
				Items.graphite, 90,
				Items.metaglass, 70,
				AstraItems.magnetite, 60
			));
			scaledHealth = 55;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.hematite, 5, Items.coal, 8, Items.sand, 6));
			consumePower(3.5f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.pyratite, 5);

			//drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-spinner", 4, true));
			craftEffect = Fx.smeltsmoke;
		}};

		magnetiteSynthesizer = new GenericCrafter("magnetite-synthesizer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 80, Items.silicon, 40, Items.graphite, 50));
			scaledHealth = 50;
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(AstraItems.hematite, 2, Items.graphite, 1));
			consumePower(1.5f);
			craftTime = 200f / 3;
			outputItem = new ItemStack(AstraItems.magnetite, 1);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat());
			ambientSound = Sounds.electricHum;
			craftEffect = Fx.smeltsmoke;
		}};

		cryofluidBlender = new GenericCrafter("cryofluid-blender") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 90, Items.metaglass, 60, Items.titanium, 40));
			scaledHealth = 50;
			size = 2;
			fogRadius = 2;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 30f;

			consumeItem(Items.titanium, 2);
			consumeLiquid(Liquids.water, 0.25f);
			consumePower(1.2f);
			craftTime = 200f;
			outputLiquid = new LiquidStack(Liquids.cryofluid, 0.25f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water),
				new DrawLiquidTile(Liquids.cryofluid) {{ drawLiquidLight = true; }},
				new DrawDefault()
			);
			lightLiquid = Liquids.cryofluid;
		}};

		cryofluidProcessor = new GenericCrafter("cryofluid-processor") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 140,
				Items.metaglass, 125,
				Items.plastanium, 90,
				Items.silicon, 100,
				Items.graphite, 120
			));
			scaledHealth = 65;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			itemCapacity = 20;
			liquidCapacity = 100f;

			consumeItem(Items.titanium, 5);
			consumeLiquid(Liquids.water, 0.8f);
			consumePower(4.4f);
			craftTime = 500f / 3;
			outputLiquid = new LiquidStack(Liquids.cryofluid, 0.8f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water),
				new DrawLiquidTile(Liquids.cryofluid) {{ drawLiquidLight = true; }},
				new DrawBubbles(Color.valueOf("8be8ff")) {{
					sides = 10;
					recurrence = 3f;
					spread = 6;
					radius = 1.5f;
					amount = 20;
				}},
				new DrawRegion("-spinner", 1, true),
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
			fogRadius = 2;
			hasPower = hasItems = hasLiquids = true;
			liquidCapacity = 60f;

			consumeItem(Items.titanium, 4);
			consumeLiquid(Liquids.oil, 0.3f);
			consumePower(3.6f);
			craftTime = 96f;
			outputItem = new ItemStack(Items.plastanium, 2);

			//drawer = new DrawMulti(new DrawDefault(), new DrawFade());
			craftEffect = Fx.formsmoke;
			updateEffect = Fx.plasticburn;
		}};

		plastaniumFabricator = new GenericCrafter("plastanium-fabricator") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 140,
				Items.silicon, 120,
				Items.plastanium, 100,
				AstraItems.lithium, 90,
				Items.phaseFabric, 50,
				AstraItems.neodymium, 60
			));
			scaledHealth = 70;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 20;
			liquidCapacity = 100f;

			consumeItems(ItemStack.with(Items.titanium, 9, AstraItems.lithium, 3));
			consumeLiquid(Liquids.oil, 0.8f);
			consumePower(9f);
			craftTime = 90f;
			outputItem = new ItemStack(Items.plastanium, 6);

			//drawer = new DrawMulti(new DrawDefault(), new DrawFade());
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
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.iron, 3, Items.coal, 4));
			consumePower(4.5f);
			craftTime = 80f;
			outputItem = new ItemStack(AstraItems.steel, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion());
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.2f;
			craftEffect = Fx.smeltsmoke;
		}};

		steelFoundry = new GenericCrafter("steel-foundry") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 200,
				Items.plastanium, 175,
				Items.silicon, 150,
				Items.graphite, 190,
				AstraItems.crystals, 100,
				Items.thorium, 120
			));
			scaledHealth = 60;
			armor = 5f;
			size = 4;
			fogRadius = 4;
			hasPower = hasItems = true;
			itemCapacity = 40;

			consumeItems(ItemStack.with(AstraItems.iron, 4, Items.coal, 6, Items.titanium, 2));
			consumePower(12f);
			craftTime = 48f;
			outputItem = new ItemStack(AstraItems.steel, 4);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawCircles() {{
				color = Color.valueOf("ffc073").a(0.25f);
				strokeMax = 2.5f;
				radius = 10f;
				amount = 2;
				timeScl = 240f;
			}}, new DrawEmitSmoke() {{
				color = Color.valueOf("2a2a2a");
				alpha = 0.4f;
				particles = 30;
				particleLife = 150f;
				particleRad = 10f;
				particleSize = 4f;
				addSizeMult = 0.3f;
			}});
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.8f;
			craftEffect = Fx.smeltsmoke;
			updateEffect = Fx.smeltsmoke;
		}};

		explosivesRefinery = new GenericCrafter("explosives-refinery") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 110,
				Items.titanium, 80,
				Items.silicon, 60,
				AstraItems.magnetite, 50
			));
			scaledHealth = 60;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.lithium, 1, Items.pyratite, 2));
			consumePower(4.2f);
			craftTime = 75f;
			outputItem = new ItemStack(Items.blastCompound, 2);

			//drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-spinner", 4, true));
			craftEffect = Fx.smeltsmoke;
		}};

		ferrofluidMixer = new BoostableCrafter("ferrofluid-mixer") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 150,
				Items.metaglass, 140,
				Items.plastanium, 80,
				AstraItems.magnetite, 125,
				Items.graphite, 100
			));
			scaledHealth = 65;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 40f;

			consumeItem(AstraItems.magnetite, 1);
			consumeItemBoost(AstraItems.crystals, 1, 1.2f);
			consumeLiquid(Liquids.oil, 0.25f);
			consumePower(4.5f);
			craftTime = 40f;
			outputLiquid = new LiquidStack(AstraFluids.ferrofluid, 0.25f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.oil),
				new DrawLiquidTile(AstraFluids.ferrofluid),
				new DrawRegion("-spinner", 1, true),
				new DrawDefault()
			);
		}};

		plasmaEnergizer = new BoostableCrafter("plasma-energizer") {{
			requirements(Category.crafting, ItemStack.with(
				AstraItems.steel, 160,
				Items.metaglass, 125,
				Items.plastanium, 110,
				AstraItems.magnetite, 140,
				Items.thorium, 90,
				AstraItems.crystals, 100
			));
			scaledHealth = 65;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 80f;

			warmupSpeed = 0.008f;
			consumeItem(AstraItems.crystals, 2);
			consumeLiquid(Liquids.water, 2f / 3f);
			consumePower(7.2f);
			craftTime = 40f;
			outputLiquid = new LiquidStack(AstraFluids.plasma, 1f / 3f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water),
				new DrawLiquidTile(AstraFluids.plasma) {{ drawLiquidLight = true; }},
				new DrawPlasmaBall(),
				new DrawDefault()
			);

			ambientSound = Sounds.flux;
			ambientSoundVolume = 0.3f;
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
			fogRadius = 2;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.thorium, 8, Items.sand, 16));
			consumePower(7f);
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
				Items.phaseFabric, 100
			));
			scaledHealth = 70;
			armor = 2f;
			size = 3;
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 40;

			consumeItems(ItemStack.with(Items.thorium, 10, Items.sand, 20, AstraItems.crystals, 3));
			consumePower(16f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.phaseFabric, 3);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawMultiWeave() {{ glowColor = new Color(1f, 0.4f, 0.4f, 0.8f); }},
				new DrawDefault(),
				new DrawGlowRegion() {{ color = Items.thorium.color; }}
			);
			ambientSound = Sounds.techloop;
			ambientSoundVolume = 0.4f;
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
			fogRadius = 3;
			hasPower = hasItems = true;
			itemCapacity = 30;

			consumeItems(ItemStack.with(Items.copper, 8, AstraItems.lithium, 4, Items.titanium, 4, Items.silicon, 6));
			consumePower(6f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.surgeAlloy, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame());
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.3f;
			craftEffect = Fx.smeltsmoke;
		}};

		surgeArcCrucible = new GenericCrafter("surge-arc-crucible") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 300,
				Items.silicon, 200,
				Items.plastanium, 250,
				AstraItems.lithium, 180,
				Items.thorium, 175,
				AstraItems.crystals, 160
			));
			scaledHealth = 70;
			armor = 6f;
			size = 4;
			fogRadius = 4;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 50;
			liquidCapacity = 75f;

			warmupSpeed = 0.014f;
			consumeItems(ItemStack.with(Items.copper, 10, AstraItems.lithium, 8, Items.titanium, 6, Items.silicon, 10));
			consumeLiquid(Liquids.slag, 0.5f);
			consumePower(18f);
			craftTime = 80f;
			outputItem = new ItemStack(Items.surgeAlloy, 4);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawSoftParticles() {{
				alpha = 0.35f;
				particleRad = 12f;
				particleSize = 9f;
				particleLife = 120f;
				particles = 27;
			}});
			ambientSound = Sounds.smelter;
			ambientSoundVolume = 0.3f;
			craftEffect = Fx.smeltsmoke;
		}};

		vacuumChamber = new GenericCrafter("vacuum-chamber") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 320,
				Items.plastanium, 280,
				AstraItems.crystals, 250,
				Items.silicon, 220,
				Items.metaglass, 260,
				AstraItems.neodymium, 200
			));
			scaledHealth = 70;
			armor = 5f;
			size = 4;
			fogRadius = 4;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 40;
			liquidCapacity = 200f;

			consumeItems(ItemStack.with(Items.phaseFabric, 4, Items.silicon, 8, AstraItems.crystals, 5));
			consumeLiquid(Liquids.cryofluid, 1.2f);
			consumePower(20f);
			craftTime = 200f;
			outputItem = new ItemStack(AstraItems.aerogel, 2);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.cryofluid) {{ drawLiquidLight = true; }},
				new DrawCrucibleFlame() {{ flameColor = AstraItems.aerogel.color.cpy(); }},
				new DrawDefault()
			);
			ambientSound = Sounds.electricHum;
			ambientSoundVolume = 0.5f;
			updateEffect = Fx.plasticburn; // Not sure why it's called that
			craftEffect = Fx.smeltsmoke;
		}};

		astraniumForge = new GenericCrafter("astranium-forge") {{
			requirements(Category.crafting, ItemStack.with(
				Items.surgeAlloy, 325,
				Items.silicon, 280,
				Items.phaseFabric, 200,
				AstraItems.lithium, 250,
				AstraItems.neodymium, 180,
				AstraItems.crystals, 225
			));
			scaledHealth = 75;
			armor = 8f;
			size = 4;
			fogRadius = 4;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 50;
			liquidCapacity = 100f;

			warmupSpeed = 0.001f;
			consumeItems(ItemStack.with(Items.surgeAlloy, 6, AstraItems.crystals, 10, AstraItems.neodymium, 8));
			consumeLiquid(AstraFluids.ferrofluid, 0.6f);
			consumePower(21f);
			craftTime = 240f;
			outputItem = new ItemStack(AstraItems.astranium, 2);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawRegion("-spinner", 5, true),
				new DrawGlowRegion("-spinner-glow") {{ color = Color.purple.cpy(); glowIntensity = 0.3f; rotateSpeed = 5f; rotate = true; layer = Layer.block; }},
				new DrawArcSmelt() {{ flameColor = AstraItems.astranium.color.cpy(); }},
				new DrawDefault(),
				new DrawTopHeat() {{ fluctuate = true; }},
				new DrawGlowRegion() {{ color = Color.purple.cpy(); glowIntensity = 0.8f; }}
			);
			ambientSound = Sounds.pulse;
			ambientSoundVolume = 0.15f;
			craftEffect = Fx.smeltsmoke;
		}};

		// region POWER

		windTurbine = new WindGenerator("wind-turbine") {{
			requirements(Category.power, ItemStack.with(AstraItems.hematite, 30, Items.copper, 40));
			size = 2;
			fogRadius = 2;

			powerProduction = 1f / 6f;

			drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator", 4f, true));
		}};

		windTurbineLarge = new WindGenerator("wind-turbine-large") {{
			requirements(Category.power, ItemStack.with(AstraItems.iron, 50, Items.copper, 90, Items.silicon, 40));
			scaledHealth = 45f;
			size = 3;
			fogRadius = 3;

			powerProduction = 0.5f;

			drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-rotator", 5f, true));
		}};

		// region DRILLS

		compactDrill = new MultiCoolantDrill("compact-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.hematite, 14, Items.lead, 6));
			size = 2;
			fogRadius = 2;

			liquidBoostIntensity = 1.4f;
			consumeLiquidBoosts(0.04f, Liquids.water, 1.4f, AstraFluids.ferrofluid, 1.6f);
			drillTime = 500;
			tier = 2;
			hardnessDrillMultiplier = 75f;
		}};

		ironDrill = new MultiCoolantDrill("iron-drill") {{
			requirements(Category.production, ItemStack.with(AstraItems.iron, 25, Items.copper, 30, Items.graphite, 20));
			size = 3;
			fogRadius = 3;
			hasPower = true;
			liquidCapacity = 7.5f;

			liquidBoostIntensity = 1.5f;
			consumePower(0.55f);
			consumeLiquidBoosts(0.07f, Liquids.water, 1.5f, AstraFluids.ferrofluid, 1.75f);
			drillTime = 360;
			tier = 3;
			hardnessDrillMultiplier = 65f;
			drillMultipliers.put(AstraItems.lithium, 0.6f);

			rotateSpeed = 3f;
			updateEffect = Fx.pulverizeMedium;
			drillEffect = Fx.mineBig;
		}};

		augerDrill = new MultiCoolantDrill("auger-drill") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.steel, 40,
				AstraItems.magnetite, 30,
				Items.graphite, 45,
				Items.titanium, 35
			));
			scaledHealth = 55;
			size = 3;
			fogRadius = 3;
			hasPower = true;
			itemCapacity = 20;
			liquidCapacity = 10f;

			liquidBoostIntensity = 1.6f;
			consumePower(1.2f);
			consumeLiquidBoosts(0.1f, Liquids.water, 1.6f, AstraFluids.ferrofluid, 1.95f);
			drillTime = 240;
			tier = 4;
			hardnessDrillMultiplier = 55f;
			drillMultipliers.put(AstraItems.lithium, 0.7f);

			rotateSpeed = 4.5f;
			drawRim = true;
			updateEffect = Fx.pulverizeMedium;
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineBig;
		}};

		plasmaDrill = new PlasmaDrill("plasma-drill") {{
			requirements(Category.production, ItemStack.with(
				Items.surgeAlloy, 60,
				AstraItems.magnetite, 50,
				Items.thorium, 55,
				AstraItems.lithium, 65,
				Items.silicon, 70
			));
			scaledHealth = 60;
			size = 4;
			fogRadius = 4;
			hasPower = true;
			itemCapacity = 30;
			liquidCapacity = 15f;

			warmupSpeed = 0.008f;
			liquidBoostIntensity = 1.65f;
			consumePower(4f);
			consumeLiquid(AstraFluids.plasma, 0.05f);
			consumeLiquidBoosts(0.14f, Liquids.water, 1.65f, AstraFluids.ferrofluid, 2.05f);
			drillTime = 200;
			tier = 5;
			hardnessDrillMultiplier = 45f;
			drillMultipliers.put(AstraItems.lithium, 0.8f);
			drillMultipliers.put(AstraItems.neodymium, 0.75f);

			rotateSpeed = 5.5f;
			drawRim = true;
			heatColor = AstraPal.plasmaGlowBlue;
			updateEffect = AstraFx.pulverizePurple;
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineHuge;
		}};

		excavationDrill = new PlasmaDrill("excavation-drill") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.astranium, 80,
				AstraItems.neodymium, 70,
				Items.plastanium, 95,
				AstraItems.lithium, 100,
				AstraItems.crystals, 85,
				AstraItems.aerogel, 70
			));
			scaledHealth = 65;
			size = 5;
			fogRadius = 5;
			hasPower = true;
			itemCapacity = 40;
			liquidCapacity = 20f;

			warmupSpeed = 0.005f;
			liquidBoostIntensity = 1.7f;
			consumePower(9f);
			consumeLiquid(AstraFluids.plasma, 0.15f);
			consumeLiquidBoosts(0.18f, Liquids.water, 1.7f, AstraFluids.ferrofluid, 2.2f);
			drillTime = 180;
			tier = 5;
			hardnessDrillMultiplier = 35f;
			drillMultipliers.put(AstraItems.lithium, 0.9f);

			rotateSpeed = 6.5f;
			drawRim = true;
			heatColor = AstraPal.plasmaGlowBlue;
			updateEffect = AstraFx.pulverizePurple;
			updateEffectChance = 0.05f;
			drillEffect = Fx.mineHuge;
		}};

		compactBore = new WallDrill("compact-bore") {{
			requirements(Category.production, ItemStack.with(AstraItems.iron, 35, Items.lead, 40, Items.graphite, 25));
			size = 2;
			fogRadius = 2;
			scaledHealth = 50;
			hasPower = true;
			liquidCapacity = 7.5f;

			liquidBoostIntensity = 1.8f;
			consumePower(0.4f);
			consumeLiquidBoosts(0.07f, Liquids.water, 1.8f, AstraFluids.ferrofluid, 3f);
			drillTime = 200f;
			tier = 3;
			hardnessDrillMultiplier = 40f;
			drillMultipliers.put(AstraItems.lithium, 1.2f);

			rotateSpeed = 3f;
			ambientSound = Sounds.drill;
			ambientSoundVolume = 0.05f;
			updateEffectChance = 0.015f;
		}};

		laserBore = new BeamDrill("lazer-bore") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.steel, 50,
				AstraItems.lithium, 40,
				Items.metaglass, 60,
				Items.silicon, 55
			));
			scaledHealth = 55;
			size = 3;
			fogRadius = 4;
			itemCapacity = 20;

			optionalBoostIntensity = 2.65f;
			consumePower(1.8f);
			consumeLiquid(AstraFluids.plasma, 0.08f).boost();
			drillTime = 120f;
			tier = 4;
			range = 4;
			drillMultipliers.put(AstraItems.lithium, 0.8f);
			drillMultipliers.put(Items.thorium, 0.6f);
			drillMultipliers.put(AstraItems.crystals, 0.6f);

			boostHeatColor = AstraFluids.plasma.color;
		}};

		pulseBore = new BeamDrill("pulse-bore") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.astranium, 75,
				AstraItems.lithium, 80,
				AstraItems.crystals, 70,
				Items.silicon, 90,
				AstraItems.neodymium, 60
			));
			scaledHealth = 60;
			size = 4;
			fogRadius = 5;
			itemCapacity = 30;
			liquidCapacity = 15f;

			optionalBoostIntensity = 2.6f;
			consumePower(5.6f);
			consumeLiquid(AstraFluids.plasma, 0.12f);
			consumeLiquid(Liquids.cryofluid, 0.22f).boost();
			drillTime = 60f;
			tier = 5;
			range = 6;
			drillMultipliers.put(AstraItems.lithium, 0.9f);
			drillMultipliers.put(Items.thorium, 0.8f);
			drillMultipliers.put(AstraItems.crystals, 0.8f);

			heatColor = AstraFluids.plasma.color;
			boostHeatColor = Liquids.cryofluid.color;
		}};

		frackingDrill = new PistonFracker("fracker-drill") {{
			requirements(Category.production, ItemStack.with(
				AstraItems.steel, 100,
				Items.graphite, 160,
				AstraItems.magnetite, 125,
				Items.metaglass, 150,
				Items.titanium, 140
			));
			scaledHealth = 60;
			size = 3;
			fogRadius = 3;
			liquidCapacity = 50f;

			attribute = Attribute.oil;
			baseEfficiency = 0f;
			consumeItem(Items.sand);
			consumePower(3.8f);
			consumeLiquid(Liquids.water, 0.25f);
			itemUseTime = 45f;
			result = Liquids.oil;
			pumpAmount = 0.35f;

			pistons = new DrawPistons() {{ sinMag = 3f; sinOffset = 8f; lenOffset = 1f; angleOffset = 45f; }};
			updateEffect = Fx.pulverize;
			updateEffectChance = 0.03f;
		}};

		// region WALLS

		hematiteWall = new Wall("hematite-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.hematite, 6));
			health = 90 * 4;
			fogRadius = 2;
		}};

		hematiteWallLarge = new Wall("hematite-wall-large") {{
			requirements(Category.defense, ItemStack.mult(hematiteWall.requirements, 4));
			health = 90 * 16;
			size = 2;
			fogRadius = 2;
		}};

		ironWall = new Wall("iron-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.iron, 6));
			health = 120 * 4;
			armor = 2f;
			fogRadius = 2;
		}};

		ironWallLarge = new Wall("iron-wall-large") {{
			requirements(Category.defense, ItemStack.mult(ironWall.requirements, 4));
			health = 120 * 16;
			armor = 2f;
			size = 2;
			fogRadius = 2;
		}};

		ironDoor = new AutoDoor("iron-door") {{
			requirements(Category.defense, ItemStack.with(AstraItems.iron, 20, Items.silicon, 10));
			health = 115 * 16;
			armor = 2f;
			size = 2;
			fogRadius = 2;
		}};

		platedTitaniumWall = new Wall("plated-titanium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.titanium, 6, Items.graphite, 2));
			health = 160 * 4;
			armor = 4f;
			fogRadius = 2;
		}};

		platedTitaniumWallLarge = new Wall("plated-titanium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedTitaniumWall.requirements, 4));
			health = 160 * 16;
			armor = 4f;
			size = 2;
			fogRadius = 2;
		}};

		platedPlastaniumWall = new Wall("plated-plastanium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.plastanium, 6, Items.metaglass, 4));
			health = 175 * 4;
			armor = 2f;
			fogRadius = 2;

			insulated = true;
			absorbLasers = true;
		}};

		platedPlastaniumWallLarge = new Wall("plated-plastanium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedPlastaniumWall.requirements, 4));
			health = 175 * 16;
			armor = 2f;
			size = 2;
			fogRadius = 2;

			insulated = true;
			absorbLasers = true;
		}};

		steelWall = new Wall("steel-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.steel, 8));
			health = 220 * 4;
			armor = 8f;
			fogRadius = 2;
		}};

		steelWallLarge = new Wall("steel-wall-large") {{
			requirements(Category.defense, ItemStack.mult(steelWall.requirements, 4));
			health = 220 * 16;
			armor = 8f;
			size = 2;
			fogRadius = 2;
		}};

		platedThoriumWall = new EffectWall("plated-thorium-wall") {{
			requirements(Category.defense, ItemStack.with(Items.thorium, 6, AstraItems.crystals, 2));
			health = 205 * 4;
			armor = 6f;
			fogRadius = 2;

			effectRange = 12f;
			effectStrength = 4f;
			effect = build -> {
				Units.nearbyEnemies(build.team, build.x, build.y, effectRange, unit -> {
					unit.damage(effectStrength * Time.delta / 60f);
				});
			};

			effectColor = Color.purple;
			effectAlpha = 0.1f;
			lightAlpha = 0.75f;
			effectStat = Stat.damage;
		}};

		platedThoriumWallLarge = new EffectWall("plated-thorium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedThoriumWall.requirements, 4));
			health = 205 * 16;
			armor = 6f;
			size = 2;
			fogRadius = 2;

			effectRange = 24f;
			effectStrength = 16f;
			effect = build -> {
				Units.nearbyEnemies(build.team, build.x, build.y, effectRange, unit -> {
					unit.damage(effectStrength * Time.delta / 60f);
				});
			};

			effectColor = Color.purple;
			effectAlpha = 0.1f;
			lightAlpha = 0.75f;
			effectStat = Stat.damage;
		}};

		platedSurgeWall = new Wall("plated-surge-wall") {{
			requirements(Category.defense, ItemStack.with(Items.surgeAlloy, 8, AstraItems.lithium, 6));
			health = 280 * 4;
			armor = 16f;
			fogRadius = 2;

			lightningChance = 0.075f;
			lightningDamage = 45f;
		}};

		platedSurgeWallLarge = new Wall("plated-surge-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedSurgeWall.requirements, 4));
			health = 280 * 16;
			armor = 16f;
			size = 2;
			fogRadius = 2;

			lightningChance = 0.075f;
			lightningDamage = 45f;
		}};

		platedPhaseWall = new Wall("plated-phase-wall") {{
			requirements(Category.defense, ItemStack.with(Items.phaseFabric, 8, AstraItems.magnetite, 6));
			health = 190 * 4;
			armor = 10f;
			fogRadius = 2;

			chanceDeflect = 16f;
			flashHit = true;
		}};

		platedPhaseWallLarge = new Wall("plated-phase-wall-large") {{
			requirements(Category.defense, ItemStack.mult(platedPhaseWall.requirements, 4));
			health = 190 * 16;
			armor = 10f;
			size = 2;
			fogRadius = 2;

			chanceDeflect = 16f;
			flashHit = true;
		}};

		aerotechWall = new ProjectorWall("aerotech-wall", 1.6f) {{
			requirements(Category.defense, ItemStack.with(AstraItems.aerogel, 8, Items.silicon, 6, AstraItems.crystals, 5));
			health = 210 * 4;
			armor = 12f;
			fogRadius = 3;

			consumePower(0.05f);
			shieldHealth = 150f;
			breakCooldown = 1500f;
			regenSpeed = 0.25f;
			flashHit = true;
			absorbLightning = absorbLasers = true;
		}};

		aerotechWallLarge = new ProjectorWall("aerotech-wall-large", 3.2f) {{
			requirements(Category.defense, ItemStack.mult(aerotechWall.requirements, 4));
			health = 210 * 16;
			armor = 12f;
			size = 2;
			fogRadius = 4;

			consumePower(0.2f);
			shieldHealth = 600f;
			breakCooldown = 1200f;
			regenSpeed = 1f;
			flashHit = true;
			absorbLightning = absorbLasers = true;
		}};

		astraniumWall = new EffectWall("astranium-wall") {{
			requirements(Category.defense, ItemStack.with(AstraItems.astranium, 8, AstraItems.neodymium, 6));
			health = 360 * 4;
			armor = 25f;
			fogRadius = 2;

			effectRange = 24f;
			effectStrength = 50f;
			effect = build -> {
				Seq<Bullet> bullets = Groups.bullet.intersect(build.x - effectRange, build.y - effectRange, effectRange * 2, effectRange * 2);
				for (Bullet bullet : bullets) {
					if (bullet.team != build.team && bullet.type.hittable) {
						bullet.vel.setAngle(Angles.moveToward(bullet.rotation(), bullet.angleTo(build), effectStrength * Time.delta / 60f));
					}
				}
			};

			effectColor = AstraItems.astranium.color;
			lightAlpha = 0.6f;
			effectStat = AstraStat.magneticStrength;
			effectUnit = StatUnit.percent;
		}};

		astraniumWallLarge = new EffectWall("astranium-wall-large") {{
			requirements(Category.defense, ItemStack.mult(astraniumWall.requirements, 4));
			health = 360 * 16;
			armor = 25f;
			size = 2;
			fogRadius = 2;

			effectRange = 40f;
			effectStrength = 150f;
			effect = build -> {
				Seq<Bullet> bullets = Groups.bullet.intersect(build.x - effectRange, build.y - effectRange, effectRange * 2, effectRange * 2);
				for (Bullet bullet : bullets) {
					if (bullet.team != build.team && bullet.type.hittable) {
						bullet.vel.setAngle(Angles.moveToward(bullet.rotation(), bullet.angleTo(build), effectStrength * Time.delta / 60f));
					}
				}
			};

			effectColor = AstraItems.astranium.color;
			lightAlpha = 0.6f;
			effectStat = AstraStat.magneticStrength;
			effectUnit = StatUnit.percent;
		}};

		// region TRANSPORT

		hematiteConveyor = new Conveyor("hematite-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.hematite, 1));
			health = 40;
			fogRadius = 1;
			speed = 0.05f;
			displayedSpeed = 7f;
		}};

		ironConveyor = new Conveyor("iron-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 1, Items.lead, 1));
			health = 60;
			fogRadius = 1;
			speed = 0.1f;
			displayedSpeed = 14f;
			buildCostMultiplier = 1.5f;
		}};

		durasteelConveyor = new Conveyor("durasteel-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 1, AstraItems.magnetite, 1, Items.titanium, 1));
			health = 180;
			fogRadius = 1;
			speed = 0.15f;
			displayedSpeed = 21f;
			buildCostMultiplier = 1.5f;
		}};

		platedSteelConveyor = new ArmoredConveyor("plated-conveyor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 1, Items.thorium, 1, Items.graphite, 2));
			health = 300;
			armor = 2f;
			fogRadius = 1;
			speed = 0.15f;
			displayedSpeed = 21f;
			buildCostMultiplier = 1.5f;
		}};

		bulkConveyor = new StackConveyor("bulk-conveyor") {{
			requirements(Category.distribution, ItemStack.with(Items.plastanium, 1, Items.metaglass, 1, Items.silicon, 1));
			health = 140;
			armor = 1f;
			fogRadius = 1;
			speed = 0.07f;
			itemCapacity = 10;
		}};

		surgeBulkConveyor = new RailConveyor("surge-bulk-conveyor") {{
			requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 1, AstraItems.magnetite, 2, AstraItems.lithium, 2));
			health = 340;
			armor = 3f;
			fogRadius = 1;
			speed = 0.08f;
			itemCapacity = 20;
			buildCostMultiplier = 1.5f;
		}};

		// region DISTRIBUTION

		ironJunction = new Junction("iron-junction") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2));
			health = 70;
			fogRadius = 1;
			buildCostMultiplier = 3f;
			speed = 12f;
			capacity = 4;

			((Conveyor)hematiteConveyor).junctionReplacement = this;
			((Conveyor)ironConveyor).junctionReplacement = this;
		}};

		ironBridge = new BufferedItemBridge("iron-bridge") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 6, Items.lead, 6));
			health = 70;
			fogRadius = 2;
			fadeIn = moveArrows = false;
			range = 4;
			speed = 40f;
			bufferCapacity = 14;

			arrowSpacing = 6f;
			bridgeWidth = 8f;
			((Conveyor)hematiteConveyor).bridgeReplacement = this;
			((Conveyor)ironConveyor).bridgeReplacement = this;
		}};

		ironRouter = new Router("iron-router") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 3));
			health = 100;
			fogRadius = 2;
			buildCostMultiplier = 3f;
		}};

		ironDistributor = new Router("iron-distributor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 10));
			health = 300;
			size = 2;
			fogRadius = 2;
			buildCostMultiplier = 2f;
			itemCapacity = 4;
		}};

		ironOverflowGate = new OverflowGate("iron-overflow-gate") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.lead, 2));
			health = 70;
			fogRadius = 2;
			buildCostMultiplier = 2f;
		}};

		ironUnderflowGate = new OverflowGate("iron-underflow-gate") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.lead, 2));
			health = 70;
			fogRadius = 2;
			buildCostMultiplier = 2f;
			invert = true;
		}};

		ironSorter = new Sorter("iron-sorter") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.graphite, 1));
			health = 70;
			fogRadius = 2;
			buildCostMultiplier = 3f;
		}};

		invertedIronSorter = new Sorter("inverted-iron-sorter") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.graphite, 1));
			health = 70;
			fogRadius = 2;
			buildCostMultiplier = 3f;
			invert = true;
		}};

		platedJunction = new Junction("plated-junction") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 2, Items.thorium, 2, Items.graphite, 4));
			health = 320;
			armor = 2f;
			fogRadius = 1;
			buildCostMultiplier = 1.5f;
			speed = 8f;
			capacity = 8;

			((Conveyor)durasteelConveyor).junctionReplacement = this;
			((Conveyor)platedSteelConveyor).junctionReplacement = this;
		}};

		platedBridge = new AstraBufferedItemBridge("plated-bridge") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 8, Items.thorium, 6, Items.plastanium, 4));
			health = 335;
			armor = 2f;
			fogRadius = 2;
			fadeIn = moveArrows = false;
			range = 7;
			speed = 25f;
			bufferSpeed = 2f;
			itemCapacity = 20;
			bufferCapacity = 22;

			arrowSpacing = 6f;
			bridgeWidth = 8f;

			((Conveyor)durasteelConveyor).bridgeReplacement = this;
			((Conveyor)platedSteelConveyor).bridgeReplacement = this;
		}};

		platedRouter = new Router("plated-router") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 3, Items.thorium, 2, AstraItems.magnetite, 2));
			health = 350;
			armor = 3f;
			fogRadius = 2;
			buildCostMultiplier = 1.8f;
			itemCapacity = 2;
		}};

		platedDistributor = new Router("plated-distributor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 10, Items.thorium, 8, AstraItems.magnetite, 6));
			health = 900;
			armor = 4f;
			size = 2;
			fogRadius = 2;
			itemCapacity = 8;
		}};

		platedOverflowGate = new OverflowGate("plated-overflow-gate") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 2, Items.thorium, 2, Items.plastanium, 1));
			health = 320;
			armor = 2f;
			fogRadius = 2;
			buildCostMultiplier = 2.2f;
		}};

		platedUnderflowGate = new OverflowGate("plated-underflow-gate") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.steel, 2, Items.thorium, 2, Items.plastanium, 1));
			health = 320;
			armor = 2f;
			fogRadius = 2;
			buildCostMultiplier = 2.2f;
			invert = true;
		}};

		platedSorter = new Sorter("plated-sorter") {{
			requirements(Category.distribution, ItemStack.with(
				AstraItems.steel, 2,
				Items.thorium, 2,
				Items.silicon, 4,
				AstraItems.magnetite, 2
			));
			health = 320;
			armor = 2f;
			fogRadius = 2;
			buildCostMultiplier = 2f;
		}};

		invertedPlatedSorter = new Sorter("inverted-plated-sorter") {{
			requirements(Category.distribution, ItemStack.with(
				AstraItems.steel, 2,
				Items.thorium, 2,
				Items.silicon, 4,
				AstraItems.magnetite, 2
			));
			health = 320;
			armor = 2f;
			fogRadius = 2;
			buildCostMultiplier = 2f;
			invert = true;
		}};

		surgeBulkJunction = new Junction("surge-bulk-junction") {{
			requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 4, Items.phaseFabric, 2));
			health = 360;
			armor = 3f;
			fogRadius = 2;
			buildCostMultiplier = 3f;
			speed = 0f;
			capacity = 25;

			((RailConveyor)surgeBulkConveyor).junctionReplacement = this;
		}};

		surgeBulkRouter = new StackRouter("surge-bulk-router") {{
			requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 10, AstraItems.neodymium, 8, Items.silicon, 16));
			health = 680;
			armor = 4f;
			fogRadius = 3;
			itemCapacity = 20;
			speed = 12.5f;
			buildCostMultiplier = 2f;

			hasPower = true;
			consumesPower = true;
			conductivePower = true;
			baseEfficiency = 0f;
			underBullets = true;
			solid = false;
			consumePower(1f / 3f);
		}};

		// region FLUIDS

		compactPump = new Pump("compact-pump") {{
			requirements(Category.liquid, ItemStack.with(AstraItems.hematite, 25, Items.copper, 30, Items.graphite, 20));
			size = 2;
			fogRadius = 2;
			hasPower = true;
			liquidCapacity = 20f;

			consumePower(0.15f);
			pumpAmount = 0.08f;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawPumpLiquid(),
				new DrawVerticalPump() {{ cycleTime = 120f; maxScale = 1.2f; }}
			);
			squareSprite = false;
		}};

		turbinePump = new Pump("turbine-pump") {{
			requirements(Category.liquid, ItemStack.with(
				AstraItems.iron, 50,
				Items.copper, 60,
				Items.metaglass, 40,
				Items.graphite, 35
			));
			scaledHealth = 50f;
			size = 2;
			fogRadius = 2;
			hasPower = true;
			liquidCapacity = 40f;

			consumePower(0.5f);
			pumpAmount = 0.21f;
			liquidPressure = 1.02f;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawPumpLiquid(),
				new DrawVerticalPump() {{ cycleTime = 90f; maxScale = 1.2f; }}
			);
			squareSprite = false;
		}};

		jetstreamPump = new Pump("jetstream-pump") {{
			requirements(Category.liquid, ItemStack.with(
				AstraItems.steel, 70,
				Items.metaglass, 75,
				Items.graphite, 60,
				AstraItems.magnetite, 40,
				Items.plastanium, 50
			));
			scaledHealth = 55f;
			armor = 1f;
			size = 3;
			fogRadius = 3;
			hasPower = true;
			liquidCapacity = 60f;

			consumePower(1.4f);
			pumpAmount = 0.23f;
			liquidPressure = 1.05f;

			drawer = new DrawMulti(
				new DrawDefault(),
				new DrawPumpLiquid(),
				new DrawVerticalPump() {{ cycleTime = 75f; maxScale = 1.2f; }}
			);
			squareSprite = false;
		}};

		crudePipeline = new Conduit("ripple-pipeline") {{
			requirements(Category.liquid, ItemStack.with(Items.copper, 1, Items.lead, 1));
			health = 50;
			fogRadius = 1;
		}};

		wavePipeline = new Conduit("wave-pipeline") {{
			requirements(Category.liquid, ItemStack.with(Items.metaglass, 1, Items.copper, 2, Items.graphite, 1));
			health = 120;
			fogRadius = 1;
			liquidCapacity = 14f;
			liquidPressure = 1.02f;
		}};

		jetPipeline = new Conduit("jet-pipeline") {{
			requirements(Category.liquid, ItemStack.with(Items.metaglass, 2, Items.titanium, 1, AstraItems.magnetite, 1));
			health = 200;
			fogRadius = 1;
			liquidCapacity = 18f;
			liquidPressure = 1.05f;
		}};

		crystalPipeline = new ArmoredConduit("crystal-pipeline") {{
			requirements(Category.liquid, ItemStack.with(AstraItems.crystals, 2, Items.thorium, 1, Items.plastanium, 1));
			health = 310;
			armor = 2;
			fogRadius = 1;
			liquidCapacity = 24f;
			liquidPressure = 1.1f;
		}};

		waveJunction = new LiquidJunction("wave-junction") {{
			requirements(Category.liquid, ItemStack.with(Items.copper, 6, Items.metaglass, 4, Items.graphite, 2));
			health = 140;
			fogRadius = 1;
			liquidCapacity = 14f;
			liquidPressure = 1.02f;

			((Conduit)crudePipeline).junctionReplacement = this;
			((Conduit)wavePipeline).junctionReplacement = this;
			((Conduit)jetPipeline).junctionReplacement = this;
		}};

		waveBridge = new AstraLiquidBridge("wave-bridge") {{
			requirements(Category.liquid, ItemStack.with(Items.copper, 10, Items.metaglass, 6, Items.graphite, 4));
			health = 140;
			fogRadius = 2;
			liquidCapacity = 16f;
			liquidPressure = 1.02f;
			range = 4;
			hasPower = false;

			((Conduit)crudePipeline).bridgeReplacement = this;
			((Conduit)wavePipeline).bridgeReplacement = this;
			((Conduit)jetPipeline).bridgeReplacement = this;
		}};

		waveRouter = new LiquidRouter("wave-router") {{
			requirements(Category.liquid, ItemStack.with(Items.copper, 4, Items.metaglass, 4, Items.graphite, 4));
			health = 140;
			fogRadius = 2;
			liquidCapacity = 16f;
			liquidPressure = 1.02f;
		}};

		crystalJunction = new LiquidJunction("crystal-junction") {{
			requirements(Category.liquid, ItemStack.with(AstraItems.crystals, 6, Items.thorium, 2, Items.plastanium, 4));
			health = 340;
			armor = 2;
			fogRadius = 1;
			liquidCapacity = 24f;
			liquidPressure = 1.1f;

			((Conduit)crystalPipeline).junctionReplacement = this;
		}};

		crystalBridge = new AstraLiquidBridge("crystal-bridge") {{
			requirements(Category.liquid, ItemStack.with(
				AstraItems.crystals, 10,
				Items.thorium, 6,
				Items.plastanium, 8,
				AstraItems.magnetite, 4
			));
			health = 350;
			armor = 3;
			fogRadius = 2;
			liquidCapacity = 26f;
			liquidPressure = 1.1f;
			range = 7;
			hasPower = false;

			((Conduit)crystalPipeline).bridgeReplacement = this;
		}};

		crystalRouter = new LiquidRouter("crystal-router") {{
			requirements(Category.liquid, ItemStack.with(
				AstraItems.crystals, 4,
				Items.thorium, 2,
				Items.plastanium, 4,
				AstraItems.magnetite, 2
			));
			health = 350;
			armor = 3;
			fogRadius = 2;
			liquidCapacity = 26f;
			liquidPressure = 1.1f;
		}};

		ironTank = new LiquidRouter("iron-container") {{
			requirements(Category.liquid, ItemStack.with(AstraItems.iron, 60, Items.copper, 90, Items.metaglass, 50));
			scaledHealth = 50f;
			size = 2;
			fogRadius = 2;
			solid = true;
			liquidCapacity = 800f;
		}};

		steelTank = new LiquidRouter("steel-tank") {{
			requirements(Category.liquid, ItemStack.with(
				AstraItems.steel, 80,
				Items.metaglass, 100,
				Items.titanium, 40,
				AstraItems.magnetite, 25
			));
			scaledHealth = 60f;
			armor = 3f;
			size = 3;
			fogRadius = 3;
			solid = true;
			liquidCapacity = 2000f;
			liquidPressure = 1.04f;
		}};

		crystalTank = new LiquidRouter("crystal-tank") {{
			requirements(Category.liquid, ItemStack.with(
				Items.surgeAlloy, 100,
				Items.metaglass, 140,
				Items.thorium, 50,
				AstraItems.neodymium, 40,
				AstraItems.crystals, 65
			));
			scaledHealth = 65f;
			armor = 6f;
			size = 4;
			fogRadius = 4;
			solid = true;
			liquidCapacity = 6000f;
			liquidPressure = 1.1f;
		}};

		// region CORES

		coreNode = new AstraCoreBlock("core-node") {{
			requirements(Category.effect, ItemStack.with(AstraItems.iron, 1000, Items.graphite, 500, Items.lead, 1000));
			health = 2000;
			size = 4;
			itemCapacity = 5000;
			alwaysUnlocked = true;
			isFirstTier = true;

			unitType = AstraUnitTypes.manager;
			unitCapModifier = 10;

			thrusterLength = 8f;
		}};

		coreHub = new AstraCoreBlock("core-hub") {{
			requirements(Category.effect, ItemStack.with(
				AstraItems.iron, 4000,
				Items.graphite, 3000,
				Items.titanium, 2000,
				Items.silicon, 2500
			));
			health = 4500;
			armor = 4f;
			size = 5;
			itemCapacity = 7500;

			unitType = AstraUnitTypes.director;
			unitCapModifier = 15;

			thrusterLength = 10f;
		}};

		// region TURRETS

		dart = new ItemTurret("dart") {{
			requirements(Category.turret, ItemStack.with(AstraItems.hematite, 50, Items.lead, 20));
			ammo(
				AstraItems.hematite, new BasicBulletType(3f, 10) {{
					lifetime = 60f;
					width = 8f;
					height = 10f;
					ammoMultiplier = 2;
					reloadMultiplier = 0.8f;
					collidesAir = false;
					fragBullets = 3;
					fragRandomSpread = 120f;
					fragBullet = new BasicBulletType(3f, 4) {{
						lifetime = 20f;
						width = 6f;
						height = 8f;
						shrinkY = 1f;
						despawnEffect = Fx.none;
						collidesAir = false;

						frontColor = AstraPal.hemaFront;
						backColor = AstraPal.hemaBack;
					}};

					frontColor = AstraPal.hemaFront;
					backColor = AstraPal.hemaBack;
				}},
				Items.lead, new BasicBulletType(3f, 18) {{
					lifetime = 60f;
					width = 8f;
					height = 10f;
					ammoMultiplier = 3;
					collidesAir = false;

					frontColor = AstraPal.leadFront;
					backColor = AstraPal.leadBack;
				}},
				AstraItems.iron, new BasicBulletType(3f, 22) {{
					lifetime = 60f;
					width = 9f;
					height = 11f;
					ammoMultiplier = 4;
					pierceCap = 2;
					collidesAir = false;

					frontColor = AstraPal.ironFront;
					backColor = AstraPal.ironBack;
				}},
				Items.graphite, new BasicBulletType(4f, 32) {{
					lifetime = 60f;
					width = 10f;
					height = 12f;
					ammoMultiplier = 4;
					reloadMultiplier = 0.75f;
					collidesAir = false;

					frontColor = AstraPal.graphiteFront;
					backColor = AstraPal.graphiteBack;
				}},
				Items.silicon, new BasicBulletType(3.5f, 24) {{
					lifetime = 60f;
					width = 9f;
					height = 11f;
					ammoMultiplier = 5;
					reloadMultiplier = 1.4f;
					homingPower = 0.15f;
					collidesAir = false;

					frontColor = AstraPal.siliconFront;
					backColor = AstraPal.siliconBack;
				}}
			);

			drawer = new DrawTurret("astranium-") {{
				parts.add(new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					under = true;
					moveY = -1f;
				}});
			}};

			scaledHealth = 120;
			size = 2;
			shootY = 7f;
			recoil = 1f;
			reload = 30f;
			rotateSpeed = 8f;
			shootCone = 12f;
			inaccuracy = 3f;
			range = 140f;
			coolant = consumeCoolant(0.15f);

			ammoUseEffect = Fx.casing1;

			targetAir = false;
			limitRange();
		}};

		viper = new ItemTurret("aa-rocket") {{
			requirements(Category.turret, ItemStack.with(AstraItems.iron, 60, Items.lead, 35));
			ammo(
				Items.copper, new MissileBulletType(3.5f, 10) {{
					lifetime = 60f;
					width = 6f;
					height = 7f;
					ammoMultiplier = 3;
					homingPower = 0.15f;
					collidesGround = false;
				}},
				Items.silicon, new MissileBulletType(4f, 15) {{
					lifetime = 60f;
					width = 7f;
					height = 7f;
					ammoMultiplier = 4;
					reloadMultiplier = 1.25f;
					homingPower = 0.6f;
					collidesGround = false;

					frontColor = AstraPal.siliconFront;
					backColor = AstraPal.siliconBack;
				}},
				Items.pyratite, new MissileBulletType(3.5f, 20) {{
					lifetime = 60f;
					width = 6f;
					height = 7f;
					ammoMultiplier = 5;
					homingPower = 0.35f;
					status = StatusEffects.burning;
					makeFire = true;
					collidesGround = false;

					frontColor = Pal.lightishOrange;
					backColor = Pal.lightOrange;
				}},
				AstraItems.lithium, new MissileBulletType(4f, 30) {{
					lifetime = 70f;
					width = 7f;
					height = 8f;
					ammoMultiplier = 4;
					reloadMultiplier = 0.9f;
					homingPower = 0.4f;
					status = StatusEffects.burning;
					statusDuration = 4f * 60;
					makeFire = true;
					collidesGround = false;

					frontColor = AstraPal.lithiumFront;
					backColor = AstraPal.lithiumBack;
				}}
			);

			drawer = new DrawTurret("astranium-") {{
				parts.add(new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					moveY = -1f;
				}});
			}};

			shoot = new ShootAlternate(6.5f) {{ shots = 2; shotDelay = 4f; }};

			scaledHealth = 150;
			size = 2;
			shootY = 4.5f;
			reload = 30f;
			rotateSpeed = 8f;
			shootCone = 30f;
			inaccuracy = 10f;
			range = 224f;
			consumeAmmoOnce = false;
			coolant = consumeCoolant(0.2f);

			shootSound = Sounds.missile;

			targetGround = false;
			limitRange();
		}};

		// region EXTRAS

		omegafactory = new GenericCrafter("omegafactory") {{
			requirements(Category.crafting, BuildVisibility.sandboxOnly, ItemStack.with(AstraItems.testium, 1500));
			health = 10000000;
			armor = 100f;
			size = 3;
			fogRadius = 10;
			hasItems = true;
			itemCapacity = 100;

			consumeItem(AstraItems.testium, 1);
			craftTime = 6f;
			outputItems = ItemStack.with(
				AstraItems.iron, 10,
				Items.graphite, 10,
				Items.silicon, 10,
				Items.metaglass, 10,
				Items.pyratite, 10,
				AstraItems.magnetite, 10,
				Items.oxide, 10,
				Items.plastanium, 10,
				AstraItems.steel, 10,
				Items.blastCompound, 10,
				Items.carbide, 10,
				Items.phaseFabric, 10,
				Items.surgeAlloy, 10,
				AstraItems.aerogel, 10,
				AstraItems.astranium, 10
			);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPlasmaBall() {{
				color = AstraPal.testPink;
				lifetime = 20f;
				particles = 20;
				ballRad = 2.5f;
			}}, new DrawDefault());
		}};

		uberwall = new EffectWall("uberwall") {{
			requirements(Category.defense, BuildVisibility.sandboxOnly, ItemStack.with(AstraItems.testium, 500));
			health = 1000000000;
			armor = 10000f;
			size = 2;
			fogRadius = 10;

			lightningChance = 1f;
			lightningDamage = 100000f;
			lightningLength = 100;
			lightningColor = AstraPal.testPink;
			chanceDeflect = 100f;
			effectRange = 80f;
			effectStrength = 100000f;
			effect = build -> {
				Units.nearbyEnemies(build.team, build.x, build.y, effectRange, unit -> {
					build.heal(unit.health);
					unit.damage(effectStrength * Time.delta / 60f);
				});
				Seq<Bullet> bullets = Groups.bullet.intersect(build.x - effectRange, build.y - effectRange, effectRange * 2, effectRange * 2);
				for (Bullet bullet : bullets) {
					if (bullet.team != build.team && bullet.type.hittable) {
						bullet.vel.setAngle(Angles.moveToward(bullet.rotation(), bullet.angleTo(build), 100f * Time.delta));
					}
				}
			};

			effectColor = AstraPal.testPink;
			effectAlpha = 0.1f;
			lightAlpha = 0.2f;
			effectStat = Stat.damage;
		}};

		superRouter = new SuperRouter("super-router") {{
			requirements(Category.distribution, BuildVisibility.sandboxOnly, ItemStack.with(AstraItems.testium, 200));
			health = 1;
			fogRadius = 10;
			speed = 1000f;
			routateSpeed = 4f;
			range = 160f;
			bullet = new LaserBoltBulletType(8f, 10000f) {{
				lifetime = 60f;
				backColor = AstraPal.testPink;
				frontColor = Color.white;
				smokeEffect = AstraFx.superLaser;
				hitEffect = AstraFx.superLaser;
				despawnEffect = AstraFx.superLaser;
			}};

			color = AstraPal.testPink;
			circles = new DrawCircles() {{
				color = AstraPal.testPink.cpy().a(0.3f);
				strokeMax = 2.5f;
				radius = 6f;
				amount = 1;
				timeScl = 100f;
			}};
		}};

		testblaster = new ItemTurret("testblaster") {{
			requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.with(AstraItems.testium, 1000));
			ammo(
				AstraItems.testium, new BasicBulletType(10f, 10000000) {{
					lifetime = 100f;
					width = 20f;
					height = 25f;
					pierceCap = 100;
					ammoMultiplier = 10;
					frontColor = AstraPal.testPinkDark;
					backColor = AstraPal.testPink;
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
			rotateSpeed = 10f;
			shootCone = 10f;
			range = 320f;
			size = 2;
			health = 100000000;
			shootY = 7.5f;

			ammoUseEffect = Fx.none;
			shootSound = Sounds.missile;

			limitRange();
		}};
	}
}