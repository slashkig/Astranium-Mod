package astramod.world.blocks.modular;

public interface CooledBuild {
	public boolean coolantFull();

	public void addCoolant(float amount);
}