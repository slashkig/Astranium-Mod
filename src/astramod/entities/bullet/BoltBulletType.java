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
		if (entity instanceof Unit u) b.damage += Math.min(u.armor(), armorPenetration);
		else if (entity instanceof Building build) b.damage += Math.min(build.block.armor, armorPenetration);

		super.hitEntity(b, entity, health);
	}
}
