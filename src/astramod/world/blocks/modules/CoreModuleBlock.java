package astramod.world.blocks.modules;

import arc.util.Nullable;
import mindustry.gen.Building;

public interface CoreModuleBlock {
	@Nullable public Building getLinkedCore();

	public void setLinkedCore(Building core);
}