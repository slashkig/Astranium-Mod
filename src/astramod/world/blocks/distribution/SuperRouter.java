package astramod.world.blocks.distribution;

import arc.audio.Sound;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.bullet.LaserBoltBulletType;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.draw.DrawCircles;

public class SuperRouter extends Router {
	public float range = 80f;
	public float damage = 10f;
	public float routateSpeed = 10f;
	public LaserBoltBulletType bullet;
	public DrawCircles circles;
	public Sound shootSound = Sounds.missile;

	public SuperRouter(String name) {
		super(name);
	}
	
	public class SuperRouterBuild extends RouterBuild {

		@Override public void damage(float damage) {
			Units.nearbyEnemies(team, x, y, range, unit -> {
				bullet.create(this, x, y, angleTo(unit));
			});
			shootSound.at(x, y);
		}

		@Override public float warmup() {
			return 1f;
		}

		@Override public void draw() {
			Drawf.spinSprite(region, x, y, (Time.time * routateSpeed) % 360f + rotdeg());
			circles.draw(this);
		}
	}
}
