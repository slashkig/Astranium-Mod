package astramod.world.blocks.modular.core;

import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import mindustry.world.blocks.storage.StorageBlock;
import astramod.content.*;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class CrafterCoreModule extends GenericCrafter implements BlockModule {
	public CrafterCoreModule(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.parentBlock, AstraStatValues.block(targetBlock(), GenericCoreModule.coreKey));
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	@Override public boolean canReplace(Block other) {
		return moduleReplaceable(other);
	}

	@Override public boolean outputsItems() {
		return false;
	}

	@Override public Block targetBlock() {
		return AstraBlocks.coreNode;
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

		@Override public void handleItem(Building source, Item item) {
			if (linkedCore instanceof CoreBuild core) {
				if (core.items.get(item) >= core.storageCapacity) {
					StorageBlock.incinerateEffect(this, source);
				}
				core.noEffect = true;
				core.handleItem(source, item);
			}
		}

		@Override public int getMaximumAccepted(Item item){
			return linkedCore != null ? linkedCore.getMaximumAccepted(item) : 0;
		}

		@Override public void handleStack(Item item, int amount, Teamc source) {
			if (linkedCore != null) linkedCore.handleStack(item, amount, source);
		}

		@Override public int removeStack(Item item, int amount) {
			return linkedCore != null ? linkedCore.removeStack(item, amount) : 0;
		}

		@Override public boolean canUnload() {
			return linkedCore != null && linkedCore.canUnload();
		}

		@Override public boolean allowDeposit(){
			return linkedCore != null && linkedCore.allowDeposit();
		}

		@Override public void itemTaken(Item item) {
			if (linkedCore != null) linkedCore.itemTaken(item);
		}

		@Override public int explosionItemCap() {
			return linkedCore != null ? Math.min(itemCapacity / 60, 6) : 0;
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

		@Override public double sense(LAccess sensor) {
			if (sensor == LAccess.itemCapacity && linkedCore != null) return linkedCore.sense(sensor);
			return super.sense(sensor);
		}
	}
}