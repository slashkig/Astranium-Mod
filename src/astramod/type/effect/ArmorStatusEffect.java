package astramod.type.effect;

import arc.math.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.world.meta.*;

public class ArmorStatusEffect extends StatusEffect {
	public float armorModifier;
	public boolean modifierAsMult = true;

	public ArmorStatusEffect(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		if (modifierAsMult) stats.addMultModifier(AstraStat.armorMultiplier, armorModifier);
		else if (armorModifier < 0f) stats.add(AstraStat.armorReduction, -armorModifier);
		else stats.add(AstraStat.armorIncrease, armorModifier);
	}

	@Override public void applied(Unit unit, float time, boolean extend) {
		super.applied(unit, time, extend);
		if (!extend) {
			float armorOverride = unit.applyDynamicStatus().armorOverride;
			unit.statusArmor(modifierAsMult ? (armorOverride < 0f ? unit.armor() : armorOverride) * armorModifier :
				Mathf.maxZero((armorOverride < 0f ? unit.armor() : armorOverride) + armorModifier));
		}
	}

	@Override public void onRemoved(Unit unit) {
		if (modifierAsMult) {
			if (armorModifier == 0f) unit.statusArmor(-1f);
			else unit.applyDynamicStatus().armorOverride /= armorModifier;
		}
		else unit.applyDynamicStatus().armorOverride -= armorModifier;
	}
}