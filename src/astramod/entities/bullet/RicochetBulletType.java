package astramod.entities.bullet;

import mindustry.entities.bullet.*;
import mindustry.gen.Bullet;
import astramod.content.AstraFx;

public class RicochetBulletType extends BasicBulletType {
	public RicochetBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);
		shrinkY = 0f;
		fragBullets = 1;
		fragVelocityMin = 0.6f;
		delayFrags = true;
	}

	public RicochetBulletType(float speed, float damage) {
		this(speed, damage, "astramod-ricochet");
		despawnEffect = AstraFx.starFlare(this, 0.7f);
	}

	public RicochetBulletType(RicochetBulletType parent) {
		super(parent.speed, parent.damage, parent.sprite);
		width = parent.width;
		height = parent.height;
		shrinkY = 0f;
		fragBullets = 0;
		delayFrags = true;
		despawnEffect = hitEffect = AstraFx.hitColor(this);
	}

	@Override public Bullet create(Bullet parent, float x, float y, float angle) {
		return create(parent, x, y, angle, 1f);
	}

	@Override public Bullet create(Bullet parent, float x, float y, float angle, float velocityScl) {
		return create(parent, x, y, angle, velocityScl, 1f);
	}

	@Override public Bullet create(Bullet parent, float x, float y, float angle, float velocityScl, float lifeScale) {
		if (parent != null && parent.type instanceof RicochetBulletType && parent.collided.size > 0) {
			Bullet b = super.create(parent, x, y, angle, velocityScl, lifeScale);
			if (b != null) b.collided.add(parent.collided.get(parent.collided.size - 1));
			return b;
		} else return super.create(parent, x, y, angle, velocityScl, lifeScale);
	}

	@Override public void createFrags(Bullet b, float x, float y) {
		if (fragOnHit || !b.hit) super.createFrags(b, x, y);
	}
}