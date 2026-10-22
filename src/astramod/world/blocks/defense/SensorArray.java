package astramod.world.blocks.defense;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;
import astramod.graphics.DrawUtil;

public class SensorArray extends Radar {
	public static final ObjectMap<SensorBuild, Unit> warnings = new ObjectMap<>();
	public static float warningSize = 8f;
	public static float warnLerpSpeed = 0.09f;
	public static float warnPulseAlpha = 0.8f;
	public static float warnPulseInterval = 1.5f * Time.toSeconds;
	public static float warnPulseMag = 0.25f;

	public final int timerSense = timers++;
	public float senseInterval = 30f;

	public SensorArray(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(Stat.range, fogRadius, StatUnit.blocks);
	}

	public static void drawWarnings() {
		if (Core.settings.getBool("indicators")) {
			Draw.z(Layer.overlayUI);
			Draw.color(Color.red);
			Draw.alpha(warnPulseAlpha + Mathf.sin(warnPulseInterval / (2f * Mathf.PI), warnPulseMag) - warnPulseMag);
			Lines.stroke(0.4f);
			Tmp.r1.setSize(Core.camera.width * 0.9f, Core.camera.height * 0.9f).setCenter(Core.camera.position.x, Core.camera.position.y);

			for (var iterator = warnings.iterator(); iterator.hasNext();) {
				var warning = iterator.next();
				SensorBuild build = warning.key;
				if (!build.isValid() || build.team != Vars.player.team() || Units.invalidateTarget(warning.value, build.team, build.x, build.y, build.fogRadius() * Vars.tilesize)) {
					iterator.remove();
				} else {
					if (Tmp.r1.contains(build.x, build.y)) {
						if (Mathf.equal(build.warnLerp, 1f)) {
							drawWarning(build, warning.value);
							continue;
						}
						build.warnLerp = Mathf.lerpDelta(build.warnLerp, 1f, warnLerpSpeed);
					} else {
						if (Mathf.equal(build.warnLerp, 0f)) {
							drawWarning(Vars.player.unit(), warning.value);
							continue;
						}
						build.warnLerp = Mathf.lerpDelta(build.warnLerp, 0f, warnLerpSpeed);
					}
					drawWarning(Vars.player.unit(), build, warning.value);
				}
			}

			Draw.reset();
		}
	}

	public static void drawWarning(Sized pos, Unit enemy) {
		drawWarning(pos, null, enemy);
	}

	public static void drawWarning(Sized pos, @Nullable SensorBuild sensor, Unit enemy) {
		if (pos != null) {
			float angle = pos.angleTo(enemy);

			Tmp.v1.trns(angle, pos.hitSize() / 2f + warningSize * 1.5f).add(pos);
			if (sensor != null) {
				Tmp.v2.trns(sensor.angleTo(enemy), sensor.hitSize() / 2f + warningSize * 1.5f).add(sensor);
				Tmp.v1.lerp(Tmp.v2, sensor.warnLerp);
				angle = Tmp.v1.angleTo(enemy);
			}
			Draw.rect(Icon.warning.getRegion(), Tmp.v1.x, Tmp.v1.y, warningSize, warningSize);

			Tmp.v2.trns(angle, warningSize).add(Tmp.v1);
			DrawUtil.chevron(Tmp.v2, warningSize * 0.75f, warningSize * 0.5f, angle - 90f);
		}
	}

	public class SensorBuild extends RadarBuild {
		public Unit detected;
		public float warnLerp = 0f;

		@Override public void updateTile() {
			super.updateTile();

			if (timer(timerSense, senseInterval)) {
				detected = Units.closestEnemy(team, x, y, fogRadius() * Vars.tilesize, this::detectable);

				if (team == Vars.player.team()) {
					warnings.put(this, detected);
				}
			}
		}

		public boolean detectable(Unit unit) {
			return unit.isEnemy() && !unit.isLocal() && !unit.inFogTo(team);
		}

		@Override public void onRemoved() {
			warnings.remove(this);
		}
	}
}