package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.gen.Building;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.draw.*;

public class DrawStoredItem extends DrawBlock {
	public Item item;
	public String suffix;
	public TextureRegion itemRegion;

	public DrawStoredItem(Item item) {
		this.item = item;
		suffix = item.name;
	}

	public DrawStoredItem(Item item, String itemName) {
		this.item = item;
		suffix = itemName;
	}

	@Override public void load(Block block) {
		itemRegion = Core.atlas.find(block.name + "-" + suffix);
	}

	@Override public void draw(Building build) {
		Draw.alpha((float)build.items.get(item) / build.block.itemCapacity);
		Draw.rect(itemRegion, build.x, build.y);
		Draw.color();
	}

	@Override public TextureRegion[] icons(Block block) {
		return new TextureRegion[] {itemRegion};
	}
}