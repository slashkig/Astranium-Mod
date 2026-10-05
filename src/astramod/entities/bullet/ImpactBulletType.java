package astramod.entities.bullet;

import mindustry.Vars;
import mindustry.entities.bullet.*;

public class ImpactBulletType extends ArtilleryBulletType {
	public ImpactBulletType(float speed, float damage, String bulletSprite) {
		super(speed, 0, bulletSprite);
		ammoMultiplier = 1;
		knockback = 3f;
		splashDamageRadius = 1f * Vars.tilesize;
		splashDamage = damage;
		scaledSplashDamage = true;
		impact = true;
		collidesAir = true;
		collidesTiles = false;
	}

	public ImpactBulletType(float speed, float damage) {
		this(speed, damage, "astramod-impact");
		width = 14f;
		height = 18f;
	}
}