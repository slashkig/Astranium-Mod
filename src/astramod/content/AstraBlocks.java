package astramod.content;

import arc.util.Log;
import arc.graphics.*;
import arc.audio.*;
import arc.math.*;
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
import mindustry.entities.effect.*;
import mindustry.graphics.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.content.*;
import astramod.content.AstraItems;
import astramod.world.blocks.defense.*;
import astramod.world.blocks.production.*;
import astramod.world.draw.*;

public class AstraBlocks {
	public static Block oreTestium, oreHematite, oreLithium, oreNeodymium, wallOreLithium,
		ironFurnace, blastFurnace, castIronPress, hydraulicPress, castIronSmelter, purificationSmelter, castIronKiln, castIronMixer, formulationMixer, magnetiteSynthesizer, explosivesRefinery, cryofluidBlender, cryofluidProcessor, plastaniumCompressor, plastaniumFabricator, steelForge, steelFoundry, ferrofluidMixer, plasmaEnergizer, phaseWeaver, phaseLoom, surgeArcFurnace, surgeArcCrucible, vacuumChamber, astraniumForge,
		compactDrill, ironDrill, augerDrill, plasmaDrill, excavationDrill,
		hematiteWall, hematiteWallLarge, ironWall, ironWallLarge, platedTitaniumWall, platedTitaniumWallLarge, platedPlastaniumWall, platedPlastaniumWallLarge, steelWall, steelWallLarge, platedThoriumWall, platedThoriumWallLarge, platedSurgeWall, platedSurgeWallLarge, platedPhaseWall, platedPhaseWallLarge, aerotechWall, aerotechWallLarge,
		hematiteConveyor, ironConveyor, bulkConveyor, surgeBulkConveyor, ironJunction, ironBridge, ironRouter, ironDistributor, ironOverflowGate, ironUnderflowGate, ironSorter, invertedIronSorter,
		coreNode,
		dart, testblaster;

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
				AstraItems.lithium, 60,
				Items.titanium, 80,
				Items.graphite, 75
			));
			scaledHealth = 55;
			size = 3;
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.hematite, 8, Items.pyratite, 2));
			consumePower(2.8f);
			craftTime = 60f;
			outputItem = new ItemStack(AstraItems.iron, 6);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawEmitSmoke() {{
				color = Color.valueOf("ffc099"); // 9d7561
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
			hasItems = true;

			consumeItem(Items.coal, 4);
			consumePower(0.25f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.graphite, 2);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{ sinMag = 2f; sinScl = 12f; lenOffset = -2f; }}, new DrawDefault());
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
			hasPower = hasLiquids = hasItems = true;
			itemCapacity = 20;
			liquidCapacity = 20f;

			consumeItem(Items.coal, 8);
			consumeLiquidsMulti(0.15f, Liquids.water, 0.75f, Liquids.oil, 1.5f);
			consumePower(2.2f);
			craftTime = 75f;
			outputItem = new ItemStack(Items.graphite, 5);

			drawer = new DrawMulti(new DrawRegion("-bottom"), new DrawPistons() {{ sinMag = 2.5f; lenOffset = -2.5f; }}, new DrawDefault());
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
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(Items.sand, 5, Items.graphite, 2));
			consumeItemBoost(Items.pyratite, 2, 0.8f);
			consumePower(5f);
			craftTime = 50f;
			outputItem = new ItemStack(Items.silicon, 5);

			drawer = new DrawMulti(new DrawDefault(), new DrawTopHeat(), new DrawGlowRegion(), new DrawEmitSmoke() {{
				color = Color.valueOf("ffef99"); // 9d8c61
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
			hasPower = hasItems = true;

			consumeItems(ItemStack.with(Items.lead, 3, Items.sand, 3));
			consumePower(0.60f);
			craftTime = 80f;
			outputItem = new ItemStack(Items.metaglass, 3);

			drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("ffc099")));
			ambientSound = Sounds.smelter;
			craftEffect = Fx.smeltsmoke;
		}};

		castIronMixer = new GenericCrafter("cast-iron-mixer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 50, Items.lead, 30, Items.graphite, 25));
			scaledHealth = 45;
			size = 2;
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
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.hematite, 5, Items.coal, 8, Items.sand, 6));
			consumePower(3.5f);
			craftTime = 120f;
			outputItem = new ItemStack(Items.pyratite, 5);

			drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-spinner", 4, true));
			craftEffect = Fx.smeltsmoke;
		}};

		magnetiteSynthesizer = new GenericCrafter("magnetite-synthesizer") {{
			requirements(Category.crafting, ItemStack.with(AstraItems.iron, 80, Items.silicon, 40, Items.graphite, 50));
			scaledHealth = 50;
			size = 2;
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
			hasPower = hasItems = hasLiquids = true;
			liquidCapacity = 60f;

			consumeItem(Items.titanium, 4);
			consumeLiquid(Liquids.oil, 0.3f);
			consumePower(3.6f);
			craftTime = 96f;
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
				Items.phaseFabric, 50,
				AstraItems.neodymium, 60
			));
			scaledHealth = 70;
			size = 3;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 20;
			liquidCapacity = 100f;

			consumeItems(ItemStack.with(Items.titanium, 9, AstraItems.lithium, 3));
			consumeLiquid(Liquids.oil, 0.8f);
			consumePower(9f);
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
			hasPower = hasItems = true;
			itemCapacity = 20;

			consumeItems(ItemStack.with(AstraItems.lithium, 1, Items.pyratite, 2));
			consumePower(4.2f);
			craftTime = 75f;
			outputItem = new ItemStack(Items.blastCompound, 2);

			drawer = new DrawMulti(new DrawDefault(), new DrawRegion("-spinner", 4, true));
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
				AstraItems.neodymium, 120,
				Items.thorium, 90,
				AstraItems.crystals, 100
			));
			scaledHealth = 65;
			size = 3;
			hasPower = hasItems = true;
			hasLiquids = outputsLiquid = true;
			liquidCapacity = 80f;

			consumeItem(AstraItems.crystals, 2);
			consumeLiquid(Liquids.water, 2f / 3f);
			consumePower(7.2f);
			craftTime = 40f;
			outputLiquid = new LiquidStack(AstraFluids.plasma, 1f / 3f);

			drawer = new DrawMulti(
				new DrawRegion("-bottom"),
				new DrawLiquidTile(Liquids.water),
				new DrawLiquidTile(AstraFluids.plasma) {{ drawLiquidLight = true; }},
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
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 50;
			liquidCapacity = 75f;

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
				Items.silicon, 200,
				Items.metaglass, 260,
				AstraItems.neodymium, 175
			));
			scaledHealth = 70;
			armor = 5f;
			size = 4;
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 40;
			liquidCapacity = 200f;

			consumeItems(ItemStack.with(Items.phaseFabric, 4, Items.silicon, 8, AstraItems.crystals, 5));
			consumeLiquid(Liquids.cryofluid, 1.2f);
			consumePower(20f);
			craftTime = 300f;
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
			hasPower = hasItems = hasLiquids = true;
			itemCapacity = 50;
			liquidCapacity = 100f;

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
			consumePower(0.55f);
			consumeLiquidBoosts(0.07f, Liquids.water, 1.5f, AstraFluids.ferrofluid, 1.75f);
			drillTime = 360;
			tier = 3;
			hardnessDrillMultiplier = 65f;

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
			hasPower = true;
			itemCapacity = 20;
			liquidCapacity = 10f;

			liquidBoostIntensity = 1.6f;
			consumePower(1.2f);
			consumeLiquidBoosts(0.1f, Liquids.water, 1.6f, AstraFluids.ferrofluid, 1.95f);
			drillTime = 240;
			tier = 4;
			hardnessDrillMultiplier = 55f;

			rotateSpeed = 4.5f;
			drawRim = true;
			updateEffect = Fx.pulverizeMedium;
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineBig;
		}};

		plasmaDrill = new MultiCoolantDrill("plasma-drill") {{
			requirements(Category.production, ItemStack.with(
				Items.surgeAlloy, 60,
				AstraItems.magnetite, 50,
				Items.thorium, 55,
				AstraItems.lithium, 65,
				Items.silicon, 70
			));
			scaledHealth = 60;
			size = 4;
			hasPower = true;
			itemCapacity = 30;
			liquidCapacity = 15f;

			liquidBoostIntensity = 1.65f;
			consumePower(4f);
			consumeLiquid(AstraFluids.plasma, 0.05f);
			consumeLiquidBoosts(0.14f, Liquids.water, 1.65f, AstraFluids.ferrofluid, 2.05f);
			drillTime = 200;
			tier = 5;
			hardnessDrillMultiplier = 45f;

			rotateSpeed = 5.5f;
			drawRim = true;
			heatColor = Color.valueOf("9292ff");
			updateEffect = Fx.pulverizeRed;
			updateEffectChance = 0.03f;
			drillEffect = Fx.mineHuge;
		}};

		excavationDrill = new MultiCoolantDrill("excavation-drill") {{
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
			hasPower = true;
			itemCapacity = 40;
			liquidCapacity = 20f;

			liquidBoostIntensity = 1.7f;
			consumePower(9f);
			consumeLiquid(AstraFluids.plasma, 0.15f);
			consumeLiquidBoosts(0.18f, Liquids.water, 1.7f, AstraFluids.ferrofluid, 2.2f);
			drillTime = 180;
			tier = 5;
			hardnessDrillMultiplier = 35f;

			rotateSpeed = 6.5f;
			drawRim = true;
			heatColor = Color.valueOf("9292ff");
			updateEffect = Fx.pulverizeRed;
			updateEffectChance = 0.05f;
			drillEffect = Fx.mineHuge;
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

		aerotechWall = new ProjectorWall("aerotech-wall", 1.6f) {{
			requirements(Category.defense, ItemStack.with(AstraItems.aerogel, 8, Items.silicon, 6));
			health = 210 * 4;
			armor = 12f;
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
			consumePower(0.2f);
			shieldHealth = 600f;
			breakCooldown = 1200f;
			regenSpeed = 1f;
			flashHit = true;
			absorbLightning = absorbLasers = true;
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
			speed = 0.08f;
			itemCapacity = 20;
		}};

		ironJunction = new Junction("iron-junction") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2));
			health = 70;
			buildCostMultiplier = 5f;
			speed = 15;
			capacity = 4;
		}};

		ironBridge = new BufferedItemBridge("iron-bridge") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 6, Items.lead, 6));
			health = 70;
			fadeIn = moveArrows = false;
			range = 4;
			speed = 30f;
			arrowSpacing = 6f;
			bufferCapacity = 14;
		}};

		((Conveyor)hematiteConveyor).junctionReplacement = ironJunction;
		((Conveyor)ironConveyor).junctionReplacement = ironJunction;
		((Conveyor)hematiteConveyor).bridgeReplacement = ironBridge;
		((Conveyor)ironConveyor).bridgeReplacement = ironBridge;

		ironRouter = new Router("iron-router") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 3));
			health = 100;
			buildCostMultiplier = 2f;
		}};

		ironDistributor = new Router("iron-distributor") {{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 10));
			health = 300;
			size = 2;
			buildCostMultiplier = 3f;
			itemCapacity = 4;
		}};

		ironOverflowGate = new OverflowGate("iron-overflow-gate"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.lead, 2));
			health = 70;
			buildCostMultiplier = 2f;
		}};

		ironUnderflowGate = new OverflowGate("iron-underflow-gate"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.lead, 2));
			health = 70;
			buildCostMultiplier = 2f;
			invert = true;
		}};

		ironSorter = new Sorter("iron-sorter"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.graphite, 1));
			health = 70;
			buildCostMultiplier = 3f;
		}};

		invertedIronSorter = new Sorter("inverted-iron-sorter"){{
			requirements(Category.distribution, ItemStack.with(AstraItems.iron, 2, Items.graphite, 1));
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

			unitType = UnitTypes.beta;
			unitCapModifier = 10;
		}};

		dart = new ItemTurret("dart") {{
			requirements(Category.turret, ItemStack.with(AstraItems.hematite, 50, Items.lead, 20));
			ammo(
				AstraItems.hematite, new BasicBulletType(3f, 14) {{
					lifetime = 60f;
					width = 8f;
					height = 10f;
					ammoMultiplier = 2;
					reloadMultiplier = 0.8f;
				}},
				Items.lead, new BasicBulletType(3f, 16) {{
					lifetime = 60f;
					width = 8f;
					height = 10f;
					ammoMultiplier = 3;
				}},
				Items.graphite, new BasicBulletType(4f, 26) {{
					lifetime = 60f;
					width = 10f;
					height = 12f;
					ammoMultiplier = 4;
					reloadMultiplier = 0.75f;
				}},
				Items.silicon, new BasicBulletType(3.5f, 22) {{
					lifetime = 60f;
					width = 9f;
					height = 11f;
					ammoMultiplier = 5;
					reloadMultiplier = 1.4f;
					homingPower = 0.1f;
				}}
			);

			drawer = new DrawTurret("astranium-") {{
				parts.add(new RegionPart("-barrel") {{
					progress = PartProgress.recoil;
					under = true;
					moveY = -1f;
				}});
			}};

			scaledHealth = 150;
			size = 2;
			shootY = 7f;
			recoil = 1f;
			reload = 30f;
			rotateSpeed = 8f;
			shootCone = 12f;
			inaccuracy = 3f;
			range = 125f;
			ammoUseEffect = Fx.casing1;
			coolant = consumeCoolant(0.15f);

			targetAir = false;
			limitRange();
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
			rotateSpeed = 10f;
			shootCone = 10f;
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