package astramod.world.blocks.storage;

import arc.graphics.g2d.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;

public class AstraStorageBlock extends StorageBlock {
	public AstraStorageBlock(String name) {
		super(name);
		coreMerge = false;
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id] } : new TextureRegion[] { region };
	}

	public class AstraStorageBuild extends StorageBuild {
		@Override public boolean acceptItem(Building source, Item item) {
			return items.total() < itemCapacity;
		}

		@Override public int getMaximumAccepted(Item item) {
			return itemCapacity - items.total() + items.get(item);
		}
	}
}