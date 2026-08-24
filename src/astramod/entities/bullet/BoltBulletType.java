package astramod.entities.bullet;

import arc.graphics.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;

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
		hitEffect = Fx.colorSpark;
	}

	public BoltBulletType(float speed, float damage) {
		this(speed, damage, "astramod-railgun-bolt");
		height = 12f;
		width = 5f;
		hitSize = 6f;
		trailWidth = 1f;
	}

	public void setColor(Color light, Color dark) {
		frontColor = Color.white;
		backColor = light;
		hitColor = trailColor = dark;
		hitEffect = despawnEffect = Fx.hitBulletColor;
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
