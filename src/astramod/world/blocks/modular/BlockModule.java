package astramod.world.blocks.modular;

import arc.func.Boolf;
import arc.math.geom.*;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.*;

import static mindustry.Vars.*;

public interface BlockModule {
	public Block parentBlock();

	public default boolean canPlaceModule(int x, int y, int rot) {
		if (checkFront(x, y, rot)) {
			return true;
		} else {
			Boolf<BuildPlan> pred = plan -> plan.block == parentBlock() ? plan.placeable(player.team()) && checkHitbox(plan, x, y, rot) : false;
			return control.input.selectPlans.find(pred) != null || player.unit().plans.indexOf(pred) != -1;
		}
	}

	public default boolean checkFront(int x, int y, int rot) {
		Block self = (Block)this;
		Point2 edge = new Point2();
		self.nearbySide(x, y, rot, 0, edge);

		Building build = world.build(edge.x, edge.y);
		if (!validLink(build)) return false;

		for (int i = 1; i < self.size; i++) {
			self.nearbySide(x, y, rot, i, edge);
			if (build != world.build(edge.x, edge.y)) return false;
		}
		return true;
	}

	public default boolean checkHitbox(BuildPlan plan, int x, int y, int rot) {
		Block self = (Block)this;
		Point2 edge = new Point2();
		Rect box = new Rect();
		plan.hitbox(box);
		box.move(0.5f * tilesize, 0.5f * tilesize);

		for (int i = 0; i < self.size; i++) {
			self.nearbySide(x, y, rot, i, edge);
			if (!box.contains((edge.x + 0.5f) * tilesize, (edge.y + 0.5f) * tilesize)) return false;
		}
		return true;
	}

	public default boolean validLink(Building build) {
		return parentBlock() == null && build instanceof BaseModularBlock || build != null && build.block == parentBlock();
	}
}