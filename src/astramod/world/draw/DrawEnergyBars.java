package astramod.world.draw;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.Building;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawEnergyBars extends DrawBlock {
	public int barsPerRow;
	public int rows = 1;
	public float xOffset = 0.5f, yOffset = 0.5f;

	public TextureRegion barRegion;
	public TextureRegion iconRegion;
	public Color emptyColor = Color.gray;
	public Color fullColor = Color.valueOf("fb9567");

	private float dx, dy;

	public DrawEnergyBars(int bars) {
		barsPerRow = bars;
	}

	@Override public void load(Block block) {
		barRegion = Core.atlas.find(block.name + "-bar");
		iconRegion = Core.atlas.find(block.name + "-bar-icon");
		xOffset += barRegion.width / 8f;
		yOffset += barRegion.height / 8f;
		dx = xOffset * (barsPerRow - 1) / 2f;
		dy = yOffset * (rows - 1) / 2f;
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] {iconRegion};
	}

	@Override public void drawPlan(Block block, BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(iconRegion, plan.drawx(), plan.drawy());
	}

	@Override public void draw(Building build) {
		float drawX;
		float drawY = build.y - dy;
		float barPower = build.power.status * rows * barsPerRow;
		for (int row = 1; row <= rows; row++) {
			drawX = build.x - dx;
			for (int bar = 1; bar <= barsPerRow; bar++) {
				if (barPower > 1f) {
					Draw.color(fullColor);
					barPower--;
				} else if (barPower > 0f) {
					Draw.color(emptyColor.cpy().lerp(fullColor, barPower));
					barPower = 0f;
				} else {
					Draw.color(emptyColor);
				}
				Draw.rect(barRegion, drawX, drawY);
				drawX += xOffset;
			}
			drawY += yOffset;
		}

		Draw.reset();
	}
}