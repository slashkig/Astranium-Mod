package astramod.world.draw;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Eachable;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.draw.*;

/** Integrates DrawDefault and DrawTeam into DrawMulti. */
public class DrawMultiIntegrated extends DrawMulti {
	public int iconCutoff = 0;

	public DrawMultiIntegrated(DrawBlock... otherDrawers) {
		super(otherDrawers);
	}

	public DrawMultiIntegrated(int includeIconsUpTo, DrawBlock... otherDrawers) {
		super(otherDrawers);
		iconCutoff = includeIconsUpTo;
	}

	public DrawMultiIntegrated(Seq<DrawBlock> otherDrawers) {
		super(otherDrawers);
	}

	@Override public void draw(Building build) {
		Draw.rect(build.block.region, build.x, build.y, build.drawrot());
		build.drawTeamTop();
		super.draw(build);
	}

	@Override public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		block.drawDefaultPlanRegion(plan, list);
		super.drawPlan(block, plan, list);
	}

	@Override public TextureRegion[] icons(Block block) {
		var icons = new Seq<TextureRegion>().add(block.region);
		if (block.teamRegion.found()) icons.add(block.teamRegions[Team.sharded.id]);

		if (iconCutoff > drawers.length) iconCutoff = drawers.length;
		for (int i = 0; i < iconCutoff; i++) {
			icons.addAll(drawers[i].icons(block));
		}
		return icons.toArray(TextureRegion.class);
	}
}
