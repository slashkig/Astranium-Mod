package astramod.world.consumers;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import astramod.world.blocks.modular.*;

public class ModuleConsumeLiquid extends ConsumeLiquid {
	public ModuleConsumeLiquid(Liquid liquid, float amount) {
        super(liquid, amount);
	}

	protected Building getLinkedBuild(Building build) {
		return ((ModuleBuild)build).getLinkedBuild();
	}

	@Override public void apply(Block block) {}

	@Override public void build(Building build, Table table) {
		table.add(new ReqImage(liquid.uiIcon, () -> {
			Building linked = getLinkedBuild(build);
			return linked != null && linked.liquids.get(liquid) > 0;
		})).size(Vars.iconMed).top().left();
	}

	@Override public void update(Building build) {
		Building linked = getLinkedBuild(build);
		if (linked != null) linked.liquids.remove(liquid, amount * build.edelta() * multiplier.get(build));
	}

	@Override public float efficiency(Building build) {
		Building linked = getLinkedBuild(build);
		float ed = build.edelta() * build.efficiencyScale();
		if (linked == null || Mathf.zero(ed)) return 0f;
        else return Math.min(linked.liquids.get(liquid) / (amount * ed * multiplier.get(build)), 1f);
	}
}
