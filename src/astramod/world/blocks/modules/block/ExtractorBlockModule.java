package astramod.world.blocks.modules.block;

import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class ExtractorBlockModule extends GenericBlockModule {
	public LiquidStack extractLiquid;

	public ExtractorBlockModule(String name) {
		super(name);
		outputsLiquid = hasLiquids = true;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.fluidThroughput, StatValues.liquids(1f, true, extractLiquid));
	}

	@Override public boolean rotatedOutput(int x, int y) {
		return false;
	}

	public class ExtractorModuleBuild extends GenericModuleBuild {
		@Override public void updateTile() {
			if (linkedBuild != null) {
				float amount = linkedBuild.liquids().get(extractLiquid.liquid);
				if (amount > 0) {
					amount = Math.min(
						efficiency * delta() * extractLiquid.amount,
						Math.min(liquidCapacity - liquids.get(extractLiquid.liquid), amount)
					);

					linkedBuild.handleLiquid(this, extractLiquid.liquid, -amount);
					handleLiquid(linkedBuild, extractLiquid.liquid, amount);
					dumpLiquid(extractLiquid.liquid);
				}
			}
		}
	}
}
