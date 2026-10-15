package astramod.ai;

import arc.math.geom.*;
import mindustry.gen.*;

import static mindustry.Vars.pathfinder;
import static mindustry.ai.Pathfinder.*;

public class AstraPathfinding {
	/** Use {@link #setPositionField} before using this field to path! */
	public static final int fieldPosition;

	static {
		fieldPosition = fieldTypes.size;
		fieldTypes.add(() -> new PositionTarget(new Vec2()));
	}

	public static void init() { }

	public static void setPositionField(Unit unit, Position pos) {
		PositionTarget field = (PositionTarget)pathfinder.getField(unit.team, unit.type.flowfieldPathType, fieldPosition);
		((Vec2)field.position).set(pos);
	}
}