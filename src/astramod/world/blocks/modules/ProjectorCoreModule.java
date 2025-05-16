package astramod.world.blocks.modules;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

import static mindustry.Vars.*;

public class ProjectorCoreModule extends ForceProjector {
	TextureRegion blockTopRegion;

	public ProjectorCoreModule(String name) {
		super(name);
		hasLiquids = false;
	}

	@Override public void load() {
		super.load();

		blockTopRegion = Core.atlas.find(name + "-block-top");
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id], blockTopRegion } : new TextureRegion[] { region, blockTopRegion };
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	@Override public void drawDefaultPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (plan.worldContext && player != null && teamRegion != null && teamRegion.found()) {
			Draw.rect(region, plan.drawx(), plan.drawy());
			Draw.color(player.team().color);
			Draw.rect(teamRegion, plan.drawx(), plan.drawy());
			Draw.color();
			Draw.rect(blockTopRegion, plan.drawx(), plan.drawy());
		} else {
			Draw.rect(getPlanRegion(plan, list), plan.drawx(), plan.drawy(), !rotate || !rotateDraw ? 0 : plan.rotation * 90);
		}

		drawPlanConfig(plan, list);
	}

	public class ShieldCoreModuleBuild extends ForceBuild implements CoreModuleBlock {
		protected @Nullable Building linkedCore;

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
		}

		@Override public void drawTeamTop() {
			super.drawTeamTop();

			Draw.rect(blockTopRegion, x, y);
		}

		@Override public boolean acceptItem(Building source, Item item) {
			return linkedCore != null && linkedCore.acceptItem(source, item);
		}

		@Override public void handleStack(Item item, int amount, Teamc source) {
			linkedCore.handleStack(item, amount, source);
		}

		@Override @Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		@Override public void setLinkedCore(Building core) {
			linkedCore = core;
		}
	}
}