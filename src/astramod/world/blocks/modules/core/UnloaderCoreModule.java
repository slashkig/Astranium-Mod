package astramod.world.blocks.modules.core;

import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import astramod.world.blocks.modules.CoreModuleBlock;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

import static mindustry.Vars.*;

public class UnloaderCoreModule extends DirectionalUnloader {
	public UnloaderCoreModule(String name) {
		super(name);
		allowCoreUnload = true;
		isDuct = false;
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] { region, teamRegions[Team.sharded.id], arrowRegion };
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	@Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		super.drawPlanRegion(plan, list);
		Draw.rect(arrowRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
		if (plan.worldContext && player != null && teamRegion != null && teamRegion.found()) {
			if (teamRegions[player.team().id] == teamRegion) Draw.color(player.team().color);
			Draw.rect(teamRegions[player.team().id], plan.drawx(), plan.drawy());
			Draw.color();
		}
	}

	@Override public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
		drawPlanConfigCenter(plan, plan.config, name + "-center");
	}

	public class UnloaderCoreModuleBuild extends DirectionalUnloaderBuild implements CoreModuleBlock {
		protected @Nullable Building linkedCore;

		@Override public void draw() {
			super.draw();
			drawTeamTop();
		}

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
		}

		@Override @Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		@Override public void setLinkedCore(Building core) {
			linkedCore = core;
		}

		@Override public boolean canPickup() {
			return false;
		}
	}
}
