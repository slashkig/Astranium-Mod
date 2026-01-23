package astramod.world.blocks.modular.core;

import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import astramod.content.AstraBlocks;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class CrafterCoreModule extends GenericCrafter {
	public CrafterCoreModule(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.parentBlock, AstraStatValues.block(AstraBlocks.coreNode, GenericCoreModule.coreKey));
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	@Override public boolean outputsItems() {
		return false;
	}

	public class CrafterCoreModuleBuild extends GenericCrafterBuild implements CoreModuleBuild {
		protected @Nullable Building linkedCore;

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
		}

		@Override public boolean productionValid() {
			return linkedCore != null;
		}

		@Override public boolean shouldConsume() {
			if (linkedCore == null) return false;
			if (outputItems != null) {
				for (ItemStack output : outputItems) {
					if (items.get(output.item) + output.amount > ((CoreBuild)linkedCore).storageCapacity) {
						return false;
					}
				}
			}
			if (outputLiquids != null && !ignoreLiquidFullness) {
				boolean allFull = true;
				for (LiquidStack output : outputLiquids) {
					if (liquids.get(output.liquid) >= liquidCapacity - 0.001f) {
						if (!dumpExtraLiquid) return false;
					} else allFull = false;
				}
				if (allFull) return false;
			}
			return enabled;
		}

		@Override public float warmupTarget() {
			return efficiencyScale();
		}

		@Override public float efficiencyScale() {
			return linkedCore != null ? 1f : 0f;
		}


		@Override public boolean acceptItem(Building source, Item item) {
			return linkedCore != null && linkedCore.acceptItem(source, item);
		}

		@Override public void handleStack(Item item, int amount, Teamc source) {
			linkedCore.handleStack(item, amount, source);
		}

		@Override public boolean canDump(Building to, Item item) {
			return false;
		}

		@Override public boolean dump(Item todump) {
			return false;
		}

		@Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		public void setLinkedCore(Building core) {
			linkedCore = core;
		}

		@Override public boolean canPickup() {
			return false;
		}
	}
}
