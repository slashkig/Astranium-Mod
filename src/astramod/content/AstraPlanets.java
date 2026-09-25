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
			cloudMeshLoader = () -> new MultiMesh(
				new HexSkyMesh(
					this, 13, 3.6f, 0.11f, 8,
					Color.white.a(75), 5, 0.65f, 1f, 0.39f
				),
				new HexSkyMesh(
					this, 11, 2.4f, 0.08f, 8,
					Color.gray.a(85), 4, 0.55f, 0.89f, 0.29f
				)
			);
			bloom = true;

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
