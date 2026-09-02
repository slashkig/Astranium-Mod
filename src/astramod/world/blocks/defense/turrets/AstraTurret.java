package astramod.world.blocks.defense.turrets;

import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Stat;
import astramod.world.meta.AstraStatValues;

public class AstraTurret extends ItemTurret {
	public boolean extraStats = true;

	public AstraTurret(String name) {
		super(name);
		drawer = new DrawTurret("astranium-");
	}

	@Override public void setStats() {
		super.setStats();
		if (extraStats) {
			stats.replace(Stat.ammo, AstraStatValues.astraAmmo(ammoTypes, name));
		}
	}
}