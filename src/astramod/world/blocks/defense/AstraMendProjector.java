package astramod.world.blocks.defense;

import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.util.Strings;
import mindustry.content.*;
import mindustry.world.blocks.defense.MendProjector;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import astramod.world.draw.*;

import static mindustry.Vars.*;

public class AstraMendProjector extends MendProjector {
	public DrawBlock drawer = new DrawSuper();
	public float healAmount = 500f;
	public float warmupSpeed = 0.08f;

	public AstraMendProjector(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		drawer.load(this);
	}

	@Override public void setStats() {
		super.setStats();
		stats.remove(Stat.repairTime);
		if (reload == 60f) {
			stats.add(Stat.repairSpeed, healAmount, StatUnit.perSecond);
		} else {
			stats.add(Stat.repairSpeed, "@ / @ seconds", Strings.autoFixed(healAmount, 2), Strings.autoFixed(reload / 60f, 2));
		}
	}

	@Override public TextureRegion[] icons() {
		return drawer.icons(this);
	}

	public class DrawMendBuild extends MendBuild implements SuperDrawable {
		@Override public void draw() {
			drawer.draw(this);
		}

		@Override public void updateTile() {
			boolean canHeal = !checkSuppression();

			smoothEfficiency = Mathf.lerpDelta(smoothEfficiency, efficiency, warmupSpeed);
			heat = Mathf.lerpDelta(heat, efficiency > 0 && canHeal ? 1f : 0f, warmupSpeed);
			charge += heat * delta();

			phaseHeat = Mathf.lerpDelta(phaseHeat, optionalEfficiency, 0.1f);

			if (optionalEfficiency > 0 && timer(timerUse, useTime) && canHeal) {
				consume();
			}

			if (charge >= reload && canHeal) {
				float realRange = range + phaseHeat * phaseRangeBoost;
				charge = 0f;

				indexer.eachBlock(this, realRange, b -> b.damaged() && !b.isHealSuppressed(), other -> {
					other.heal(healAmount * efficiency);
					other.recentlyHealed();
					Fx.healBlockFull.at(other.x, other.y, other.block.size, baseColor, other.block);
				});
			}
		}

		@Override public float warmup() {
			return heat;
		}

		public void drawSuper() {
			super.draw();
		}
	}
}