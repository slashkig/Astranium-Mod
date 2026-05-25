package astramod.world.meta;

import mindustry.world.meta.*;

public class AstraStat {
	public static final Stat
		detonation = new Stat("detonation", StatCat.function),
		magneticStrength = new Stat("magneticStrength", StatCat.function),
		bridgeRange = new Stat("bridgeRange", StatCat.function),
		liquidPressure = new Stat("liquidPressure", StatCat.liquids),
		fluidThroughput = new Stat("fluidThroughput", StatCat.liquids),
		coolantProduction = new Stat("coolantProduction", StatCat.crafting),
		coolantConsumption = new Stat("coolantConsumption", StatCat.crafting),
		coolantBoost = new Stat("coolantBoost", StatCat.crafting),
		coolantCapacity = new Stat("coolantCapacity", StatCat.liquids),
		heatAbsorption = new Stat("heatAbsorption", StatCat.function),
		heatCapacity = new Stat("heatCapacity", StatCat.function),
		heatSpeed = new Stat("heatSpeed", StatCat.function),
		hLiquidCapacity = new Stat("hLiquidCapacity", StatCat.liquids),
		vLiquidCapacity = new Stat("vLiquidCapacity", StatCat.liquids),
		unstableSpeed = new Stat("unstableSpeed", StatCat.function),
		powerPerItem = new Stat("powerPerItem", StatCat.power),
		maxPowerProduction = new Stat("maxPower", StatCat.power),
		itemLifetime = new Stat("itemLifetime", StatCat.crafting),
		numDrones = new Stat("numDrones", StatCat.function),
		droneBuildTime = new Stat("droneBuildTime", StatCat.function),
		rampupTime = new Stat("rampupTime", StatCat.function),
		incendivity = new Stat("incendivity", StatCat.function),
		lightningCount = new Stat("lightningCount", StatCat.function),
		lightningDamage = new Stat("lightningDamage", StatCat.function),
		wireCost = new Stat("wireCost", StatCat.function),
		parentBlock = new Stat("parentBlock", StatCat.general),
		parentBlocks = new Stat("parentBlocks", StatCat.general),
		moduleBlocks = new Stat("moduleBlocks", StatCat.general);
}