package astramod.world.blocks.defense;

import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;

public class SensorArray extends Radar {
	public SensorArray(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(Stat.range, fogRadius, StatUnit.blocks);
	}
}
