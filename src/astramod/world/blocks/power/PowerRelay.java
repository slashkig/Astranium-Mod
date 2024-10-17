package astramod.world.blocks.power;

import arc.Core;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.gen.Building;
import mindustry.world.*;
import mindustry.world.blocks.power.*;

import static mindustry.Vars.*;

public class PowerRelay extends PowerNode {
	TextureRegion glowRegion;

	public PowerRelay(String name) {
		super(name);
		conductivePower = false;
	}

	@Override public void load() {
		super.load();

		glowRegion = Core.atlas.find(name + "-glow");
	}

	@Override public boolean linkValid(Building tile, Building link, boolean checkMaxNodes) {
		return link instanceof PowerNodeBuild && super.linkValid(tile, link, checkMaxNodes);
	}

	@Override protected void getPotentialLinks(Tile tile, Team team, Cons<Building> others) {
		if (!autolink) return;

		Boolf<Building> valid = other -> {
			return other != null && other.tile() != tile && other.block.connectedPower && other instanceof PowerNodeBuild oNode &&
			overlaps(tile.x * tilesize + offset, tile.y * tilesize + offset, other.tile(), laserRange * tilesize) && other.team == team &&
			!graphs.contains(other.power.graph) && !insulated(tile, other.tile) && oNode.power.links.size < ((PowerNode)oNode.block).maxNodes &&
			!Structs.contains(Edges.getEdges(size), p -> {
				var t = world.tile(tile.x + p.x, tile.y + p.y);
				return t != null && t.build == other;
			});
		};

		tempBuilds.clear();
		graphs.clear();

		//add conducting graphs to prevent double link
		for (var p : Edges.getEdges(size)) {
			Tile other = tile.nearby(p);
			if (other != null && other.team() == team && other.build != null && other.build.power != null) {
				graphs.add(other.build.power.graph);
			}
		}

		if (tile.build != null && tile.build.power != null) {
			graphs.add(tile.build.power.graph);
		}

		var worldRange = laserRange * tilesize;
		var tree = team.data().buildingTree;
		if (tree != null) {
			tree.intersect(tile.worldx() - worldRange, tile.worldy() - worldRange, worldRange * 2, worldRange * 2, build -> {
				if (valid.get(build) && !tempBuilds.contains(build)) {
					tempBuilds.add(build);
				}
			});
		}

		tempBuilds.sort((a, b) -> {
			int type = -Boolean.compare(a.block instanceof PowerNode, b.block instanceof PowerNode);
			if (type != 0) return type;
			return Float.compare(a.dst2(tile), b.dst2(tile));
		});

		returnInt = 0;

		tempBuilds.each(valid, t -> {
			if (returnInt++ < maxNodes) {
				graphs.add(t.power.graph);
				others.get(t);
			}
		});
	}

	public class PowerRelayBuild extends PowerNodeBuild {
		@Override public void draw() {
			super.draw();

			if (power.graph.getPowerProduced() > 0f) {
				Draw.rect(glowRegion, x, y);
			}
		}
	}
}
