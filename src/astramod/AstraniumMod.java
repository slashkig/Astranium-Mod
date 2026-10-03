package astramod;

import mindustry.mod.*;
import arc.util.Log;
import astramod.ai.*;
import astramod.content.*;
import astramod.gen.*;
import astramod.graphics.*;
import astramod.io.AstraNetHandlers;
import astramod.ui.*;

public class AstraniumMod extends Mod {
	public AstraniumMod() {
		Log.info("Initializing Astranium Mod");
	}

	@Override public void loadContent() {
		EntityRegistry.register();
		AstraSounds.load();
		AstraPal.load();
		AstraItems.load();
		AstraStatusEffects.load();
		AstraFluids.load();
		AstraUnitStance.load();
		AstraUnitCommand.load();
		AstraUnitTypes.load();
		AstraBlocks.load();
		AstraWeathers.load();
		AstraPlanets.load();
		AstraSectorPresets.load();
		AzirisTechTree.load();
		AstraEvents.load();
		EntityRegistry.registerUnits();
		Icons.load();

		Log.info("Astranium Mod loaded");
	}

	@Override public void init() {
		AstraVars.init();
		AstraNetHandlers.init();
		AstraShaders.init();
		Displays.init();
	}
}