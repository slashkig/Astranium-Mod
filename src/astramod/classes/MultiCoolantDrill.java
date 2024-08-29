package astramod.classes.blocks.production;

import java.util.*;
import java.util.stream.Collectors;
import arc.math.Mathf;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.production.Drill;
import mindustry.world.consumers.ConsumeLiquidFilter;

public class MultiCoolantDrill extends Drill {
	Map<Liquid, Float> boostMultMap = new HashMap<>();

	public MultiCoolantDrill(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (boostMultMap.size() > 0) {
			stats.remove(Stat.booster);

			List<Map.Entry<Liquid, Float>> boosters = boostMultMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.collect(Collectors.toList());

			for (Map.Entry<Liquid, Float> booster : boosters) {
				float boostMult = Mathf.lerp(1, liquidBoostIntensity, booster.getValue());
				stats.add(Stat.booster,
					StatValues.speedBoosters("{0}" + StatUnit.timesSpeed.localized(),
					((ConsumeLiquidFilter) findConsumer(f -> f instanceof ConsumeLiquidFilter)).amount,
					boostMult * boostMult,
					false,
					l -> l == booster.getKey())
				);
			}
		}
	}

	public ConsumeLiquidFilter consumeLiquidBoosts(float boostCost, Object... liquidBoosts) {
		if (liquidBoosts.length % 2 != 0) {
			throw new IllegalArgumentException("Expected an even number of arguments, but got " + liquidBoosts.length);
		}

		for (int i = 0; i < liquidBoosts.length; i += 2) {
			float mult = ((float)liquidBoosts[i + 1] - 1) / (liquidBoostIntensity - 1);
			boostMultMap.put((Liquid)liquidBoosts[i], mult);
		}
	
		return (ConsumeLiquidFilter)(consume(new ConsumeLiquidFilter(liquid -> boostMultMap.containsKey(liquid), boostCost)).boost());
	}

	public class MultiCoolantDrillBuild extends DrillBuild {
		@Override public void updateEfficiencyMultiplier() {
			float scale = efficiencyScale();
			efficiency *= scale;
			optionalEfficiency *= scale * boostMultMap.get(liquids.current());
		}
	}
}