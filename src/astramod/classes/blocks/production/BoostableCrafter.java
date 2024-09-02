package astramod.classes.blocks.production;

import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.blocks.production.GenericCrafter;
import astramod.classes.meta.*;

// Can be boosted by items.
public class BoostableCrafter extends GenericCrafter {
	public float boostStrength = 0f;
	public ItemStack itemBooster;

	public BoostableCrafter(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (boostStrength != 0) {
			stats.remove(Stat.booster);

			stats.add(Stat.booster, AstraStatValues.craftBooster("{0}" + StatUnit.timesSpeed.localized(), itemBooster.amount / craftTime,
				boostStrength, itemBooster.item));
		}
	}

	public ConsumeItems consumeItemBoost(Item item, int amount, float percentBoost) {
		boostStrength = percentBoost;
		return (ConsumeItems)consumeItems(itemBooster = new ItemStack(item, amount)).boost();
	}

	public class BoostableCrafterBuild extends GenericCrafterBuild {
		@Override public float getProgressIncrease(float base) {
			return super.getProgressIncrease(base) * efficiencyMultiplier();
		}

		public float efficiencyMultiplier() {
			return 1f + boostStrength * optionalEfficiency;
		}
	}
}