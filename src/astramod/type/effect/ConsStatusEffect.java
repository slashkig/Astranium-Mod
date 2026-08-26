package astramod.type.effect;

import arc.func.*;
import mindustry.Vars;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class ConsStatusEffect extends StatusEffect {
	public Cons3<Unit, Float, Boolean> appliedEffect = (u, t, e) -> {};
	public Cons2<Unit, StatusEntry> updateEffect = (u, e) -> {};
	public Cons<Unit> removedEffect = u -> {};

	public float effectStrength;
	public float effectRange = 0f;
	public Stat effectStat;
	public StatUnit effectUnit = StatUnit.none;

	public ConsStatusEffect(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (effectRange > 0f) stats.add(Stat.range, effectRange / Vars.tilesize, StatUnit.blocks);
		if (effectStat != null) stats.add(effectStat, effectStrength, effectUnit);
	}

	@Override public void applied(Unit unit, float time, boolean extend) {
		super.applied(unit, time, extend);
		appliedEffect.get(unit, time, extend);
	}

	@Override public void update(Unit unit, StatusEntry entry) {
		super.update(unit, entry);
		updateEffect.get(unit, entry);
	}

	@Override public void onRemoved(Unit unit) {
		removedEffect.get(unit);
	}
}