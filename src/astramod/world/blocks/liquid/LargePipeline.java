package astramod.world.blocks.liquid;

import java.util.Arrays;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;

import static mindustry.Vars.*;

public class LargePipeline extends ArmoredPipeline {
	static final float rotatePad = 6, hpad = rotatePad / 2f / 4f;
	static final float[][] rotateOffsets = {{hpad, hpad}, {-hpad, hpad}, {-hpad, -hpad}, {hpad, -hpad}};

	public LargePipeline(String name) {
		super(name);
		solid = true;
		placeableLiquid = true;
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
		int dir = Mathf.mod(rotation - direction, 4);
        Tile other = tile.nearby(Geometry.d4x(dir) * size, Geometry.d4y(dir) * size);
        return other != null && other.build != null && other.team() == tile.team() &&
			blends(tile, rotation, other.x, other.y, other.build.rotation, other.build.block);
    }

	@Override public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
		if (tile.build instanceof LargePipelineBuild p) return p.checkSide(world.build(otherx, othery)) && super.blends(tile, rotation, otherx, othery, otherrot, otherblock);
		else if (otherblock instanceof LargePipeline || otherblock instanceof LiquidJunction) return otherblock.size == size && (blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock) || lookingAt(tile, rotation, otherx, othery, otherblock));
		else return otherblock.size >= size && super.blends(tile, rotation, otherx, othery, otherrot, otherblock);
	}

	@Override public boolean blendsArmored(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
		return Point2.equals(tile.x + Geometry.d4x(rotation) * size, tile.y + Geometry.d4y(rotation) * size, otherx, othery) ||
			((!otherblock.rotatedOutput(otherx, othery) && Edges.getFacingEdge(otherblock, otherx, othery, tile) != null &&
			Edges.getFacingEdge(otherblock, otherx, othery, tile).relativeTo(tile) == rotation) ||
			(otherblock.rotatedOutput(otherx, othery) &&
			Point2.equals(otherx + Geometry.d4x(otherrot) * size, othery + Geometry.d4y(otherrot) * size, tile.x, tile.y)));
	}

	@Override public int[] buildBlending(Tile tile, int rotation, BuildPlan[] directional, boolean world) {
		int[] blendresult = super.buildBlending(tile, rotation, directional, world);
		blendresult[4] = 0;
		for (int i = 0; i < 4; i++) {
			if (tile != null && tile.build != null && blends(tile, rotation, directional, i, world)) {
				int realDir = Mathf.mod(rotation - i, 4);
				int tileOffset = realDir <= 1 ? size : 1;
				Building build = tile.build.nearby(Geometry.d4x(realDir) * tileOffset, Geometry.d4y(realDir) * tileOffset);
				if (build != null && !build.block.squareSprite) {
					blendresult[4] |= (1 << i);
				}
			}
		}
		return blendresult;
	}

	public class LargePipelineBuild extends ArmoredConduitBuild {
		@Override public void draw() {
			int r = this.rotation;

			//draw extra conduits facing this one for tiling purposes
			Draw.z(Layer.blockUnder);
			for (int i = 0; i < 4; i++) {
				if ((blending & (1 << i)) != 0) {
					int dir = r - i;
					float offsetMult = size * tilesize * 0.75f;
					drawAt(x + Geometry.d4x(dir) * offsetMult, y + Geometry.d4y(dir) * offsetMult, 0, i == 0 ? r : dir, i != 0 ? SliceMode.bottom : SliceMode.top);
				}
			}

			Draw.z(Layer.block);

			Draw.scl(xscl, yscl);
			drawAt(x, y, blendbits, r, SliceMode.none);
			Draw.reset();

			if (capped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg());
			if (backCapped && capRegion.found()) Draw.rect(capRegion, x, y, rotdeg() + 180);
		}

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
			if (next != null && checkSide(next)) {
				return moveLiquid(next, liquid);
			}
			else return 0.0f;
		}

		@Nullable @Override public Building next() {
			int tileOffset = rotation <= 1 ? size : 1;
			Building next = nearby(Geometry.d4x(rotation) * tileOffset, Geometry.d4y(rotation) * tileOffset);
			if (next != null && next instanceof LargePipelineBuild && next.block.size == size) {
				return next;
			}
			else return null;
		}

		@Override public boolean acceptLiquid(Building source, Liquid liquid) {
			return super.acceptLiquid(source, liquid) && checkSide(source);
		}

		public boolean checkSide(Building other) {
			if (other == null || other.block.size < size || other.block instanceof Conduit && other.block.size != size) return false;
			int direction = relativeTo(other);
			for (int i = 0; i < size; i++) {
				nearbySide(tile.x, tile.y, direction, i, Tmp.p1);
				Tile near = world.tile(Tmp.p1.x, Tmp.p1.y);
				if (near == null || !(near.build == other)) return false;
			}
			return true;
		}
	}
}