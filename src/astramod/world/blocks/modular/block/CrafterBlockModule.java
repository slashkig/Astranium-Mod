package astramod.world.blocks.modular.block;

import arc.graphics.g2d.Draw;
import arc.math.geom.*;
import arc.util.*;
import astramod.world.blocks.modular.*;
import astramod.world.meta.AstraStat;
import astramod.world.meta.AstraStatValues;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

import static mindustry.Vars.*;

public class CrafterBlockModule extends GenericCrafter implements BlockModule {
	public Block targetBlockType;
	public @Nullable LiquidStack byproductLiquid;

	public CrafterBlockModule(String name) {
		super(name);
		rotate = true;
		rotateDraw = false;
		drawArrow = true;
		outputFacing = false;
	}

	@Override public void init() {
		schematicPriority = targetBlockType.schematicPriority - 1;
		super.init();
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.parentBlock, AstraStatValues.block(targetBlockType));
		if (byproductLiquid != null) {
			stats.add(Stat.output, StatValues.liquids(1f, byproductLiquid));
		}
	}

	public Block parentBlock() {
		return targetBlockType;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return canPlaceModule(tile.x, tile.y, rotation);
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		if (byproductLiquid != null) {
			Draw.rect(byproductLiquid.liquid.fullIcon, x + Geometry.d4x(rotation) * (size * tilesize / 2f + 4), y + Geometry.d4y(rotation) * (size * tilesize / 2f + 4), 8f, 8f);
		}
	}

	public class CrafterModuleBuild extends GenericCrafterBuild implements ModuleBuild {
		public @Nullable Building linkedBuild;

		@Override public void updateTile() {
			super.updateTile();
			if (linkedBuild != null) {
				float cap = byproductLiquid != null ? linkedBuild.block.liquidCapacity - linkedBuild.liquids().get(byproductLiquid.liquid) : -1f;
				if (efficiency > 0 && cap > 0) {
					linkedBuild.handleLiquid(this, byproductLiquid.liquid, Math.min(byproductLiquid.amount * getProgressIncrease(1f), cap));
				}
			}
		}

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			if (checkFront(tile.x, tile.y, rotation)) setLinkedBuild(front());
		}

		@Override public void drawSelect() {
			if (linkedBuild != null) {
				Drawf.selected(linkedBuild, Pal.accent);
				linkedBuild.drawSelect();
			}
			super.drawSelect();
		}

		@Override public boolean productionValid() {
			return linkedBuild != null && (byproductLiquid == null || ignoreLiquidFullness || linkedBuild.block.liquidCapacity - linkedBuild.liquids().get(byproductLiquid.liquid) > 0.1f);
		}

		@Override public float warmupTarget() {
			return efficiencyScale();
		}

		@Override public float efficiencyScale() {
			return linkedBuild != null ? linkedBuild.efficiency() : 0f;
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