package astramod.world.meta;

import arc.Core;
import arc.graphics.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.ctype.*;
import mindustry.entities.bullet.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;
import astramod.content.*;
import astramod.entities.bullet.*;
import astramod.type.effect.*;
import astramod.ui.*;

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
					c.add(displayItem(stack.item, stack.amount, timePeriod, true)).left().grow().padBottom(5f).padRight(5f);
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

	public static StatValue statusEffect(StatusEffect effect, float time) {
		return statusEffect(effect, time, false);
	}

	public static StatValue statusEffect(StatusEffect effect, float time, boolean timeStatColor) {
		return table -> addRowString(table, "@[stat]@[lightgray] ~ [@]@[lightgray] @",
			(effect.hasEmoji() ? effect.emoji() + " " : ""),
			effect.localizedName,
			timeStatColor ? "stat" : "white",
			Strings.autoFixed(time / Time.toSeconds, 1),
			Core.bundle.get("unit.seconds")
		);
	}

	public static StatValue effectStack(StatusEffectStack[] stacks) {
		return table -> {
			table.row();
			for (StatusEffectStack stack : stacks) {
				table.table(Styles.grayPanel, t -> {
					t.left().top().defaults().padRight(3).left();
					stack.display(t);
				}).padLeft(5).padTop(5).padBottom(5).growX().margin(10);
				table.row();
			}
		};
	}

	public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map) {
		return astraAmmo(map, false, false, null);
	}

	public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map, boolean showUnit){
		return astraAmmo(map, false, showUnit, null);
	}

    public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map, String blockName){
        return astraAmmo(map, false, false, blockName);
    }

    public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map, boolean showUnit, String blockName){
        return astraAmmo(map, false, showUnit, blockName);
    }

    public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map, boolean nested, boolean showUnit){
        return astraAmmo(map, nested, showUnit, null);
    }

	/** An extended ammo display for custom bullet classes. */
	public static <T extends UnlockableContent> StatValue astraAmmo(ObjectMap<T, BulletType> map, boolean nested, boolean showUnit, @Nullable String blockName) {
		return table -> {
			StatValues.ammo(map, nested, showUnit, blockName).display(table);
			var orderedKeys = map.keys().toSeq().sort();
			int offset = table.getCells().size - orderedKeys.size;
			Block block = Vars.content.block(blockName);

			for (int i = 0; i < orderedKeys.size; i++) {
				BulletType bullet = map.get(orderedKeys.get(i));
				Table entry = (Table)table.getCells().get(i + offset).get();

				if (bullet instanceof BoltBulletType bt) {
					if (bt.armorPenetration > 0f) addRow(entry, "bullet.armorpenetration", bt.armorPenetration);
				} else if (bullet instanceof SonicBulletType bt) {
					Displays.replaceLabelText(entry,
						Core.bundle.format("bullet.damage", bullet.damage),
						Core.bundle.format("bullet.damage", Strings.autoFixed(bullet.damage * (1f - bt.falloffFactor), 1) + " - " + Strings.autoFixed(bullet.damage, 1))
					);
				} else if (bullet instanceof MagneticBulletType bt) {
					statusEffect(AstraStatusEffects.magnetized, bt.magnetizedDuration, true).display(entry);
					addRow(entry, "bullet.magnetism", bt.magneticStrength);
				}

				if (bullet.pierceDamageFactor > 0f) {
					addRow(entry, "bullet.piercedamagefactor", 100f * bullet.pierceDamageFactor);
				}
				if (bullet.shootPattern != null && block instanceof Turret t) {
					int shots = bullet.shootPattern.shots - t.shoot.shots;
					if (shots != 0) addRow(entry, "bullet.extrashots", shots > 0 ? "+" + shots : shots);
				}
			}

			if (map.notEmpty()) {
				T key = orderedKeys.first();
				if (key instanceof Item) {
					sortCells(table, AstraItems.itemSortingOrder, orderedKeys, offset);
				} else if (key instanceof Liquid) {
					sortCells(table, AstraFluids.liquidSortingOrder, orderedKeys, offset);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T extends UnlockableContent> void sortCells(Table table, Seq<T> order, Seq<? extends UnlockableContent> orderedKeys, int offset) {
		var cells = Seq.with(table.getCells());
		table.getCells().sort(e -> {
			int index = cells.indexOf(e);
			return index < offset ? index : (offset + order.indexOf((T)orderedKeys.get(index - offset)));
		});
	}

	public static StatValue numberRange(float low, float high, StatUnit unit) {
		return table -> {
			table.add(Strings.format("@@-@", unit.icon == null ? "" : unit.icon + " ", fixValue(low), fixValue(high))).left();
			table.add((unit.space ? " " : "") + unit.localized()).left();
		};
	}

	public static StatValue block(Block block) {
		return block(block, null);
	}

	public static StatValue block(Block block, @Nullable String key) {
		return table -> {
			table.marginTop(4);
			table.add(block.emoji() + " " + (key == null ? block.localizedName : Core.bundle.get(key))).padRight(10).left().top();
		};
	}

	public static StatValue blocks(Seq<Block> blocks) {
		return table -> {
			table.row().table(t -> {
				blocks.each(b -> {
					if (!b.isHidden()) {
						block(b, Strings.format("block.@.name", b.name)).display(t);
						t.row();
					}
				});
			});
		};
	}

	public static void addRow(Table table, String key, Object... args) {
		table.row();
		table.add(Core.bundle.format(key, args));
	}

	public static void addRow(Table table, String key, Object value, boolean valueFirst) {
		table.row();
		table.add(valueFirst ? Strings.format("[stat]@ [lightgray]@", value, Core.bundle.get(key).toLowerCase()) : Strings.format("[lightgray]@: [stat]@", Core.bundle.get(key), value));
	}

	public static void addRowString(Table table, String key, Object... args) {
		table.row();
		table.add(Strings.format(key, args));
	}
}