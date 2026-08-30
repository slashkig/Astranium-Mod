package astramod.type.effect;

import arc.graphics.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.world.meta.*;

public class StackableStatusEffect extends StatusEffect {
	public StatusEffectStack[] tiers;
	public boolean resetAtMaxTier = true;

	public StackableStatusEffect(String name) {
		super(name);

		init(() -> {
			int i = 1;
			for (StatusEffectStack stack : tiers) {
				if (stack.useParentVisuals) {
					stack.color = color;
					stack.effect = effect;
					stack.effectChance = effectChance * i++;
					if (stack.applyColor.equals(Color.white)) {
						stack.applyColor = color;
					}
				}
			}
		});
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(AstraStat.effectStack, AstraStatValues.effectStack(tiers));
	}

	@Override public void applied(Unit unit, float time, boolean extend) {
		unit.unapply(this);

		for (int i = 0; i < tiers.length; i++) {
			float duration = unit.getDuration(tiers[i]);
			if (duration > 0f) {
				duration += time;
				if (duration > tiers[i].maxStackTime) {
					unit.unapply(tiers[i]);
					tiers[i].stackEffect.get(unit);
					if (i == tiers.length - 1) {
						if (!resetAtMaxTier) unit.apply(tiers[i], time);
					} else {
						unit.apply(tiers[i + 1], time);
					}
				} else {
					unit.apply(tiers[i], duration);
				}
				return;
			}
		}

		unit.apply(tiers[0], time);
	}
}