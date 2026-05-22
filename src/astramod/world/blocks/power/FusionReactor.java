package astramod.world.blocks.power;

import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.graphics.Pal;
import mindustry.type.*;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import astramod.math.Mathx;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

public class FusionReactor extends ImpactReactor implements BaseModularBlock {
	public LiquidStack byproductLiquid;
	public float byproductPoisoning = 0.5f;
	public float coolantCapacity = 20f, coolantBoost = 0.2f, coolantConsumption = 1f;
	public float warmupDecay = 0.01f;

	protected Seq<Block> validModules = new Seq<>();

	public FusionReactor(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();

		if (byproductLiquid != null) {
			stats.add(Stat.output, StatValues.liquid(byproductLiquid.liquid, byproductLiquid.amount * 60f, true));
		}

		if (coolantCapacity > 0f) {
			stats.add(AstraStat.coolantCapacity, coolantCapacity, StatUnit.liquidUnits);
			stats.add(AstraStat.coolantConsumption, coolantConsumption * 60f, StatUnit.liquidSecond);
			stats.add(AstraStat.coolantBoost, coolantBoost * 100f, StatUnit.percent);
		}

		stats.add(AstraStat.rampupTime, 1f / (60f * warmupSpeed), StatUnit.seconds);
		stats.add(AstraStat.moduleBlocks, AstraStatValues.blocks(validModules));
	}

	@Override public void setBars() {
		super.setBars();

		if (byproductLiquid != null) {
			addLiquidBar(byproductLiquid.liquid);
		}
		if (coolantCapacity > 0f) {
			addBar("coolant", entity -> new Bar(
				"bar.coolant", Pal.techBlue,
				() -> ((FusionReactorBuild)entity).coolant / coolantCapacity
			));
		}
	}

	public Seq<Block> getValidModules() {
		return validModules;
	}

	public void addValidModule(Block block) {
		validModules.add(block);
	}

	public EnumSet<ModularType> getModuleTypes() {
		return EnumSet.of(ModularType.cooled);
	}

	public class FusionReactorBuild extends ImpactReactorBuild implements CooledBuild {
		public float coolant;

		@Override public void updateTile() {
			float boostFactor = coolantBoost * coolant / coolantCapacity;

			if (efficiency >= 0.9999f && power.status >= 0.99f) {
				warmup = Mathx.elerpDelta(warmup, 1f, warmupSpeed * timeScale * (1f + boostFactor), 0.0001f);

				if (timer(timerUse, itemDuration / timeScale)) {
					consume();
				}
			} else {
				warmup = Mathf.lerpDelta(warmup, 0f, warmupDecay * timeScale);
			}

			totalProgress += warmup * Time.delta;

			productionEfficiency = Mathf.pow(warmup, 5f) *
				(1f - byproductPoisoning * liquids.get(byproductLiquid.liquid) / liquidCapacity + boostFactor);

			coolant = Math.min(coolant - coolantConsumption * warmup * edelta(), 0f);

			if (byproductLiquid != null) {
				liquids.add(byproductLiquid.liquid, Math.min(
					warmup * productionEfficiency * delta() * byproductLiquid.amount,
					liquidCapacity - liquids.get(byproductLiquid.liquid)
				));
			}
		}

		public boolean coolantFull() {
			return coolant >= coolantCapacity - 0.001f;
		}

		public void addCoolant(float amount) {
			coolant = Math.min(coolant + amount, coolantCapacity);
		}

		@Override public float efficiency() {
			return productionEfficiency;
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.f(coolant);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			coolant = read.f();
		}
	}
}