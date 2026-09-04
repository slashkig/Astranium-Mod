package astramod.world.blocks.defense.turrets;

import mindustry.entities.*;
import mindustry.game.Team;
import mindustry.gen.*;

public class BlindspotTurret extends AstraTurret {
	public BlindspotTurret(String name) {
		super(name);
	}

	public class BlindspotTurretBuild extends ItemTurretBuild {
		@Override protected boolean validateTarget() {
			return controlled() || logicControlled() || !Units.invalidateTarget(target, canHeal() ? Team.derelict : team, x, y) && !within(target, minRange());
		}

		@Override protected Posc findEnemy(float range) {
			if (targetAir && !targetGround) {
				return Units.bestEnemy(team, x, y, range, e -> !e.dead() && !e.isGrounded() && !within(e, minRange()) && unitFilter.get(e), unitSort);
			} else {
				var ammo = peekAmmo();
				boolean buildings = targetGround && targetBlocks && (ammo == null || ammo.targetBlocks), missiles = ammo == null || ammo.targetMissiles;
				return Units.bestTarget(team, x, y, range,
					e -> !e.dead() && unitFilter.get(e) && (e.isGrounded() || targetAir) && (!e.isGrounded() || targetGround) && (missiles || !(e instanceof TimedKillc)) && !within(e, minRange()),
					b -> buildings && buildingFilter.get(b), unitSort);
			}
		}
	}
}
