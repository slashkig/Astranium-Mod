package astramod.world.meta;

import arc.util.*;
import arc.struct.*;
import arc.graphics.*;
import arc.scene.ui.layout.*;
import mindustry.ui.*;
import mindustry.type.*;
import mindustry.world.meta.*;

public class AstraStatValues {
	public static StatValue craftBooster(String unit, float amount, float boost, Item item) {
		return table -> {
			table.row();
			table.table(c -> {
				c.table(Styles.grayPanel, b -> {
					b.image(item.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
					b.table(info -> {
						info.add(item.localizedName).left().row();
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
}