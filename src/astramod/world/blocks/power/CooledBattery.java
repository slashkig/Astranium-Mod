package astramod.world.blocks.power;

import arc.Core;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;

import static mindustry.Vars.*;

public class CooledBattery extends Battery {
    /** how quickly instability moves towards 1 per frame */
	public float unstableSpeed = 0.006f;
    /** how quickly instability moves towards 0 when cooled, per frame */
	public float stabilizeSpeed = 0.002f;
	/** instability level where the block starts taking damage and displaying unstable visuals */
	public float unstableThreshold = 0.5f;
	public Liquid coolant;
	public float coolantAmount;

    public float explosionMinCharge = 0.25f;
    public int explosionRadius = 6;
    public int explosionDamage = 12000;
    public float explosionShake = 0f, explosionShakeDuration = 6f;
    public Effect explodeEffect = Fx.bigShockwave;
    public Sound explodeSound = Sounds.explosionbig;

	public int maxLightning = 30;
	public float lightningDamage = 500f;
	public int minLightningLength = 5;
	public int maxLightningLength = 15;


	public Color lightningColor = Pal.power;
	public Color lightColor = Pal.power;
	public Color unstableGlowColor = Color.scarlet;

	public TextureRegion lightsRegion;
	public TextureRegion coreGlowRegion;
	public float flashAlpha = 0.3f;
	public float flashScl = 1f;
	public float glowAlpha = 0.6f;

	public CooledBattery(String name) {
		super(name);
		hasLiquids = true;
		update = true;
		rebuildable = false;
	}

	@Override public void init() {
		consumeLiquid(coolant, coolantAmount).update(false);
		super.init();
	}

	@Override public void load() {
		super.load();

		lightsRegion = Core.atlas.find(name + "-lights");
		coreGlowRegion = Core.atlas.find(name + "-core-glow");
	}

	@Override public void setBars() {
		super.setBars();
		addBar("instability", (CooledBatteryBuild entity) -> new Bar("bar.instability", Pal.sap, () -> entity.instability));
	}

	public class CooledBatteryBuild extends BatteryBuild {
		public float instability = 0f;
		public float flash;
		public float smoothLight;

		@Override public void updateTile() {
			if (power.status > 0.001f) {
				float neededCoolant = coolantAmount * power.status;
				float satisfaction = liquids.get(coolant) / neededCoolant;

				if (satisfaction < 1f) {
					instability += unstableSpeed * power.status * (1f - satisfaction) * Time.delta;
					liquids.set(coolant, 0f);
				} else {
					instability = Mathf.lerp(instability, 0f, stabilizeSpeed * Time.delta);
					liquids.remove(coolant, neededCoolant);
				}

				if (instability > unstableThreshold) {
					float overload = (instability - unstableThreshold) / (1f - unstableThreshold);

					damageContinuous(overload * 10f);

					if (Mathf.chanceDelta(overload)) {
						Seq<Vec2> lines = new Seq<>();
						float rot = Mathf.random(360f);
						float sparkX = x, sparkY = y;
						for (int i = 0; i < 3; i++) {
							lines.add(new Vec2(sparkX + Mathf.range(3f), sparkY + Mathf.range(3f)));
							rot += Mathf.range(20f);
							sparkX += Angles.trnsx(rot, 5f);
							sparkY += Angles.trnsy(rot, 5f);
						}
						Fx.lightning.at(x, y, rot, lightningColor, lines);
					}
				}
			}
			else {
				instability = Mathf.lerp(instability, 0f, stabilizeSpeed * 2f * delta());
			}
		}

		@Override public void draw() {
			super.draw();

			if (instability > unstableThreshold) {
				flash += ((instability - unstableThreshold) / (1f - unstableThreshold)) * Time.delta;
				Draw.color(Color.red, Color.yellow, Mathf.absin(flash, flashScl, 1f));
                Draw.alpha(flashAlpha);
                Draw.rect(lightsRegion, x, y);
			}
			if (coreGlowRegion.found()) {
				Draw.color(unstableGlowColor);
				Draw.alpha(instability * glowAlpha);
				Draw.rect(coreGlowRegion, x, y);
			}
			Draw.reset();
		}

		@Override public void drawLight() {
			smoothLight = Mathf.lerpDelta(smoothLight, power.status, 0.08f);
			Drawf.light(x, y, (90f + Mathf.absin(5, 5f)) * smoothLight, Tmp.c1.set(lightColor).lerp(Color.scarlet, instability), 0.6f * smoothLight);
		}

		@Override public void onDestroyed() {
			float explosiveness = baseExplosiveness + liquids.sum((liquid, amount) -> (liquid.explosiveness * amount / 2f));
			float flammability = liquids.sum((liquid, amount) -> (liquid.flammability * amount / 2f));
			float powerStatus = power.status;

			if (block.hasLiquids && state.rules.damageExplosions) {
				liquids.each((liquid, amount) -> {
					float splash = Mathf.clamp(amount / 4f, 0f, 10f);
	
					for (int i = 0; i < Mathf.clamp(amount / 5, 0, 30); i++) {
						Time.run(i / 2f, () -> {
							Tile other = world.tileWorld(x + Mathf.range(size * tilesize / 2), y + Mathf.range(size * tilesize / 2));
							if(other != null){
								Puddles.deposit(other, liquid, splash);
							}
						});
					}
				});
			}

			Damage.dynamicExplosion(x, y, flammability, explosiveness * 3.5f, 0f, tilesize * size / 2f, state.rules.damageExplosions, destroyEffect);
	
			if(block.createRubble && !floor().solid && !floor().isLiquid){
				Effect.rubble(x, y, size);
			}

			int lightningLength = Mathf.clamp((int)(powerStatus * maxLightningLength), minLightningLength, maxLightningLength);
			for (int i = 0; i < powerStatus * maxLightning; i++) {
				Time.run(i * 0.8f + Mathf.random(4f), () -> Lightning.create(Team.derelict,
					lightningColor,
					lightningDamage * powerStatus,
					x, y,
					Mathf.random(360f),
					lightningLength + Mathf.range(3)
				));
			}

			if (state.rules.reactorExplosions && power.status >= explosionMinCharge) {
				if (explosionDamage > 0) {
					Damage.damage(x, y, explosionRadius * tilesize * powerStatus, explosionDamage * powerStatus);
				}

				explodeEffect.at(this);
				explodeSound.at(this);

				if (explosionShake > 0) {
					Effect.shake(explosionShake * powerStatus, explosionShakeDuration, this);
				}
			}
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.f(instability);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			instability = read.f();
		}
	}
}