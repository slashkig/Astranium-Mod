package astramod.world.blocks.modular;

import arc.struct.*;
import mindustry.world.Block;

public interface BaseModularBlock {
	public Seq<Block> getValidModules();

	public void addValidModule(Block block);

	public EnumSet<ModularType> getModuleTypes();
}