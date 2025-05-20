package astramod.world.blocks.modules;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

import static mindustry.Vars.*;

public class UnloaderCoreModule extends DirectionalUnloader {
	public UnloaderCoreModule(String name) {
		super(name);
		allowCoreUnload = true;
		isDuct = false;
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] { region, teamRegions[Team.sharded.id] };
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	public class UnloaderCoreModuleBuild extends DirectionalUnloaderBuild implements CoreModuleBlock {
		protected @Nullable Building linkedCore;

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
		}

		@Override @Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		@Override public void setLinkedCore(Building core) {
			linkedCore = core;
		}
	}
}
