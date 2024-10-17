package astramod.world.modules;

import arc.util.*;
import arc.util.io.*;
import mindustry.type.*;
import mindustry.world.modules.*;

public class DirectionalLiquidModule extends LiquidModule {
	public final LiquidModule hModule = new LiquidModule();
	public final LiquidModule vModule = new LiquidModule();

	/** 0 for horizontal, 1 for vertical */
	private int axis;
	private boolean axisSet = false;

	public void setAxis(int dir) {
		axis = dir % 2;
		axisSet = true;
	}

	public int getAxis() {
		return axis;
	};

	public void resetAxis() {
		axisSet = false;
	}

	@Nullable public LiquidModule getModule() {
		return axis == 0 ? hModule : vModule;
	}

	@Override public void updateFlow() {
		if (axisSet) {
			getModule().updateFlow();
		} else {
			hModule.updateFlow();
			vModule.updateFlow();
		}
	}

	@Override public void stopFlow() {
		if (axisSet) {
			getModule().stopFlow();
		} else {
			hModule.stopFlow();
			vModule.stopFlow();
		}
	}

	@Override public float getFlowRate(Liquid liquid) {
		if (axisSet) {
			return getModule().getFlowRate(liquid);
		} else {
			float out = Math.max(hModule.getFlowRate(liquid), 0f) + Math.max(vModule.getFlowRate(liquid), 0f);
			return out == 0f ? -1f : out;
		}
	}

	@Override public boolean hasFlowLiquid(Liquid liquid) {
		return axisSet ? getModule().hasFlowLiquid(liquid) : (hModule.hasFlowLiquid(liquid) || vModule.hasFlowLiquid(liquid));
	}

	@Override public Liquid current() {
		return getModule().current();
	}

	@Override public void reset(Liquid liquid, float amount) {
		if (axisSet) {
			getModule().reset(liquid, amount);
		} else {
			hModule.reset(liquid, amount / 2f);
			vModule.reset(liquid, amount / 2f);
		}
	}

	@Override public void set(Liquid liquid, float amount) {
		if (axisSet) {
			getModule().set(liquid, amount);
		} else {
			hModule.set(liquid, amount / 2f);
			vModule.set(liquid, amount / 2f);
		}
	}

	@Override public float currentAmount() {
		return axisSet ? getModule().currentAmount() : (hModule.currentAmount() + vModule.currentAmount());
	}

	@Override public float get(Liquid liquid) {
		return axisSet ? getModule().get(liquid) : (hModule.get(liquid) + vModule.get(liquid));
	}

	@Override public void clear() {
		if (axisSet) {
			getModule().clear();
		} else {
			hModule.clear();
			vModule.clear();
		}
	}

	@Override public void add(Liquid liquid, float amount) {
		if (axisSet) {
			getModule().add(liquid, amount);
		} else {
			hModule.add(liquid, amount / 2f);
			vModule.add(liquid, amount / 2f);
		}
	}

	@Override public void handleFlow(Liquid liquid, float amount) {
		if (!axisSet) throw new IllegalStateException("Junction direction is not set!");
		getModule().handleFlow(liquid, amount);
	}

	@Override public void remove(Liquid liquid, float amount) {
		if (axisSet) {
			getModule().remove(liquid, amount);
		} else {
			hModule.remove(liquid, amount / 2f);
			vModule.remove(liquid, amount / 2f);
		}
	}

	@Override public void each(LiquidConsumer cons) {
		if (axisSet) {
			getModule().each(cons);
		} else {
			hModule.each(cons);
			vModule.each(cons);
		}
	}

	@Override public float sum(LiquidCalculator calc) {
		return axisSet ? getModule().sum(calc) : (hModule.sum(calc) + vModule.sum(calc));
	}

	@Override public void write(Writes write) {
		hModule.write(write);
		vModule.write(write);
	}

	@Override public void read(Reads read, boolean legacy) {
		hModule.read(read, legacy);
		vModule.read(read, legacy);
	}
}