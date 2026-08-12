package astramod.ai;

import mindustry.ai.*;
import arc.util.Log;
import astramod.ai.types.*;

public class AstraUnitCommand {
	public static UnitCommand protect, combatFollow, shieldCore, shieldFollow;


	public static void load() {
		Log.info("Loading unit commands");
		protect = new UnitCommand("protect", "eye", u -> new AnchoredProtectorAI());
		combatFollow = new UnitCommand("combatAssist", "commandAttack", u -> new CombatAssistAI());
		shieldCore = new UnitCommand("shieldCore", "defense", u -> new AnchoredShieldAI());
		shieldFollow = new UnitCommand("shieldFollow", "commandRally", u -> new ShieldAssistAI());
	}
}