package astramod.world.blocks.liquid;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import astramod.world.meta.AstraStat;
import mindustry.content.Fx;
import mindustry.world.blocks.liquid.*;

public class ArmoredPipeline extends ArmoredConduit {
	public float heatCapacity = 0.5f;

	public ArmoredPipeline(String name) {
		super(name);
		botColor = Color.white;
	}

	@Override public void setStats() {
		super.setStats();
		stats.addPercent(AstraStat.liquidPressure, liquidPressure);
		stats.addPercent(AstraStat.heatCapacity, heatCapacity);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] {bottomRegion, topRegions[0]};
	}

	public class ArmoredPipelineBuild extends ArmoredConduitBuild {
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