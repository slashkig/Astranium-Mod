package astramod.world.blocks.modular.block;

import static mindustry.Vars.tilesize;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import astramod.world.blocks.modular.*;
import astramod.world.consumers.*;
import astramod.world.meta.*;

public class GeneratorBlockModule extends ConsumeGenerator implements BlockModule {
	public Block targetBlockType;

	public GeneratorBlockModule(String name) {
		super(name);
		rotate = true;
		rotateDraw = false;
		outputFacing = false;
	}

	@Override public void init() {
		((BaseModularBlock)targetBlockType).addValidModule(this);
		schematicPriority = -9;
		super.init();
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.parentBlock, AstraStatValues.block(targetBlockType));
	}

	@Override public void drawOverlay(float x, float y, int rotation) {
		ConsumeLiquid consLiquid = findConsumer(cons -> cons instanceof ModuleConsumeLiquid);
		if (consLiquid != null) {
			Draw.rect(consLiquid.liquid.fullIcon, x + Geometry.d4x(rotation) * (size * tilesize / 2f + 4), y + Geometry.d4y(rotation) * (size * tilesize / 2f + 4), 8f, 8f);
		}
	}

	public Block targetBlock() {
		return targetBlockType;
	}

	@Override public boolean rotatedOutput(int x, int y) {
		return false;
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return canPlaceModule(tile.x, tile.y, rotation);
	}

	@Override public ConsumeLiquid consumeLiquid(Liquid liquid, float amount) {
		return consume(new ModuleConsumeLiquid(liquid, amount));
	}

	public class GeneratorModuleBuild extends ConsumeGeneratorBuild implements ModuleBuild {
		public @Nullable Building linkedBuild;

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();
			if (checkFront(tile.x, tile.y, rotation)) setLinkedBuild(front());
			else setLinkedBuild(null);
		}

		@Override public void drawSelect() {
			if (linkedBuild != null) {
				Drawf.selected(linkedBuild, Pal.accent);
				linkedBuild.drawSelect();
			}
			super.drawSelect();
		}

		@Nullable public Building getLinkedBuild() {
			return linkedBuild;
		}

		public void setLinkedBuild(Building build) {
			linkedBuild = build;
		}

		@Override public BlockStatus status() {
			return linkedBuild == null ? BlockStatus.noInput : super.status();
		}
	}
}