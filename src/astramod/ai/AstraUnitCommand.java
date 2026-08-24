package astramod.ai;

import mindustry.ai.*;
import arc.util.Log;
import astramod.ai.types.*;

public class AstraUnitCommand {
	public static UnitCommand anchorIdle, anchorMine, anchorSupport, anchorProtect, anchorShield, followShoot, followShield;

	public static void load() {
		Log.info("Loading unit commands");
		anchorIdle = new UnitCommand("anchorIdle", "home", u -> new AnchoredAI());
		anchorMine = new UnitCommand("anchorMine", "production", u -> new AnchoredMinerAI()) {{ refreshOnSelect = true; }};
		anchorSupport = new UnitCommand("anchorSupport", "hammer", u -> new AnchoredSupportAI());
		anchorProtect = new UnitCommand("anchorProtect", "turret", u -> new AnchoredAttackerAI()) {{ extraStances.add(UnitStance.pursueTarget); }};
		anchorShield = new UnitCommand("anchorShield", "defense", u -> new AnchoredShieldAI());
		followShoot = new UnitCommand("followShoot", "commandAttack", u -> new FollowShootAI()) {{ extraStances.add(AstraUnitStance.lockFollow); }};
		followShield = new UnitCommand("followShield", "commandRally", u -> new FollowShieldAI(200f)) {{ extraStances.add(AstraUnitStance.lockFollow); }};
	}
}