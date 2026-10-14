package astramod.math;

import arc.math.*;
import arc.math.geom.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.world.*;

import static arc.math.Mathf.*;

public final class MathUtil {
	private MathUtil() { }

	/** @return 0 if odd, 1 if even. */
	public static int oddEven(int num) {
		return (num + 1) % 2;
	}

	public static float min(float... values) {
		float min = Float.MAX_VALUE;
		for (float val : values) {
			min = Math.min(val, min);
		}
		return min;
	}

	public static float pow2(float num) {
		return num * num;
	}

	public static float snapZero(float val, float tolerance) {
		return Mathf.zero(val, tolerance) ? 0f : val;
	}

	public static Rect squareRect(float xy, float size) {
		return new Rect(xy, xy, size, size);
	}

	public static float elerpDelta(float fromValue, float toValue, float progress) {
		return equal(fromValue, toValue) ? toValue : lerpDelta(fromValue, toValue, progress);
	}

	public static float elerpDelta(float fromValue, float toValue, float progress, float tolerance) {
		return equal(fromValue, toValue, tolerance) ? toValue : lerpDelta(fromValue, toValue, progress);
	}

	/** Manhattan distance between two points. */
	public static float dstm(Point2 p1, Point2 p2) {
		return Mathf.dstm(p1.x, p1.y, p2.x, p2.y);
	}

	/** @return The signed angular difference of {@code target} relative to {@code base}, between -180 and 180. */
	public static float angleDiff(float base, float target) {
		return Mathf.mod(target - base + 180f, 360f) - 180f;
	}

	/** @return The direction of {@code target} relative to {@code base}: 0 if equal, -1 if left, 1 if right. */
	public static int angleBearing(float base, float target) {
		float angle = angleDiff(base, target);
		return Mathf.equal(angle, 0f) ? 0 : Mathf.sign(angle);
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