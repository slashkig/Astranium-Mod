package astramod.world.blocks.modular.core;

import arc.math.Mathf;
import arc.math.geom.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import astramod.content.AstraBlocks;
import astramod.math.Mathx;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class PowerCoreModule extends PowerGenerator {
    public float warmupSpeed = 0.05f;
    public float effectChance = 0.01f;
    public Effect generateEffect = Fx.none;
    public float generateEffectRange = 3f;

	public PowerCoreModule(String name) {
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

	public class GeneratorCoreModuleBuild extends GeneratorBuild implements CoreModuleBuild {
		protected @Nullable Building linkedCore;

		@Override public void updateTile() {
			productionEfficiency = Mathx.elerpDelta(productionEfficiency, linkedCore == null ? 0f : 1f, warmupSpeed);
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

		@Override public void handleStack(Item item, int amount, Teamc source) {
			linkedCore.handleStack(item, amount, source);
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