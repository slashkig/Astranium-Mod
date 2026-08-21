package astramod.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.entities.*;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.*;
import astramod.content.AstraFx;

public class TeamLaserBoltBulletType extends LaserBoltBulletType {
	public Effect laserHitEffect;

	public TeamLaserBoltBulletType(float speed, float damage) {
		super(speed, damage);
		frontColor = Color.white;
		smokeEffect = laserHitEffect = despawnEffect = AstraFx.colorLaser;
		hitEffect = Fx.none;
	}

	public TeamLaserBoltBulletType() {
		this(1f, 1f);
	}

	@Override public void hit(Bullet b, float x, float y, boolean createFrags){
		laserHitEffect.at(x, y, b.rotation(), b.owner() instanceof Teamc t ? t.team().color : hitColor);
		super.hit(b, x, y, createFrags);
	}

	@Override public void draw(Bullet b) {
		if (b.owner() instanceof Teamc t) {
			drawTrail(b);
			drawParts(b);

			Draw.color(t.team().color);
			Lines.stroke(width);
			Lines.lineAngleCenter(b.x, b.y, b.rotation(), height);
			Draw.color(frontColor);
			Lines.lineAngleCenter(b.x, b.y, b.rotation(), height / 2f);
			Draw.reset();
		} else super.draw(b);
	}

	@Override public void drawTrail(Bullet b) {
		if (trailLength > 0 && b.trail != null) {
			// Draw below bullets
			float z = Draw.z();
			Draw.z(z - 0.0001f);
			b.trail.draw(b.owner() instanceof Teamc t ? t.team().color : trailColor, trailWidth);
			Draw.z(z);
		}
	}

	public void despawned(Bullet b) {
		if (despawnHit) {
			hit(b, b.x, b.y, false);
		} else {
			createUnits(b, b.x, b.y);
		}

		despawnEffect.at(b.x, b.y, b.rotation(), b.owner() instanceof Teamc t ? t.team().color : hitColor);
		despawnSound.at(b, 1f + Mathf.range(hitSoundPitchRange));

		Effect.shake(despawnShake, despawnShake, b);
	}
}