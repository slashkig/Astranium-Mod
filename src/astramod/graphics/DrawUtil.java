package astramod.graphics;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.graphics.*;

public final class DrawUtil {
	private static final Vec2[] vecs = new Vec2[6];

	static {
		for (int i = 0; i < vecs.length; i++) {
			vecs[i] = new Vec2();
		}
	}

	public static void drawPhase(TextureRegion region, float x, float y, float alpha) {
		Draw.draw(Layer.blockOver, () -> {
			AstraShaders.phase.region = region;
			AstraShaders.phase.alpha = alpha;

			Draw.shader(AstraShaders.phase);
			Draw.rect(region, x, y);
			Draw.shader();
		});
	}

	public static void quad(Position p1, Position p2, Position p3, Position p4) {
		Fill.quad(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY(), p4.getX(), p4.getY());
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float size) {
		chevron(pos.getX(), pos.getY(), size);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float size, float rot) {
		chevron(pos.getX(), pos.getY(), size, rot);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float w, float h, float rot) {
		chevron(pos.getX(), pos.getY(), w, h, rot);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float size, @Nullable Color outline) {
		chevron(pos.getX(), pos.getY(), size, outline);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float size, float rot, @Nullable Color outline) {
		chevron(pos.getX(), pos.getY(), size, rot, outline);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(Position pos, float w, float h, float rot, @Nullable Color outline) {
		chevron(pos.getX(), pos.getY(), w, h, rot, outline);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(float x, float y, float size) {
		chevron(x, y, size, 0f);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(float x, float y, float size, float rot) {
		chevron(x, y, size, size, rot);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(float x, float y, float w, float h, float rot) {
		chevron(x, y, w, h, rot, null);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(float x, float y, float size, @Nullable Color outline) {
		chevron(x, y, size, 0f, outline);
	}

	/** See {@link #chevron(float, float, float, float, float, boolean)}. */
	public static void chevron(float x, float y, float size, float rot, @Nullable Color outline) {
		chevron(x, y, size, size, rot, outline);
	}

	/** Draws a chevron arrow. */
	public static void chevron(float x, float y, float w, float h, float rot, @Nullable Color outline) {
		for (int i = 0; i < vecs.length; i++) {
			chevronVert(i, w, h).rotate(rot).add(x, y);
		}

		quad(vecs[0], vecs[1], vecs[2], vecs[3]);
		quad(vecs[3], vecs[4], vecs[5], vecs[0]);

		if (outline != null) {
			Color color = Draw.getColor();
			Draw.color(outline);
			Draw.alpha(color.a);
			Lines.poly(vecs, 0f, 0f, 1f);
			Draw.color(color);
		}
	}

	private static Vec2 chevronVert(int i, float w, float h) {
		return switch (i) {
			case 1 -> vecs[i].set(-w / 2f, -h / 2f);
			case 2 -> vecs[i].set(-w / 2f, 0);
			case 3 -> vecs[i].set(0, h / 2f);
			case 4 -> vecs[i].set(w / 2f, 0);
			case 5 -> vecs[i].set(w / 2f, -h / 2f);
			default -> vecs[0].setZero();
		};
	}
}