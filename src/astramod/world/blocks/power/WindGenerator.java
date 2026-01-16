package astramod.world.blocks.power;

import astramod.content.*;
import mindustry.world.blocks.power.*;
import astramod.math.Mathx;

public class WindGenerator extends PowerGenerator {
	public float overloadDamage = 5f;

	public WindGenerator(String name) {
		super(name);
	}

	public AstraWeathers.WindLogic windManager() {
		return AstraWeathers.windManager;
	}

	public class WindGeneratorBuild extends GeneratorBuild {
		public float totalProgress = 0f;

		@Override public void updateTile() {
			productionEfficiency = Mathx.elerpDelta(productionEfficiency, enabled ? AstraWeathers.globalWind() : 0f, 0.01f * timeScale);
			if (productionEfficiency > 1.5f) {
				damageContinuous(overloadDamage * (productionEfficiency * 2f - 1f) * timeScale / 60f);
			}
			totalProgress += productionEfficiency * delta();
		}

		@Override public float totalProgress() {
			return totalProgress;
		}
	}
}