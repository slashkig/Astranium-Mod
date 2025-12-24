package astramod.world.blocks.power;

import arc.Core;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.*;

public class StartupGenerator extends PowerGenerator {
	public final int timerUse = timers++;
	public float warmupSpeed = 0.004f;
	private Seq<Liquid> consumeLiquids;

	public StartupGenerator(String name) {
		super(name);
		hasPower = outputsPower = consumesPower = true;
	}

	@Override public void setBars() {
		super.setBars();

		addBar("power", (GeneratorBuild entity) -> new Bar(() -> Core.bundle.format("bar.poweroutput",
			Strings.fixed((entity.getPowerProduction() - consPower.usage) * 60 * entity.timeScale(), 1)),
			() -> Pal.powerBar,
			() -> entity.productionEfficiency
		));
	}

	public ConsumeLiquidFilter consumeLiquidsMulti(float amount, Liquid... liquids) {
		consumeLiquids = new Seq<Liquid>().add(liquids);
		return (ConsumeLiquidFilter)consume(new ConsumeLiquidFilter(liquid -> consumeLiquids.contains(liquid), amount));
	}

	public class StartupGeneratorBuild extends GeneratorBuild {
		public float warmup, totalProgress;

		@Override public void updateTile(){
			if (efficiency >= 0.9999f && power.status >= 0.99f) {
				warmup = Mathf.lerpDelta(warmup, 1f, warmupSpeed * timeScale);
				if (Mathf.equal(warmup, 1f, 0.001f)) {
					warmup = 1f;
				}
			} else {
				warmup = Mathf.lerpDelta(warmup, 0f, 0.01f);
			}

			totalProgress += warmup * Time.delta;

			productionEfficiency = Mathf.pow(warmup, 3f);
		}

		@Override public float warmup() {
			return warmup;
		}

		@Override public float totalProgress() {
			return totalProgress;
		}

		@Override public float ambientVolume() {
			return warmup;
		}

		@Override public double sense(LAccess sensor) {
			if(sensor == LAccess.heat) return warmup;
			return super.sense(sensor);
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