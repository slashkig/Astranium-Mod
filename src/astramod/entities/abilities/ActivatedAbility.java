package astramod.entities.abilities;

import arc.Core;
import arc.input.*;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.ui.Bar;

public abstract class ActivatedAbility extends Ability {
	public KeyBind keybind = Binding.boost;

	@Override public void displayBars(Unit unit, Table bars) {
		bars.add(new Bar(getBundle(), Pal.accent, () -> getProgress(unit))).row();
    }

	@Override public void update(Unit unit) {
		if (Vars.player.unit() == unit) {
			updatePlayer(unit);
			if (getProgress(unit) >= 1f && Core.input.keyDown(keybind)) {
				activate(unit);
			}
		}
	}

	public void updatePlayer(Unit unit) { }

	public abstract float getProgress(Unit unit);

	public abstract void activate(Unit unit);
}