package astramod.world.blocks.distribution;

import arc.graphics.g2d.Draw;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.distribution.DirectionalUnloader;

public class AstraDirectionalUnloader extends DirectionalUnloader {
	public AstraDirectionalUnloader(String name) {
		super(name);
		isDuct = false;
		allowCoreUnload = true;
	}

	@Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		super.drawPlanRegion(plan, list);
		if (plan.config == null) Draw.rect(arrowRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
	}

	@Override public void drawPlanConfig(BuildPlan plan, Eachable<BuildPlan> list) {
		drawPlanConfigCenter(plan, plan.config, name + "-center");
	}
}