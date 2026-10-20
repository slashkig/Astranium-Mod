package astramod.game;

import arc.Events;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.type.*;
import astramod.io.*;
import astramod.math.MathUtil;

import static mindustry.Vars.*;
import static astramod.content.AstraWeathers.*;

public class WindLogic {
	public static final float defaultSetupRandMin = 0.4f, defaultSetupRandMax = 0.6f, defaultSetupCounterMin = 5f, defaultSetupCounterMax = 8f;
	public static final float defaultRandMin = -0.5f, defaultRandMax = 1f, defaultCounterMin = 1f, defaultCounterMax = 4f;
	public static final float rulesRandMin = -2f, rulesRandMax = 10f;

	public float setupRandMin, setupRandMax, setupCounterMin, setupCounterMax;
	public float randMin, randMax, counterMin, counterMax;

	/** How much total wind there is right now. */
	public float globalWind;
	/** Target random wind value. */
	public float randWind;
	/** Wind modifier to smooth weather changes; decays towards 0. */
	public float tempWind;
	/** How many frames there are left until wind is rerolled. */
	public float windCounter;
	/** The lifetime wind weathers are set to if they are ended early. */
	public float fadeDelay = 300f;

	public static final long windUpdateInterval = 1000;
	protected long lastNetUpdate;

	/** Magnitude of variation noise. */
	public float windVarMag = 0.5f;
	/** Magnitude of lerp noise. */
	public float windLerpMag = 2f;
	/** Scale for wind conversion chance. */
	public float weatherChanceScl = 5f * Time.toSeconds;

	public final float windBase, highWindBase;

	public WindLogic() {
		SaveVersion.addCustomChunk("astramod-wind", new CustomChunk(
			in -> {
				globalWind = in.readFloat();
				randWind = in.readFloat();
				tempWind = in.readFloat();
				windCounter = in.readFloat();
			},
			out -> {
				out.writeFloat(globalWind);
				out.writeFloat(randWind);
				out.writeFloat(tempWind);
				out.writeFloat(windCounter);
			},
			this::windEnabled
		));

		windBase = windy.attrs.get(wind);
		highWindBase = heavyWind.attrs.get(wind);
		lastNetUpdate = Time.millis();

		setupRandMin = defaultSetupRandMin;
		setupRandMax = defaultSetupRandMax;
		setupCounterMin = defaultSetupCounterMin;
		setupCounterMax = defaultSetupCounterMax;
		randMin = defaultRandMin;
		randMax = defaultRandMax;
		counterMin = defaultCounterMin;
		counterMax = defaultCounterMax;

		Events.run(Trigger.newGame, this::setupWind);
		Events.run(Trigger.update, this::updateWind);
	}

	public void setupWind() {
		if (windEnabled()) {
			StringMap tags = Vars.state.rules.tags;
			setupRandMin = tags.getFloat("astramod-wind-setupRand-low", defaultSetupRandMin * 100f) / 100f;
			setupRandMax = tags.getFloat("astramod-wind-setupRand-high", defaultSetupRandMax * 100f) / 100f;
			setupCounterMin = tags.getFloat("astramod-wind-setupCounter-low", defaultSetupCounterMin);
			setupCounterMax = tags.getFloat("astramod-wind-setupCounter-high", defaultSetupCounterMax);
			randMin = tags.getFloat("astramod-wind-rand-low", defaultRandMin * 100f) / 100f;
			randMax = tags.getFloat("astramod-wind-rand-high", defaultRandMax * 100f) / 100f;
			counterMin = tags.getFloat("astramod-wind-counter-low", defaultCounterMin);
			counterMax = tags.getFloat("astramod-wind-counter-high", defaultCounterMax);

			Mathf.rand.setSeed(Time.millis());
			resetWind();
		}
	}

	public float randWeatherLife(float base) {
		return (base + Time.toMinutes) * Mathf.random(1f, 1.5f);
	}

	public boolean windEnabled() {
		return Vars.state.rules.tags.getBool("astramod-wind-enabled");
	}

	public float envWind() {
		return wind.env() + tempWind;
	}

	public void updateWind() {
		if (!state.isPlaying() || !windEnabled()) return;

		float targetWind = Mathf.maxZero(envWind() + randWind + windVarMag * (float)Noise.rawNoise(Time.time / Time.toMinutes));
		globalWind = Mathf.equal(globalWind, targetWind) ? targetWind : Mathf.lerpDelta(globalWind, targetWind,
			(1f + Noise.snoise(windCounter, Time.time, Time.toMinutes, windLerpMag)) / Time.toMinutes);

		// TODO possible weather desync
		if (!net.client()) {
			if (globalWind > windBase && !windy.isActive() && envWind() < windBase
			&& Mathf.chanceDelta((globalWind - windBase) / weatherChanceScl)) {
				// Calm -> Wind
				createWindWeather(windy, windCounter * 2f);
				deltaWind(windBase);
			} else if (globalWind < windBase && windy.isActive() && tempWind > -0.1f
			&& Mathf.chanceDelta((windBase - globalWind) / weatherChanceScl)) {
				// Wind -> Calm
				fadeWindWeather(windy);
				deltaWind(-windBase);
			} else if (globalWind > highWindBase && windy.isActive() && !heavyWind.isActive()
			&& envWind() <= Mathf.clamp(targetWind / 2f, windBase, randMax)
			&& Mathf.chanceDelta((globalWind - highWindBase) / weatherChanceScl)) {
				// Wind -> High Wind
				createWindWeather(heavyWind, Math.max(windy.instance().life(), windCounter));
				fadeWindWeather(windy);
				deltaWind(highWindBase - windBase);
			} else if (globalWind < highWindBase && heavyWind.isActive() && !windy.isActive() && tempWind > -0.1f
			&& Mathf.chanceDelta((highWindBase - globalWind) / weatherChanceScl)) {
				// High Wind -> Wind
				createWindWeather(windy, Math.max(heavyWind.instance().life(), windCounter) * 2f);
				fadeWindWeather(heavyWind);
				deltaWind(windBase - highWindBase);
			}

			if (windCounter <= 0) {
				changeWind();
			}
		}

		tempWind = MathUtil.elerpDelta(tempWind, 0f, 0.004f);
		windCounter -= Time.delta;

		if (net.server() && Time.timeSinceMillis(lastNetUpdate) > windUpdateInterval) {
			lastNetUpdate = Time.millis();
			NetUtil.clientFloatUnreliable("wind-update", globalWind);
		}
	}

	public void changeWind() {
		changeWind(randMin, randMax, counterMin, counterMax);
	}

	public void changeWind(float rMin, float rMax, float cMin, float cMax) {
		randWind = Mathf.random(rMin, rMax);
		windCounter = Time.toMinutes * Mathf.random(cMin, cMax);
		NetUtil.clientFloatsReliable("wind-change", randWind, windCounter);
	}

	public void resetWind() {
		tempWind = 0f;
		changeWind(setupRandMin, setupRandMax, setupCounterMin, setupCounterMax);
		globalWind = randWind;
	}

	public void createWindWeather(Weather wind, float baseLife) {
		Tmp.v1.setToRandomDirection();
		Call.createWeather(wind, globalWind, randWeatherLife(baseLife), Tmp.v1.x, Tmp.v1.y);
	}

	public void fadeWindWeather(Weather wind) {
		wind.instance().life(fadeDelay);
		Call.clientPacketReliable("astramod-wind-fade", wind.name);
	}

	public void deltaWind(float value) {
		randWind -= value;
		tempWind += value;
		NetUtil.clientFloatReliable("wind-delta", value);
	}
}