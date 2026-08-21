package astramod.type.weapons;

import arc.scene.ui.layout.Table;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class RampUpWeapon extends Weapon {
	/** Maximum fraction of reload time that can be reduced by ramp-up. */
	public float rampupFactor = 0f;

	public RampUpWeapon(String name) {
		super(name);
		linearWarmup = true;
	}

	@Override public void addStats(UnitType u, Table t){
		if (inaccuracy > 0) {
			AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @", Stat.inaccuracy.localized(), (int)inaccuracy, StatUnit.degrees.localized());
		}
		if (!alwaysContinuous && reload > 0 && !bullet.killShooter) {
			AstraStatValues.addRowString(t, "[lightgray]@: @[white]@ - @ @",
				Stat.reload.localized(),
				mirror ? "2x " : "",
				Strings.autoFixed(shoot.shots * 60f / reload, 2),
				Strings.autoFixed(shoot.shots * 60f / (reload * (1f - rampupFactor)), 2),
				StatUnit.perSecond.localized()
			);
			AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @",
				AstraStat.rampupTime.localized(),
				Strings.autoFixed(1f / (Time.toSeconds * shootWarmupSpeed), 2),
				StatUnit.seconds.localized()
			);
		}

		StatValues.ammo(ObjectMap.of(u, bullet)).display(t);
	}

	@Override public void update(Unit unit, WeaponMount mount) {
		super.update(unit, mount);

		var status = unit.applyDynamicStatus();
		status.reloadMultiplier = 1f / (status.speedMultiplier = 1f - (mount.shoot ? rampupFactor * mount.warmup : 0f));
	}

	@Override public float dps() {
		return bullet.estimateDPS() * shotsPerSec();
	}

	@Override public float shotsPerSec() {
		return super.shotsPerSec() / (1f - rampupFactor * 0.5f);
	}
}