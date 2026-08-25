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
import mindustry.world.meta.BuildVisibility;
import astramod.content.*;

import mindustry.Vars;

public class Icons {
	private static final StringMap extraIcons = new StringMap();

	public static void load() {
		Log.info("Loading icons");
		addIcon("sentinels", "astramod-team-sentinels");

		for (Item item : AstraItems.azirisItems) {
			addIcon(item.name, item.name);
		}
		for (Liquid liquid : Vars.content.liquids()) {
			if (liquid.name.startsWith("astramod-")) {
				addIcon(liquid.name, liquid.name);
			}
		}
		for (Block block : Vars.content.blocks()) {
			if (block.name.startsWith("astramod-") && block.name != "astramod-ohno" && block.buildVisibility != BuildVisibility.hidden) {
				addIcon(block.name, "block-" + block.name + "-full");
			}
		}
		for (UnitType unit : Vars.content.units()) {
			if (unit.name.startsWith("astramod-")) {
				addIcon(unit.name, unit.name + "-full");
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
			AtlasRegion region = Core.atlas.find(entry.value);
			if (!region.found()) {
				Log.warn("Could not find icon region: \"" + entry.value + "\"");
				continue;
			}

			page.setDirty(false);
			PixmapRegion pixmapRegion = Core.atlas.getPixmap(region);

			Team team = teams.find(t -> t.name == entry.key);
			if (team != null) {
				// Generate team icon
				Pixmap px = pixmapRegion.pixmap;
				rect.set(pixmapRegion.x, pixmapRegion.y, pixmapRegion.width - 1, pixmapRegion.height - 1);
				px.each((x, y) -> {
					if (rect.contains(x, y)) {
						px.setRaw(x, y, Color.muli(px.getRaw(x, y), team.color.rgba()));
					}
				});
				pixmapRegion.pixmap = px.outline(Pal.gray, 3);
			}

			rect.set(UI.packer.pack(region.name, pixmapRegion, region.splits, region.pads));

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
	}
}
