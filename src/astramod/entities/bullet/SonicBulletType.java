package astramod.entities.bullet;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import astramod.content.*;
import astramod.graphics.*;

public class SonicBulletType extends BasicBulletType {
	public float falloffFactor = 0.5f;

	public SonicBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);

		shrinkX = -3f;
		shrinkY = 0f;
		pierce = true;
		pierceBuilding = pierceArmor = true;
		hittable = reflectable = false;
		shieldDamageMultiplier = 0.5f;
		
		hitColor = backColor = AstraPal.sonicShotBack;
		frontColor = AstraPal.sonicShotFront;
		smokeEffect = AstraFx.sonicPulse;
		hitEffect = AstraFx.sonicHit;
		despawnEffect = shootEffect = Fx.none;
	}

	public SonicBulletType(float speed, float damage) {
		this(speed, damage, "astramod-sonic-shot");
	}

	@Override public void update(Bullet b) {
		super.update(b);

		b.damage = damage * damageMultiplier(b);
		b.hitSize = hitSize * ((1f - shrinkX) + shrinkX * shrinkInterp.apply(b.fout()));
	}

	@Override public float damageMultiplier(Bullet b) {
		return super.damageMultiplier(b) * (1f - falloffFactor * b.fin());
	}
}