package astramod.content;

import arc.graphics.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;
public class AstraWeathers {
	public static final Attribute wind = Attribute.add("wind");
	public static Weather windy, heavyWind;

	public static void load() {
		Log.info("Loading weathers");

		Weathers.rain.attrs.set(wind, 0.2f);
		Weathers.snow.attrs.set(wind, 0.4f);
		Weathers.sandstorm.attrs.set(wind, 1f);
		Weathers.sandstorm.attrs.set(Attribute.light, -0.3f);
		Weathers.sporestorm.attrs.set(wind, 0.8f);

		windy = new Weather("windy") {{
			hidden = true;
			duration = 15f * Time.toMinutes;
			attrs.set(wind, 0.6f);
			sound = Sounds.wind;
			soundVol = 0.6f;
		}};

		heavyWind = new ParticleWeather("heavy-wind") {{
			hidden = true;
			color = noiseColor = Color.grays(0.7f);
			drawNoise = true;
			drawParticles = false;
			useWindVector = true;
			particleRegion = "";
			opacityMultiplier = 0.4f;
			noiseLayers = 2;
			noiseLayerSpeedM = 2.5f;
			force = 0.25f;
			attrs.set(wind, 1.2f);
			attrs.set(Attribute.light, -0.1f);
			sound = Sounds.windHowl;
			soundVol = 0.8f;
		}};
	}
}