package astramod.type.weapons;

import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Strings;
import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class AstraWeapon extends Weapon {
	public boolean extendedStats = true;

	public AstraWeapon(String name) {
		super(name);
	}

	@Override public void addStats(UnitType u, Table t) {
		if (inaccuracy > 0f) {
			AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @", Stat.inaccuracy.localized(), (int)inaccuracy, StatUnit.degrees.localized());
		}
		if (!alwaysContinuous && reload > 0 && !bullet.killShooter) {
			statReload(u, t);
		}

		if (extendedStats) AstraStatValues.astraAmmo(ObjectMap.of(u, bullet)).display(t);
		else StatValues.ammo(ObjectMap.of(u, bullet)).display(t);
	}

	public void statReload(UnitType u, Table t) {
		AstraStatValues.addRowString(t, "[lightgray]@: @[white]@ @",
			Stat.reload.localized(),
			mirror ? "2x " : "",
			Strings.autoFixed(shoot.shots * 60f / reload, 2),
			StatUnit.perSecond.localized()
		);
	}
}