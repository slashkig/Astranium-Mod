package astramod;

import mindustry.mod.*;
import arc.util.Log;
import astramod.ai.*;
import astramod.content.*;
import astramod.gen.*;
import astramod.graphics.*;

public class AstraniumMod extends Mod {
	public AstraniumMod() {
		Log.info("Initializing Astranium Mod");
	}

	@Override public void loadContent() {
		EntityRegistry.register();
		AstraPal.load();
		AstraItems.load();
		AstraFluids.load();
		AstraUnitTypes.load();
		AstraBlocks.load();
		AstraPlanets.load();
		AzirisTechTree.load();
		AstraUnitCommand.load();

		Log.info("Astranium Mod loaded");
	}
}