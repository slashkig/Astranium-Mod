package astramod.ai.types;

import arc.util.Time;
import mindustry.ai.*;
import mindustry.ai.types.CommandAI;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class AnchoredMinerAI extends AnchoredAI {
	public boolean mining = true;
	public Item mineItem = null;
	public Tile ore;
	public boolean refreshOre;

	@Override public void init() {
		super.init();
		ore = unit.mineTile;
		mineItem = unit.getMineResult(ore);
	}

	@Override public void stanceChanged(){
		if(mineItem != null && unit.controller() instanceof CommandAI ai && !ai.hasStance(UnitStance.mineAuto) && !ai.hasStance(ItemUnitStance.getByItem(mineItem))) {
			mining = false;
			mineItem = null;
		}
	}

	@Override public void updateMovement() {
		if (!unit.canMine()) {
			super.updateMovement();
			return;
		}

		if (!unit.validMine(unit.mineTile)) {
			unit.mineTile(null);
		}

		if (mining) {
			if (timer.get(timerTarget2, 4f * Time.toSeconds) || mineItem == null) {
				CommandAI ai = unit.controller() instanceof CommandAI a ? a : null;

				if (ai != null && !ai.hasStance(UnitStance.mineAuto)) {
					mineItem = content.items().min(i -> ((unit.type.mineFloor && indexer.hasOre(i)) ||
						(unit.type.mineWalls && indexer.hasWallOre(i))) && unit.canMine(i) &&
						ai.hasStance(ItemUnitStance.getByItem(i)), i -> anchor.items.get(i));
					refreshOre = true;
				} else {
					float minDst = boundRadius;
					for (Player player : Groups.player) {
						if (!player.dead() && player.team() == unit.team && player.unit().mining()) {
							float dst = player.dst(anchor);	
							if (dst < minDst) {
								Tile tile = player.unit().mineTile;
								Item item = unit.getMineResult(tile);
								if (unit.type.mineItems.contains(item)) {
									ore = tile;
									mineItem = item;
								}
								minDst = dst;
							}
						}
					}
					refreshOre = false;
				}
			}

			if (mineItem != null && anchor.acceptStack(mineItem, 1, unit) == 0) {
				unit.clearItem();
				unit.mineTile(null);
				mineItem = null;
				super.updateMovement();
				return;
			}

			if (unit.stack.amount >= unit.type.itemCapacity || (mineItem != null && !unit.acceptsItem(mineItem))) {
				mining = false;
			} else {
				if (timer.get(timerTarget3, Time.toSeconds) && mineItem != null && refreshOre) {
					if (unit.type.mineFloor) ore = indexer.findClosestOre(anchor.x, anchor.y, mineItem);
					if (ore == null && unit.type.mineWalls) ore = indexer.findClosestWallOre(anchor.x, anchor.y, mineItem);
					if (ore != null && anchor.dst(ore) > boundRadius) ore = null;
				}

				if (ore != null) {
					moveTo(ore, unit.type.mineRange / 2f, 50f);
					if (unit.within(ore, unit.type.mineRange) && unit.validMine(ore)) unit.mineTile = ore;
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

			if (unit.within(anchor, unit.range())) {
				if (anchor.acceptStack(unit.stack.item, unit.stack.amount, unit) > 0) {
					Call.transferItemTo(unit, unit.stack.item, unit.stack.amount, unit.x, unit.y, anchor);
				}

				unit.clearItem();
				mining = true;
			}

			super.updateMovement();
		}

		if (!unit.type.flying) {
			unit.updateBoosting(unit.type.boostWhenMining || unit.floorOn().isDuct || unit.floorOn().damageTaken > 0f || unit.floorOn().isDeep());
		}
	}
}