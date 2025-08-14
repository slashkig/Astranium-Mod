package astramod.math;

import arc.math.geom.*;

public final class Mathx {
	private Mathx() { }

	public static Rect squareRect(float xy, float size) {
		return new Rect(xy, xy, size, size);
	}
}
