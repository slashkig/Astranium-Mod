package astramod.world.blocks.storage;

import arc.graphics.g2d.*;
import mindustry.game.Team;
import mindustry.world.blocks.storage.CoreBlock;

public class AstraCoreBlock extends CoreBlock {
	public AstraCoreBlock(String name) {
		super(name);
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id] } : new TextureRegion[] { region };
	}
}
