package astramod.type.effect;

import arc.math.Mathf;
import mindustry.gen.Unit;
import mindustry.type.*;
import astramod.world.meta.*;

public class ArmorStatusEffect extends StatusEffect {
	public float armorModifier;

	public ArmorStatusEffect(String name) {
		super(name);
	}

	@Override
	public void setStats() {
		super.setStats();
		if (armorModifier < 0f) stats.add(AstraStat.armorReduction, -armorModifier);
		else stats.add(AstraStat.armorIncrease, armorModifier);
	}

	@Override public void applied(Unit unit, float time, boolean extend) {
		super.applied(unit, time, extend);
		if (!extend) {
			float armorOverride = unit.applyDynamicStatus().armorOverride;
			unit.statusArmor(Mathf.maxZero((armorOverride > 0f ? armorOverride : unit.armor()) + armorModifier));
		}
	}

	@Override public void onRemoved(Unit unit) {
		unit.applyDynamicStatus().armorOverride -= armorModifier;
	}
}