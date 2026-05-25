package astramod.world.blocks.power;

import arc.math.Mathf;
import arc.struct.*;
import arc.util.io.*;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.*;
import mindustry.world.meta.*;
import astramod.math.Mathx;
import astramod.world.blocks.modular.*;
import astramod.world.blocks.production.*;
import astramod.world.meta.*;

public class FissionReactor extends ExplodableCrafter implements BaseModularBlock {
	/** Minimum coolant for 100% cooling effectiveness. */
	public float coolantThreshhold = 10f;
	public float coolantCapacity = 20f;
	public float maxCoolantConsumption = 0.1f;

	protected Seq<Block> validModules = new Seq<>();

	public FissionReactor(String name) {
		super(name);
		flags = EnumSet.of(BlockFlag.reactor);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.coolantCapacity, coolantCapacity, StatUnit.liquidUnits);
		stats.add(AstraStat.coolantConsumption, maxCoolantConsumption * 60f, StatUnit.liquidSecond);

		stats.add(AstraStat.moduleBlocks, AstraStatValues.blocks(validModules));
	}

	@Override public void setBars() {
		super.setBars();

		addBar("coolant", entity -> new Bar(
			"bar.coolant", Pal.techBlue,
			() -> ((FissionReactorBuild)entity).coolant / coolantCapacity
		));
	}

	public Seq<Block> getValidModules() {
		return validModules;
	}

	public void addValidModule(Block block) {
		validModules.add(block);
	}

	public EnumSet<ModularType> getModuleTypes() {
		return EnumSet.of(ModularType.cooled, ModularType.heat);
	}

	public class FissionReactorBuild extends ExplodableCrafterBuild implements CooledBuild {
		public float coolant;

		@Override public void handleCoolant() {
			float maxUsed = Math.min(coolant, maxCoolantConsumption * edelta());
			heat -= maxUsed * coolantPower * Mathx.pow2(Mathf.clamp(coolant / coolantThreshhold));
			coolant -= maxUsed;
		}

		public boolean coolantFull() {
			return coolant >= coolantCapacity - 0.001f;
		}

		public void addCoolant(float amount) {
			coolant = Math.min(coolant + amount, coolantCapacity);
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.f(coolant);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			coolant = read.f();
		}	}
}