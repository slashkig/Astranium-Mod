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

		// TODO replace the placeholder colors

		enraged = new StatusEffect("enraged") {{
			color = AstraPal.heat;
			damageMultiplier = 1.2f;
			speedMultiplier = 1.4f;
		}};

		reinforced = new ArmorStatusEffect("reinforced") {{
			color = Pal.metalGrayDark;
			applyEffect = AstraFx.applyShield;
			applyColor = Pal.darkerMetal;
			armorModifier = 2f;
			init(() -> opposite(breached));
		}};

		breached = new ArmorStatusEffect("breached") {{
			color = Pal.negativeStat;
			armorModifier = 0f;
			init(() -> opposite(reinforced));
		}};

		magnetized = new ConsStatusEffect("magnetized") {{
			color = AstraItems.magnetite.color.cpy();
			effect = AstraFx.attractMetalParticles;
			effectChance = 0.1f;

			speedMultiplier = 0.8f;
			effectStrength = 30f;
			effectRange = 4f * Vars.tilesize;
			effectStat = AstraStat.magneticStrength;
			effectUnit = AstraStatUnit.percentSecond;
			updateEffect = (unit, entry) -> {
				float strength = effectStrength * Time.delta / Time.toSeconds;
				Units.nearby(null, unit.x, unit.y, effectRange, other -> {
					if (unit != other) {
						UnitUtil.attract(other, unit, strength, effectRange);
						unit.impulse(Tmp.v2.scl(-1f));
					}
				});
			};
		}};

		irradiated = new ConsStatusEffect("irradiated") {{
			color = AstraItems.nuclearRod.color.cpy();
			effect = AstraFx.radiate;
			parentizeEffect = true;

			damage = 5f / Time.toSeconds;
			updateEffect = (u, e) -> {
				u.maxHealth = u.health;
				if (u.hitTime < -1f) u.hitTime = 1f;
			};
			removedEffect = u -> u.maxHealth = u.type.health;
		}};

		overcharged = new StackableStatusEffect("overcharged") {{
			color = AstraItems.crystals.color.cpy();
			effect = AstraFx.charged;
			effectChance = 0.05f;
			tiers = new StatusEffectStack[] {
				new StatusEffectStack("overcharged-1", 100f) {{ applyEffect = AstraFx.overcharged1; }},
				new StatusEffectStack("overcharged-2", 250f) {{ applyEffect = AstraFx.overcharged2; }},
				new StatusEffectStack("overcharged-3", 600f) {{ applyEffect = AstraFx.overcharged3; }}
			};
		}};
	}
}