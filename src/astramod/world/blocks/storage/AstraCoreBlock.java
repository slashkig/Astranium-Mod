package astramod.world.blocks.storage;

import arc.graphics.g2d.*;
import astramod.world.blocks.modular.*;
import mindustry.game.*;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.modules.ItemModule;

import static mindustry.Vars.*;

public class AstraCoreBlock extends CoreBlock {
	public AstraCoreBlock(String name) {
		super(name);
		conductivePower = true;
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id] } : new TextureRegion[] { region };
	}

	public class AstraCoreBuild extends CoreBuild {
		@Override public boolean owns(Building core, Building tile) {
			return tile instanceof CoreModuleBuild m && (m.getLinkedCore() == core || m.getLinkedCore() == null);
		}

		@Override public void onProximityUpdate() {
			noSleep();

			for (Building other : state.teams.cores(team)) {
				if (other.tile() != tile) {
					this.items = other.items;
				}
			}
			state.teams.registerCore(this);

			storageCapacity = itemCapacity; // + proximity().sum(e -> owns(e) ? e.block.itemCapacity : 0);
			proximity.each(this::owns, t -> {
				storageCapacity += t.block.itemCapacity;
				t.items = items;
				((CoreModuleBuild)t).setLinkedCore(this);
			});

			for (Building other : state.teams.cores(team)) {
				if (other.tile() == tile) continue;
				storageCapacity += other.block.itemCapacity + other.proximity().sum(e -> owns(other, e) ? e.block.itemCapacity : 0);
			}

			if (!world.isGenerating()) {
				for (Item item : content.items()) {
					items.set(item, Math.min(items.get(item), storageCapacity));
				}
			}

			for (CoreBuild other : state.teams.cores(team)) {
				other.storageCapacity = storageCapacity;
			}
		}

		@Override public void onRemoved() {
			int total = proximity.count(e -> e.items != null && e.items == items);
			float fract = 1f / total / state.teams.cores(team).size;

			proximity.each(e -> owns(e) && e instanceof CoreModuleBuild m && m.getLinkedCore() == this, t -> {
				((CoreModuleBuild)t).setLinkedCore(null);
				t.items = new ItemModule();
				for (Item item : content.items()) {
					t.items.set(item, (int)(fract * items.get(item)));
				}
			});

			state.teams.unregisterCore(this);

			for (CoreBuild other : state.teams.cores(team)) {
				other.onProximityUpdate();
			}
		}
	}
}