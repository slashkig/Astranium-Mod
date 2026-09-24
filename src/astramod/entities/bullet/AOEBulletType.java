package astramod.entities.bullet;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import astramod.world.meta.*;

public class AOEBulletType extends BulletType {
	@Nullable public Cons<Unit> effect;
	public float effectInterval = 10f;
	public boolean showInterval = false;

	public AOEBulletType(float duration) {
		lifetime = duration;
		speed = damage = 0f;
		hittable = reflectable = absorbable = false;
		scaledSplashDamage = true;
		collides = false;
		keepVelocity = false;
		shootEffect = smokeEffect = despawnEffect = Fx.none;
		setDefaults = false;
		showStats = false;
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
			if (effect == null) {
				if (damage > 0f) Damage.damage(b.team, b.x, b.y, splashDamageRadius, b.damage);
				if (status != StatusEffects.none) Damage.status(b.team, b.x, b.y, splashDamageRadius, status, statusDuration, collidesAir, collidesGround);
			}

			if (collidesTeam) {
				Units.nearby(b.team, b.x, b.y, splashDamageRadius, effect);
			} else {
				Units.nearbyEnemies(b.team, b.x, b.y, splashDamageRadius, effect);
			}
		}
	}

	public void addStats(Table table) {
		AstraStatValues.addRow(table,
			showInterval ? "bullet.areaeffect.radiussecond" : "bullet.areaeffect.radius",
			Strings.autoFixed(splashDamageRadius / Vars.tilesize, 2),
			Strings.autoFixed(effectInterval / Time.toSeconds, 2)
		);
		AstraStatValues.addRow(table, "ability.stat.duration", Strings.autoFixed(lifetime / Time.toSeconds, 2));
		table.row();
	}
}