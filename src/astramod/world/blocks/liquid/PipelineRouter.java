package astramod.world.blocks.liquid;

import mindustry.content.*;
import mindustry.world.blocks.liquid.*;
import arc.math.*;
import astramod.world.meta.*;

public class PipelineRouter extends LiquidRouter {
	public float heatCapacity = 0.5f;

	public PipelineRouter(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.addPercent(AstraStat.heatCapacity, heatCapacity);
	}

	public class PipelineRouterBuild extends LiquidRouterBuild {
		@Override public void update() {
			super.update();

			if (liquids.currentAmount() > 0.1f && liquids.current().temperature > heatCapacity) {
				float strength = liquids.currentAmount() / (size * size) * liquids.current().temperature / heatCapacity;
				damageContinuous(strength / 60f);
				if (Mathf.chanceDelta(strength / (100f * size * size))) Fx.fire.at(x + Mathf.random(size - 1), y + Mathf.random(size - 1));
			}
		}
	}
}