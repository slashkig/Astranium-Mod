package astramod.world.blocks.defense.turrets;

import mindustry.content.*;
import mindustry.entities.part.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class AstraLiquidTurret extends LiquidTurret {
	public boolean extraStats = true;

	public AstraLiquidTurret(String name) {
		super(name);
		drawer = new DrawTurret("astranium-");
		shootEffect = Fx.shootLiquid;
	}

	@Override public void init() {
		super.init();
		if (extinguish) flags = flags.with(BlockFlag.extinguisher);
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
}