package astramod.type.unit;

import astramod.gen.MechUnit;
import mindustry.world.meta.Env;

public class AstraMechUnitType extends AstraUnitType {
	public AstraMechUnitType(String name) {
		super(name, MechUnit::create);
		omniMovement = true;
		rotateMoveFirst = true;
		envDisabled = Env.none;
	}
}