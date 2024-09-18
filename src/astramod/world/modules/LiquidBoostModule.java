package astramod.world.modules;

import arc.struct.*;
import arc.util.io.*;
import mindustry.type.*;
import mindustry.world.modules.LiquidModule;

import static mindustry.Vars.*;

public class LiquidBoostModule extends LiquidModule {
	private ObjectFloatMap<Liquid> boostWeights;
	private Liquid currentBoost = content.liquid(0);

	public LiquidBoostModule(ObjectFloatMap<Liquid> weights) {
		super();
		boostWeights = weights;
	}

	public void setCurrent(Liquid liquid) {
		if (boostWeights.get(liquid, 0f) > boostWeights.get(currentBoost, 0f) || (get(currentBoost) < 0.1f && boostWeights.get(liquid, 0f) > 0f)) {
			currentBoost = liquid;
		}
	}

	@Override public void set(Liquid liquid, float amount) {
		super.set(liquid, amount);
		setCurrent(liquid);
	}

	@Override public Liquid current() {
		return currentBoost;
	}

	@Override public float currentAmount() {
		return get(currentBoost);
	}

	@Override public void add(Liquid liquid, float amount) {
		super.add(liquid, amount);
		setCurrent(liquid);
	}

	@Override public void reset(Liquid liquid, float amount) {
		super.reset(liquid, amount);
		setCurrent(liquid);
	}

	@Override public void read(Reads read, boolean legacy) {
		clear();
		int count = legacy ? read.ub() : read.s();

		for (int j = 0; j < count; j++) {
			Liquid liq = content.liquid(legacy ? read.ub() : read.s());
			float amount = read.f();
			if (liq != null) {
				set(liq, amount);
			}
		}
	}
}