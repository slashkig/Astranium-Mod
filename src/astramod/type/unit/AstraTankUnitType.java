package astramod.type.unit;

import mindustry.world.meta.Env;
import astramod.gen.TankUnit;

public class AstraTankUnitType extends AstraUnitType {
	public AstraTankUnitType(String name) {
		super(name, TankUnit::create);
		squareShape = true;
		omniMovement = false;
		rotateMoveFirst = true;
		envDisabled = Env.none;
	}

	// TODO custom treads
}