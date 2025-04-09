package astramod.world.blocks.production;

import arc.Core;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

/** Modeled after the Thorium Reactor. */
public class ExplodableCrafter extends GenericCrafter {
	public float heating = 0.005f;
	/** Threshold at which block starts smoking */
	public float smokeThreshold = 0.3f;
	/** Heat threshold at which lights start flashing */
	public float flashThreshold = 0.5f;

	public Item hazardItem;
	public Liquid coolant = Liquids.cryofluid;
	public float coolantPower = 0.5f;

	public float explosionMinWarmup = 0f;
	public int explosionRadius = 10;
	public int explosionDamage = 2500;
	public Effect explodeEffect = new MultiEffect(Fx.titanExplosion, Fx.titanSmoke);
	public Sound explodeSound = Sounds.explosionbig;
	public float explosionShake = 6f, explosionShakeDuration = 16f;

	public TextureRegion topRegion;
	public TextureRegion lightsRegion;

	public Color lightColor = Color.valueOf("7f19ea");
	public Color coolColor = new Color(1, 1, 1, 0f);
	public Color hotColor = Color.valueOf("ff9575a3");

	public ExplodableCrafter(String name) {
		super(name);
		schematicPriority = -5;
		rebuildable = false;
	}

	@Override public void load() {
		super.load();
		topRegion = Core.atlas.find(name + "-top");
		lightsRegion = Core.atlas.find(name + "-lights");
	}

	@Override public void setBars() {
		super.setBars();
		addBar("heat", (ExplodableCrafterBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.heat));
	}

	public class ExplodableCrafterBuild extends GenericCrafterBuild{
		public float heat;
		public float flash;
		public float smoothLight;

		@Override public void updateTile() {
			super.updateTile();

			if ((!hasPower || power.graph.getSatisfaction() > 0f) && hazardItem != null) {
				heat += (items.get(hazardItem) / itemCapacity) * heating * Math.min(delta(), 4f);

				if (heat > 0) {
					float maxUsed = Math.min(liquids.get(coolant), heat / coolantPower);
					heat -= maxUsed * coolantPower;
					liquids.remove(coolant, maxUsed);
				}

				if (heat > smokeThreshold) {
					float smoke = 1f + (heat - smokeThreshold) / (1f - smokeThreshold);
					if (Mathf.chance(smoke / 20 * delta())) {
						Fx.reactorsmoke.at(x + Mathf.range(size * tilesize / 2f), y + Mathf.range(size * tilesize / 2f));
					}
				}

				if (heat >= 1f) {
					kill();
				}

				heat = Mathf.clamp(heat);
			}
		}

		@Override public double sense(LAccess sensor) {
			if (sensor == LAccess.heat) return heat;
			return super.sense(sensor);
		}

        @Override public void onDestroyed() {
            super.onDestroyed();

            if (state.rules.reactorExplosions && warmup >= explosionMinWarmup && (items.get(hazardItem) >= 5 || heat >= 0.5f)) {
				if (explosionDamage > 0) {
					Damage.damage(x, y, explosionRadius * tilesize, explosionDamage);
				}

				explodeEffect.at(this);
				explodeSound.at(this);

				if(explosionShake > 0) {
					Effect.shake(explosionShake, explosionShakeDuration, this);
				}
			}
		}

		@Override public void drawLight() {
			smoothLight = Mathf.lerpDelta(smoothLight, efficiency, 0.08f);
			Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, heat), 0.6f * smoothLight);
		}

		@Override public void draw() {
			super.draw();

			Draw.color(coolColor, hotColor, heat);
			Fill.rect(x, y, size * tilesize, size * tilesize);

			if (topRegion.found()) {
				Draw.color(liquids.current().color);
				Draw.alpha(liquids.currentAmount() / liquidCapacity);
				Draw.rect(topRegion, x, y);
			}
			if (lightsRegion.found() && heat > flashThreshold) {
				flash += (1f + ((heat - flashThreshold) / (1f - flashThreshold)) * 5.4f) * Time.delta;
				Draw.color(Color.red, Color.yellow, Mathf.absin(flash, 9f, 1f));
				Draw.alpha(0.3f);
				Draw.rect(lightsRegion, x, y);
			}

			Draw.reset();
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.f(heat);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			heat = read.f();
		}
	}
}