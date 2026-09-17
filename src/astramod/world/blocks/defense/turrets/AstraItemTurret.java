package astramod.world.blocks.defense.turrets;

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
}