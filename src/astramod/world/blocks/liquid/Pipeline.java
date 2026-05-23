package astramod.world.blocks.liquid;

import arc.func.Boolf;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.math.geom.*;
import arc.struct.Seq;
import arc.util.*;
import mindustry.content.Fx;
import mindustry.entities.units.BuildPlan;
import mindustry.input.Placement;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.*;
import astramod.world.meta.AstraStat;

import static mindustry.Vars.*;

public class Pipeline extends Conduit {
	public float heatCapacity = 0.5f;
	public @Nullable Block fallbackJunction, fallbackBridge;

	public Pipeline(String name) {
		super(name);
		botColor = Color.white;
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.liquidPressure, liquidPressure * 100, StatUnit.percent);
		stats.add(AstraStat.heatCapacity, heatCapacity * 100, StatUnit.percent);
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] {bottomRegion, topRegions[0]};
	}

	@Override public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
		if(fallbackJunction == null) return super.getReplacement(req, plans);

		Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x && o.y == req.y + p.y && (req.block instanceof Conduit || req.block instanceof LiquidJunction));
		return cont.get(Geometry.d4(req.rotation)) && cont.get(Geometry.d4(req.rotation - 2)) &&
			req.tile() != null && req.tile().block() instanceof Conduit &&
			Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ? getReplacement(junctionReplacement, fallbackJunction) : this;
	}

	@Override public void handlePlacementLine(Seq<BuildPlan> plans) {
		if (fallbackBridge == null) {
			super.handlePlacementLine(plans);
		} else if (rotBridgeReplacement != null) {
			Placement.calculateBridges(plans, (DirectionBridge)getReplacement(rotBridgeReplacement, fallbackBridge), true, b -> b instanceof Conduit);
		} else {
			Placement.calculateBridges(plans, (ItemBridge)getReplacement(bridgeReplacement, fallbackBridge));
		}
	}

	protected Block getReplacement(Block block, Block fallback) {
		var team = player.team();
		return state.rules.infiniteResources || team.rules().cheat || team.items().has(block.requirements) ? block : fallback;
	}

	public class PipelineBuild extends ConduitBuild {
		@Override public void update() {
			super.update();

			if (liquids.currentAmount() > 0.1f && liquids.current().temperature > heatCapacity) {
				float strength = liquids.currentAmount() * liquids.current().temperature / heatCapacity;
				damageContinuous(strength / 60f);
				if (Mathf.chanceDelta(strength / 100f)) Fx.fire.at(x, y);
			}
		}
	}
}