package astramod.classes.blocks.production;

import arc.struct.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.production.GenericCrafter;
import astramod.classes.meta.*;

// Can intake different liquids that provide different efficiency multipliers.
public class MultiLiquidCrafter extends GenericCrafter {
	ObjectFloatMap<Liquid> liquidStrengthMap = new ObjectFloatMap<Liquid>();

	public MultiLiquidCrafter(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (liquidStrengthMap.size > 0) {
			stats.remove(Stat.input);

			Seq<LiquidStack> liquidInputs = new Seq<LiquidStack>(liquidStrengthMap.size);
			for (ObjectFloatMap.Entry<Liquid> entry : liquidStrengthMap) { liquidInputs.add(new LiquidStack(entry.key, entry.value)); }
			liquidInputs.sort(e -> e.amount);

			stats.add(Stat.input, AstraStatValues.itemsLiquidsVariable(
				"{0}" + StatUnit.timesSpeed.localized(),
				((ConsumeItems)findConsumer(f -> f instanceof ConsumeItems)).items,
				liquidInputs,
				stats.timePeriod,
				((ConsumeLiquidFilter)findConsumer(f -> f instanceof ConsumeLiquidFilter)).amount
			));
		}
	}

	public ConsumeLiquidFilter consumeLiquidsMulti(float amount, Object... liquidInputs) {
		if (liquidInputs.length % 2 != 0) {
			throw new IllegalArgumentException("Expected an even number of arguments, but got " + liquidInputs.length);
		}

		for (int i = 0; i < liquidInputs.length; i += 2) {
			liquidStrengthMap.put((Liquid)liquidInputs[i], (float)liquidInputs[i + 1]);
		}
	
		return (ConsumeLiquidFilter)consume(new ConsumeLiquidFilter(liquid -> liquidStrengthMap.containsKey(liquid), amount));
	}

	public class BoostableCrafterBuild extends GenericCrafterBuild {
		@Override public float getProgressIncrease(float base) {
			return super.getProgressIncrease(base) * efficiencyMultiplier();
		}

		public float efficiencyMultiplier() {
			return liquidStrengthMap.get(liquids.current(), 1f);
		}
	}
}