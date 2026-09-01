package astramod.entities.abilities;

import arc.Core;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.abilities.StatusFieldAbility;
import mindustry.gen.*;
import mindustry.type.*;

public class ApplyStatusFieldAbility extends StatusFieldAbility {
	protected boolean applied = false;

	public ApplyStatusFieldAbility(StatusEffect effect, float duration, float reload, float range) {
		super(effect, duration, reload, range);
	}

	@Override public void addStats(Table t) {
		super.addStats(t);

		((Label)t.getCells().get(t.getCells().size - 1).get()).getText().append(Strings.format(
			"[lightgray] ~ [white]@[stat] @",
			Strings.autoFixed(duration / Time.toSeconds, 1),
			Core.bundle.get("unit.seconds"))
		);
	}
	
	@Override public void update(Unit unit) {
		timer += Time.delta;

		if (timer >= reload && (!onShoot || unit.isShooting)) {
			applied = false;
			Units.nearby(unit.team, unit.x, unit.y, range, other -> {
				if (other != unit) {
					other.apply(effect, duration);
					applyEffect.at(other, parentizeEffects);
					applied = true;
				}
			});

			if (applied) {
				float x = unit.x + Angles.trnsx(unit.rotation, effectY, effectX), y = unit.y + Angles.trnsy(unit.rotation, effectY, effectX);
				activeEffect.at(x, y, effectSizeParam ? range : unit.rotation, color, parentizeEffects ? unit : null);
			}

			timer = 0f;
		}
	}

	@Override public String getBundle() {
		return "ability.statusfield";
	}
}