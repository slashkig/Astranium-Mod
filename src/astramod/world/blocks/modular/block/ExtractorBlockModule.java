package astramod.world.blocks.modular.block;

import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.util.Time;
import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class ExtractorBlockModule extends GenericBlockModule {
	public LiquidStack extractLiquid;
	public float warmupSpeed = 0.02f;

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
		public float totalProgress = 0f;
		public float warmup = 0f;

		@Override public void updateTile() {
			boolean active = false;
			if (linkedBuild != null) {
				float amount = Math.min(Math.min(
					efficiency * delta() * extractLiquid.amount,
					liquidCapacity - liquids.get(extractLiquid.liquid)),
					linkedBuild.liquids().get(extractLiquid.liquid)
				);

				if (amount > 0) {
					active = true;
					linkedBuild.handleLiquid(this, extractLiquid.liquid, -amount);
					handleLiquid(linkedBuild, extractLiquid.liquid, amount);
				}
			}

			warmup = Mathf.approachDelta(warmup, active ? 1f : 0f, warmupSpeed);
			totalProgress += warmup * Time.delta;
			dumpLiquid(extractLiquid.liquid);
		}

		@Override public float totalProgress() {
			return totalProgress;
		}
	}
}
