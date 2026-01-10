package astramod.content;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class AstraWeathers {
	public static final Attribute wind = Attribute.add("wind");
	public static Weather windy, heavyWind;

	public static float globalWind;
	public static float randWind;
	protected static float windCounter;

	public static void load() {
		Weathers.rain.attrs.set(wind, 0.2f);
		Weathers.snow.attrs.set(wind, 0.5f);
		Weathers.sandstorm.attrs.set(wind, 1f);
		Weathers.sporestorm.attrs.set(wind, 1f);

		windy = new Weather("windy") {{
			duration = 15f * Time.toMinutes;
			sound = Sounds.windhowl;
			attrs.set(wind, 0.6f);
		}};

		heavyWind = new ParticleWeather("heavy-wind") {{
			force = 0.2f;
			sound = Sounds.wind;
			color = Color.grays(0.8f);
			attrs.set(wind, 1.2f);
			attrs.set(Attribute.light, -0.1f);
		}};
	}

	public static void resetWind() {
		globalWind = wind.env();
		changeWind();
	}

	public static void updateWind() {
		if (state.isPlaying()) {
			float target = (wind.env() + randWind) * Mathf.random(0.9f, 1.1f);
			globalWind = Mathf.maxZero(globalWind + (target - globalWind) / Time.toMinutes);
			windCounter -= Time.delta;
			if (windCounter <= 0) {
				changeWind();
			}
		}
	}

	public static void changeWind() {
		randWind = Mathf.random(-0.5f, 1f);
		windCounter = Time.toMinutes * Mathf.random(1f, 4f);
	}
}