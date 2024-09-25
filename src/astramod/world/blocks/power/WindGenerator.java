package astramod.world.blocks.power;

import mindustry.world.blocks.power.*;

public class WindGenerator extends PowerGenerator {
	public WindGenerator(String name) {
		super(name);
	}

	public class WindGeneratorBuild extends GeneratorBuild{
		@Override public void updateTile() {
			productionEfficiency = 1f;
		}
	}
}