package astramod;

import mindustry.mod.*;
import arc.util.Log;
import astramod.ai.*;
import astramod.content.*;
import astramod.gen.*;
import astramod.graphics.*;
import astramod.ui.Icons;

public class AstraniumMod extends Mod {
	public AstraniumMod() {
		Log.info("Initializing Astranium Mod");
	}

	@Override public void loadContent() {
		EntityRegistry.register();
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
		AzirisTechTree.load();
		AstraEvents.load();
		AstraSectorPresets.load();
		Icons.load();

		Log.info("Astranium Mod loaded");
	}
}