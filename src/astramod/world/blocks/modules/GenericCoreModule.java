package astramod.world.blocks.modules;

import arc.graphics.g2d.TextureRegion;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

import static mindustry.Vars.*;

public class GenericCoreModule extends Block {
	public GenericCoreModule(String name) {
		super(name);
		solid = true;
		destructible = true;
		hasItems = true;
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

	@Override public boolean outputsItems() {
		return false;
	}

	public class GenericCoreModuleBuild extends Building implements CoreModuleBlock {
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

		@Override @Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		@Override public void setLinkedCore(Building core) {
			linkedCore = core;
		}
	}
}