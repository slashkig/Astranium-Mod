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
		ironFront = Color.valueOf("faf2c3"),
		ironBack = Color.valueOf("ffaa75"),
		graphiteFront = Color.valueOf("c3cce3"),
		graphiteBack = Color.valueOf("a6b2ca"),
		siliconFront = Color.valueOf("c2c0b9"),
		siliconBack = Color.valueOf("96948d"),
		titaniumFront = Color.valueOf("99c9ff"),
		titaniumBack = Color.valueOf("7baadf"),
		lithiumFront = Color.valueOf("ff5555"),
		lithiumBack = Color.valueOf("d80000"),
		crystalFront = Color.valueOf("fb8aa8"),
		crystalBack = Color.valueOf("ef3a6a"),
		magnetFront = Color.valueOf("ffffff"),
		magnetBack = Color.valueOf("ffadad"),
		steelFront = Color.valueOf("ffffff"),
		steelBack = Color.valueOf("ffe4b0"),
		neoFront = Color.valueOf("fff3a3"),
		neoBack = Color.valueOf("ffc400"),

		darkerOutline = Color.valueOf("181818"),
		siegeMachineOutline = Color.valueOf("1c032e"),

		fireBulletFront = Color.valueOf("ffbf75"),
		fireBulletBack = Color.valueOf("d17104"),
		fireBulletTrail = Color.valueOf("994102"),

		missileOrange = Color.valueOf("de8067"),
		missileOrangeBack = Color.valueOf("c82c01"),

		sonicShotFront = Color.valueOf("a8e9ff"),
		sonicShotBack = Color.valueOf("a8e9ff"),

		deflectFront = Color.valueOf("ffe77d"),
		deflectBack = Color.valueOf("e3be07"),
		deflectTrail = Color.valueOf("ba4a00"),

		crystalLazerBack = Color.valueOf("bf2851"),
		crystalLazerLight = Color.valueOf("ffabc1"),
		crystalShoot = Color.valueOf("fc235d"),

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