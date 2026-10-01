package astramod.entities.bullet;

import astramod.content.AstraFx;
import mindustry.entities.bullet.ArtilleryBulletType;

public class AstraArtilleryBulletType extends ArtilleryBulletType {

    public AstraArtilleryBulletType(float speed, float damage, String bulletSprite){
        super(speed, damage, bulletSprite);
        hitEffect = AstraFx.dynamicExplosion(this);
        despawnEffect = AstraFx.scaledDespawn(this);
        shootEffect = AstraFx.mortarShoot(this);
    }

    public AstraArtilleryBulletType(float speed, float damage){
        this(speed, damage, "shell");
    }
}
