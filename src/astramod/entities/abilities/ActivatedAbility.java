package astramod.entities.abilities;

import arc.Core;
import arc.input.*;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.ui.*;
import astramod.io.*;

public abstract class ActivatedAbility extends Ability {
	protected static int index = 0;

	public int id;
	public KeyBind keybind = Binding.boost;

	@Override public void init(UnitType type) {
		id = index++;
	}

	@Override public void displayBars(Unit unit, Table bars) {
		bars.add(new Bar(getBundle(), Pal.accent, () -> getProgress(unit))).row();
    }

	@Override public void update(Unit unit) {
		if (Vars.player.unit() == unit) {
			updatePlayer(unit);
			if (Core.input.keyDown(keybind) && canActivate(unit)) {
				activateNet(unit);
			}
		}
	}

	public void updatePlayer(Unit unit) { }

	public boolean canActivate(Unit unit) {
		return getProgress(unit) >= 1f;
	}

	public abstract float getProgress(Unit unit);

	public abstract void activate(Unit unit);

	public final void activateNet(Unit unit) {
		NetUtil.serverIntsReliable("ability-activate", unit.id, id);
	}
}