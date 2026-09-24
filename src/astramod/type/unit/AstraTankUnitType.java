package astramod.type.unit;

import arc.func.Prov;
import mindustry.gen.*;
import mindustry.world.meta.Env;

public class AstraTankUnitType extends AstraUnitType {
	public AstraTankUnitType(String name, Prov<? extends Unit> cons) {
		super(name, cons);
		squareShape = true;
		omniMovement = false;
		rotateMoveFirst = true;
		envDisabled = Env.none;
	}

	public AstraTankUnitType(String name) {
		this(name, TankUnit::create);
		knockbackMultiplier = 0.8f;
	}

	// TODO custom treads
}