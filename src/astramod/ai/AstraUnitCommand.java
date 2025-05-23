package astramod.ai;

import mindustry.ai.*;
import arc.util.Log;
import astramod.ai.types.*;

@SuppressWarnings("unused")
public class AstraUnitCommand {
	public static final UnitCommand
		protect = new UnitCommand("protect", "eye", u -> new ProtectorAI(400f)),
		combatFollow = new UnitCommand("combatAssist", "commandAttack", u -> new CombatAssistAI());
	
	public static void load() {
		Log.info("Loading unit commands");
		UnitCommand.all.add(new UnitCommand[] {protect, combatFollow});
	}
}