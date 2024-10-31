package astramod.content;

import arc.struct.*;
import arc.util.Log;
import mindustry.content.*;
import mindustry.game.Objectives.*;

import static mindustry.content.TechTree.*;
import static astramod.content.AstraBlocks.*;

public class AzirisTechTree {
	public static void load() {
		Log.info("Loading tech trees");

		AstraPlanets.aziris.techTree = nodeRoot("aziris", coreNode, () -> {
			node(coreHub, Seq.with(new Research(ironWallLarge)), () -> {

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
							node(platedJunction, Seq.with(new Research(ironJunction)), () -> {
								node(platedBridge, Seq.with(new Research(ironBridge)), () -> { });
							});
							node(platedRouter, Seq.with(new Research(ironRouter)), () -> {
								node(platedDistributor, Seq.with(new Research(ironDistributor)), () -> { });
								node(platedOverflowGate, Seq.with(new Research(ironOverflowGate)), () -> {
									node(platedUnderflowGate, Seq.with(new Research(ironUnderflowGate)), () -> { });
								});
								node(platedSorter, Seq.with(new Research(ironSorter)), () -> {
									node(invertedPlatedSorter, Seq.with(new Research(invertedIronSorter)), () -> { });
								});
							});
						});
					});
					node(bulkConveyor, () -> {
						node(surgeBulkConveyor, () -> {
							node(surgeBulkJunction, Seq.with(new Research(platedJunction)), () -> { });
							node(surgeBulkRouter, Seq.with(new Research(platedRouter)), () -> { });
						});
					});
				});
			});

			node(compactDrill, () -> {
				node(ironDrill, () -> {
					node(augerDrill, () -> {
						node(plasmaDrill, Seq.with(new Research(AstraFluids.plasma)), () -> {
							node(excavationDrill);
						});
						node(frackingDrill);
					});
				});
				node(compactBore, () -> {
					node(ironBore, () -> {
						node(laserBore, () -> {
							node(pulseBore, Seq.with(new Research(plasmaDrill)), () -> { });
						});
					});
				});
			});

			node(compactPump, Seq.with(new Research(compactDrill)), () -> {
				node(turbinePump, Seq.with(new Research(wavePipeline)), () -> {
					node(jetstreamPump, Seq.with(new Research(steelTank)), () -> {
						node(tidalPump, Seq.with(new Research(tidalPipeline)), () -> { });
					});
				});
				node(crudePipeline, () -> {
					node(wavePipeline, () -> {
						node(jetPipeline, () -> {
							node(crystalPipeline, Seq.with(new Research(platedSteelConveyor)), () -> {
								node(tidalPipeline, Seq.with(new Research(crystalTank)), () -> {
									node(tidalJunction, Seq.with(new Research(crystalJunction)), () -> { });
									node(tidalRouter, Seq.with(new Research(crystalRouter)), () -> { });
								});
								node(crystalJunction, Seq.with(new Research(waveJunction)), () -> {
									node(crystalBridge, Seq.with(new Research(waveBridge)), () -> { });
								});
								node(crystalRouter, Seq.with(new Research(waveRouter)), () -> { });
							});
						});
						node(waveJunction, () -> {
							node(waveBridge);
						});
						node(waveRouter);
					});
				});
				node(ironTank, Seq.with(new Research(wavePipeline)), () -> {
					node(steelTank, Seq.with(new Research(jetPipeline)), () -> {
						node(crystalTank, Seq.with(new Research(crystalPipeline)), () -> { });
					});
				});
			});

			node(windTurbine, () -> {
				node(wireRelay, () -> {
					node(powerRelay, () -> {
						node(largePowerRelay, () -> {
							node(relayTower);
						});
					});
					node(powerCell, () -> {
						node(largePowerCell, () -> {
							node(erythronitePowerCell);
						});
					});
				});
				node(steamTurbine, Seq.with(new Research(coalPlant)), () -> {
					node(exothermicReactor);
					node(repulsionGenerator);
				});
				node(windTurbineLarge);
			});

			node(ironFurnace, Seq.with(new Research(compactDrill)), () -> {
				node(castIronPress, () -> {
					node(AstraBlocks.plastaniumCompressor, () -> {
						node(AstraBlocks.phaseWeaver, () -> {
							node(phaseLoom, Seq.with(new Research(magnetiteSynthesizer)), () -> { });
						});
						node(plastaniumFabricator, Seq.with(new Research(hydraulicPress)), () -> {
							node(vacuumChamber, Seq.with(new Research(plasmaEnergizer), new Research(cryofluidProcessor)), () -> { });
						});
					});
					node(hydraulicPress, Seq.with(new Research(AstraBlocks.plastaniumCompressor)), () -> { });
				});
				node(castIronSmelter, () -> {
					node(steelForge, () -> {
						node(surgeArcFurnace, () -> {
							node(surgeArcCrucible, Seq.with(new Research(purificationSmelter)), () -> { });
							node(astraniumForge, Seq.with(new Research(steelFoundry), new Research(phaseLoom)), () -> { });
						});
						node(steelFoundry, Seq.with(new Research(surgeArcFurnace)), () -> { });
					});
					node(purificationSmelter, Seq.with(new Research(blastFurnace)), () -> { });
				});
				node(castIronKiln, () -> {
					node(crystaglassKiln);
					node(cryofluidBlender, Seq.with(new Research(turbinePump)), () -> {
						node(ferrofluidMixer);
						node(cryofluidProcessor, Seq.with(new Research(hydraulicPress)), () -> { });
					});
				});
				node(coalPlant);
				node(castIronMixer, () -> {
					node(magnetiteSynthesizer, () -> {
						node(plasmaEnergizer);
					});
					node(explosivesRefinery);
					node(formulationMixer, Seq.with(new Research(magnetiteSynthesizer)), () -> { });
				});
				node(blastFurnace, Seq.with(new Research(steelForge)), () -> { });
			});

			node(dart, () -> {
				node(viper, () -> {

				});
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
						nodeProduce(AstraItems.crystals, () -> {
							nodeProduce(AstraFluids.plasma, () -> { });
						});
						nodeProduce(Items.surgeAlloy, () -> { });
					});
					nodeProduce(Liquids.water, () -> {
						nodeProduce(Liquids.oil, () -> {
							nodeProduce(Items.plastanium, () -> { });
							nodeProduce(AstraFluids.ferrofluid, () -> { });
						});
					});
				});
				nodeProduce(Items.lead, () -> {
					nodeProduce(Items.metaglass, () -> { });
					nodeProduce(Items.titanium, () -> {
						nodeProduce(Items.thorium, () -> {
							nodeProduce(AstraItems.neodymium, () -> {
								nodeProduce(AstraItems.astranium, () -> { });
							});
							nodeProduce(Items.phaseFabric, () -> {
								nodeProduce(AstraItems.aerogel, () -> { });
							});
						});
						nodeProduce(Liquids.cryofluid, () -> { });
					});
				});
				nodeProduce(AstraItems.iron, () -> {
					nodeProduce(AstraItems.steel, () -> { });
				});
				nodeProduce(Items.coal, () -> {
					nodeProduce(Items.graphite, () -> {
						nodeProduce(AstraItems.magnetite, () -> { });
					});
					nodeProduce(Items.pyratite, () -> {
						nodeProduce(Items.blastCompound, () -> { });
					});
				});
				nodeProduce(Items.sand, () -> {
					nodeProduce(Items.silicon, () -> { });
				});
				nodeProduce(Items.scrap, () -> {
					nodeProduce(Liquids.slag, () -> { });
				});
			});
		});
	};
}