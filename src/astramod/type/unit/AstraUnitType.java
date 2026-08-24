package astramod.type.unit;

import arc.func.Prov;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.UnitType;
import astramod.gen.UnitEntity;
import astramod.graphics.AstraPal;

public class AstraUnitType extends UnitType {
	public AstraUnitType(String name, Prov<? extends Unit> cons) {
		super(name);
		constructor = cons;
		outlineColor = AstraPal.darkerOutline;
	}

	public AstraUnitType(String name) {
		this(name, UnitEntity::create);
	}
}