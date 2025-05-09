package astramod.ai;

import mindustry.ai.*;
import arc.util.Log;
import astramod.ai.types.*;

public class AstraUnitCommand {
	public static final UnitCommand
		protect = new UnitCommand("protect", "eye", _ -> new ProtectorAI(400f)),
		combatFollow = new UnitCommand("combatAssist", "commandAttack", _ -> new CombatAssistAI());
	
	public static void load() {
		Log.info("Loading unit commands");
		UnitCommand.all.add(new UnitCommand[] {protect, combatFollow});
	}
}