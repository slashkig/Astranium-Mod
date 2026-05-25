package astramod.world.blocks.modular.block;

import arc.Core;
import arc.math.Mathf;
import mindustry.graphics.*;
import mindustry.ui.Bar;
import mindustry.world.blocks.heat.*;
import mindustry.world.meta.*;
import astramod.content.*;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

public class HeatsinkBlockModule extends GenericBlockModule {
	/** Heat per tick absorbed from the linked block at 100% efficiency. */
	public float heatConduction = 0.01f;
	/** The multiplier on heat applied to the block. */
	public float heatFactor = 1f;
	/** The rate at which the block's heat decreases. */
	public float heatDecay = 0.005f;
	/** How much Erekir heat the heatsink can output. */
	public float heatOutput = 0f;

	public HeatsinkBlockModule(String name) {
		super(name);
	}

	@Override public void init() {
		if (targetBlockType == null) targetBlocks = AstraBlocks.heatedBlocks;
		super.init();
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(AstraStat.heatCapacity, 100f / heatFactor, StatUnit.percent);
		stats.add(AstraStat.heatAbsorption, 6000f * heatConduction, AstraStatUnit.percentSecond);
		stats.add(Stat.cooldownTime, 1f / (60f * heatDecay), StatUnit.seconds);
		stats.add(Stat.output, heatOutput, StatUnit.heatUnits);
	}

	@Override public void setBars() {
		super.setBars();

		if (heatOutput > 0f) {
			addBar("heat", (HeatsinkModuleBuild entity) -> new Bar(
				() -> Core.bundle.format("bar.heatamount", (int)(entity.heat * heatOutput + 0.001f)),
				() -> Pal.lightOrange,
				() -> entity.heat
			));
		} else {
			addBar("heat", (HeatsinkModuleBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.heat));
		}
	}

	public class HeatsinkModuleBuild extends GenericModuleBuild implements HeatBlock {
		public float heat = 0f;

		@Override public void updateTile() {
			if (linkedBuild instanceof HeatedBuild hb) {
				float heatTransfer = Math.min(hb.getHeatFrac(), ((1f - heat) + Mathf.maxZero(hb.getHeatFrac() - heat)) * heatConduction * delta());
				hb.handleHeat(-heatTransfer);
				heat = Mathf.clamp(heat + heatTransfer * heatFactor - heatDecay);
			}
		}

		@Override public float warmup() {
			return heat;
		}

		public float heat() {
			return heat * heatOutput;
		}

		public float heatFrac() {
			return heat / 3f;
		}
	}
}