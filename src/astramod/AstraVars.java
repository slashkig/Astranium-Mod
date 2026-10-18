package astramod;

import astramod.game.*;

public class AstraVars {
	public static WindLogic windManager;

	public static void init() {
		windManager = new WindLogic();
	}
}