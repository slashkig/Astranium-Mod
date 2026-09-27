package astramod;

import astramod.content.AstraWeathers.WindLogic;

public class AstraVars {
	public static WindLogic windManager;

	public static void init() {
		windManager = new WindLogic();
	}
}