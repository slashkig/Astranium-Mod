package astramod.world.blocks.modular;

import arc.util.Nullable;
import mindustry.gen.Building;

public interface CoreModuleBuild {
	@Nullable public Building getLinkedCore();

	public void setLinkedCore(Building core);
}