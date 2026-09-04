package astramod.world.blocks.units;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.units.*;
import mindustry.world.blocks.units.UnitFactory.UnitPlan;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

public class DynamicReconstructor extends Reconstructor {
	public ObjectMap<UnitType, UnitPlan> recipes = new ObjectMap<>();

	public DynamicReconstructor(String name) {
		super(name);
		consume(new ConsumeItemDynamic((DynamicReconstructorBuild b) -> {
			UnitPlan plan = b.currentPlan();
			return plan != null ? plan.requirements : ItemStack.empty;
		}));
	}

	@Override public void init() {
		recipes.forEach(e -> upgrades.add(new UnitType[] { e.key, e.value.unit }));
		super.init();
	}

	@Override public void initCapacities() {
		capacities = new int[Vars.content.items().size];
		itemCapacity = 10;
		for (UnitPlan plan : recipes.values()) {
			for (ItemStack stack : plan.requirements) {
				capacities[stack.item.id] = Math.max(capacities[stack.item.id], stack.amount * 2);
				itemCapacity = Math.max(itemCapacity, stack.amount * 2);
			}
		}

		consumeBuilder.each(c -> c.multiplier = b -> Vars.state.rules.unitCost(b.team));
	}

	@Override public void setStats() {
		super.setStats();

		stats.remove(Stat.itemCapacity);
		stats.remove(Stat.productionTime);
		stats.replace(Stat.output, table -> {
			table.row();

			for (var recipe : recipes.entries()) {
				UnitPlan plan = recipe.value;

				if (recipe.key.isBanned() || plan.unit.isBanned()) {
					table.table(Styles.grayPanel, t -> t.image(Icon.cancel).color(Pal.remove).size(40)).growX().pad(5);
				} else if (recipe.key.unlockedNow() && plan.unit.unlockedNow()) {
					table.table(Styles.grayPanel, t -> {
						t.image(recipe.key.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit).with(i -> StatValues.withTooltip(i, recipe.key));
						t.table(info -> {
							info.add(recipe.key.localizedName).left();
							info.row();
						}).left();

						t.table(req -> {
							req.right();
							for (int i = 0; i < plan.requirements.length; i++) {
								if (i % 6 == 0) {
									req.row();
								}

								ItemStack stack = plan.requirements[i];
								req.add(StatValues.displayItem(stack.item, stack.amount, plan.time, true)).pad(5);
							}
						}).right().grow().pad(10f);
					}).growX().padTop(5);
					table.row();
					table.table(Styles.grayPanel, t -> {
						t.image(Icon.right).color(Pal.darkishGray).size(40).pad(10f);
						t.image(plan.unit.uiIcon).size(40).pad(10f).right().scaling(Scaling.fit).with(i -> StatValues.withTooltip(i, plan.unit));
						t.table(info -> {
							info.add(plan.unit.localizedName).left();
							info.row();
							info.add(Strings.autoFixed(plan.time / Time.toSeconds, 1) + " " + Core.bundle.get("unit.seconds")).color(Color.lightGray).left();
						}).pad(10).right();
					}).fill().padBottom(5);
				} else {
					table.table(Styles.grayPanel, t -> t.image(Icon.lock).color(Pal.darkerGray).size(40)).growX().pad(5);
				}
				
				table.row();
			}
		});
	}

	@Override public void addUpgrade(UnitType from, UnitType to) {
		addUpgrade(from, to, 1f, ItemStack.empty);
	}

	public void addUpgrade(UnitType from, UnitType to, float time, ItemStack[] requirements) {
		recipes.put(from, new UnitPlan(to, time, requirements));
	}

	public class DynamicReconstructorBuild extends ReconstructorBuild {
		@Override public void draw() {
			Draw.rect(region, x, y);

			boolean fallback = true;
			for (int i = 0; i < 4; i++) {
				if (blends(i) && i != rotation) {
					Draw.rect(inRegion, x, y, (i * 90f) - 180f);
					fallback = false;
				}
			}
			if (fallback) Draw.rect(inRegion, x, y, rotdeg());

			Draw.rect(outRegion, x, y, rotdeg());

			if (constructing() && hasArrived()) {
				Draw.draw(Layer.blockOver, () -> {
					float fraction = progress / currentPlan().time;
					Draw.alpha(1f - fraction);
					Draw.rect(payload.unit.type.fullIcon, x, y, payload.rotation() - 90f);
					Draw.reset();
					Drawf.construct(this, upgrade(payload.unit.type), payload.rotation() - 90f, fraction, speedScl, time);
				});
			}else{
				Draw.z(Layer.blockOver);
				drawPayload();
			}

			Draw.z(Layer.blockOver + 0.1f);
			Draw.rect(topRegion, x, y);
		}

		@Override public void updateTile(){
			if (constructing()) constructTime = currentPlan().time;
			super.updateTile();
		}

		@Override public boolean acceptItem(Building source, Item item) {
			UnitPlan currentPlan = currentPlan();
			return currentPlan != null && items.get(item) < getMaximumAccepted(item) && Structs.contains(currentPlan.requirements, stack -> stack.item == item);
		}

		@Override public UnitType upgrade(UnitType type) {
			if (type == null) return null;
			UnitPlan plan = recipes.get(type);
			return plan != null ? plan.unit : null;
		}

		@Override public float fraction() {
			UnitPlan plan = currentPlan();
			return plan != null ? progress / plan.time : 0f;
		}

		public UnitPlan currentPlan() {
			return payload != null ? recipes.get(payload.unit.type) : null;
		}
	}
}