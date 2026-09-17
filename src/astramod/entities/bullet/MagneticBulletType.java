package astramod.entities.bullet;

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

	@Override public void hitEntity(Bullet b, Hitboxc entity, float health) {
		super.hitEntity(b, entity, health);

		if (entity instanceof Unit unit) {
			UnitUtil.attract(unit, b, magneticStrength, splashDamageRadius);
			unit.apply(AstraStatusEffects.magnetized, magnetizedDuration);
		}
	}
}