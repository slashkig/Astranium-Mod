package astramod.world.blocks.power;

import arc.math.Mathf;
import arc.util.Time;
import astramod.content.*;
import mindustry.world.blocks.power.*;

public class WindGenerator extends PowerGenerator {
	public float overloadDamage = 5f;

	public WindGenerator(String name) {
		super(name);
	}

	public class WindGeneratorBuild extends GeneratorBuild {
		public float totalProgress = 0f;

		@Override public void updateTile() {
			productionEfficiency = Mathf.lerp(productionEfficiency, enabled ? AstraWeathers.globalWind : 0f, 0.01f);
			if (productionEfficiency > 1.5f) {
				damageContinuous(overloadDamage * (productionEfficiency * 2f - 1f) * timeScale / 60f);
			}
			totalProgress += productionEfficiency * Time.delta;
		}

		@Override public float totalProgress() {
			return totalProgress;
		}
	}
}