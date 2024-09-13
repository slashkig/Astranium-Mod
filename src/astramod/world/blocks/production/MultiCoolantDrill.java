package astramod.world.blocks.production;

import arc.Core;
import arc.struct.*;
import arc.util.*;
import arc.math.Mathf;
import mindustry.ui.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.production.Drill;
import astramod.world.modules.*;

// Can be boosted by different amounts by different liquids.
public class MultiCoolantDrill extends Drill {
	ObjectFloatMap<Liquid> boostMultMap = new ObjectFloatMap<Liquid>();

	public MultiCoolantDrill(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (boostMultMap.size > 0) {
			stats.remove(Stat.booster);

			Seq<LiquidStack> boosters = new Seq<LiquidStack>(boostMultMap.size);
			for (ObjectFloatMap.Entry<Liquid> entry : boostMultMap) { boosters.add(new LiquidStack(entry.key, entry.value)); }
			boosters.sort(e -> e.amount);

			for (LiquidStack booster : boosters) {
				float boostMult = Mathf.lerp(1, liquidBoostIntensity, booster.amount);
				stats.add(Stat.booster, StatValues.speedBoosters(
					"{0}" + StatUnit.timesSpeed.localized(),
					((ConsumeLiquidFilter)findConsumer(f -> f instanceof ConsumeLiquidFilter)).amount,
					boostMult * boostMult,
					false,
					l -> l == booster.liquid
				));
			}
		}
	}

	@Override public void setBars() {
		super.setBars();

		if (findConsumer(f -> (f instanceof ConsumeLiquid && !f.booster)) != null) {
			removeBar("drillspeed");

			addLiquidBar(build -> build.liquids.current());

			addBar("drillspeed", (DrillBuild e) -> new Bar(() -> Core.bundle.format("bar.drillspeed",
				Strings.fixed(e.lastDrillSpeed * 60 * e.timeScale(), 2)), () -> Pal.ammo, () -> e.warmup));
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
	
		return ((ConsumeLiquidFilter)consume(new ConsumeLiquidFilter(liquid -> boostMultMap.containsKey(liquid), boostCost)).boost());
	}

	public class MultiCoolantDrillBuild extends DrillBuild {
		@Override public Building create(Block block, Team team) {
			Building build = super.create(block, team);
			liquids = new LiquidBoostModule(boostMultMap);
			return build;
		}

		@Override public void updateEfficiencyMultiplier() {
			float scale = efficiencyScale();
			efficiency *= scale;
			optionalEfficiency *= scale * boostMultMap.get(liquids.current(), 1f);
		}
	}
}