package astramod.game;

import arc.Events;
import arc.math.Mathf;
import arc.util.*;
import arc.util.noise.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.io.*;
import mindustry.type.*;
import astramod.content.*;
import astramod.io.*;
import astramod.math.MathUtil;

import static mindustry.Vars.*;
import static astramod.content.AstraWeathers.*;

public class WindLogic {
	public float globalWind;
	public float randWind;
	public float tempWind;
	public float windCounter;

	public static final long windUpdateInterval = 1000;
	protected long lastNetUpdate;

	public float setupRandMin = 0.4f, setupRandMax = 0.6f, setupCounterMin = 5f, setupCounterMax = 8f;

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

		
		Events.run(Trigger.update, this::updateWind);
		Events.run(Trigger.newGame, this::setupWind);
	}

	public void setupWind() {
		if (windEnabled()) {
			Mathf.rand.setSeed(Time.millis());
			resetWind();
		}
	}

	public float randWeatherLife(float base) {
		return (base + Time.toMinutes) * Mathf.random(1f, 1.5f);
	}

	public boolean windEnabled() {
		return AstraPlanets.windPlanets.contains(state.rules.planet);
	}

	public float envWind() {
		return wind.env() + tempWind;
	}

	public void updateWind() {
		if (!state.isPlaying() || !windEnabled()) {
			return;
		}

		float targetWind = Mathf.maxZero(
			envWind() + randWind + windVarMag * (float)Noise.rawNoise(Time.time / Time.toMinutes)
		);
		globalWind = Mathf.equal(globalWind, targetWind) ? targetWind : Mathf.lerpDelta(
			globalWind, targetWind,
			(1f + Noise.snoise(windCounter, Time.time, Time.toMinutes, windLerpMag)) / Time.toMinutes
		);

		// TODO possible weather desync
		if (!net.client()) {
			if (globalWind > windBase
			&& !windy.isActive() && envWind() < windBase
			&& Mathf.chanceDelta((globalWind - windBase) / weatherChanceScl)) {
				// Calm -> Wind
				createWindWeather(windy, windCounter * 2f);
				deltaWind(windBase);
			} else if (globalWind < windBase
			&& windy.isActive() && tempWind > -0.1f
			&& Mathf.chanceDelta((windBase - globalWind) / weatherChanceScl)) {
				// Wind -> Calm
				fadeWindWeather(windy);
				deltaWind(-windBase);
			} else if (globalWind > highWindBase
			&& windy.isActive() && !heavyWind.isActive()
			&& envWind() <= Mathf.clamp(targetWind / 2f, windBase, randMax)
			&& Mathf.chanceDelta((globalWind - highWindBase) / weatherChanceScl)) {
				// Wind -> High Wind
				createWindWeather(heavyWind, Math.max(windy.instance().life(), windCounter));
				fadeWindWeather(windy);
				deltaWind(highWindBase - windBase);
			} else if (globalWind < highWindBase
			&& heavyWind.isActive() && !windy.isActive() && tempWind > -0.1f
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