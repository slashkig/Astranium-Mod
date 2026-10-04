package astramod.entities.bullet;

import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.content.*;
import astramod.entities.UnitUtil;

public class MagneticBulletType extends ExplosionBulletType {
	public float magneticStrength;
	public float magnetizedDuration;

	public MagneticBulletType(float splashDamage, float splashDamageRadius) {
		super(splashDamage, splashDamageRadius);
	}

	@Override public void createSplashDamage(Bullet b, float x, float y) {
		super.createSplashDamage(b, x, y);

		Units.nearbyEnemies(b.team, x, y, splashDamageRadius, u -> UnitUtil.attract(u, b, magneticStrength, splashDamageRadius));
		if (magnetizedDuration > 0f) {
			Damage.status(b.team, x, y, splashDamageRadius, AstraStatusEffects.magnetized, magnetizedDuration, collidesAir, collidesGround, statusChance);
		}
	}
}