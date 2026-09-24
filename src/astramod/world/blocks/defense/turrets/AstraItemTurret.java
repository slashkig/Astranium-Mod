package astramod.world.blocks.defense.turrets;

import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class AstraItemTurret extends ItemTurret {
	public boolean extraStats = true;

	public AstraItemTurret(String name) {
		super(name);
		drawer = new DrawTurret("astranium-");
		depositCooldown = 10f;
	}

	@Override public void setStats() {
		super.setStats();
		if (extraStats) {
			stats.replace(Stat.ammo, AstraStatValues.astraAmmo(ammoTypes, name));
		}
	}

	public void addParts(DrawPart... parts) {
		((DrawTurret)drawer).parts.add(parts);
	}

	public void addBarrelParts(boolean drawUnder, float recoil) {
		for (int i = 0; i < recoils; i++) {
			final int f = i;
			addParts(new RegionPart("-barrel-" + (recoils == 2 ? (i == 0 ? "l" : "r") : i)) {{
				progress = PartProgress.recoil;
				recoilIndex = f;
				under = drawUnder;
				moveY = recoil;
			}});
		}
	}

	public RegionPart addBarrelPart(boolean drawUnder, float recoil) {
		RegionPart barrel = new RegionPart("-barrel") {{
			progress = PartProgress.recoil;
			under = drawUnder;
			moveY = recoil;
		}};
		addParts(barrel);
		return barrel;
	}

	public RegionPart addBarrelPart() {
		return addBarrelPart(false, -1f);
	}

	public void colorHitEffects() {
		ammoTypes.forEach(e -> {
			if (e.value instanceof BasicBulletType b && (b.hitEffect == Fx.hitBulletSmall || b.hitEffect == Fx.flakExplosion)) {
				b.hitColor = b.backColor;
				b.hitEffect = b.despawnEffect = Fx.hitBulletColor;
			}
		});
	}
}