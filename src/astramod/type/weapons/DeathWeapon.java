package astramod.type.weapons;

import arc.scene.ui.layout.Table;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.world.meta.*;

public class DeathWeapon extends AstraWeapon {
	public DeathWeapon() {
		super("");

		shootOnDeath = true;
		controllable = aiControllable = false;
		useAttackRange = false;
		extendedStats = false;
		mirror = false;
		x = shootY = 0f;
	}

	public DeathWeapon(BulletType bullet) {
		this();
		this.bullet = bullet;

		shootSound = Sounds.explosionCrawler;
		shootSoundVolume = 0.5f;
	}

	@Override public void addStats(UnitType u, Table t) {
		AstraStatValues.addRow(t, bullet.instantDisappear ? "bullet.triggerondeath" : "bullet.shootondeath");
		super.addStats(u, t);
	}
}