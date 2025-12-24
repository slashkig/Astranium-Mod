package astramod.world.blocks.modules.block;

import mindustry.type.*;

public class ExtractorBlockModule extends GenericBlockModule {
	public LiquidStack extractLiquid;

	public ExtractorBlockModule(String name) {
		super(name);
		outputsLiquid = hasLiquids = true;
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
