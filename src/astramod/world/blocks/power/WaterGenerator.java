package astramod.world.blocks.power;

import mindustry.content.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;

public class WaterGenerator extends PowerGenerator {
	public WaterGenerator(String name) {
		super(name);
		rotate = true;
		floating = true;
		group = BlockGroup.liquids;
		envEnabled = Env.terrestrial;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		if (isMultiblock()) {
			for (Tile other : tile.getLinkedTilesAs(this, tempTiles)) {
				if (other.floor().liquidDrop != Liquids.water) return false;
			}
			return true;
		} else return tile.floor().liquidDrop == Liquids.water;
	}

	public class WaterGeneratorBuild extends GeneratorBuild {
		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			productionEfficiency = 0f;
			for (Tile t : tile.getLinkedTiles(tempTiles)) {
				if (t.floor().liquidDrop == Liquids.water) {
					productionEfficiency += t.floor().liquidMultiplier;
				}
			}
			productionEfficiency /= size * size;
		}
	}
}