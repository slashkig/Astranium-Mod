package astramod.entities.bullet;

import mindustry.entities.bullet.*;
import mindustry.gen.*;

public class EnergyBulletType extends BasicBulletType {
	public EnergyBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);
		keepVelocity = false;
		hittable = false;
		pierce = pierceBuilding = true;
		shrinkY = 0f;
		trailChance = 0.2f;
		lightOpacity = 0.6f;
		hitSound = despawnSound = Sounds.explosion;
	}

	public EnergyBulletType(float speed, float damage) {
		this(speed, damage, "large-orb");
		width = height = 10f;
		hitSize = 6f;
	}

	@Override public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct) {
		super.hitTile(b, build, x, y, initialHealth, direct);
		if (!b.hit && build.team != b.team && direct && build.isInsulated()) {
			b.hit = true;
			b.remove();
		}
	}
}
