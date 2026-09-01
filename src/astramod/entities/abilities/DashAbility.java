package astramod.entities.abilities;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.Binding;

public class DashAbility extends Ability {
	public float boostForce = 200f;
	public float duration = 20f;
	public float cooldown = 100f;
	public Effect boostEffect = Fx.none;
	public boolean drawBar = true;
	public Rect barRect = new Rect(0f, 4f, 24f, 3f);

	public DashAbility(float boostForce, float duration, float cooldown) {
		this.boostForce = boostForce;
		this.duration = duration;
		this.cooldown = cooldown;
	}

	@Override public void update(Unit unit) {
		if (data < 0f) {
			unit.impulse(Tmp.v1.trns(unit.rotation(), boostForce * Time.delta));
		} else if (data >= cooldown && Vars.player.unit() == unit && Core.input.keyDown(Binding.boost)) {
			activate(unit);
		}
		data += Time.delta;
	}

	@Override public void draw(Unit unit) {
		if (drawBar && data <= cooldown) {
			float barX = unit.x() + barRect.x, barY = unit.y() + unit.hitSize / 2f + barRect.y;
			Draw.z(Layer.overlayUI);
			Draw.color(Pal.gray);
			Fill.rect(barX, barY, barRect.width, barRect.height);
			Draw.color(Pal.boostTo);
			Fill.rect(barX, barY, barRect.width * data / (data < 0f ? -duration : cooldown), barRect.height);
			Draw.colorl(0.1f);
			Lines.stroke(0.5f);
			Lines.rect(barX - barRect.width / 2f, barY - barRect.height / 2f, barRect.width, barRect.height);
			Draw.reset();
		}
	}

	public void activate(Unit unit) {
		data = -duration;
	}
}