package astramod.entities;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import astramod.math.*;

public final class UnitUtil {
	private static final Queue<Tile> tileQueue = new Queue<>();
	private static final ObjectSet<Tile> tileSet = new ObjectSet<>();

	public static void knockback(Unit unit, Position origin, float knockback, float radius) {
		applyForce(unit, origin, knockback * 80f * (1f - (unit.dst(origin) / radius)));
	}

	public static void attract(Unit unit, Position origin, float strength, float radius) {
		applyForce(unit, origin, strength * -80f * Mathf.clamp(1f - Mathf.sqrt(unit.dst(origin) / radius)));
	}

	public static void applyForce(Unit unit, Position from, float force) {
		Tmp.v2.set(unit).sub(from).nor().scl(force);
		unit.impulse(Tmp.v2);
	}

	public static float targetPriority(Teamc e) {
		return e instanceof Building b ? b.block.priority : e instanceof Unit u ? u.type.targetPriority : 0f;
	}

	public static boolean withinFiringArc(Unit unit, Teamc target, Weapon weapon) {
		return weapon.rotationLimit > 360f || Angles.angleDist(unit.angleTo(target), unit.rotation() + weapon.baseRotation) < weapon.rotationLimit / 2f;
	}

	public static boolean isEmptyTile(Tile tile) {
		return tile != null && !tile.solid() && !tile.dangerous() && tile.inMapArea() && !Units.anyEntities(tile);
	}

	/** Searches for the closest empty tile to {@code pos} under the following conditions:
	 * @param pos - The position to start from.
	 * @param target - The position to search towards.
	 * @param maxDist - The maximum distance from {@code target} that can be returned.
	 * @return The tile at {@code pos} if it's empty, otherwise the first tile found by the search.
	 * */
	public static Tile closestEmptyTile(Posc pos, Posc target, float maxDist) {
		Tile origin = pos.tileOn();
		if (isEmptyTile(origin)) return origin;

		tileQueue.clear();
		tileQueue.add(origin);
		tileSet.clear();
		tileSet.add(origin);
		Tile dest = target.tileOn();
		Tile current, adj;
		maxDist = MathUtil.pow2(maxDist);

		while (!tileQueue.isEmpty()) {
			current = tileQueue.removeFirst();
			float dist = current.dst2(dest);

			if (isEmptyTile(current) && dist <= maxDist) {
				tileQueue.clear();
				tileSet.clear();
				return current;
			} else {
				for (int i = 0; i < 4; i++) {
					adj = current.nearby(i);
					// Allow tiles outside the bound if the current tile is already outside, but only to traverse towards the target.
					if (adj != null && !tileSet.contains(adj) && adj.dst2(dest) <= Math.max(dist, maxDist)) {
						tileSet.add(adj);
						tileQueue.addLast(adj);
					}
				}
			}
		}

		tileQueue.clear();
		tileSet.clear();
		return null;
	}
}