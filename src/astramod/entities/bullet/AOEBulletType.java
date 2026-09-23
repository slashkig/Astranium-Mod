package astramod.entities.bullet;

import arc.func.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.Team;
import mindustry.gen.*;

public class AOEBulletType extends BulletType {
	@Nullable public Cons<Unit> effect;
	public float effectInterval = 10f;

	public AOEBulletType(float duration) {
		lifetime = duration;
		speed = damage = 0f;
		hittable = reflectable = absorbable = false;
		scaledSplashDamage = true;
		collides = false;
		keepVelocity = false;
		shootEffect = smokeEffect = despawnEffect = Fx.none;
		setDefaults = false;
	}

	@Override public Bullet create(Entityc owner, Entityc shooter, Team team, float x, float y, float angle, float damage,
	float velocityScl, float lifetimeScl, Object data, Mover mover, float aimX, float aimY, Teamc target) {
		Bullet b = super.create(owner, shooter, team, x, y, angle, damage, velocityScl, lifetimeScl, data, mover, aimX, aimY, target);
		if (b != null) shootEffect.at(x, y, angle, hitColor);
		return b;
	}

	@Override public void update(Bullet b) {
		super.update(b);

		if (b.timer.get(3, effectInterval)) {
			if (effect == null) Damage.damage(b.team, b.x, b.y, splashDamageRadius, b.damage);

			if (collidesTeam) {
				Units.nearby(b.team, b.x, b.y, splashDamageRadius, effect);
			} else {
				Units.nearbyEnemies(b.team, b.x, b.y, splashDamageRadius, effect);
			}
		}
	}
}