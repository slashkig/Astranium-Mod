package astramod.entities.bullet;

import arc.graphics.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.content.*;

public class BoltBulletType extends BasicBulletType {
	public float armorPenetration = 0f;

	public BoltBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);
		ammoMultiplier = 1;
		displayAmmoMultiplier = false;
		buildingDamageMultiplier = 0.5f;
		pierce = true;
		pierceBuilding = true;
		impact = true;
		hitEffect = AstraFx.boltPierce(this, 8f, 13f, 20f, 9);
		shootEffect = AstraFx.railgunShoot(this, 7f, 12f, 12f, 20f);
	}

	public BoltBulletType(float speed, float damage) {
		this(speed, damage, "astramod-railgun-bolt");
		height = 12f;
		width = 5f;
		hitSize = 6f;
		trailWidth = 1f;
	}

	public void setColor(Color light, Color dark) {
		frontColor = light;
		backColor = dark;
		hitColor = trailColor = dark;
		despawnEffect = AstraFx.dynamicBurst(light, dark, false);
	}

	@Override public void hitEntity(Bullet b, Hitboxc entity, float health) {
		float pierceDamage = 0f;
		if (!pierceArmor) {
			if (entity instanceof Unit u) pierceDamage = Math.min(u.armorOverride() >= 0f ? u.armorOverride() : u.armor(), armorPenetration);
			else if (entity instanceof Building build) pierceDamage = Math.min(build.block.armor, armorPenetration);
			b.damage += pierceDamage *= b.type.armorMultiplier;
		}

		super.hitEntity(b, entity, health);

		b.damage -= pierceDamage;

		if (removeAfterPierce && b.damage <= 0) {
			b.hit = true;
			b.remove();
		}
	}
}
