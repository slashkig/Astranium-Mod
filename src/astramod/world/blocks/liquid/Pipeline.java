package astramod.world.blocks.liquid;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import astramod.world.meta.AstraStat;
import mindustry.content.Fx;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.StatUnit;

public class Pipeline extends Conduit {
	public float heatCapacity = 0.5f;

	public Pipeline(String name) {
		super(name);
		botColor = Color.white;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.liquidPressure, liquidPressure * 100, StatUnit.percent);
		stats.add(AstraStat.heatCapacity, heatCapacity * 100, StatUnit.percent);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] {bottomRegion, topRegions[0]};
	}

	public class PipelineBuild extends ConduitBuild {
		@Override public void update() {
			super.update();

			if (liquids.currentAmount() > 0.1f && liquids.current().temperature > heatCapacity) {
				float strength = liquids.currentAmount() * liquids.current().temperature / heatCapacity;
				damageContinuous(strength / 60f);
				if (Mathf.chanceDelta(strength / 100f)) Fx.fire.at(x, y);
			}
		}
	}
}