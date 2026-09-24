package astramod.entities.abilities;

import arc.Core;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;

public class DashAbility extends ActivatedAbility {
	public float speedBoost = 1f;
	public float duration = 20f;
	public float cooldown = 100f;
	public Effect boostEffect = Fx.none;

	public DashAbility(float speedBoost, float duration, float cooldown) {
		this.speedBoost = speedBoost;
		this.duration = duration;
		this.cooldown = cooldown;
	}

	@Override public void addStats(Table t) {
		super.addStats(t);
		t.add(abilityStat("tiles", Strings.autoFixed(speedBoost * duration / Vars.tilesize, 2)));
		t.row();
		t.add(abilityStat("duration", duration / Time.toSeconds));
		t.row();
		t.add(abilityStat("cooldown", cooldown / Time.toSeconds));
	}

	@Override public void update(Unit unit) {
		if (data < 0f) {
			unit.vel.add(Tmp.v1.trns(unit.rotation(), speedBoost * unit.type.accel * Time.delta));
		} else if (data >= cooldown && Vars.player.unit() == unit && Core.input.keyDown(keybind)) {
			activate(unit);
		}
		data += Time.delta;
	}

	@Override public float getProgress(Unit unit) {
		return Mathf.clamp(data / (data < 0f ? -duration : cooldown));
	}

	public void activate(Unit unit) {
		data = -duration;
	}
}