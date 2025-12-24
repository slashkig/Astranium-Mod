package astramod.world.blocks.modules.block;

import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import astramod.world.blocks.modules.*;

import static mindustry.Vars.*;

public class CrafterBlockModule extends GenericCrafter {
	public @Nullable Block targetBlockType;
	public @Nullable LiquidStack byproductLiquid;

	public CrafterBlockModule(String name) {
		super(name);
		rotate = true;
		rotateDraw = false;
		drawArrow = true;
		outputFacing = false;
	}

	@Override public void setStats() {
		super.setStats();
		if (byproductLiquid != null) {
			stats.add(Stat.output, StatValues.liquids(1f, byproductLiquid));
		}
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return checkEdge(tile.x, tile.y, rotation);
	}

	public boolean checkEdge(int x, int y, int rotation) {
		Point2 edge = new Point2();
		nearbySide(x, y, rotation, 0, edge);
		Building build = world.build(edge.x, edge.y);
		if (!validLink(build)) return false;

		for (int i = 1; i < size; i++) {
			nearbySide(x, y, rotation, i, edge);
			if (build != world.build(edge.x, edge.y)) return false;
		}
		return true;
	}

	public boolean validLink(Building build) {
		return targetBlockType == null && build instanceof BaseModularBlock || build != null && build.block == targetBlockType;
	}

	public class CrafterModuleBlock extends GenericCrafterBuild implements ModuleBlock {
		public @Nullable Building linkedBuild;

		@Override public void updateTile() {
			super.updateTile();
			float cap = byproductLiquid != null ? linkedBuild.block.liquidCapacity - linkedBuild.liquids().get(byproductLiquid.liquid) : -1f;
			if (efficiency > 0 && cap > 0) {
				linkedBuild.handleLiquid(this, byproductLiquid.liquid, Math.min(byproductLiquid.amount * getProgressIncrease(1f), cap));
			}
		}

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			if (checkEdge(tile.x, tile.y, rotation)) setLinkedBuild(front());
		}

		@Override public void drawSelect() {
			if (linkedBuild != null) {
				linkedBuild.drawSelect();
			}
			super.drawSelect();
		}

		@Override public boolean productionValid() {
			return byproductLiquid == null || ignoreLiquidFullness || linkedBuild.block.liquidCapacity - linkedBuild.liquids().get(byproductLiquid.liquid) > 0.1f;
		}

		@Override public float warmupTarget() {
			return efficiencyScale();
		}

		@Override public float efficiencyScale() {
			return linkedBuild != null ? linkedBuild.efficiency() : 0f;
		}

		@Override public float getProgressIncrease(float baseTime) {
			return super.getProgressIncrease(baseTime) * efficiency;
		}

		@Nullable public Building getLinkedBuild() {
			return linkedBuild;
		}

		public void setLinkedBuild(Building build) {
			linkedBuild = build;
		}

		@Override public boolean canPickup() {
			return linkedBuild == null;
		}
	}
}