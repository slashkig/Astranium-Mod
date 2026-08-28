package astramod.content;

import arc.struct.*;
import arc.util.Log;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives.*;

import static mindustry.content.TechTree.*;
import static astramod.content.AstraBlocks.*;

public class AzirisTechTree {
	public static final Runnable none = () -> { };

	public static void load() {
		Log.info("Loading tech tree");

		AstraPlanets.aziris.techTree = nodeRoot("aziris", coreNode, () -> {
			node(coreHub, research(platedTitaniumWallLarge, platedContainer), () -> {
				node(coreNexus, research(steelWallLarge, platedVault), () -> {
					// Core Matrix
				});
			});

			node(smelterModule, research(ironFurnace), () -> {
				node(fabricatorModule, research(castIronPress, castIronSmelter, castIronKiln), () -> {
					node(rtgModule, research(largePowerCell), none);
				});
				node(storageModule, research(platedContainer), () -> {
					node(storageModuleLarge, research(platedVault), none);
				});
				node(controlModule, research(primaryMechAssembler), () -> {
					node(gathererModule, research(primaryAirAssembler), () -> {
						node(initiateModule, research(mendBeam), () -> {
							node(wardModule, research(coreNexus), none);
						});
						node(seekerModule, research(coreHub), none);
					});
				});
				node(defenseModule, research(seekerModule), () -> {
					node(shieldModule, research(wardModule), none);
				});
			});

			node(hematiteConveyor, () -> {
				node(ironConveyor, () -> {
					node(ironJunction, () -> {
						node(ironBridge);
					});
					node(ironRouter, () -> {
						node(ironDistributor);
						node(ironOverflowGate, () -> {
							node(ironUnderflowGate);
						});
						node(ironSorter, () -> {
							node(invertedIronSorter);
						});
					});
					node(durasteelConveyor, () -> {
						node(platedSteelConveyor, () -> {
							node(platedJunction, research(ironJunction), () -> {
								node(platedBridge, research(ironBridge), none);
							});
							node(platedRouter, research(ironRouter), () -> {
								node(platedDistributor, research(ironDistributor), none);
								node(platedOverflowGate, research(ironOverflowGate), () -> {
									node(platedUnderflowGate, research(ironUnderflowGate), none);
								});
								node(platedSorter, research(ironSorter), () -> {
									node(invertedPlatedSorter, research(invertedIronSorter), none);
								});
							});
						});
					});
					node(bulkConveyor, () -> {
						node(surgeBulkConveyor, () -> {
							node(surgeBulkJunction, research(platedJunction), none);
							node(surgeBulkRouter, research(platedRouter), none);
						});
					});
				});
			});

			node(compactDrill, () -> {
				node(ironDrill, research(ironFurnace), () -> {
					node(augerDrill, research(steelForge), () -> {
						node(plasmaDrill, research(plasmaEnergizer), () -> {
							node(excavationDrill, research(astraniumForge), none);
						});
						node(frackingDrill, research(turbinePump), none);
					});
				});
				node(compactBore, research(windTurbine), () -> {
					node(ironBore, research(ironFurnace), () -> {
						node(laserBore, research(steelForge), () -> {
							node(pulseBore, research(plasmaEnergizer), none);
						});
					});
				});
			});

			node(compactPump, research(compactDrill), () -> {
				node(turbinePump, research(wavePipeline), () -> {
					node(jetstreamPump, research(steelTank), () -> {
						node(tidalPump, research(tidalPipeline), none);
					});
				});
				node(crudePipeline, research(hematiteConveyor), () -> {
					node(wavePipeline, research(ironConveyor), () -> {
						node(jetPipeline, research(durasteelConveyor), () -> {
							node(crystalPipeline, research(platedSteelConveyor), () -> {
								node(tidalPipeline, research(crystalTank), () -> {
									node(tidalJunction, research(crystalJunction), none);
									node(tidalRouter, research(crystalRouter), none);
								});
								node(crystalJunction, research(waveJunction), () -> {
									node(crystalBridge, research(waveBridge), none);
								});
								node(crystalRouter, research(waveRouter), none);
							});
						});
						node(waveJunction, () -> {
							node(waveBridge);
						});
						node(waveRouter);
					});
				});
				node(ironTank, research(wavePipeline), () -> {
					node(steelTank, research(jetPipeline), () -> {
						node(crystalTank, research(crystalPipeline), none);
					});
				});
			});

			node(ironFurnace, research(compactDrill), () -> {
				node(castIronPress, research(windTurbine), () -> {
					node(plastaniumCompressor, research(Items.titanium, castIronKiln), () -> {
						node(phaseWeaver, research(magnetiteSynthesizer, purificationSmelter), () -> {
							node(phaseLoom, research(plasmaDrill, crystalReactor, formulationMixer), none);
						});
						node(plastaniumFabricator, research(hydraulicPress, oilPlant, explosivesRefinery), () -> {
							node(vacuumChamber, research(plasmaEnergizer, cryofluidProcessor, crystalTank), none);
						});
					});
					node(hydraulicPress, research(plastaniumCompressor, repulsionGenerator, jetPipeline), none);
				});
				node(castIronSmelter, research(windTurbine), () -> {
					node(steelForge, research(coalPlant, ironDrill), () -> {
						node(surgeArcFurnace, research(augerDrill, purificationSmelter, steamEngine), () -> {
							node(surgeArcCrucible, research(highCapacityPowerCell, steelFoundry, plasmaEnergizer), none);
							node(astraniumForge, research(crystaglassKiln, platedSurgeWallLarge, enrichmentPlant), none);
						});
						node(steelFoundry, research(AstraItems.vanadium, surgeArcFurnace, oilPlant), none);
					});
					node(purificationSmelter, research(blastFurnace, geothermalPlant, steelWallLarge), none);
				});
				node(castIronKiln, research(windTurbine), () -> {
					node(crystaglassKiln, research(AstraItems.crystals, blastFurnace, platedPlastaniumWallLarge), none);
					node(cryofluidBlender, research(turbinePump, ironTank), () -> {
						node(ferrofluidMixer, research(Liquids.oil, steelTank, formulationMixer), none);
						node(cryofluidProcessor, research(hydraulicPress, steelTank, jetPipeline), none);
					});
				});
				node(castIronMixer, research(windTurbine), () -> {
					node(magnetiteSynthesizer, research(castIronPress), () -> {
						node(plasmaEnergizer, research(crystalPipeline, hydrogenPlant, crystalReactor), none);
					});
					node(explosivesRefinery, research(exothermicReactor), none);
					node(formulationMixer, research(ember, magnetiteSynthesizer), none);
				});
				node(hydrogenPlant, research(coalPlant, ironTank), none);
				node(blastFurnace, research(steelForge, castIronMixer, platedTitaniumWallLarge), none);
			});

			node(windTurbine, () -> {
				node(wireRelay, () -> {
					node(powerRelay, () -> {
						node(switchRelay);
						node(largePowerRelay, () -> {
							node(relayTower);
						});
					});
					node(powerCell, () -> {
						node(largePowerCell, research(steamTurbine), () -> {
							node(highCapacityPowerCell, research(steamEngine), () -> {
								node(erythronitePowerCell, research(crystalReactor), none);
							});
						});
					});
					node(largeWireRelay, research(largePowerRelay), none);
				});
				node(solarCell, () -> {
					node(solarCellLarge, () -> {
						node(solarArray);
					});
				});
				node(coalPlant, research(compactPump, ironFurnace), () -> {
					node(oilPlant, research(steelTank, steelForge), () -> {
						node(fissionReactor, research(crystalReactor, enrichmentPlant), () -> {
							node(thermalSink, research(geothermalPlant), none);
							node(coolantPump, research(jetstreamPump), none);
							node(nuclearSteamTower, research(steamEngine), none);
						});
					});
					node(geothermalPlant, research(turbinePump, castIronKiln), none);
				});
				node(steamTurbine, research(coalPlant, crudePipeline), () -> {
					node(exothermicReactor, research(castIronMixer, ironBore), () -> {
						node(crystalReactor, research(steamEngine, laserBore), none);
					});
					node(repulsionGenerator, research(magnetiteSynthesizer, windTurbineLarge), () -> {
						node(fusionReactor, research(fissionReactor, plasmaEnergizer), () -> {
							node(heliumDiverter, research(tidalPipeline), none);
							node(hydrogenBreeder, research(exothermicReactor), none);
						});
					});
					node(steamEngine, research(oilPlant, windTurbineLarge), none);
				});
				node(windTurbineLarge, research(powerRelay), none);
			});

			node(dart, research(compactDrill), () -> {
				node(viper, research(compactBore), () -> {

				});
				node(ember, research(Items.coal), none);
			});

			node(hematiteWall, () -> {
				node(hematiteWallLarge);
				node(ironWall, () -> {
					node(ironWallLarge, () -> {
						node(ironDoor);
					});
					node(platedTitaniumWall, () -> {
						node(platedTitaniumWallLarge);
						node(steelWall, () -> {
							node(steelWallLarge);
							node(platedSurgeWall, () -> {
								node(platedSurgeWallLarge);
								node(astraniumWall, () -> {
									node(astraniumWallLarge);
								});
							});
						});
						node(platedPlastaniumWall, () -> {
							node(platedPlastaniumWallLarge);
							node(platedThoriumWall, () -> {
								node(platedThoriumWallLarge);
							});
							node(platedPhaseWall, () -> {
								node(platedPhaseWallLarge);
								node(phaseDoor);
								node(aerotechWall, () -> {
									node(aerotechWallLarge);
								});
							});
						});
					});
				});
			});

			nodeProduce(AstraItems.hematite, () -> {
				nodeProduce(Items.copper, () -> {
					nodeProduce(AstraItems.lithium, () -> {
						nodeProduce(AstraItems.crystals, none);
						nodeProduce(Items.surgeAlloy, none);
					});
					nodeProduce(Liquids.water, () -> {
						nodeProduce(AstraFluids.steam, () -> {
							nodeProduce(Liquids.hydrogen, () -> {
								nodeProduce(AstraFluids.plasma, () -> {
									nodeProduce(AstraFluids.helium, none);
								});
							});
						});
						nodeProduce(Liquids.oil, () -> {
							nodeProduce(Items.plastanium, none);
							nodeProduce(AstraFluids.ferrofluid, none);
						});
					});
				});
				nodeProduce(Items.lead, () -> {
					nodeProduce(Items.metaglass, () -> {
						nodeProduce(AstraItems.crystaglass, none);
					});
					nodeProduce(Items.titanium, () -> {
						nodeProduce(AstraItems.vanadium, none);
						nodeProduce(Items.thorium, () -> {
							nodeProduce(AstraItems.neodymium, () -> {
								nodeProduce(AstraItems.astranium, none);
							});
							nodeProduce(Items.phaseFabric, () -> {
								nodeProduce(AstraItems.aerogel, none);
							});
							nodeProduce(AstraItems.nuclearRod, none);
						});
						nodeProduce(Liquids.cryofluid, none);
					});
				});
				nodeProduce(AstraItems.iron, () -> {
					nodeProduce(AstraItems.steel, none);
				});
				nodeProduce(Items.coal, () -> {
					nodeProduce(Items.graphite, () -> {
						nodeProduce(AstraItems.magnetite, none);
					});
					nodeProduce(Items.pyratite, () -> {
						nodeProduce(Items.blastCompound, none);
					});
				});
				nodeProduce(Items.sand, () -> {
					nodeProduce(Items.silicon, none);
				});
				nodeProduce(Items.scrap, () -> {
					nodeProduce(Liquids.slag, none);
				});
			});
		});
	};

	public static Seq<Objective> research(UnlockableContent... content) {
		Seq<Objective> r = new Seq<>();
		for (UnlockableContent c : content) { r.add(new Research(c)); }
		return r;
	}
}