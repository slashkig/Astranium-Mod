package astramod.ui;

import arc.*;
import arc.util.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.PixmapPacker.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.math.geom.*;
import arc.struct.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.Block;
import mindustry.world.meta.*;
import astramod.content.*;
import astramod.type.effect.*;

import static mindustry.Vars.content;

public final class Icons {
	private static final StringMap extraIcons = new StringMap();

	public static void load() {
		Log.info("Loading icons");
		addIcon("sentinels", "astramod-team-sentinels|team");

		for (Item item : AstraItems.azirisItems) {
			addIcon(item.name, item.name);
		}
		for (Liquid liquid : content.liquids()) {
			if (liquid.name.startsWith("astramod-")) {
				addIcon(liquid.name, liquid.name);
			}
		}
		for (Block block : content.blocks()) {
			if (block.name.startsWith("astramod-") && block.name != "astramod-ohno" && block.buildVisibility != BuildVisibility.hidden) {
				addIcon(block.name, "block-" + block.name + "-full");
			}
		}
		for (UnitType unit : content.units()) {
			if (unit.name.startsWith("astramod-")) {
				addIcon(unit.name, unit.name + "-full");
			}
		}
		for (StatusEffect status : content.statusEffects()) {
			if (status.name.startsWith("astramod-") && !(status instanceof StatusEffectStack)) {
				addIcon(status.name, status.name + "|status");
			}
		}
	}

	public static void addIcon(String iconName, String regionName) {
		extraIcons.put(iconName, regionName);
	}

	/** Adds the custom icon regions to the icon atlas. Must be called during {@code AtlasPackEvent}. */
	public static void packIcons() {
		Page page = UI.packer.getPages().first();
		Seq<Team> teams = Seq.with(Team.all);
		Rect rect = new Rect();

		for (var entry : extraIcons.entries()) {
			String[] split = entry.value.split("\\|");
			String name = split[0];
			AtlasRegion region = Core.atlas.find(name);
			if (!region.found()) {
				Log.warn("Could not find icon region: \"" + name + "\"");
				continue;
			}

			page.setDirty(false);
			PixmapRegion pixmapRegion = Core.atlas.getPixmap(region);

			// Generate icon if needed
			if (split.length > 1) {
				switch (split[1]) {
					case "team" -> generateIcon(pixmapRegion, rect, teams.find(t -> t.name == entry.key).color);
					case "status" -> generateIcon(pixmapRegion, rect, content.statusEffect(name).color, true);
				}
				extraIcons.put(entry.key, name);
			}

			rect.set(UI.packer.pack(name, pixmapRegion, region.splits, region.pads));

			region.texture = page.getTexture();
			region.set((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
			Core.atlas.getTextures().add(region.texture);
			region.pixmapRegion = null;
		}

		page.setDirty(true);
		page.updateTexture(TextureFilter.linear, TextureFilter.linear, false);
	}

	/** Adds the custom icons to the icon registry. Must be called after {@code Fonts} has been loaded. */
	public static void registerIcons() {
		int ch = 0xE001;
		for (var entry : extraIcons.entries()) {
			Fonts.registerIcon(entry.key, entry.value, ch++, Core.atlas.find(entry.value));
		}
		extraIcons.clear();
	}

	public static void generateIcon(PixmapRegion region, Rect rect, Color color) {
		generateIcon(region, rect, color, false);
	}

	public static void generateIcon(PixmapRegion region, Rect rect, Color color, boolean expand) {
		generateIcon(region, rect, color, expand, 3);
	}

	public static void generateIcon(PixmapRegion region, Rect rect, Color color, boolean expand, int radius) {
		Pixmap pixmap = region.pixmap;
		rect.set(region.x, region.y, region.width - 1, region.height - 1);
		pixmap.each((x, y) -> {
			if (rect.contains(x, y)) {
				pixmap.setRaw(x, y, Color.muli(pixmap.getRaw(x, y), color.rgba()));
			}
		});
		if (expand) {
			Pixmap expanded = new Pixmap(region.width + radius * 2, region.height + radius * 2);
			expanded.draw(pixmap, region.x, region.y, region.width, region.height, radius, radius, region.width, region.height, false, true);
			region.x = radius;
			region.y = radius;
			region.pixmap = expanded.outline(Pal.gray, radius);
		} else region.pixmap = pixmap.outline(Pal.gray, radius);
	}

	// TODO generate fullicons?
}
