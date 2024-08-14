package testmod;

import testmod.content.*;
import mindustry.mod.Mod;
import arc.util.Log;

public class TestMod extends Mod {
	public TestMod() {
		Log.info("Initializing testmod");
	}

	@Override public void loadContent() {
		TestItems.load();
		TestOres.load();
	}
}