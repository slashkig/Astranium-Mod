package astramod.type.unit;

import mindustry.world.meta.Env;

public class AstraTankUnitType extends AstraUnitType {
	public AstraTankUnitType(String name) {
		super(name);
		squareShape = true;
		omniMovement = false;
		rotateMoveFirst = true;
		envDisabled = Env.none;
		knockbackMultiplier = 0.8f;
	}

	// TODO custom treads
}