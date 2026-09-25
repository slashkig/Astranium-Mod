package astramod.content;

import arc.util.Log;
import mindustry.type.*;

import static mindustry.type.SectorDifficulty.*;

public class AstraSectorPresets {
	public static SectorPreset
		pointOne;

	public static void load() {
		Log.info("Loading sectors");

		pointOne = new SectorPreset("pointOne", AstraPlanets.aziris, 0) {{
			difficulty = low;
			alwaysUnlocked = true;
			addStartingItems = true;
			overrideLaunchDefaults = true;
		}};
	}
}