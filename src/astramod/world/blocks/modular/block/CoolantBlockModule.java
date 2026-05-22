package astramod.world.blocks.modular.block;

import arc.math.Mathf;
import arc.util.*;
import arc.util.io.*;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.*;
import astramod.content.AstraBlocks;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

public class CoolantBlockModule extends GenericBlockModule {
	public float warmupSpeed = 0.002f;
	public float coolantProduction;

	public CoolantBlockModule(String name) {
		super(name);
		hasLiquids = true;
	}

	@Override public void init() {
		if (targetBlockType == null) targetBlocks = AstraBlocks.cooledBlocks;
		super.init();
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.coolantProduction, coolantProduction * 60f, StatUnit.liquidSecond);
		stats.add(AstraStat.rampupTime, 1f / (60f * warmupSpeed), StatUnit.seconds);
	}

	@Override public void setBars() {
		super.setBars();

		addBar("warmup", entity -> new Bar(
			"bar.pumpspeed", Pal.techBlue,
			() -> entity.warmup()
		));
	}

	public class CoolantModuleBuild extends GenericModuleBuild {
		public float totalProgress = 0f;
		public float warmup = 0f;

		@Override public void updateTile() {
			if (efficiency > 0 && linkedBuild instanceof CooledBuild cb) {
				warmup = Mathf.approachDelta(warmup, 1f, warmupSpeed * timeScale);
				cb.addCoolant(coolantProduction * warmup * edelta());
			} else {
				warmup = Mathf.approachDelta(warmup, 0f, warmupSpeed * timeScale);
			}

			totalProgress += warmup * Time.delta;
		}

		@Override public boolean shouldConsume() {
			return enabled && linkedBuild instanceof CooledBuild cb && !cb.coolantFull();
		}

		@Override public float totalProgress() {
			return totalProgress;
		}

		@Override public float warmup() {
			return warmup;
		}
		
		@Override public void write(Writes write) {
			super.write(write);
			write.f(warmup);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			warmup = read.f();
		}
	}
}