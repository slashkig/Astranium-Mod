package astramod.entities.bullet;

import mindustry.Vars;
import mindustry.entities.bullet.*;

public class ImpactBulletType extends ArtilleryBulletType {
	public ImpactBulletType(float speed, float damage, String bulletSprite) {
		super(speed, 0, bulletSprite);
		ammoMultiplier = 1;
		knockback = 1f;
		splashDamageRadius = 1f * Vars.tilesize;
		splashDamage = damage;
		scaledSplashDamage = true;
		impact = true;
		collidesAir = true;
		collidesTiles = false;
	}

	public ImpactBulletType(float speed, float damage) {
		this(speed, damage, "shell");
		width = 12f;
		height = 15f;
	}
}