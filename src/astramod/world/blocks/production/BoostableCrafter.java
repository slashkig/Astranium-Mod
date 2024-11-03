package astramod.world.blocks.production;

import arc.util.Nullable;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.production.*;
import astramod.world.meta.*;

/** A crafter that can be boosted. */
public class BoostableCrafter extends GenericCrafter {
	public float boostStrength = 0f;
	public @Nullable ItemStack itemBooster;
	public @Nullable LiquidStack liquidBooster;

	public BoostableCrafter(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (itemBooster != null || liquidBooster != null) stats.remove(Stat.booster);

		if (itemBooster != null) {
			stats.add(Stat.booster, AstraStatValues.craftBooster("{0}" + StatUnit.timesSpeed.localized(), itemBooster.amount / craftTime,
				boostStrength, itemBooster.item));
		}
		if (liquidBooster != null) {
			stats.add(Stat.booster, AstraStatValues.craftBooster("{0}" + StatUnit.timesSpeed.localized(), liquidBooster.amount,
				boostStrength, liquidBooster.liquid));
		}
	}

	public ConsumeItems consumeItemBoost(Item item, int amount, float percentBoost) {
		boostStrength = percentBoost;
		return (ConsumeItems)consumeItems(itemBooster = new ItemStack(item, amount)).boost();
	}

	public ConsumeLiquid consumeLiquidBoost(Liquid liquid, float amount, float percentBoost) {
		boostStrength = percentBoost;
		liquidBooster = new LiquidStack(liquid, amount);
		return (ConsumeLiquid)consumeLiquid(liquid, amount).boost();
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