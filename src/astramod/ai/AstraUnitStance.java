package astramod.ai;

import arc.util.Log;
import astramod.content.AstraItems;
import mindustry.ai.*;
import mindustry.type.Item;

public class AstraUnitStance {
	public static UnitStance lockFollow;

	public static void load() {
		Log.info("Loading unit stances");
		lockFollow = new UnitStance("lockfollow", "lock", null);
		
		for (Item item : AstraItems.azirisItems) {
			if (item.hardness > 0) {
				new ItemUnitStance(item);
			}
		}
	}
}
