package astramod.world.blocks.modular.block;

import arc.Core;
import arc.struct.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.BlockStatus;
import astramod.world.blocks.GenericBlock;
import astramod.world.blocks.modular.*;
import astramod.world.meta.*;

public class GenericBlockModule extends GenericBlock implements BlockModule {
	public @Nullable Block targetBlockType;
	public ObjectSet<Block> targetBlocks;

	public GenericBlockModule(String name) {
		super(name);
		update = true;
		rotate = true;
		rotateDraw = false;
		outputFacing = false;
	}

	@Override public void init() {
		localizedName = Core.bundle.get("module").replace("{0}", localizedName);
		if (targetBlocks == null) targetBlocks = ObjectSet.with(targetBlockType);

		targetBlocks.each(b -> ((BaseModularBlock)b).addValidModule(this));
		schematicPriority = -9;

		super.init();
	}

	@Override public void setStats() {
		super.setStats();
		if (targetBlockType != null) stats.add(AstraStat.parentBlock, AstraStatValues.block(targetBlockType));
		else stats.add(AstraStat.parentBlocks, AstraStatValues.blocks(targetBlocks.toSeq()));
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

	@Override public boolean validBlock(Block block) {
		return targetBlocks.contains(block);
	}

	public class GenericModuleBuild extends GenericBuild implements ModuleBuild {
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

		@Override public float drawrot() {
			return super.drawrot();
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