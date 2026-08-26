package astramod.entities;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;

public class Unitsx {
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
}