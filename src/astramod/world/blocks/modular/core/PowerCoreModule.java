package astramod.world.blocks.modular.core;

import arc.math.Mathf;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.LAccess;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import mindustry.world.blocks.storage.StorageBlock;
import astramod.content.*;
import astramod.math.MathUtil;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class PowerCoreModule extends PowerGenerator implements BlockModule {
    public float warmupSpeed = 0.05f;
    public float effectChance = 0.01f;
    public Effect generateEffect = Fx.none;
    public float generateEffectRange = 3f;

	public PowerCoreModule(String name) {
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

	@Override public Block targetBlock() {
		return AstraBlocks.coreNode;
	}

	public class GeneratorCoreModuleBuild extends GeneratorBuild implements CoreModuleBuild {
		protected @Nullable Building linkedCore;

		@Override public void updateTile() {
			productionEfficiency = MathUtil.elerpDelta(productionEfficiency, linkedCore == null ? 0f : 1f, warmupSpeed);
			if (productionEfficiency > 0.01f && Mathf.chanceDelta(effectChance * productionEfficiency)) {
				generateEffect.at(x + Mathf.range(generateEffectRange), y + Mathf.range(generateEffectRange));
			}
		}

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
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