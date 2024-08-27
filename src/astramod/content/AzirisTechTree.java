package astramod.content;

import arc.struct.*;
import mindustry.game.Objectives.*;

import static mindustry.content.TechTree.*;
import static mindustry.content.Blocks.*;
import static astramod.content.AstraBlocks.*;

public class AzirisTechTree {
	public static void load() {
		AstraPlanets.aziris.techTree = nodeRoot("aziris", coreNode, () -> {
			node(hematiteConveyor, () -> {
				node(ironConveyor, () -> {
					node(ironRouter, () -> {
						node(ironOverflowGate, () -> {
							node(ironSorter);
						});
						node(ironUnderflowGate, () -> {
							node(invertedIronSorter);
						});
					});
					node(bulkConveyor);
				});
			});
		});
	};
}