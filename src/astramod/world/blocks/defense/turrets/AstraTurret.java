package astramod.world.blocks.defense.turrets;

import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Stat;
import astramod.world.meta.AstraStatValues;

public class AstraTurret extends ItemTurret {
	public boolean extraStats = false;

	public AstraTurret(String name) {
		super(name);
		drawer = new DrawTurret("astranium-");
		fogRadiusMultiplier = 0;
	}

	@Override public void setStats() {
		super.setStats();
		if (extraStats) {
			stats.remove(Stat.ammo);
			stats.add(Stat.ammo, AstraStatValues.astraAmmo(ammoTypes));
		}
	}
}