package astramod.world.blocks.power;

import arc.math.*;
import arc.util.*;
import astramod.world.blocks.modular.*;
import mindustry.type.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

public class FusionReactor extends ImpactReactor {
	public LiquidStack byproductLiquid;
	public float byproductPoisoning = 0.5f;

	public FusionReactor(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (byproductLiquid != null) {
			stats.add(Stat.output, StatValues.liquid(byproductLiquid.liquid, byproductLiquid.amount * 60f, true));
		}
	}

	@Override public void setBars() {
		super.setBars();

		if (byproductLiquid != null) {
			addLiquidBar(byproductLiquid.liquid);
		}
	}

	public class FusionReactorBuild extends ImpactReactorBuild implements BaseModularBlock {
		@Override public void updateTile() {
			if (efficiency >= 0.9999f && power.status >= 0.99f) {
				warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
				if (Mathf.equal(warmup, 1f, 0.001f)) {
					warmup = 1f;
				}

				if (timer(timerUse, itemDuration / timeScale)) {
					consume();
				}
			} else {
				warmup = Mathf.lerpDelta(warmup, 0f, 0.01f);
			}

			totalProgress += warmup * Time.delta;

			productionEfficiency = Mathf.pow(warmup, 5f) * (1 - byproductPoisoning * liquids.get(byproductLiquid.liquid) / liquidCapacity);

			if (byproductLiquid != null) {
				liquids.add(byproductLiquid.liquid, Math.min(
					warmup * productionEfficiency * delta() * byproductLiquid.amount,
					liquidCapacity - liquids.get(byproductLiquid.liquid)
				));
			}
		}

		@Override public float efficiency() {
			return productionEfficiency;
		}
	}
}
