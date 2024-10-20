package astramod.world.blocks.liquid;

import arc.Core;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.*;
import astramod.world.meta.*;
import astramod.world.modules.*;

/** Junction that has heat capacity and can contain liquids in both directions. */
public class PipelineJunction extends LiquidJunction {
	public final int timerHflow = timers++;
	public final int timerVflow = timers++;
	public boolean leaks = true;
	public float heatCapacity = 0.5f;

	public PipelineJunction(String name) {
		super(name);
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.hLiquidCapacity, liquidCapacity, StatUnit.liquidUnits);
		stats.add(AstraStat.vLiquidCapacity, liquidCapacity, StatUnit.liquidUnits);
		stats.add(AstraStat.heatCapacity, heatCapacity * 100, StatUnit.percent);
	}

	@Override public void setBars() {
		super.setBars();

		for (int i = 0; i < 2; i++) {
			final int axis = i;
			Func<PipelineJunctionBuild, Liquid> current = build -> {
				build.setJunctionDirection(axis);
				return build.liquids.current();
			};

			addBar(i == 0 ? "hliquid" : "vliquid", entity -> {
				PipelineJunctionBuild j = (PipelineJunctionBuild)entity;
				return new Bar(
					() -> {
						var out = current.get(j) == null || entity.liquids.get(current.get(j)) <= 0.001f ? Core.bundle.get("bar.liquid") : current.get(j).localizedName;
						j.resetJunction();
						return out;
					},
					() -> {
						var out = current.get(j) == null ? Color.clear : current.get(j).barColor();
						j.resetJunction();
						return out;
					},
					() -> {
						var out = current.get(j) == null ? 0f : entity.liquids.get(current.get(j)) / liquidCapacity;
						j.resetJunction();
						return out;
					}
				);
			});
		}
	}

	public class PipelineJunctionBuild extends LiquidJunctionBuild {
		@Override public Building create(Block block, Team team) {
			Building build = super.create(block, team);
			liquids = new DirectionalLiquidModule();
			return build;
		}

		@Override public void update() {
			super.update();

			for (int i = 0; i < 2; i++) {
				setJunctionDirection(i);
				if (liquids.currentAmount() > 0.1f && liquids.current().temperature > heatCapacity) {
					float strength = liquids.currentAmount() * liquids.current().temperature / heatCapacity;
					damageContinuous(strength / 60f);
					if (Mathf.chanceDelta(strength / 100f)) Fx.fire.at(x, y);
				}
			}
			resetJunction();
		}

		@Override public void updateTile() {
			for (int i = 0; i < 2; i++) {
				setJunctionDirection(i);
				if (liquids.currentAmount() > 0.0001f && timer(i == 0 ? timerHflow : timerVflow, 1)) {
					moveLiquidForward(leaks, liquids.current(), i);
				}
			}
			resetJunction();
		}

		/** Should use {@code resetJunction()} afterwards. */
		public void setJunctionDirection(int axis) {
			((DirectionalLiquidModule)liquids).setAxis(axis);
		}

		public void resetJunction() {
			((DirectionalLiquidModule)liquids).resetAxis();
		}

		@Override public boolean acceptLiquid(Building source, Liquid liquid) {
			return true;
		}

		@Override public Building getLiquidDestination(Building source, Liquid liquid) {
			setJunctionDirection(relativeTo(source));
			return this;
		}

		@Override public void handleLiquid(Building source, Liquid liquid, float amount) {
			setJunctionDirection(relativeTo(source));
			super.handleLiquid(source, liquid, amount);
			resetJunction();
		}

		@Override public void dumpLiquid(Liquid liquid, float scaling, int outputDir) {
			setJunctionDirection(outputDir + rotation);
			super.dumpLiquid(liquid, scaling, outputDir);
			resetJunction();
		}

		@Override public void transferLiquid(Building next, float amount, Liquid liquid) {
			setJunctionDirection(relativeTo(next));
			super.transferLiquid(next, amount, liquid);
			resetJunction();
		}

		/** @param axis - 0 for horizontal, 1 for vertical. */
		public float moveLiquidForward(boolean leaks, Liquid liquid, int axis) {
			Tile aTile = tile.nearby(axis), bTile = tile.nearby(axis + 2);
			boolean aLeaks = false, bLeaks = false;
			float total = 0f;

			if (aTile != null && (aTile.build == null || aTile.build.front() != this || !aTile.block().rotate)) {
				if (aTile.build != null) {
					total += moveLiquid(aTile.build, liquid);
				} else if (leaks && !aTile.block().solid && !aTile.block().hasLiquids) {
					aLeaks = true;
				}
			}
			if (bTile != null && (bTile.build == null || bTile.build.front() != this || !bTile.block().rotate)) {
				if (bTile.build != null) {
					total += moveLiquid(bTile.build, liquid);
				} else if (leaks && !bTile.block().solid && !bTile.block().hasLiquids) {
					bLeaks = true;
				}
			}

			float leakAmount = liquids.get(liquid) / 2f;
			if (aLeaks && bLeaks) {
				leakAmount *= 1.5f;
				Puddles.deposit(aTile, tile, liquid, leakAmount / 2f, true, true);
				Puddles.deposit(bTile, tile, liquid, leakAmount / 2f, true, true);
			} else if (aLeaks) {
				Puddles.deposit(aTile, tile, liquid, leakAmount, true, true);
			} else if (bLeaks) {
				Puddles.deposit(bTile, tile, liquid, leakAmount, true, true);
			}
			if (aLeaks || bLeaks) liquids.remove(liquid, leakAmount);

			return total;
		}

		@Override public float moveLiquidForward(boolean leaks, Liquid liquid) {
			setJunctionDirection(rotation);
			float out = moveLiquidForward(leaks, liquid, rotation % 2);
			resetJunction();
			return out;
		}

		@Override public float moveLiquid(Building next, Liquid liquid) {
			setJunctionDirection(relativeTo(next));
			float out = super.moveLiquid(next, liquid);
			resetJunction();
			return out;
		}

		@Override public void drawLight() { }

		@Override public void setProp(UnlockableContent content, double value) {
			if (content instanceof Liquid) {
				for (int i = 0; i < 2; i++) {
					setJunctionDirection(i);
					super.setProp(content, value);
				}
				resetJunction();
			}
			else super.setProp(content, value);
		}
	}
}