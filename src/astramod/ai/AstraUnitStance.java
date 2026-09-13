package astramod.ai;

import arc.util.Log;
import mindustry.ai.*;

public class AstraUnitStance {
	public static UnitStance lockFollow;

	public static void load() {
		Log.info("Loading unit stances");
		lockFollow = new UnitStance("lockfollow", "lock", null);
	}
}
