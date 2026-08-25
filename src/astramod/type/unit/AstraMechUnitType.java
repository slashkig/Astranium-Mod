package astramod.type.unit;

import astramod.gen.MechUnit;

public class AstraMechUnitType extends AstraUnitType {
	public AstraMechUnitType(String name) {
		super(name, MechUnit::create);
	}
}