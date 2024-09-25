package astramod.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

/** Extended functionality for static walls. */
public class AstraStaticWall extends StaticWall {
	public TextureRegion[][][] splitRegions;
	public int largeVariants = 1;

	public AstraStaticWall(String name) {
		super(name);
	}

	@Override public void drawBase(Tile tile) {
		int rx = tile.x / 2 * 2;
		int ry = tile.y / 2 * 2;
		int seed = Point2.pack(rx, ry);

		if (largeVariants > 0 && eqcpy(rx, ry) && Mathf.randomSeed(seed) < 0.5) {
			Draw.rect(splitRegions[Mathf.randomSeed(seed, 0, Math.max(0, splitRegions.length - 1))][tile.x % 2][1 - tile.y % 2],
				tile.worldx(), tile.worldy());
		} else if (variants > 0) {
			Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))],
				tile.worldx(), tile.worldy());
		} else {
			Draw.rect(region, tile.worldx(), tile.worldy());
		}

		if (tile.overlay().wallOre) {
			tile.overlay().drawBase(tile);
		}
	}

	@Override public void load() {
		super.load();

		splitRegions = new TextureRegion[largeVariants][][];
		for (int i = 1; i <= largeVariants; i++) {
			splitRegions[i - 1] = Core.atlas.find(name + "-large" + i).split(32, 32);
		}
	}

	// Copied from StaticWall
	protected boolean eqcpy(int rx, int ry) {
		return rx < world.width() - 1 && ry < world.height() - 1
			&& world.tile(rx + 1, ry).block() == this
			&& world.tile(rx, ry + 1).block() == this
			&& world.tile(rx, ry).block() == this
			&& world.tile(rx + 1, ry + 1).block() == this;
	}
}