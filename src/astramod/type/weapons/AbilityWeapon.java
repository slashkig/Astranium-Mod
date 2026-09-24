package astramod.type.weapons;

import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.type.*;
import astramod.world.meta.*;

public class AbilityWeapon extends AstraWeapon {
	public AbilityWeapon(String name) {
		super(name);
		controllable = aiControllable = false;
		useAttackRange = false;
		display = false;
	}

	@Override public void addStats(UnitType u, Table t) {
		AstraStatValues.addRow(t, "bullet.range", Strings.autoFixed(range() / Vars.tilesize, 2));
		super.addStats(u, t);
	}

	@Override public void statReload(UnitType u, Table t) {
		AstraStatValues.addRow(t, "ability.stat.cooldown", Strings.autoFixed(reload / Time.toSeconds, 2));
	}
}