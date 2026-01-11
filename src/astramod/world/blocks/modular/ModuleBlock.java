package astramod.world.blocks.modular;

import arc.util.Nullable;
import mindustry.gen.Building;

public interface ModuleBlock {
	@Nullable public Building getLinkedBuild();

	public void setLinkedBuild(Building core);
}
