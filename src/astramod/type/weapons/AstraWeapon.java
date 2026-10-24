package astramod.type.weapons;

import arc.scene.ui.layout.Table;
import arc.struct.*;
import arc.util.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class AstraWeapon extends Weapon {
	public boolean extendedStats = true;

	public AstraWeapon(String name) {
		super(name);
	}

	public AstraWeapon() {
		super();
	}

	@Override public void addStats(UnitType u, Table t) {
		if (inaccuracy > 0f) {
			AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @", Stat.inaccuracy.localized(), (int)inaccuracy, StatUnit.degrees.localized());
		}
		if (!alwaysContinuous && reload > 0 && !bullet.killShooter) {
			statReload(u, t);
		}
		extraWeaponStats(u, t);

		if (extendedStats) AstraStatValues.astraAmmo(ObjectMap.of(u, bullet)).display(t);
		else StatValues.ammo(ObjectMap.of(u, bullet)).display(t);
	}

	public void statReload(UnitType u, Table t) {
		AstraStatValues.addRowString(t, "[lightgray]@: @[white]@ @",
			Stat.reload.localized(),
			mirror ? "2x " : "",
			Strings.autoFixed(shoot.shots * bullet.createChance * 60f / reload, 2),
			StatUnit.perSecond.localized()
		);
	}

	public void extraWeaponStats(UnitType u, Table t) {
		if (minShootVelocity >= 0f) {
			AstraStatValues.addRow(t, "bullet.minshootvel", minShootVelocity * Time.toSeconds / tilesize);
		}
		if (maxShootVelocity >= 0f) {
			AstraStatValues.addRow(t, "bullet.maxshootvel", minShootVelocity * Time.toSeconds / tilesize);
		}
	}
}