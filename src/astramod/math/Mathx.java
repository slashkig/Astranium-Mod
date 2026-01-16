package astramod.math;

import arc.math.geom.*;

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
}