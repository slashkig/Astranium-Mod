package astramod.world.blocks.liquid;

import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;

import static mindustry.Vars.*;

import java.util.Arrays;

public class LargePipeline extends ArmoredPipeline {
	static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
	static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};

	public LargePipeline(String name) {
		super(name);
	}

	@Override public Block getReplacement(BuildPlan req, Seq<BuildPlan> plans) {
		if (junctionReplacement == null) return this;

		Boolf<Point2> cont = p -> plans.contains(o -> o.x == req.x + p.x * size && o.y == req.y + p.y * size &&
			(req.block instanceof LargePipeline || req.block instanceof LiquidJunction));
		Tile tile = req.tile();
		return cont.get(Geometry.d4(req.rotation)) && cont.get(Geometry.d4(req.rotation - 2)) &&
			tile != null && tile.block() instanceof LargePipeline && tile == tile.build.tile &&
			Mathf.mod(req.build().rotation - req.rotation, 2) == 1 ? junctionReplacement : this;
	}

	@Override public void handlePlacementLine(Seq<BuildPlan> plans) {
		return;
	}

	@Override @Nullable public int[] getTiling(BuildPlan req, Eachable<BuildPlan> list) {
		if (req.tile() == null) return null;
		BuildPlan[] directionals = new BuildPlan[4];
		Arrays.fill(directionals, null);

		list.each(other -> {
			if (other.breaking || other == req) return;

			int i = 0;
			for (Point2 point : Geometry.d4) {
				int x = req.x + point.x * size, y = req.y + point.y * size;
				if (x >= other.x - (other.block.size - 1) / 2 && x <= other.x + (other.block.size / 2) &&
				y >= other.y - (other.block.size - 1) / 2 && y <= other.y + (other.block.size / 2)) {
					directionals[i] = other;
				}
				i++;
			}
		});

		return buildBlending(req.tile(), req.rotation, directionals, req.worldContext);
	}

	@Override public boolean blends(Tile tile, int rotation, int direction) {
		Point2 dir = Geometry.d4(Mathf.mod(rotation - direction, 4));
        Building other = tile.nearby(dir.x * size, dir.y * size).build;
        return other != null && other.team == tile.team() && blends(tile, rotation, other.tileX(), other.tileY(), other.rotation, other.block);
    }

	@Override public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
		return (otherblock.size == size && (otherblock instanceof LiquidJunction ||
			(otherblock instanceof LargePipeline && blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock)))) ||
			(otherblock instanceof LiquidRouter && otherblock.size >= size &&
			(lookingAt(tile, rotation, otherx, othery, otherblock) || blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock)));
	}

	@Override public boolean blendsArmored(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
		return Point2.equals(tile.x + Geometry.d4(rotation).x * size, tile.y + Geometry.d4(rotation).y * size, otherx, othery) ||
			((!otherblock.rotatedOutput(otherx, othery) && Edges.getFacingEdge(otherblock, otherx, othery, tile) != null &&
			Edges.getFacingEdge(otherblock, otherx, othery, tile).relativeTo(tile) == rotation) ||
			(otherblock.rotatedOutput(otherx, othery) &&
			Point2.equals(otherx + Geometry.d4(otherrot).x * size, othery + Geometry.d4(otherrot).y * size, tile.x, tile.y)));
	}

	public class LargePipelineBuild extends ArmoredConduitBuild {

		@Override protected void drawAt(float x, float y, int bits, int rotation, SliceMode slice) {
			float angle = rotation * 90f;
			Draw.color(botColor);
			Draw.rect(sliced(botRegions[bits], slice), x, y, angle);

			int offset = yscl == -1 ? 3 : 0;

			int frame = liquids.current().getAnimationFrame();
			int gas = liquids.current().gas ? 1 : 0;
			float ox = 0f, oy = 0f;
			int wrapRot = (rotation + offset) % 4;
			TextureRegion liquidr = bits == 1 && padCorners ? rotateRegions[wrapRot][gas][frame] : renderer.fluidFrames[gas][frame];

			if (bits == 1 && padCorners) {
				ox = rotateOffsets[wrapRot][0] * size;
				oy = rotateOffsets[wrapRot][1] * size;
			}

			float xscl = Draw.xscl, yscl = Draw.yscl;
			Draw.scl(size, size);
			Drawf.liquid(sliced(liquidr, slice), x + ox, y + oy, smoothLiquid, liquids.current().color.write(Tmp.c1).a(1f));
			Draw.scl(xscl, yscl);

			Draw.rect(sliced(topRegions[bits], slice), x, y, angle);
		}

		@Override public float moveLiquidForward(boolean leaks, Liquid liquid) {
			Building next = front();
			if (next != null && ((next instanceof LargePipelineBuild || next.block instanceof LiquidJunction) &&
			next.block.size == size || next.block instanceof LiquidRouter && next.block.size >= size)) {
				return moveLiquid(next, liquid);
			}
			else return 0.0f;
		}

		@Nullable @Override public Building next() {
			Tile next = tile.nearby(Geometry.d4x(rotation) * size, Geometry.d4y(rotation) * size);
			if (next != null && next.build instanceof LargePipelineBuild) {
				return next.build;
			}
			else return null;
		}

		@Override public boolean acceptLiquid(Building source, Liquid liquid) {
			noSleep();
			if (liquids.current() == liquid || liquids.currentAmount() < 0.2f && source instanceof LargePipelineBuild ||
			source.block instanceof LiquidJunction || source.block instanceof LiquidRouter) {
				return checkSide(source, (rotation + 2) % 4);
			}
			else return false;
		}

		public boolean checkSide(Building other, int direction) {
			for (int i = 0; i < size; i++) {
				nearbySide(tile.x, tile.y, direction, i, Tmp.p1);
				Tile near = world.tile(Tmp.p1.x, Tmp.p1.y);
				if (near == null || !(near.build == other)) return false;
			}
			return true;
		}
	}
}