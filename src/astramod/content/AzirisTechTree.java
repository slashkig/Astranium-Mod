package astramod.content;

import mindustry.content.*;

import static mindustry.content.TechTree.*;
import static astramod.content.AstraBlocks.*;

public class AzirisTechTree {
	public static void load() {
		AstraPlanets.aziris.techTree = nodeRoot("aziris", coreNode, () -> {
			node(hematiteConveyor, () -> {
				node(ironConveyor, () -> {
					node(durasteelConveyor, () -> {
						node(platedSteelConveyor);
					});
					node(ironRouter, () -> {
						node(ironJunction, () -> {
							node(ironBridge);
						});
						node(ironOverflowGate, () -> {
							node(ironSorter);
						});
						node(ironUnderflowGate, () -> {
							node(invertedIronSorter);
						});
						node(ironDistributor);
					});
					node(bulkConveyor, () -> {
						node(surgeBulkConveyor);
					});
				});
			});

			node(compactDrill, () -> {
				node(ironDrill, () -> {
					node(augerDrill, () -> {
						node(plasmaDrill, () -> {
							node(excavationDrill);
						});
						node(frackingDrill);
					});
				});
				node(compactBore, () -> {

				});
			});

			node(ironFurnace, () -> {
				node(castIronPress, () -> {
					node(AstraBlocks.plastaniumCompressor, () -> {
						node(AstraBlocks.phaseWeaver, () -> {
							node(phaseLoom);
						});
						node(plastaniumFabricator, () -> {
							node(vacuumChamber);
						});
					});
					node(hydraulicPress);
				});
				node(castIronSmelter, () -> {
					node(steelForge, () -> {
						node(surgeArcFurnace, () -> {
							node(astraniumForge);
							node(surgeArcCrucible);
						});
						node(steelFoundry);
					});
					node(purificationSmelter);
				});
				node(castIronKiln, () -> {
					node(cryofluidBlender, () -> {
						node(cryofluidProcessor);
						node(ferrofluidMixer);
					});
				});
				node(castIronMixer, () -> {
					node(magnetiteSynthesizer, () -> {
						node(plasmaEnergizer);
					});
					node(explosivesRefinery);
					node(formulationMixer);
				});
				node(blastFurnace);
			});

			node(dart, () -> {
				node(viper, () -> {

				});
			});

			node(hematiteWall, () -> {
				node(hematiteWallLarge);
				node(ironWall, () -> {
					node(ironWallLarge);
					node(platedTitaniumWall, () -> {
						node(platedTitaniumWallLarge);
						node(steelWall, () -> {
							node(steelWallLarge);
							node(platedSurgeWall, () -> {
								node(platedSurgeWallLarge);
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