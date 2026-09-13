package astramod.entities.bullet;

import mindustry.content.Fx;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class MeleeBulletType extends BulletType {
	public MeleeBulletType(float damage) {
		super(0f, damage);
		lifetime = 2f;
		collidesAir = false;
		hittable = reflectable = absorbable = false;
		keepVelocity = false;
		shootEffect = smokeEffect = despawnEffect = Fx.none;
		hitEffect = Fx.pulverize;
		hitSound = Sounds.blockExplodeWall;
		hitSoundVolume = 0.8f;
	}
}