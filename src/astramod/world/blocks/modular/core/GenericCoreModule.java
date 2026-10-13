package astramod.world.blocks.modular.core;

import arc.graphics.g2d.TextureRegion;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;
import astramod.content.*;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class GenericCoreModule extends Block implements BlockModule {
	public static String coreKey = "aziris-core";

	public GenericCoreModule(String name) {
		super(name);
		solid = true;
		destructible = true;
		hasItems = true;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.parentBlock, AstraStatValues.block(targetBlock(), coreKey));
	}

	@Override protected TextureRegion[] icons() {
		return new TextureRegion[] { region, teamRegions[Team.sharded.id] };
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

	public class GenericCoreModuleBuild extends Building implements CoreModuleBuild {
		protected @Nullable Building linkedCore;

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