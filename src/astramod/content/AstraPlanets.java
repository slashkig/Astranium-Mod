package astramod.content;

import mindustry.type.Planet;

import static mindustry.content.Planets.*;

public class AstraPlanets {
	public static Planet aziris;

	public static void load() {
		aziris = new Planet("aziris", sun, 1f) {{
			alwaysUnlocked = true;
		}};
	};
}