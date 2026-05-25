package astramod.world.blocks.modular;

public interface HeatedBuild {
	public float getHeatFrac();

	public void handleHeat(float amount);
}
