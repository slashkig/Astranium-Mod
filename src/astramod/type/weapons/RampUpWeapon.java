package astramod.type.weapons;

import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.Table;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class RampUpWeapon extends Weapon {
	/** Maximum fraction of reload time that can be reduced by ramp-up. */
	public float rampupFactor = 0f;
	/** Maximum fraction of speed that is reduced by ramp-up. */
	public float slowdownFactor = -1f;

	public RampUpWeapon(String name) {
		super(name);
		linearWarmup = true;
		continuous = false;
	}

	@Override public void init() {
		super.init();
		if (slowdownFactor == -1f) slowdownFactor = rampupFactor;
	}

	@Override public void addStats(UnitType u, Table t) {
		if (inaccuracy > 0f) {
			AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @", Stat.inaccuracy.localized(), (int)inaccuracy, StatUnit.degrees.localized());
		}
		AstraStatValues.addRowString(t, "[lightgray]@: @[white]@ - @ @",
			Stat.reload.localized(),
			mirror ? "2x " : "",
			Strings.autoFixed(shoot.shots * 60f / reload, 2),
			Strings.autoFixed(shoot.shots * 60f / (reload * (1f - rampupFactor)), 2),
			StatUnit.perSecond.localized()
		);
		AstraStatValues.addRowString(t, "[lightgray]@: [white]@ @",
			AstraStat.rampupTime.localized(),
			Strings.autoFixed(1f / (Time.toSeconds * shootWarmupSpeed), 2),
			StatUnit.seconds.localized()
		);

		StatValues.ammo(ObjectMap.of(u, bullet)).display(t);
	}

	@Override public void update(Unit unit, WeaponMount mount) {
		boolean canShoot = unit.canShoot();
		float lastReload = mount.reload;
		mount.reload = Math.max(mount.reload - Time.delta * unit.reloadMultiplier / (1f - rampupFactor * mount.warmup), 0f);
		mount.recoil = Mathf.approachDelta(mount.recoil, 0f, unit.reloadMultiplier / recoilTime);
		unit.applyDynamicStatus().speedMultiplier = 1f - (slowdownFactor * mount.warmup);

		if (recoils > 0) {
			if (mount.recoils == null) mount.recoils = new float[recoils];
			for (int i = 0; i < recoils; i++) {
				mount.recoils[i] = Mathf.approachDelta(mount.recoils[i], 0f, unit.reloadMultiplier / recoilTime);
			}
		}
		mount.smoothReload = Mathf.lerpDelta(mount.smoothReload, mount.reload / reload, smoothReloadSpeed);
		mount.charge = mount.charging && shoot.firstShotDelay > 0f ? Mathf.approachDelta(mount.charge, 1f, 1f / shoot.firstShotDelay) : 0f;

		float warmupTarget = (canShoot && mount.shoot) || (continuous && mount.bullet != null) || mount.charging ? 1f : 0f;
		mount.warmup = linearWarmup ? Mathf.approachDelta(mount.warmup, warmupTarget, shootWarmupSpeed) :
			Mathf.lerpDelta(mount.warmup, warmupTarget, shootWarmupSpeed);

		float mountX = unit.x + Angles.trnsx(unit.rotation - 90f, x, y), mountY = unit.y + Angles.trnsy(unit.rotation - 90f, x, y);

		// Find a new target
		if (!controllable && autoTarget) {
			if ((mount.retarget -= Time.delta) <= 0f) {
				mount.target = findTarget(unit, mountX, mountY, bullet.range, bullet.collidesAir, bullet.collidesGround);
				mount.retarget = mount.target == null ? targetInterval : targetSwitchInterval;
			}

			if (mount.target != null && checkTarget(unit, mount.target, mountX, mountY, bullet.range)) {
				mount.target = null;
			}

			boolean shoot = false;

			if (mount.target != null) {
				shoot = mount.target.within(mountX, mountY, bullet.range + Math.abs(shootY) + (mount.target instanceof Sized s ? s.hitSize() / 2f : 0f)) && canShoot;

				if (predictTarget) {
					Vec2 to = Predict.intercept(unit, mount.target, bullet);
					mount.aimX = to.x;
					mount.aimY = to.y;
				} else {
					mount.aimX = mount.target.x();
					mount.aimY = mount.target.y();
				}
			}

			mount.shoot = mount.rotate = shoot;
		}

		// Rotate if applicable
		if (rotate && (mount.rotate || mount.shoot) && canShoot) {
			float axisX = unit.x + Angles.trnsx(unit.rotation - 90f, x, y), axisY = unit.y + Angles.trnsy(unit.rotation - 90f, x, y);

			mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY) - unit.rotation;
			mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, rotateSpeed * Time.delta);
			if (rotationLimit < 360f) {
				float dst = Angles.angleDist(mount.rotation, baseRotation);
				if (dst > rotationLimit / 2f) {
					mount.rotation = Angles.moveToward(mount.rotation, baseRotation, dst - rotationLimit / 2f);
				}
			}
		} else if (!rotate) {
			mount.rotation = baseRotation;
			mount.targetRotation = unit.angleTo(mount.aimX, mount.aimY);
		}

		float weaponRotation = unit.rotation - 90f + (rotate ? mount.rotation : baseRotation),
			bulletX = mountX + Angles.trnsx(weaponRotation, this.shootX, this.shootY),
			bulletY = mountY + Angles.trnsy(weaponRotation, this.shootX, this.shootY),
			shootAngle = bulletRotation(unit, mount, bulletX, bulletY);

		if (alwaysShooting) mount.shoot = true;

		// Heat decreases when not firing
		mount.heat = Math.max(mount.heat - Time.delta * unit.reloadMultiplier / cooldownTime, 0f);

		if (mount.sound != null) {
			mount.sound.update(bulletX, bulletY, false);
		}

		// Flip weapon shoot side for alternating weapons
		boolean wasFlipped = mount.side;
		if (otherSide >= 0 && alternate && mount.side == flipSprite && otherSide < unit.mounts.length && mount.reload <= reload / 2f && lastReload > reload / 2f) {
			unit.mounts[otherSide].side = !unit.mounts[otherSide].side;
			mount.side = !mount.side;
		}

		if (!Vars.headless && activeSound != Sounds.none && mount.shoot && canShoot && mount.warmup >= minWarmup) {
			Vars.control.sound.loop(activeSound, unit, activeSoundVolume);
		}

		float velLen = unit.isRemote() ? unit.vel.len() : unit.deltaLen() / Time.delta;

		// Shoot if applicable
		if (mount.shoot && canShoot && (!alternate || wasFlipped == flipSprite) && mount.warmup >= minWarmup &&
		velLen >= minShootVelocity && mount.reload <= 0.0001f &&
		(alwaysShooting || Angles.within(rotate ? mount.rotation : unit.rotation + baseRotation, mount.targetRotation, shootCone))) {
			shoot(unit, mount, bulletX, bulletY, shootAngle);
			mount.reload = reload;
		}
	}

	@Override public float dps() {
		return bullet.estimateDPS() * shotsPerSec();
	}

	@Override public float shotsPerSec() {
		return super.shotsPerSec() / (1f - rampupFactor * 0.5f);
	}
}