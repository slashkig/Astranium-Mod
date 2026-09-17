package astramod.entities.bullet;

import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class EnergyBulletType extends BasicBulletType {
	/** Effect created on the first hit the bullet makes. */
	public Effect firstHitEffect = Fx.none;
	/** If true, despawnEffect is also created when removed() is called. */
	public boolean despawnOnRemove = false;

	public EnergyBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);
		keepVelocity = false;
		hittable = false;
		pierce = pierceBuilding = true;
		laserBullet = true;
		shrinkY = 0f;
		lightOpacity = 0.6f;
		hitSound = despawnSound = Sounds.explosion;
	}

	public EnergyBulletType(float speed, float damage) {
		this(speed, damage, "large-orb");
		width = height = 10f;
		hitSize = 6f;
	}

	@Override public void hit(Bullet b, float x, float y, boolean createFrags) {
		if (b.collided.size == 0) firstHitEffect.at(x, y, b.rotation(), hitColor);
		super.hit(b, x, y, createFrags);
	}

	@Override public void removed(Bullet b) {
		if (despawnOnRemove) despawnEffect.at(b.x, b.y, b.rotation(), hitColor);
		super.removed(b);
	}
}