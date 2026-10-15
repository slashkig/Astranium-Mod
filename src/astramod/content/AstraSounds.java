package astramod.content;

import arc.audio.*;
import arc.util.*;

import static mindustry.Vars.tree;

public class AstraSounds {
	public static Sound
		shootSonic, shootSniper, shootPilos, shootBolt, shootIce,
		hitIce, hitIceSmall,
		loopHelicopter;

	public static void load() {
		Log.info("Loading sounds");

		shootSonic = tree.loadSound("shootSonic");
		shootSniper = tree.loadSound("shootSniper");
		shootPilos = tree.loadSound("shootPilos");
		shootBolt = tree.loadSound("shootBolt");
		shootIce = tree.loadSound("shootIce");
		hitIce = tree.loadSound("hitIce");
		hitIceSmall = tree.loadSound("hitIce");
		loopHelicopter = tree.loadSound("helicopterLoop");
	}
}