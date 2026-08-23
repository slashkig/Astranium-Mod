package astramod.type.unit;

import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.abilities.*;
import mindustry.gen.Unit;
import mindustry.type.Item;
import arc.struct.Seq;
import astramod.ai.*;
import astramod.gen.BuildingTetherUnit;

import static mindustry.Vars.indexer;

public class AstraAnchoredUnitType extends AstraUnitType {
	public AstraAnchoredUnitType(String name) {
		super(name, BuildingTetherUnit::create);
		controller = u -> u.team.isAI() && !u.team.rules().rtsAi ? aiController.get() : new CommandAI();
		useUnitCap = false;
		playerControllable = false;
		logicControllable = false;
		controlSelectGlobal = false;
		isEnemy = false;
	}

	@Override public void init() {
		boolean attacks = weapons.contains(w -> !w.noAttack), noCommands = false;

		if (commands.size == 0) {
			if (mineTier > 0) {
				commands.add(AstraUnitCommand.anchorMine);
			}
			if (buildSpeed > 0) {
				commands.add(AstraUnitCommand.anchorSupport);
			}
			if (attacks) {
				commands.add(AstraUnitCommand.anchorProtect, AstraUnitCommand.followShoot);
			}
			if (abilities.contains(a -> a instanceof ShieldArcAbility)) {
				commands.add(AstraUnitCommand.anchorShield, AstraUnitCommand.followShield);
			}
			if (commands.size > 0) {
				commands.insert(0, AstraUnitCommand.anchorIdle);
			} else {
				noCommands = true;
			}
		}

		if (stances.size == 0) {
			stances.add(UnitStance.stop);
			if (attacks) {
				stances.add(UnitStance.holdFire);
			}
		}

		super.init();

		if (noCommands) commands.clear();
	}

	@Override public void getUnitStances(Unit unit, Seq<UnitStance> out) {
		if (!(unit.controller() instanceof CommandAI ai)) return;

		var current = ai.currentCommand();

		// Return mining stances based on present items
		if (current == UnitCommand.mineCommand || current == AstraUnitCommand.anchorMine) {
			out.add(UnitStance.mineAuto);
			for (Item item : indexer.getAllPresentOres()) {
				if (unit.canMine(item) && ((mineFloor && indexer.hasOre(item)) || (mineWalls && indexer.hasWallOre(item)))) {
					ItemUnitStance itemStance = ItemUnitStance.getByItem(item);
					if (itemStance != null) out.add(itemStance);
				}
			}
		} else {
			for (UnitStance stance : stances) {
				if (stance.isCompatible(current)) out.add(stance);
			}
		}

		out.addAll(current.extraStances);
	}
}