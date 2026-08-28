package astramod.entities.bullet;

import mindustry.content.Fx;
import mindustry.entities.bullet.BasicBulletType;
import astramod.content.AstraFx;
import astramod.graphics.AstraPal;

public class SonicBulletType extends BasicBulletType {
	public SonicBulletType(float speed, float damage, String bulletSprite) {
		super(speed, damage, bulletSprite);

		pierce = true;
		pierceBuilding = true;
		pierceArmor = true;
		hitColor = backColor = AstraPal.sonicShotBack;
		frontColor = AstraPal.sonicShotFront;
		smokeEffect = AstraFx.sonicPulse;
		hitEffect = despawnEffect = AstraFx.sonicHit;
		shootEffect = Fx.none;
	}

	public SonicBulletType(float speed, float damage) {
		this(speed, damage, "astramod-sonic-shot");
	}
}