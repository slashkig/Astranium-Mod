package astramod.content;

import arc.util.*;
import mindustry.Vars;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.type.*;
import astramod.entities.*;
import astramod.graphics.*;
import astramod.type.effect.*;
import astramod.world.meta.*;

public class AstraStatusEffects {
	public static StatusEffect enraged, reinforced, breached, magnetized, irradiated, overcharged;

	public static void load() {
		Log.info("Loading status effects");
		StatusEffects.slow.show = true;

		enraged = new StatusEffect("enraged") {{
			color = AstraPal.heat;
			damageMultiplier = 1.2f;
			speedMultiplier = 1.4f;
		}};

		reinforced = new ArmorStatusEffect("reinforced") {{
			color = Pal.metalGrayDark;
			armorModifier = 6f;
			init(() -> opposite(breached));
		}};

		breached = new ArmorStatusEffect("breached") {{
			color = Pal.negativeStat;
			armorModifier = -8f;
			init(() -> opposite(reinforced));
		}};

		magnetized = new ConsStatusEffect("magnetized") {{
			color = AstraItems.magnetite.color.cpy();
			speedMultiplier = 0.8f;
			effectStrength = 30f;
			effectRange = 3f * Vars.tilesize;
			effectStat = AstraStat.magneticStrength;
			effectUnit = AstraStatUnit.percentSecond;
			updateEffect = (unit, entry) -> {
				float strength = effectStrength * Time.delta / Time.toSeconds;
				Units.nearby(null, unit.x, unit.y, effectRange, other -> {
					if (unit != other) {
						Unitsx.attract(other, unit, strength, effectRange);
						unit.impulse(Tmp.v2.scl(-1f));
					}
				});
			};
		}};

		irradiated = new ConsStatusEffect("irradiated") {{
			color = AstraItems.nuclearRod.color.cpy();
			damage = 5f / Time.toSeconds;
			updateEffect = (u, e) -> {
				u.maxHealth = u.health;
				if (u.hitTime < -1f) u.hitTime = 1f;
			};
			removedEffect = u -> u.maxHealth = u.type.health;
		}};

		overcharged = new StackableStatusEffect("overcharged") {{
			color = AstraItems.crystals.color.cpy();
			tiers = new StatusEffectStack[] {
				new StatusEffectStack("overcharged-1", 100f),
				new StatusEffectStack("overcharged-2", 250f),
				new StatusEffectStack("overcharged-3", 600f)
			};
		}};
	}
}