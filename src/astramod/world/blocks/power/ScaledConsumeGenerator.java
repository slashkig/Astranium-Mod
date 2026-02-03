package astramod.world.blocks.power;

import arc.Core;
import arc.util.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;

public class ScaledConsumeGenerator extends ConsumeGenerator {
	public Item targetItem;

	public ScaledConsumeGenerator(String name) {
		super(name);
		generationType = AstraStat.powerPerItem;
	}

	@Override public void init() {
		super.init();
		if (targetItem == null) targetItem = ((ConsumeItems)findConsumer(f -> f instanceof ConsumeItems)).items[0].item;
	}

	@Override public void setStats() {
		super.setStats();
		stats.remove(Stat.productionTime);
		stats.add(AstraStat.maxPowerProduction, powerProduction * itemCapacity * 60f, StatUnit.powerSecond);
		stats.add(AstraStat.itemLifetime, itemDuration / 60f, StatUnit.seconds);
	}

	@Override public void setBars() {
		super.setBars();
		removeBar("power");
		addBar("power", (ScaledConsumeGeneratorBuild entity) -> new Bar(() ->
			Core.bundle.format("bar.poweroutput",
			Strings.fixed(entity.getPowerProduction() * 60f * entity.timeScale(), 1)),
			() -> Pal.powerBar,
			() -> entity.productionEfficiency / itemCapacity
		));
	}

	public class ScaledConsumeGeneratorBuild extends ConsumeGeneratorBuild {
		@Override public void updateTile() {
			super.updateTile();
			if (efficiencyMultiplier >= 1) generateTime -= (efficiencyMultiplier - 1) * delta() / itemDuration;
		}

		@Override public float efficiency() {
			return efficiencyMultiplier / itemCapacity;
		}

		@Override public boolean consumeTriggerValid() {
			return generateTime > 0 && efficiencyMultiplier > 0;
		}

		@Override public void updateEfficiencyMultiplier() {
			// Efficiency multiplier is effectively the number of target items
			efficiencyMultiplier = items.get(targetItem);
		}
	}
}