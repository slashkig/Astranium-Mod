package astramod.type.unit;

import arc.func.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.gen.UnitEntity;
import astramod.graphics.*;
import astramod.world.blocks.units.DynamicReconstructor;

public class AstraUnitType extends UnitType {
	public AstraUnitType(String name, Prov<? extends Unit> cons) {
		super(name);
		constructor = cons;
		outlineColor = AstraPal.darkerOutline;
	}

	public AstraUnitType(String name) {
		this(name, UnitEntity::create);
	}

	@Override @Nullable public ItemStack[] getRequirements(@Nullable UnitType[] prevReturn, @Nullable float[] timeReturn) {
		var assembler = (DynamicReconstructor)Vars.content.blocks().find(b -> b instanceof DynamicReconstructor dr && dr.upgrades.contains(u -> u[1] == this));

		if (assembler != null) {
			UnitType prev = assembler.upgrades.find(u -> u[1] == this)[0];
			if (prevReturn != null) {
				prevReturn[0] = prev;
			}
			if (timeReturn != null) {
				timeReturn[0] = assembler.recipes.get(prev).time;
			}
			return assembler.recipes.get(prev).requirements;
		} else return super.getRequirements(prevReturn, timeReturn);
	}
}