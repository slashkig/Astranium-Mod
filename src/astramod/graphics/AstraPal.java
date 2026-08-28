package astramod.graphics;

import arc.graphics.Color;
import arc.util.Log;
import mindustry.game.Team;

public class AstraPal {
	public static Color
		waterBubble = Color.valueOf("7693e3"),
		ironSmoke = Color.valueOf("ffc099"),
		siliconSmoke = Color.valueOf("ffef99"),
		glassSmoke = Color.valueOf("efc9b9"),
		plasmaGlowBlue = Color.valueOf("9292ff"),
		plasmaGlowPurple = Color.valueOf("c080ff"),
		crystalRed = Color.valueOf("ff0044"),
		crystalGlow = Color.valueOf("f0c2ce"),
		powerGlow = Color.valueOf("ffe08f"),
		mend = Color.valueOf("84f491"),

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
		magnetFront = Color.valueOf("878787"),
		magnetBack = Color.valueOf("817878"),
		steelFront = Color.valueOf("ffffff"),
		steelBack = Color.valueOf("e4e4e4"),
		neoFront = Color.valueOf("9c9c9c"),
		neoBack = Color.valueOf("c0954c"),

		darkerOutline = Color.valueOf("181818"),

		fireBulletFront = Color.valueOf("ffbf75"),
		fireBulletBack = Color.valueOf("d17104"),
		fireBulletTrail = Color.valueOf("994102"),

		sonicShotFront = Color.valueOf("a8e9ff"),
		sonicShotBack = Color.valueOf("a8e9ff"),

		deflectFront = Color.valueOf("ffe77d"),
		deflectBack = Color.valueOf("e3be07"),
		deflectTrail = Color.valueOf("ba4a00"),

		heat = Color.valueOf("f9350f"),
		sonicHeat = Color.valueOf("92f0fc"),

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