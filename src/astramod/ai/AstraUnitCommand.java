package astramod.ai;

import mindustry.ai.*;
import arc.util.Log;
import astramod.ai.types.*;

@SuppressWarnings("unused")
public class AstraUnitCommand {
	public static final UnitCommand
		protect = new UnitCommand("protect", "eye", u -> new AnchoredProtectorAI()),
		combatFollow = new UnitCommand("combatAssist", "commandAttack", u -> new CombatAssistAI()),
		shieldCore = new UnitCommand("shieldCore", "defense", u -> new AnchoredShieldAI()),
		shieldFollow = new UnitCommand("shieldFollow", "commandRally", u -> new ShieldAssistAI());
	
	public static void load() {
		Log.info("Loading unit commands");
		UnitCommand.all.add(new UnitCommand[] { protect, combatFollow, shieldCore, shieldFollow });
	}
}