package astramod.world.blocks.storage;

import static mindustry.Vars.world;

import arc.math.geom.Point2;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

public class CoreModule extends StorageBlock {
	public CoreModule(String name) {
		super(name);
		coreMerge = true;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}
}