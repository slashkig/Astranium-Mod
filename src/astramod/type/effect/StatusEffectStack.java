package astramod.type.effect;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.type.*;
import astramod.world.meta.*;

public class StatusEffectStack extends StatusEffect {
	public float stackDamage;
	public Cons<Unit> stackEffect = u -> u.damage(stackDamage);

	public float maxStackTime = 10f * Time.toSeconds;

	public StatusEffectStack(String name) {
		super(name);
		show = false;
	}

	public StatusEffectStack(String name, float damage) {
		this(name);
		stackDamage = damage;
	}

	public void display(Table table) {
		AstraStatValues.statusEffect(this, maxStackTime).display(table);
		AstraStatValues.addRow(table, "stat.damage", stackDamage, true);
	}
}