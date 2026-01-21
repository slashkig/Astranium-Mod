package astramod.world.meta;

import arc.Core;
import arc.graphics.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import astramod.world.blocks.defense.*;

import static mindustry.world.meta.StatValues.*;

public class AstraStatValues {
	public static StatValue craftBooster(String unit, float amount, float boost, UnlockableContent booster) {
		return table -> {
			table.row();
			table.table(c -> {
				c.table(Styles.grayPanel, b -> {
					b.image(booster.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
					b.table(info -> {
						info.add(booster.localizedName).left().row();
						info.add(Strings.autoFixed(amount * 60f, 2) + StatUnit.perSecond.localized()).left().color(Color.lightGray);
					});

					b.table(bt -> {
						bt.right().defaults().padRight(3).left();
						if (boost != Float.MAX_VALUE) bt.add(unit.replace("{0}", "[stat]" + Strings.autoFixed((1f + boost), 2) + "[lightgray]")).pad(5);
					}).right().grow().pad(10f).padRight(15f);
				}).growX().pad(5).row();
			}).growX().colspan(table.getColumns());
			table.row();
		};
	}

	public static StatValue itemsLiquidsVariable(String unit, ItemStack[] items, Seq<LiquidStack> liquids, float timePeriod, float liqAmount) {
		return table -> {
			table.table(c -> {
				for (ItemStack stack : items) {
					c.add(new ItemDisplay(stack.item, stack.amount, timePeriod, true)).left().grow().padBottom(5f).padRight(5f);
				}
			}).left().expand();
			table.row();

			table.table(c -> {
				for (LiquidStack stack : liquids) {
					c.table(Styles.grayPanel, b -> {
						b.image(stack.liquid.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
						b.table(info -> {
							info.add(stack.liquid.localizedName).left().row();
							info.add(Strings.autoFixed(liqAmount * 60f, 2) + StatUnit.perSecond.localized()).left().color(Color.lightGray);
						});

						b.table(bt -> {
							bt.right().defaults().padRight(3).left();
							if(stack.amount != Float.MAX_VALUE) bt.add(unit.replace("{0}", "[stat]" + Strings.autoFixed(stack.amount, 2) + "[lightgray]")).pad(5);
						}).right().grow().pad(10f).padRight(15f);
					}).growX().pad(5).row();
				}
			}).growX().colspan(table.getColumns());
			table.row();
		};
	}

	public static StatValue mine(LandMine mine, int indent) {
		return table -> {
			table.row();

			table.table(Styles.grayPanel, bt -> {
				bt.left().top().defaults().padRight(3).left();
				bt.table(title -> {
					title.image(mine.uiIcon).size(3 * 8).padRight(4).right().scaling(Scaling.fit).top();
					title.add(mine.localizedName).padRight(10).left().top();
				});

				if(mine.explodePower > 0) {
					addRow(bt, "bullet.damage", Mathf.floor(mine.explodePower / 11f) * mine.explodePower / 2f);
				}

				addRow(bt, "stat.tileradius", mine.explodeRadius, true);

				if (mine.knockback > 0) {
					addRow(bt, "bullet.knockback", mine.knockback);
				} else if (mine.knockback < 0) {
					addRow(bt, "stat.magneticstrength", -mine.knockback, true);
				}
		
				if (mine.explodeFire > 0) {
					addRow(bt, "stat.incendivity", mine.explodeFire, true);
				}

				if (mine.numLightning > 0) {
					addRow(bt, "stat.lightningcount", mine.numLightning, false);
					addRow(bt, "stat.lightningdamage", (int)mine.lightningDamage, false);
				}

				if (mine.status != StatusEffects.none) {
					bt.row();
					bt.add((mine.status.minfo.mod == null ? mine.status.emoji() : "") + "[stat]" + mine.status.localizedName + "[lightgray] ~ [stat]" + ((int)(mine.statusDuration / 60f)) + "[lightgray] " + Core.bundle.get("unit.seconds"));
				}

				if (mine.bullet != null) {
					bt.row();

					Table fc = new Table();
					ammo(ObjectMap.of(mine, mine.bullet), indent + 1, false).display(fc);
					Collapser coll = new Collapser(fc, true);
					coll.setDuration(0.1f);

					bt.table(ft -> {
						ft.left().defaults().left();

						ft.add(Core.bundle.format("bullet.frags", mine.shots));
						ft.button(Icon.downOpen, Styles.emptyi, () -> coll.toggle(false)).update(i -> i.getStyle().imageUp = (!coll.isCollapsed() ? Icon.upOpen : Icon.downOpen)).size(8).padLeft(16f).expandX();
					});
					bt.row();
					bt.add(coll);
				}
			}).padLeft(indent * 5).padTop(5).padBottom(5).growX().margin(10);
			table.row();
		};
	}

	public static StatValue numberRange(float low, float high, StatUnit unit) {
		return table -> {
			table.add(Strings.format("@@-@", unit.icon == null ? "" : unit.icon + " ", fixValue(low), fixValue(high))).left();
			table.add((unit.space ? " " : "") + unit.localized()).left();
		};
	}

	public static StatValue block(Block block) {
		return table -> {
			table.marginTop(4);
			table.image(block.uiIcon).size(3 * 8).padRight(4).right().scaling(Scaling.fit).top();
			table.add(block.localizedName).padRight(10).left().top();
		};
	}

	public static void addRow(Table table, String key, Object... args) {
		table.row();
		table.add(Core.bundle.format(key, args));
	}

	public static void addRow(Table table, String key, Object value, boolean valueFirst) {
		table.row();
		table.add(valueFirst ? ("[stat]" + value + "[lightgray] " + Core.bundle.get(key).toLowerCase()) : ("[lightgray]" + Core.bundle.get(key) + ": [stat]" + value));
	}
}