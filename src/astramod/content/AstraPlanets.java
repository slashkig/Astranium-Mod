package astramod.content;

import arc.graphics.*;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.BuildVisibility;
import astramod.maps.planet.*;

import static mindustry.Vars.*;
import static mindustry.content.Planets.*;

public class AstraPlanets {
	public static final Seq<Planet> windPlanets = new Seq<>();
	public static Planet aziris;

	public static void load() {
		Log.info("Loading planets");

		// TODO make Aziris more than just a white sphere
		aziris = new Planet("aziris", sun, 1f, 1) {{
			generator = new AzirisPlanetGenerator();

			alwaysUnlocked = true;
			allowLaunchToNumbered = false;

			ruleSetter = r -> {
				r.waveTeam = Team.blue;
				r.showSpawns = true;
				r.fog = true;
				r.staticFog = true;

				r.loadout = ItemStack.list(AstraItems.hematite, 100);

				r.blockWhitelist = true;
				r.hideBannedBlocks = true;
				for (Block block : content.blocks()) {
					if (AstraBlocks.azirisBlocks.contains(block) || block.buildVisibility == BuildVisibility.sandboxOnly) {
						r.bannedBlocks.add(block);
					}
				}
			};

			iconColor = Color.valueOf("bf2851");

			defaultCore = AstraBlocks.coreNode;
			unlockedOnLand.add(AstraBlocks.coreNode);
		}};

		// TODO how does vanilla do this
		final var serpuloRules = serpulo.ruleSetter;
		serpulo.hiddenItems.add(AstraItems.azirisItems);
		serpulo.ruleSetter = r -> {
			serpuloRules.get(r);
			for (Block block : AstraBlocks.azirisBlocks) {
				r.bannedBlocks.add(block);
			}
		};
		final var erekirRules = erekir.ruleSetter;
		erekir.hiddenItems.add(AstraItems.azirisItems);
		erekir.ruleSetter = r -> {
			erekirRules.get(r);
			for (Block block : AstraBlocks.azirisBlocks) {
				r.bannedBlocks.add(block);
			}
		};

		windPlanets.add(AstraPlanets.aziris, sun);
	};
}