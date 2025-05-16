package astramod.graphics;

import arc.graphics.*;
import arc.util.Log;
import mindustry.game.*;

public class AstraPal {
	public static Color
		waterBubble = Color.valueOf("7693e3"),
		ironSmoke = Color.valueOf("ffc099"),
		siliconSmoke = Color.valueOf("ffef99"),
		glassSmoke = Color.valueOf("efc9b9"),
		plasmaGlowBlue = Color.valueOf("9292ff"),
		plasmaGlowPurple = Color.valueOf("c080ff"),
		crystalRed = Color.valueOf("ff0044"),
		powerGlow = Color.valueOf("ffe08f"),

		hemaFront = Color.valueOf("d89a7d"),
		hemaBack = Color.valueOf("bf7656"),
		leadFront = Color.valueOf("c0b9cd"),
		leadBack = Color.valueOf("a096b5"),
		ironFront = Color.valueOf("a09597"),
		ironBack = Color.valueOf("5d5556"),
		graphiteFront = Color.valueOf("c3cce3"),
		graphiteBack = Color.valueOf("a6b2ca"),
		siliconFront = Color.valueOf("c2c0b9"),
		siliconBack = Color.valueOf("96948d"),
		titaniumFront = Color.valueOf("99c9ff"),
		titaniumBack = Color.valueOf("7baadf"),
		lithiumFront = Color.valueOf("f87d42"),
		lithiumBack = Color.valueOf("f65021"),
		crystalFront = Color.valueOf("fb8aa8"),
		crystalBack = Color.valueOf("ef3a6a"),

		testPink = Color.valueOf("ff22ff"),
		testPinkDark = Color.valueOf("ee00ee");

	public static Color[] teamFaded;

	public static void load() {
		Log.info("Loading palette");

		teamFaded = new Color[Team.all.length];
		for (Team team : Team.all) {
			teamFaded[team.id] = team.color.cpy().a(0.5f);
		}
	}
}