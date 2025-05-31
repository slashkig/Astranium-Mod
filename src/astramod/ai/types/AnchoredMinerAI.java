package astramod.ai.types;

import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import astramod.world.blocks.modules.*;

public class AnchoredMinerAI extends AnchoredAI {
	public boolean mining = true;
	public Item mineItem = null;
	public Tile ore;

	@Override public void init() {
		super.init();
		ore = unit.mineTile;
		mineItem = unit.getMineResult(ore);
	}

	@Override public void updateMovement() {
		Building anchor = anchor();
		Building core = anchor instanceof CoreModuleBlock module ? module.getLinkedCore() : unit.closestCore();

		if (!(unit.canMine()) || core == null) return;

		if (unit.mineTile != null && !unit.mineTile.within(unit, unit.type.mineRange)) {
			unit.mineTile(null);
		}

		if (ore != null && !unit.validMine(ore, false)) {
			ore = null;
			unit.mineTile(null);
			mineItem = null;
		}

		if (mining) {
			float minDst = boundRadius;
			Tile targetTile = null;
			for (Player player : Groups.player) {
				if (!player.dead() && player.team() == unit.team && player.unit().mining()) {
					float dst = player.dst(anchor);
					if (dst < minDst) {
						Tile tile = player.unit().mineTile;
						Item item = unit.getMineResult(tile);
						if (unit.type.mineItems.contains(item)) {
							targetTile = tile;
							mineItem = item;
						}
						minDst = dst;
					}
				}
			}

			if (mineItem != null && core.acceptStack(mineItem, 1, unit) == 0) {
				unit.clearItem();
				unit.mineTile(null);
				mineItem = null;
				return;
			}

			if (unit.stack.amount >= unit.type.itemCapacity || (mineItem != null && !unit.acceptsItem(mineItem))) {
				mining = false;
			} else {
				if (timer.get(timerTarget3, 60) && targetTile != null && targetTile.within(unit, boundRadius)) {
					ore = targetTile;
				}

				if (ore != null) {
					moveTo(ore, unit.type.mineRange / 2f, 50f);

					if ((ore.block() == Blocks.air || ore.wallDrop() != null)) {
						if (unit.within(ore, unit.type.mineRange)) unit.mineTile = ore;
					} else {
						mining = false;
					}
				} else {
					super.updateMovement();
				}
			}
		} else {
			unit.mineTile = null;

			if (unit.stack.amount == 0) {
				mining = true;
				return;
			}

			if (unit.within(core, unit.type.range)) {
				if (core.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0) {
					Call.transferItemTo(unit, unit.stack.item, unit.stack.amount, unit.x, unit.y, core);
				}

				unit.clearItem();
				mining = true;
			}

			circle(core, unit.type.range / 1.8f);
		}
	}
}