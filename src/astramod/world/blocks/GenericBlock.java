package astramod.world.blocks;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.*;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.world.*;
import mindustry.world.draw.*;

public class GenericBlock extends Block {
	public DrawBlock drawer = new DrawDefault();
	
	public GenericBlock(String name) {
		super(name);
		solid = true;
		destructible = true;
	}

	@Override public void load() {
		super.load();
		drawer.load(this);
	}

	@Override public TextureRegion[] icons() {
		return drawer.finalIcons(this);
	}

	@Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		drawer.drawPlan(this, plan, list);
	}

	@Override public void getRegionsToOutline(Seq<TextureRegion> out) {
		drawer.getRegionsToOutline(this, out);
	}

	public class GenericBuild extends Building {
		@Override public void draw() {
			drawer.draw(this);
		}
	}
}