package astraniumMod;

import astraniumMod.content.*;
import mindustry.mod.Mod;
import arc.util.Log;

public class AstraniumMod extends Mod {
	public AstraniumMod() {
		Log.info("Initializing Astranium Mod");
	}

	@Override public void loadContent() {
		AstraItems.load();
		AstraBlocks.load();
	}
}
