package astramod.world.blocks.modules.block;

import arc.graphics.g2d.TextureRegion;
import arc.math.geom.*;
import arc.struct.Seq;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.draw.*;
import astramod.world.blocks.modules.*;

import static mindustry.Vars.*;

public class GenericBlockModule extends Block {
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

	@Override public void load() {
		super.load();
		drawer.load(this);
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
		return checkEdge(tile.x, tile.y, rotation);
	}

	public boolean checkEdge(int x, int y, int rotation) {
		Point2 edge = new Point2();
		nearbySide(x, y, rotation, 0, edge);
		Building build = world.build(edge.x, edge.y);
		if (!validLink(build)) return false;

		for (int i = 1; i < size; i++) {
			nearbySide(x, y, rotation, i, edge);
			if (build != world.build(edge.x, edge.y)) return false;
		}
		return true;
	}

	public boolean validLink(Building build) {
		return targetBlockType == null && build instanceof BaseModularBlock || build != null && build.block == targetBlockType;
	}

	public class GenericModuleBuild extends Building implements ModuleBlock {
		public @Nullable Building linkedBuild;

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			if (checkEdge(tile.x, tile.y, rotation)) setLinkedBuild(front());
		}

		@Override public void draw() {
			drawer.draw(this);
		}

		@Override public void drawSelect() {
			if (linkedBuild != null) {
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