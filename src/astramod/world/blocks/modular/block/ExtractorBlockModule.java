package astramod.world.blocks.modular.block;

import mindustry.type.*;
import mindustry.world.meta.*;
import arc.graphics.g2d.*;
import arc.math.geom.Geometry;
import astramod.world.meta.*;

import static mindustry.Vars.*;

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

	@Override public void drawOverlay(float x, float y, int rotation) {
		Draw.rect(extractLiquid.liquid.fullIcon, x + Geometry.d4x(rotation) * (size * tilesize / 2f + 4), y + Geometry.d4y(rotation) * (size * tilesize / 2f + 4), 8f, 8f);
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
