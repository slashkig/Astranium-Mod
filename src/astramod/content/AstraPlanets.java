package astramod.content;

import arc.util.Log;
import mindustry.game.*;
import mindustry.type.*;
import astramod.maps.planet.*;

import static mindustry.content.Planets.*;

public class AstraPlanets {
	public static Planet aziris;

	public static void load() {
		Log.info("Loading planets");

		aziris = new Planet("aziris", sun, 1f, 1) {{
			generator = new AzirisPlanetGenerator();

			alwaysUnlocked = true;
			allowLaunchToNumbered = false;

			ruleSetter = r -> {
				r.waveTeam = Team.blue;
				r.showSpawns = true;
				r.fog = true;
				r.staticFog = true;
			};

			defaultCore = AstraBlocks.coreNode;
			unlockedOnLand.add(AstraBlocks.coreNode);
		}};
	};
}