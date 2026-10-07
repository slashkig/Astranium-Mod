package astramod.content;

import arc.util.Log;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.type.*;

public class AstraSectorPresets {
	public static SectorPreset
		pointOne, stoneMesa, hardstoneCanyon, mines;

	public static void load() {
		Log.info("Loading sectors");

		pointOne = new SectorPreset("pointOne", AstraPlanets.aziris, 0) {{
			difficulty = 0;
			captureWave = 20;
			alwaysUnlocked = true;
			addStartingItems = true;
			overrideLaunchDefaults = true;
			rules = r -> {
				r.winWave = captureWave;
				r.waveTeam = Team.crux;
				r.loadout = ItemStack.list(Items.copper, 100, Items.lead, 100);
				r.tags.put("aziris-custom-rules", "true");
			};
		}};

		stoneMesa = new SectorPreset("stoneMesa", AstraPlanets.aziris, 1) {{
			difficulty = 2;
			captureWave = 30;
		}};

		hardstoneCanyon = new SectorPreset("hardstoneCanyon", AstraPlanets.aziris, 2) {{
			difficulty = 3;
			captureWave = 40;
		}};

		mines = new SectorPreset("mines", AstraPlanets.aziris, 3) {{
			difficulty = 3;
			captureWave = 35;
		}};
	}
}