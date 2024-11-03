package astramod;

import mindustry.mod.Mod;
import arc.util.Log;
import astramod.content.*;
import astramod.gen.EntityRegistry;

public class AstraniumMod extends Mod {
	public AstraniumMod() {
		Log.info("Initializing Astranium Mod");
	}

	@Override public void loadContent() {
		EntityRegistry.register();
		AstraItems.load();
		AstraFluids.load();
		AstraUnitTypes.load();
		AstraBlocks.load();
		AstraPlanets.load();
		AzirisTechTree.load();
	}
}