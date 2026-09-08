package astramod.entities.abilities;

import arc.audio.Sound;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class ShieldRegenAbility extends Ability {
	public float amount = 1f, max = 100f, reload = 100f;
	public Effect applyEffect = Fx.shieldApply;
	public Sound sound = Sounds.shieldWave;
	public float soundVolume = 0.7f;
	public boolean parentizeEffects;

	protected float timer;

	public ShieldRegenAbility(float amount, float max, float reload) {
		this.amount = amount;
		this.max = max;
		this.reload = reload;
	}

	@Override public void addStats(Table t) {
		super.addStats(t);
		t.add(abilityStat("firingrate", Strings.autoFixed(Time.toSeconds / reload, 2)));
		t.row();
		t.add(abilityStat("pulseregen", Strings.autoFixed(amount, 2)) + "[lightgray] ~ []" + abilityStat("regen", Strings.autoFixed(amount * Time.toSeconds / reload, 2)));
		t.row();
		t.add(abilityStat("shield", Strings.autoFixed(max, 2)));
	}

	@Override public void update(Unit unit) {
		timer += Time.delta;

		if (timer >= reload) {
			if (unit.shield < max) {
				unit.shield = Math.min(unit.shield + amount, max);
				unit.shieldAlpha = 1f;

				applyEffect.at(unit.x, unit.y, 0f, unit.type.shieldColor(unit), parentizeEffects ? unit : null);
				sound.at(unit, 1f + Mathf.range(0.1f), soundVolume);
			}

			timer = 0f;
		}
	}
}