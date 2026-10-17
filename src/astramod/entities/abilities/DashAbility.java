package astramod.entities.abilities;

import arc.math.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.AIController;
import mindustry.gen.*;
import mindustry.type.*;

public class DashAbility extends ActivatedAbility {
	public float speedBoost = 1f;
	public float duration = 20f;
	public float cooldown = 100f;
	public Effect boostEffect = Fx.none;

	public DashAbility(float speedBoost, float duration, float cooldown) {
		this.speedBoost = speedBoost;
		this.duration = duration;
		this.cooldown = cooldown;
	}

	@Override public void init(UnitType type) {
		super.init(type);
		data = cooldown;
	}

	@Override public void addStats(Table t) {
		super.addStats(t);
		t.add(abilityStat("tiles", Strings.autoFixed(speedBoost * duration / Vars.tilesize, 2)));
		t.row();
		t.add(abilityStat("duration", duration / Time.toSeconds));
		t.row();
		t.add(abilityStat("cooldown", cooldown / Time.toSeconds));
	}

	@Override public void update(Unit unit) {
		if (data < 0f) {
			unit.vel.add(Tmp.v1.trns(unit.rotation(), speedBoost * unit.type.accel * Time.delta));
		} else {
			super.update(unit);
		}
		data += Time.delta;
	}

	@Override public boolean canActivate(Unit unit) {
		return data >= cooldown;
	}

	@Override public boolean shouldActivate(Unit unit, Teamc target) {
		return target != null;
	}

	@Override public Teamc selectTarget(AIController controller) {
		Unit unit = controller.unit();
		return Units.closestTarget(unit.team, unit.x, unit.y, speedBoost * duration + unit.range() * 0.5f,
			u -> u.checkTarget(unit.type.targetAir, unit.type.targetGround) && validTargetPos(unit, u),
			t -> unit.type.targetGround && (unit.type.targetUnderBlocks || !t.block.underBullets) && validTargetPos(unit, t)
		);
	}

	protected boolean validTargetPos(Unit unit, Teamc target) {
		return !unit.within(target, speedBoost * duration - unit.range() * 0.5f) && Angles.within(unit.angleTo(target), unit.rotation(), 45f);
	}

	@Override public float getProgress(Unit unit) {
		return Mathf.clamp(data / (data < 0f ? -duration : cooldown));
	}

	public void activate(Unit unit) {
		data = -duration;
		boostEffect.at(unit.x, unit.y, unit.rotation, unit);
	}
}