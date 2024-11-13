package astramod.world.blocks.power;

import arc.Core;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.Seq;
import arc.util.*;
import astramod.world.blocks.power.SwitchRelay.SwitchRelayBuild;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.gen.Building;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;

import static mindustry.Vars.*;

public class PowerRelay extends PowerNode {
	public float warmupSpeed = 0.02f;
	public float glowAlpha = 0.75f;
	public float glowMag = 0.25f;
	public float glowScl = 60f;
	public TextureRegion glowRegion, laserGlow, laserGlowEnd;

	public PowerRelay(String name) {
		super(name);

		laserScale = 0.5f;
		laserColor1 = Color.white;
		laserColor2 = Color.valueOf("ffe08f");
	}

	@Override public void load() {
		super.load();

		glowRegion = Core.atlas.find(name + "-glow");
		laser = Core.atlas.find("astramod-power-relay-cable");
		laserEnd = Core.atlas.find("astramod-power-relay-cable-end");
		laserGlow = Core.atlas.find("astramod-power-relay-cable-glow");
		laserGlowEnd = Core.atlas.find("astramod-power-relay-cable-glow-end");
	}

	@Override public boolean linkValid(Building tile, Building link, boolean checkMaxNodes) {
		if (tile != link && link != null && link.block.connectedPower && tile.team == link.team && link.block instanceof PowerNode node) {
			return (overlaps(tile, link, laserRange * tilesize) || overlaps(link, tile, node.laserRange * tilesize)) &&
				(!checkMaxNodes || link.power.links.size < node.maxNodes || link.power.links.contains(tile.pos()));
		}
		else return false;
	}

	public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2, float warmup) {
		float angle1 = Angles.angle(x1, y1, x2, y2),
			vx = Mathf.cosDeg(angle1), vy = Mathf.sinDeg(angle1),
			len1 = size1 * tilesize / 2f - 1.5f, len2 = size2 * tilesize / 2f - 1.5f;

		Draw.alpha(Renderer.laserOpacity);
		Drawf.laser(laser, laserEnd, x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, laserScale);
		if (warmup > 0.001f) {
			Draw.alpha(Renderer.laserOpacity * (glowAlpha + Mathf.sin(glowScl, glowMag)) * warmup);
			Drawf.laser(laserGlow, laserGlowEnd, x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, laserScale);
		}
	}

	@Override protected boolean overlaps(float srcx, float srcy, Tile other, Block otherBlock, float range) {
		return otherBlock instanceof PowerNode && super.overlaps(srcx, srcy, other, otherBlock, range);
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

		// Add conducting graphs to prevent double link
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
		public float warmup = 0f;

		@Override public void draw() {
			Draw.rect(region, x, y);
			if (Mathf.zero(Renderer.laserOpacity) || isPayload()) return;

			Draw.z(Layer.blockOver);
			warmup = Mathf.approachDelta(warmup, warmupTarget(), warmupSpeed);
			Draw.color(laserColor2);
			Draw.alpha(warmup);
			Draw.rect(glowRegion, x, y);

			Draw.z(Layer.power);
			Draw.color(Color.white);
			for (int i = 0; i < power.links.size; i++) {
				Building link = world.build(power.links.get(i));

				if (!linkValid(this, link) || link.block instanceof PowerNode && link.id >= id) continue;

				drawLaser(x, y, link.x, link.y, size, link.block.size, isInactiveSwitch(link) ? link.warmup() : warmup);
			}

			Draw.reset();
		}

		@Override public Seq<Building> getPowerConnections(Seq<Building> out) {
			super.getPowerConnections(out);
			for (Building build : out) {
				if (isInactiveSwitch(build)) out.remove(build);
			}
			return out;
		}

		@Override public float warmup() {
			return warmup;
		}

		public float warmupTarget() {
			return power.graph.getSatisfaction();
		}

		protected boolean isInactiveSwitch(Building build) {
			return build instanceof SwitchRelayBuild && !build.enabled;
		}
	}
}