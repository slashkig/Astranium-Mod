package astramod.world.blocks.defense;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.scene.ui.layout.Table;
import arc.struct.*;
import mindustry.Vars;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.world.*;
import astramod.graphics.*;
import astramod.world.meta.*;

public class Mine extends Block {
	public float damageResistFactor = 0.5f;

	public float drawAlpha = 1f;
	public boolean cloaked = false; // TODO units can still be commanded to attack this when cloaked

	public Mine(String name) {
		super(name);
		update = false;
		destructible = true;
		solid = false;
		targetable = false;
		underBullets = true;
		squareSprite = false;
		hasShadow = false;
		destroyBulletSameTeam = true;
	}

	@Override public void init() {
		super.init();
		if (cloaked) drawTeamOverlay = false;
	}

	@Override public void setStats() {
		super.setStats();
		stats.addPercent(AstraStat.damageResistance, (1f - damageResistFactor));
		stats.add(AstraStat.detonation, AstraStatValues.astraAmmo(ObjectMap.of(this, destroyBullet)));
	}

	@Override public TextureRegion[] icons() {
		return teamRegion.found() ? new TextureRegion[] { region, teamRegions[Team.sharded.id] } : new TextureRegion[] { region };
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Tile t : tile.getLinkedTilesAs(this, tempTiles)) {
			if (!validTile(t)) return false;
		}

		for (Point2 edge : Edges.getEdges(size)) {
			if (Vars.world.build(tile.x + edge.x, tile.y + edge.y) instanceof LandMineBuild) return false;
		}
		return true;
	}

	public boolean validTile(Tile tile) {
		return placeableLiquid || !tile.floor().isLiquid;
	}

	@Override public boolean canReplace(Block other) {
		return other.alwaysReplace || !other.privileged && other != this && other instanceof Mine;
	}

	@Override public void changePlacementPath(Seq<Point2> points, int rotation) {
		if (points.size > 1) {
			Point2 origin = points.first(), point;
			float maxDist = points.peek().dst(origin);
			for (int i = 1; i < points.size; i++) {
				point = points.get(i).add(Geometry.d4x(rotation) * i * size, Geometry.d4y(rotation) * i * size);
				if (point.dst(origin) > maxDist) {
					points.removeRange(i, points.size - 1);
					return;
				}
			}
		}
	}

	@Override public int minimapColor(Tile tile) {
		return (!cloaked || tile.team() == Vars.player.team() ? AstraPal.teamFaded[tile.team().id] : tile.floor().mapColor).rgba();
	}

	public class LandMineBuild extends Building {
		@Override public void draw() {
			if (!cloaked || team == Vars.player.team()) {
				Draw.alpha(drawAlpha);
				super.draw();
				Draw.reset();
			}
		}

		@Override public void drawCracks() { }

		@Override public void unitOn(Unit unit) {
			if (enabled && unit.team != team) kill();
		}

		@Override public void damage(float damage) {
			super.damage(damage * damageResistFactor);
		}

		@Override public void control(LAccess type, double p1, double p2, double p3, double p4) {
			if (type == LAccess.shoot && p3 == 1) kill();
			super.control(type, p1, p2, p3, p4);
		}

		@Override public void control(LAccess type, Object p1, double p2, double p3, double p4) {
			if (type == LAccess.shootp && p2 == 1) kill();
			super.control(type, p1, p2, p3, p4);
		}

		@Override public void display(Table table) {
			if (cloaked && team != Vars.player.team()) {
				Vars.world.tileWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY()).display(table);
			} else super.display(table);
		}
	}
}