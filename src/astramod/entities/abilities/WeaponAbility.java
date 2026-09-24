package astramod.entities.abilities;

import arc.Core;
import arc.graphics.g2d.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.type.weapons.*;
import astramod.world.meta.*;

public class WeaponAbility extends ActivatedAbility {
	public String name;
	protected WeaponMount mount;
	protected UnitType unit;

	public WeaponAbility(String name) {
		this.name = name;
	}

	@Override public void init(UnitType type) {
		unit = type;
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

	@Override public float getProgress(Unit unit) {
		return mount != null ? 1f - mount.reload / mount.weapon.reload : 0f;
	}

	@Override public void activate(Unit unit) {
		mount.shoot = true;
	}

	@Override public String getBundle() {
		return "weapon." + name;
	}
}