package astramod.math;

import arc.math.*;
import arc.math.geom.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.world.*;

import static arc.math.Mathf.*;

public final class Mathx {
	private Mathx() { }

	public static Rect squareRect(float xy, float size) {
		return new Rect(xy, xy, size, size);
	}

	public static float elerpDelta(float fromValue, float toValue, float progress) {
		return equal(fromValue, toValue) ? toValue : lerpDelta(fromValue, toValue, progress);
	}

	public static float elerpDelta(float fromValue, float toValue, float progress, float tolerance) {
		return equal(fromValue, toValue, tolerance) ? toValue : lerpDelta(fromValue, toValue, progress);
	}

	public static float dstm(Point2 p1, Point2 p2) {
		return Mathf.dstm(p1.x, p1.y, p2.x, p2.y);
	}

	public static Tile findClosestTile(Building from, Position to) {
		Tile best = null;
		float mindst = 0f;

		float dst;
		for (Point2 point : Edges.getInsideEdges(from.block.size)) {
			Tile other = Vars.world.tile(from.tile.x + point.x, from.tile.y + point.y);
			dst = to.dst2(other);
			if (other != null && (best == null || dst < mindst)) {
				best = other;
				mindst = dst;
			}
		}

		return best;
	}
}