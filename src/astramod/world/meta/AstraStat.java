package astramod.world.meta;

import mindustry.world.meta.*;

public class AstraStat {
	public static final Stat
	
	magneticStrength = new Stat("magneticStrength", StatCat.function),
	bridgeRange = new Stat("bridgeRange", StatCat.function),
	liquidPressure = new Stat("liquidPressure", StatCat.liquids),
	heatCapacity = new Stat("heatCapacity", StatCat.liquids),
	hLiquidCapacity = new Stat("hLiquidCapacity", StatCat.liquids),
	vLiquidCapacity = new Stat("vLiquidCapacity", StatCat.liquids);
}