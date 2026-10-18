package astramod.entities.abilities;

import arc.Core;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.entities.UnitUtil;
import astramod.io.*;
import astramod.type.weapons.*;
import astramod.world.meta.*;

public class WeaponAbility extends ActivatedAbility {
	public String name;
	public Func2<AIController, WeaponMount, Teamc> targeter;
	public float rangeMultiplier = 1f;
	protected WeaponMount mount;
	protected UnitType unit;

	public WeaponAbility(String name) {
		this.name = name;
	}

	@Override public void init(UnitType type) {
		super.init(type);
		unit = type;
		if (targeter == null) targeter = (ai, mount) -> {
			Unit unit = ai.unit();
			return Units.closestTarget(unit.team, unit.x, unit.y, mount.weapon.range() * rangeMultiplier,
				u -> u.checkTarget(unit.type.targetAir, unit.type.targetGround) && UnitUtil.withinFiringArc(unit, u, mount.weapon),
				t -> unit.type.targetGround && (unit.type.targetUnderBlocks || !t.block.underBullets) && UnitUtil.withinFiringArc(unit, t, mount.weapon)
			);
		};
	}

	@Override public void created(Unit unit) {
		mount = Structs.find(unit.mounts, m -> m.weapon.name == name);
	}

	@Override public void addStats(Table t) {
		super.addStats(t);

		Weapon weapon = unit.weapons.find(w -> w.name == name && !w.flipSprite);

		if (weapon.hasStats(unit) || weapon instanceof AbilityWeapon) {
			TextureRegion region = !name.isEmpty() ? Core.atlas.find(name + "-preview", weapon.region) : null;
			if (region != null && region.found() && weapon.showStatSprite) {
				t.image(region).size(60).scaling(Scaling.bounded).left().top();
				t.row();
			}
			AstraStatValues.tableInfo(t, getBundle() + ".info");

			weapon.addStats(unit, t);
			t.row();
		}
	}

	@Override public void update(Unit unit) {
		if (mount == null) created(unit);
		else if (mount.shoot && mount.reload > 0f) mount.shoot = false;
		else super.update(unit);
	}

	@Override public void updatePlayer(Unit unit) {
		mount.aimX = unit.aimX;
		mount.aimY = unit.aimY;
	}

	@Override public boolean shouldActivate(Unit unit, Teamc target) {
		if (target != null) {
			mount.aimX = target.x();
			mount.aimY = target.y();
			return true;
		} else return false;
	}

	@Override public Teamc selectTarget(AIController controller) {
		return targeter.get(controller, mount);
	}

	@Override public boolean validTarget(Unit unit, Teamc target) {
		return !Units.invalidateTarget(target, mount.weapon.bullet.collidesTeam ? null : unit.team, unit.x, unit.y, mount.weapon.range() * rangeMultiplier)
			&& UnitUtil.withinFiringArc(unit, target, mount.weapon);
	}

	@Override public float getProgress(Unit unit) {
		return mount != null ? 1f - mount.reload / mount.weapon.reload : 0f;
	}

	@Override public void activate(Unit unit) {
		if (Vars.net.server()) NetUtil.clientIntsReliable("weapon-shoot", unit.id, Structs.indexOf(unit.mounts, mount), 1);
		mount.shoot = true;
	}

	@Override public String getBundle() {
		return "weapon." + name;
	}
}