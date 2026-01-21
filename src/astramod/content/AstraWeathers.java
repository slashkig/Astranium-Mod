package astramod.content;

import arc.graphics.*;
import arc.math.*;
import arc.util.*;
import arc.util.noise.Noise;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.type.weather.*;
import mindustry.world.meta.*;
import java.io.*;
import astramod.math.Mathx;

import static mindustry.Vars.*;

public class AstraWeathers {
	public static final Attribute wind = Attribute.add("wind");
	public static Weather windy, heavyWind;

	public static WindLogic windManager;

	public static void load() {
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
			sound = Sounds.windhowl;
			soundVol = 0.8f;
		}};

		windManager = new WindLogic();
	}

	public static float globalWind() {
		return windManager.globalWind;
	}

	public static void updateWind() {
		if (state.isPlaying() && windManager.windEnabled()) {
			windManager.updateWind();
		}
	}

	public static float randWeatherLife(float base) {
		return (base + Time.toMinutes) * Mathf.random(1f, 1.5f);
	}

	public static class WindLogic {
		public float globalWind;
		public float randWind;
		public float tempWind;
		public float windCounter;

		public static final long windUpdateInterval = 1000;
		protected long lastNetUpdate;

		public float counterMin = 1f, counterMax = 4f;
		public float randMin = -0.5f, randMax = 1f;
		public float fadeDelay = 300f;

		/** Magnitude of variation noise. */
		public float windVarMag = 0.5f;
		/** Magnitude of lerp noise. */
		public float windLerpMag = 2f;
		/** Scale for wind conversion chance. */
		public float weatherChanceScl = 5f * Time.toSeconds;

		public final float windBase;
		public final float highWindBase;

		public WindLogic() {
			SaveVersion.addCustomChunk("astramod-wind", new SaveFileReader.CustomChunk() {
				@Override public void write(DataOutput stream) throws IOException {
					stream.writeFloat(globalWind);
					stream.writeFloat(randWind);
					stream.writeFloat(tempWind);
					stream.writeFloat(windCounter);
				}
				@Override public void read(DataInput stream) throws IOException {
					globalWind = stream.readFloat();
					randWind = stream.readFloat();
					tempWind = stream.readFloat();
					windCounter = stream.readFloat();
				}
				@Override public boolean shouldWrite() {
					return windEnabled();
				}
			});

			netClient.addPacketHandler("astramod-updateWind", data -> { if (!net.server()) globalWind = Float.parseFloat(data); });
			netClient.addPacketHandler("astramod-changeWind", data -> { if (!net.server()) {
				int sep = data.indexOf('|');
				randWind = Float.parseFloat(data.substring(0, sep));
				windCounter = Float.parseFloat(data.substring(sep + 1));
			}});
			netClient.addPacketHandler("astramod-deltaWind", data -> { if (!net.server()) {
				float deltaWind = Float.parseFloat(data);
				randWind -= deltaWind;
				tempWind += deltaWind;
			}});
			netClient.addPacketHandler("astramod-fadeWind", data -> { if (!net.server()) {
				WeatherState instance = Groups.weather.find(w -> w.weather.name.equals(data));
				if (instance != null) instance.life(fadeDelay);
				else Log.warn("Failed to fade weather: " + data);
			}});

			windBase = windy.attrs.get(wind);
			highWindBase = heavyWind.attrs.get(wind);
			lastNetUpdate = Time.millis();
		}

		public boolean windEnabled() {
			return AstraPlanets.windPlanets.contains(state.rules.planet);
		}

		public float envWind() {
			return wind.env() + tempWind;
		}

		public void updateWind() {
			float targetWind = Mathf.maxZero(envWind() + randWind + windVarMag * (float)Noise.rawNoise(Time.time / Time.toMinutes));
			globalWind = Mathf.equal(globalWind, targetWind) ? targetWind : Mathf.lerpDelta(
				globalWind, targetWind,
				(1f + Noise.snoise(windCounter, Time.time, Time.toMinutes, windLerpMag)) / Time.toMinutes
			);

			if (!net.client()) { // TODO possible weather desync
				if (globalWind > windBase && !windy.isActive() && envWind() < windBase && Mathf.chanceDelta((globalWind - windBase) / weatherChanceScl)) {
					// Calm -> Wind
					createWindWeather(windy, windCounter * 2f);
					deltaWind(windBase);
				} else if (globalWind < windBase && windy.isActive() && tempWind > -0.1f && Mathf.chanceDelta((windBase - globalWind) / weatherChanceScl)) {
					// Wind -> Calm
					fadeWindWeather(windy);
					deltaWind(-windBase);
				} else if (globalWind > highWindBase && windy.isActive() && !heavyWind.isActive() && envWind() <= Mathf.clamp(targetWind / 2f, windBase, randMax) && Mathf.chanceDelta((globalWind - highWindBase) / weatherChanceScl)) {
					// Wind -> High Wind
					createWindWeather(heavyWind, Math.max(windy.instance().life(), windCounter));
					fadeWindWeather(windy);
					deltaWind(highWindBase - windBase);
				} else if (globalWind < highWindBase && heavyWind.isActive() && !windy.isActive() && tempWind > -0.1f && Mathf.chanceDelta((highWindBase - globalWind) / weatherChanceScl)) {
					// High Wind -> Wind
					createWindWeather(windy, Math.max(heavyWind.instance().life(), windCounter) * 2f);
					fadeWindWeather(heavyWind);
					deltaWind(windBase - highWindBase);
				}

				if (windCounter <= 0) {
					changeWind();
				}
			}

			tempWind = Mathx.elerpDelta(tempWind, 0f, 0.004f);
			windCounter -= Time.delta;

			if (net.server() && Time.timeSinceMillis(lastNetUpdate) > windUpdateInterval) {
				lastNetUpdate = Time.millis();
				Call.clientPacketUnreliable("astramod-updateWind", String.valueOf(globalWind));
			}
		}

		public void changeWind() {
			randWind = Mathf.random(randMin, randMax);
			windCounter = Time.toMinutes * Mathf.random(counterMin, counterMax);
			Call.clientPacketReliable("astramod-changeWind", String.format("%f|%f", randWind, windCounter));
		}

		public void resetWind() {
			globalWind = 0f;
			randWind = 0f;
			tempWind = 0f;
			windCounter = 0f;
		}

		public void createWindWeather(Weather wind, float baseLife) {
			Tmp.v1.setToRandomDirection();
			Call.createWeather(wind, globalWind, randWeatherLife(baseLife), Tmp.v1.x, Tmp.v1.y);
		}

		public void fadeWindWeather(Weather wind) {
			wind.instance().life(fadeDelay);
			Call.clientPacketReliable("astramod-fadeWind", wind.name);
		}

		public void deltaWind(float value) {
			randWind -= value;
			tempWind += value;
			Call.clientPacketReliable("astramod-deltaWind", String.valueOf(value));
		}
	}
}