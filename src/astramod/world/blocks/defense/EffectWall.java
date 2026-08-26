package astramod.world.blocks.defense;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.func.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.world.blocks.defense.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

/** A wall that has an area effect. */
public class EffectWall extends Wall {
	public float effectRange = 24f;
	public float effectStrength = 10f;
	public StatusEffect statusEffect = StatusEffects.none;
	public float statusEffectTime = 0f;
	public Cons<EffectWallBuild> effect = b -> { return; };

	public Color effectColor;
	public float effectAlpha = 0f;
	public float lightAlpha = 0.5f;

	public Stat effectStat;
	public StatUnit effectUnit = StatUnit.none;

	public EffectWall(String name) {
		super(name);
		update = true;
	}

	@Override public void init() {
		super.init();
		clipSize = Math.max(clipSize, effectRange);
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(Stat.range, effectRange / tilesize, StatUnit.blocks);
		stats.add(effectStat, effectStrength, effectUnit);
		if (statusEffect != StatusEffects.none) stats.add(AstraStat.applies, AstraStatValues.statusEffect(statusEffect, statusEffectTime));
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, effectRange, effectColor);
	}

	public class EffectWallBuild extends WallBuild implements Ranged {

		public float range() {
			return effectRange;
		}

		@Override public void updateTile() {
			effect.get(this);
		}

		@Override public void drawCached() {
			super.drawCached();

			if (effectAlpha > 0f) {
				Draw.color(effectColor, effectAlpha);
				Draw.z(Layer.overlayUI - 0.1f);
				Fill.circle(x, y, effectRange);
				Lines.circle(x, y, effectRange);

				Draw.reset();
			}
		}

		@Override public void drawSelect() {
			Drawf.dashCircle(x, y, effectRange, effectColor);
		}

		@Override public void drawLight() {
			Drawf.light(x, y, effectRange, effectColor, lightAlpha);
		}
	}
}