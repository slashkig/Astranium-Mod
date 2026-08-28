package astramod.content;

import arc.audio.*;
import arc.util.*;

import static mindustry.Vars.tree;

public class AstraSounds {
	public static Sound shootSonic;

	public static void load() {
		Log.info("Loading sounds");
		shootSonic = tree.loadSound("sonicShoot");
	}
}