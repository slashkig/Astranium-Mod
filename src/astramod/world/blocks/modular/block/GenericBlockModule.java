package astramod.world.blocks.modular.block;

import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import astramod.world.blocks.modular.*;
import mindustry.entities.units.BuildPlan;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class GenericBlockModule extends Block implements BlockModule {
	public @Nullable Block targetBlockType;
	public DrawBlock drawer = new DrawDefault();

	public GenericBlockModule(String name) {
		super(name);
		update = true;
		solid = true;
		rotate = true;
		rotateDraw = false;
		outputFacing = false;
	}

	@Override public void init() {
		schematicPriority = targetBlockType.schematicPriority - 1;
		super.init();
	}

	@Override public void load() {
		super.load();
		drawer.load(this);
	}

	@Override public Block parentBlock() {
		return targetBlockType;
	}

	@Override public boolean rotatedOutput(int x, int y) {
		return false;
	}

	@Override public TextureRegion[] icons() {
		return drawer.finalIcons(this);
	}

	@Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		return canPlaceModule(tile.x, tile.y, rotation);
	}

	public class GenericModuleBuild extends Building implements ModuleBuild {
		public @Nullable Building linkedBuild;

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();
			if (checkFront(tile.x, tile.y, rotation)) setLinkedBuild(front());
		}

		@Override public void draw() {
			drawer.draw(this);
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
	}
}