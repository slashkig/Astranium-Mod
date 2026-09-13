package astramod.world.blocks.production;

import arc.Core;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.effect.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import astramod.world.blocks.modular.HeatedBuild;
import astramod.world.meta.*;

import static mindustry.Vars.*;

/** Modeled after the Thorium Reactor. */
public class ExplodableCrafter extends GenericCrafter {
	public float heating = 0.005f;
	/** Heat threshold at which block starts smoking */
	public float smokeThreshold = 0.3f;
	/** Heat threshold at which lights start flashing */
	public float flashThreshold = 0.5f;
	/** Heat threshold at which the block cannot be deconstructed */
	public float noRemoveThreshold = 0.99f;

	public Item hazardItem;
	public Liquid coolant = Liquids.cryofluid;
	public float coolantPower = 0.5f;

	public float explosionMinWarmup = 0f;
	public int explosionRadius = 10;
	public int explosionDamage = 2500;
	public Effect explodeEffect = new MultiEffect(Fx.titanExplosion, Fx.titanSmoke);
	public Sound explodeSound = Sounds.explosionReactor;
	public float explosionShake = 6f, explosionShakeDuration = 16f;
	public boolean explosionBreaksProps = true;
	/** Size of scorch effect on the ground after explosion. Value from 1-9. < 1 to disable. */
	public int explosionScorchSize = 0;
	/** Chance for each tile in the explosion radius to catch on fire. */
	public float explosionIgnitionChance = 0f;
	/** If true, the ignition chance decreases with distance. */
	public boolean explosionScaleIgnitionChance = true;
	/** The speed at which ignition spreads. */
	public float explosionSpeed = 0.4f;
	/** Extra number of fireballs spawned from explosions. */
	public int explosionFireballs = 0;

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

	@Override public void setStats() {
		super.setStats();

		stats.add(AstraStat.heatSpeed, 6000f * heating, AstraStatUnit.percentSecond);
		stats.add(Stat.meltdownTime, table -> {
			float avg = craftTime / Time.toSeconds;
			float val = 30f * heating * itemCapacity * avg;
			float time = itemCapacity * avg * (1f - Mathf.sqrt(1f - 1f / val));
			if (val > 1f) {
				table.add(Strings.autoFixed(time, 2) + " " + StatUnit.seconds.localized() + " " + Core.bundle.format("bar.whenfull"));
			} else {
				table.add(Core.bundle.format("bar.nevermelts"));
			}
		});
	}

	@Override public void setBars() {
		super.setBars();
		addBar("heat", (ExplodableCrafterBuild entity) -> new Bar("bar.heat", Pal.lightOrange, () -> entity.heat));
	}

	@Override public boolean canBreak(Tile tile) {
		return state.rules.infiniteResources || tile.build.cheating() || tile.build instanceof ExplodableCrafterBuild b && b.heat < noRemoveThreshold;
	}

	public class ExplodableCrafterBuild extends GenericCrafterBuild implements HeatedBuild {
		public float heat;
		public float flash;
		public float smoothLight;

		@Override public void updateTile() {
			super.updateTile();

			if ((!hasPower || power.graph.getSatisfaction() > 0f) && hazardItem != null) {
				heat += efficiencyScale() * heating * Math.min(delta(), 4f);

				if (heat > 0) {
					handleCoolant();
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

		@Override public void drawLight() {
			smoothLight = Mathf.lerpDelta(smoothLight, efficiency, 0.08f);
			Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, heat), 0.6f * smoothLight);
		}

		@Override public void onDestroyed() {
			super.onDestroyed();

			if (state.rules.reactorExplosions && warmup >= explosionMinWarmup && (items.get(hazardItem) >= 5 || heat >= 0.5f)) {
				onExplosion();
			}
		}

		public void onExplosion() {
			if (explosionDamage > 0) {
				Damage.damage(x, y, explosionRadius * tilesize, explosionDamage);
			}

			if (explosionIgnitionChance > 0 || explosionBreaksProps) {
				Geometry.circle(tileX(), tileY(), explosionRadius, (tx, ty) -> {
					Tile t = world.tile(tx, ty);
					float dst = Mathf.dst(tileX(), tileY(), tx, ty);

					// Create fires
					if (explosionIgnitionChance > 0 && Mathf.chance(explosionIgnitionChance * (explosionScaleIgnitionChance ? 1f - Mathf.sqrt(dst / explosionRadius) : 1f))) {
						Time.run(dst / explosionSpeed, () -> {
							Fires.create(t);
						});
					}

					// Break boulders
					if (explosionBreaksProps && t != null && t.block().unitMoveBreakable) {
						ConstructBlock.deconstructFinish(t, t.block(), null);
					}
				});
			}

			if (explosionFireballs > 0) {
				int amount = Mathf.random(1, explosionFireballs);
				for(int i = 0; i < amount; i++){
					Bullets.fireball.createNet(Team.derelict, x, y, Mathf.random(360f), -1f, Mathf.random(0.5f, 1f), 1);
				}
			}

			explodeEffect.at(this);
			explodeSound.at(this);

			if (explosionShake > 0) {
				Effect.shake(explosionShake, explosionShakeDuration, this);
			}

			if (explosionScorchSize > 0) {
				Effect.scorch(x, y, explosionScorchSize);
			}
		}

		@Override public float efficiencyScale() {
			return (float)items.get(hazardItem) / itemCapacity;
		}

		public void handleCoolant() {
			float maxUsed = Math.min(liquids.get(coolant), heat / coolantPower);
			heat -= maxUsed * coolantPower;
			liquids.remove(coolant, maxUsed);
		}

		public float getHeatFrac() {
			return heat;
		}

		public void handleHeat(float amount) {
			heat += amount;
		}

		@Override public double sense(LAccess sensor) {
			if (sensor == LAccess.heat) return heat;
			return super.sense(sensor);
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