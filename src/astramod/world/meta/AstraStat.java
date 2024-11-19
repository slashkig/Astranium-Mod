package astramod.world.meta;

import mindustry.world.meta.*;

public class AstraStat {
	public static final Stat
		magneticStrength = new Stat("magneticStrength", StatCat.function),
		knockback = new Stat("knockback", StatCat.function),
		bridgeRange = new Stat("bridgeRange", StatCat.function),
		liquidPressure = new Stat("liquidPressure", StatCat.liquids),
		heatCapacity = new Stat("heatCapacity", StatCat.liquids),
		hLiquidCapacity = new Stat("hLiquidCapacity", StatCat.liquids),
		vLiquidCapacity = new Stat("vLiquidCapacity", StatCat.liquids),
		powerPerItem = new Stat("powerPerItem", StatCat.power),
		itemLifetime = new Stat("itemLifetime", StatCat.crafting),
		numDrones = new Stat("numDrones", StatCat.function),
		droneBuildTime = new Stat("droneBuildTime", StatCat.function),
		incendivity = new Stat("incendivity", StatCat.function),
		lightningCount = new Stat("lightningCount", StatCat.function),
		lightningDamage = new Stat("lightningDamage", StatCat.function),
		wireCost = new Stat("wireCost", StatCat.function);
}