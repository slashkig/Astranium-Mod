package astramod.content;

import arc.audio.*;
import arc.util.*;

import static mindustry.Vars.tree;

public class AstraSounds {
	public static Sound
		shootSonic, shootSniperSmall, shootPilos, shootBolt, shootIce,
		hitIce, hitIceSmall,
		helicopterLoop;

	public static void load() {
		Log.info("Loading sounds");
		shootSonic = tree.loadSound("shootSonic");
		shootSniperSmall = tree.loadSound("shootSniper");
		shootPilos = tree.loadSound("shootPilos");
		shootBolt = tree.loadSound("shootBolt");
		shootIce = tree.loadSound("shootIce");
		hitIce = tree.loadSound("hitIce");
		hitIceSmall = tree.loadSound("hitIce");
		helicopterLoop = tree.loadSound("helicopterLoop");
	}
}