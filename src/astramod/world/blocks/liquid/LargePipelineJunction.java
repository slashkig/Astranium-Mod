package astramod.world.blocks.liquid;

import arc.math.geom.Geometry;
import arc.util.*;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;

import static mindustry.Vars.*;

public class LargePipelineJunction extends PipelineJunction {
	public LargePipelineJunction(String name) {
		super(name);
		solid = true;
		placeableLiquid = true;
		leaks = false;
	}

	public class LargePipelineJunctionBuild extends PipelineJunctionBuild {
		@Override public float moveLiquidForward(boolean leaks, Liquid liquid, int axis) {
			Building aBuild = nearby(Geometry.d4x(axis) * size, Geometry.d4y(axis) * size);
			Building bBuild = nearby(axis + 2);
			float total = 0f;

			if (aBuild != null && (aBuild.front() != this || !aBuild.block.rotate) && checkSide(aBuild)) {
				total += moveLiquid(aBuild, liquid);
			}
			if (bBuild != null && (bBuild.front() != this || !bBuild.block.rotate) && checkSide(bBuild)) {
				total += moveLiquid(bBuild, liquid);
			}

			return total;
		}

		public boolean checkSide(Building other) {
			if (other.block.size < size || other.block instanceof Conduit && other.block.size != size) return false;
			int direction = relativeTo(other);
			for (int i = 0; i < size; i++) {
				nearbySide(tile.x, tile.y, direction, i, Tmp.p1);
				Tile near = world.tile(Tmp.p1.x, Tmp.p1.y);
				if (near == null || !(near.build == other)) return false;
			}
			return true;
		}
	}
}