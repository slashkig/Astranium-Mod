package astramod.type.unit;

import arc.func.Prov;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.UnitType;
import astramod.gen.UnitEntity;

public class AstraUnitType extends UnitType {
	public AstraUnitType(String name, Prov<? extends Unit> cons) {
		super(name);
		constructor = cons;
		outlineColor = Pal.darkOutline;
	}

	public AstraUnitType(String name) {
		this(name, UnitEntity::create);
	}
}