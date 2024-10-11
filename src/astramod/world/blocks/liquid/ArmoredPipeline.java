package astramod.world.blocks.liquid;

import arc.graphics.*;
import arc.graphics.g2d.*;
import astramod.world.meta.AstraStat;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.StatUnit;

public class ArmoredPipeline extends ArmoredConduit {
	public ArmoredPipeline(String name) {
		super(name);
		botColor = Color.white;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.liquidPressure, liquidPressure * 100, StatUnit.percent);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] { bottomRegion, topRegions[0] };
	}
}