package astramod.content;

import arc.graphics.*;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import astramod.maps.planet.*;

import static mindustry.content.Planets.*;

public class AstraPlanets {
	public static final Seq<Planet> windPlanets = new Seq<>();
	public static Planet aziris;

	public static void load() {
		Log.info("Loading planets");

		// TODO make Aziris more than just a white sphere
		aziris = new Planet("aziris", sun, 1f, 1) {{
			generator = new AzirisPlanetGenerator();
			meshLoader = () -> new HexMesh(this, 6);

			alwaysUnlocked = true;
			allowLaunchToNumbered = false;

			ruleSetter = r -> {
				r.waveTeam = Team.blue;
				r.hideSpawns = false;
				r.fog = true;
				r.staticFog = true;

				r.loadout = ItemStack.list(AstraItems.hematite, 100);
			};

			iconColor = Color.valueOf("bf2851");

			defaultCore = AstraBlocks.coreNode;
			unlockedOnLand.add(AstraBlocks.coreNode);
		}};

		windPlanets.add(AstraPlanets.aziris, sun);
	};
}
