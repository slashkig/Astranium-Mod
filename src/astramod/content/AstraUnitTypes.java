package astramod.content;

import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.*;
import astramod.ai.types.*;
import astramod.entities.abilities.*;
import astramod.entities.bullet.*;
import astramod.entities.part.*;
import astramod.gen.*;
import astramod.graphics.*;
import astramod.type.unit.*;
import astramod.type.weapons.*;

import static mindustry.Vars.*;

public class AstraUnitTypes {
	// air
	@EntityPoint(UnitEntity.class) public static UnitType
		manager, director,
		milvus, pandion,
		newt;

	// air + payload
	@EntityPoint(PayloadUnit.class) public static UnitType
		overseer;

	// air + tether
	@EntityDef({ Unitc.class, BuildingTetherc.class }) public static UnitType
		gatherer, initiate, seeker, ward;

	// mech + conceal
	@EntityDef({ Unitc.class, Concealc.class, Mechc.class }) public static UnitType
		dicentra, achillion, strelitz,
		zenaida, trexon, oriolus,
		legion, decanus, centurion;

	// tank + conceal
	@EntityDef({ Unitc.class, Concealc.class, Tankc.class }) public static UnitType
		hymeno;

	// tank
	@EntityPoint(TankUnit.class) public static UnitType
		vitex,
		aculei, echidna,
		arbalest, bartizan,
		meissa, saiph,
		superBartizan;

	// legs + conceal
	@EntityDef({ Unitc.class, Concealc.class, Legsc.class }) public static UnitType
		baeri, vorhies;

	// hover + conceal
	@EntityDef({ Unitc.class, Concealc.class, ElevationMovec.class }) public static UnitType
		fledge;

	public static void load() {
		Log.info("Loading units");

		// region CORE

		manager = new AstraUnitType("manager") {{
			controller = u -> u.team.isAI() ? new BuilderAI(true, 400f) : new CommandAI();
			flying = true;
			alwaysUnlocked = true;
			targetBuildingsMobile = false;
			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 120f;
			armor = 1f;
			hitSize = 10f;
			fogRadius = 0f;
			itemCapacity = 25;

			drag = 0.06f;
			accel = 0.1f;
			speed = 4f;
			rotateSpeed = 15f;

			buildSpeed = 0.6f;
			mineSpeed = 5f;
			mineTier = 1;
			mineWalls = true;

			lowAltitude = true;
			engineOffset = 7f;

			weapons.add(new Weapon("astramod-manager-weapon") {{
				reload = 30f;

				top = false;
				x = 5f;
				y = 3.75f;
				layerOffset = -0.1f;
				shootSound = Sounds.shootAlpha;

				bullet = new TeamLaserBoltBulletType(5f, 15) {{
					width = 1.6f;
					height = 6f;
					lifetime = 35f;
					scaleKeepVelocity = true;
					buildingDamageMultiplier = 0f;
					homingPower = 0.03f;
				}};
			}});
		}};

		director = new AstraUnitType("director") {{
			controller = u -> u.team.isAI() ? new BuilderAI(true, 400f) : new CommandAI();
			flying = true;
			targetBuildingsMobile = false;
			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 180f;
			armor = 2f;
			hitSize = 12f;
			fogRadius = 0;
			itemCapacity = 40;

			drag = 0.07f;
			accel = 0.1f;
			speed = 4.5f;
			rotateSpeed = 17.5f;

			buildSpeed = 0.8f;
			mineTier = 1;
			mineSpeed = 6f;
			mineWalls = true;

			lowAltitude = true;
			engineOffset = 10f;
			engineSize = 3.5f;

			weapons.add(new Weapon("astramod-director-weapon") {{
				reload = 50f;
				shoot.shots = 3;
				shoot.shotDelay = 5f;
				inaccuracy = 6f;
				x = 5.5f;
				y = 2.5f;
				top = false;
				layerOffset = -0.1f;
				shootSound = Sounds.shootAlpha;

				bullet = new TeamLaserBoltBulletType(5.5f, 15) {{
					width = 1.6f;
					height = 5f;
					lifetime = 37.5f;
					scaleKeepVelocity = true;
					buildingDamageMultiplier = 0f;
					homingPower = 0.03f;
				}};
			}});
		}};

		overseer = new AstraUnitType("overseer") {{
			controller = u -> u.team.isAI() ? new BuilderAI(true, 400f) : new CommandAI();
			flying = true;

			targetBuildingsMobile = false;
			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 260f;
			armor = 3f;
			hitSize = 15f;
			fogRadius = 0;
			itemCapacity = 60;
			payloadCapacity = 4f * tilePayload;
			pickupUnits = false;

			drag = 0.07f;
			accel = 0.1f;
			speed = 5f;
			rotateSpeed = 17.5f;

			buildSpeed = 1.3f;
			mineTier = 2;
			mineSpeed = 10f;
			mineWalls = true;

			lowAltitude = true;
			engineOffset = 13f;
			engineSize = 5f;
			setEnginesMirror(new UnitEngine(7f, -12f, 3f, 315f));

			weapons.add(new RampUpWeapon("astramod-overseer-weapon") {{
				reload = 35f;
				rampupFactor = 0.875f;
				shootWarmupSpeed = 1f / 400f;
				inaccuracy = 4f;

				top = false;
				x = 7.9f;
				y = 1.6f;
				shootY = 4.5f;
				layerOffset = -0.1f;
				shootSound = Sounds.shootLaser;

				bullet = new TeamLaserBoltBulletType(6.5f, 15) {{
					lifetime = 34f;
					scaleKeepVelocity = true;
					buildingDamageMultiplier = 0f;
					homingPower = 0.03f;
				}};

				extendedStats = false;
			}});
		}};

		// region CORE MODULES

		gatherer = new AstraAnchoredUnitType("gatherer") {{
			aiController = AnchoredMinerAI::new;
			flying = true;
			canAttack = false;
			targetPriority = -5f;

			health = 150f;
			hitSize = 6f;
			fogRadius = 6f;
			itemCapacity = 30;

			drag = 0.06f;
			accel = 0.12f;
			speed = 1.7f;

			mineTier = 2;
			mineSpeed = 8f;
			mineWalls = true;
			mineItems = Seq.with(AstraItems.hematite, Items.copper, Items.lead, Items.coal, Items.graphite);

			lowAltitude = true;
			engineOffset = 5.5f;
			engineSize = 2f;
		}};

		initiate = new AstraAnchoredUnitType("initiate") {{
			aiController = AnchoredSupportAI::new;
			flying = true;
			targetPriority = -5f;

			health = 250f;
			armor = 1f;
			hitSize = 9f;
			fogRadius = 6f;
			itemCapacity = 0;

			drag = 0.06f;
			accel = 0.12f;
			speed = 2.8f;

			buildSpeed = 0.6f;
			buildRange = 100f;

			lowAltitude = true;
			engineOffset = 5.5f;
			engineSize = 2f;

			weapons.add(new RepairBeamWeapon() {{
				reload = 20f;
				rotate = false;
				beamWidth = 0.5f;
				repairSpeed = 2.5f;
				shootCone = 4f;

				aimDst = 0f;
				targetUnits = false;
				targetBuildings = true;

				mirror = false;
				x = 0f;
				y = 4f;
				shootY = 0f;
				widthSinMag = 0.1f;
				laserColor = healColor = Pal.yellowBoltFront;

				bullet = new BulletType() {{
					maxRange = 30f;
				}};
			}});
		}};

		seeker = new AstraAnchoredUnitType("seeker") {{
			aiController = AnchoredAttackerAI::new;
			flying = true;

			health = 600f;
			armor = 2f;
			hitSize = 9f;
			fogRadius = 6f;
			itemCapacity = 0;

			drag = 0.05f;
			accel = 0.1f;
			speed = 2.2f;

			lowAltitude = true;
			engineOffset = 5.6f;
			engineSize = 2.2f;
			trailLength = 7;

			weapons.add(new Weapon("astramod-seeker-weapon") {{
				reload = 60f;
				inaccuracy = 1f;
				shoot.shots = 4;
				shoot.shotDelay = 6f;

				top = false;
				mirror = false;
				x = 0f;
				y = 3f;
				shootSound = Sounds.shootLaser;

				bullet = new TeamLaserBoltBulletType(5f, 15) {{
					lifetime = 25f;
					scaleKeepVelocity = true;
					buildingDamageMultiplier = 0.5f;
				}};
			}});
		}};

		ward = new AstraAnchoredUnitType("warder") {{
			aiController = AnchoredShieldAI::new;
			flying = true;
			targetPriority = -4f;

			health = 400f;
			armor = 3f;
			hitSize = 9f;
			range = 40f;
			fogRadius = 6f;
			itemCapacity = 0;

			drag = 0.05f;
			accel = 0.15f;
			speed = 2.5f;

			lowAltitude = true;
			engineOffset = 5.6f;
			engineSize = 2.2f;

			abilities.add(new ShieldArcAbility() {{
				max = 500f;
				regen = 0.15f;
				cooldown = 10f * Time.toSeconds;
				whenShooting = false;

				angle = 120f;
				radius = 10f;
				width = 5f;
			}});
		}};

		// region OFFENSIVE MECHS

		dicentra = new AstraUnitType("dicentra") {{
			aiController = GroundBacklineAI::new;

			health = 220;
			armor = 1f;
			hitSize = 10f;
			fogRadius = 12f;
			itemCapacity = 10;

			speed = 0.6f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			immunities.add(StatusEffects.slow);

			weapons.add(new Weapon("astramod-dicentra-weapon") {{
				reload = 25f;
				recoil = 1f;

				top = false;
				x = 7f;
				y = 0.7f;
				shootY = 5f;
				ejectEffect = Fx.casing2;

				shootSound = Sounds.shootStell;
				shootSoundVolume = 1.8f;

				bullet = new ImpactBulletType(5f, 18) {{
					width = 7f;
					height = 12f;
					lifetime = 30f;
					knockback = 2f;

					status = StatusEffects.slow;
					statusDuration = Time.toSeconds;
					statusChance = 0.12f;

					hitColor = backColor = AstraPal.ironBack;
					frontColor = AstraPal.ironFront;
				}};
			}});
		}};

		achillion = new AstraUnitType("achillion") {{
			health = 700;
			armor = 4f;
			hitSize = 13f;
			fogRadius = 12f;
			itemCapacity = 20;

			speed = 0.6f;
			accel = 0.25f;
			stepSoundVolume = 0.4f;

			immunities.add(StatusEffects.burning);

			weapons.add(new Weapon("astramod-achillion-weapon") {{
				reload = 50f;
				recoil = 1.8f;
				inaccuracy = 15f;
				shoot.shots = 12;
				shoot.shotDelay = 0f;
				velocityRnd = 0.1f;
				lifeRnd = 0.1f;

				top = false;
				x = 7.96f;
				y = 0.34f;
				shootY = 6.5f;
				cooldownTime = 30f;
				heatColor = AstraPal.heat;
				ejectEffect = Fx.casing3;

				shootSound = Sounds.shootDiffuse;
				shootSoundVolume = 1.2f;

				bullet = new BasicBulletType(4.5f, 14) {{
					width = 5f;
					height = 6f;
					lifetime = 28f;
					knockback = 0.4f;

					status = StatusEffects.burning;
					statusDuration = 8f * Time.toSeconds;
					statusChance = 0.25f;
					
					hitColor = backColor = AstraPal.fireBulletBack;
					frontColor = AstraPal.fireBulletFront;
					shootEffect = AstraFx.fireShootShotgun;
					trailEffect = AstraFx.fireBulletTrailSmall;
					trailChance = 0.3f;
					hitEffect = despawnEffect = AstraFx.fireBulletHit;
				}};
			}});
		}};

		strelitz = new AstraUnitType("strelitz") {{
			singleTarget = true;

			health = 1350;
			armor = 7f;
			hitSize = 17f;
			fogRadius = 14f;
			itemCapacity = 40;

			speed = 0.5f;
			accel = 0.25f;
			rotateSpeed = 2.6f;
			stepSoundVolume = 0.7f;

			immunities.add(StatusEffects.freezing);

			weapons.add(new Weapon("astramod-strelitz-ice-cannon") {{
				reload = 45f;
				recoil = 1.5f;

				top = false;
				x = 11.5f;
				y = -0.5f;
				shootY = 10f;

				heatColor = AstraPal.freezeFront;
				cooldownTime = 70f;
				ejectEffect = Fx.casing3;
				shootSound = AstraSounds.shootIce;
				shootSoundVolume = 0.5f;

				bullet = new BasicBulletType(4.5f, 75, "astramod-fragger") {{
					width = 12f;
					height = 18f;
					lifetime = 35f;
					knockback = 1f;

					status = StatusEffects.freezing;
					statusDuration = 6f * Time.toSeconds;
					statusChance = 0.4f;

					frontColor = AstraPal.freezeFront;
					backColor = trailColor = AstraPal.freezeBack;
					trailEffect = AstraFx.iceBulletTrail;
					trailChance = 0.8f;
					trailWidth = 2f;
					trailLength = 7;
					shootEffect = AstraFx.iceShoot;
					hitEffect = AstraFx.icyBurst;
					hitSound = AstraSounds.hitIce;
					hitSoundVolume = 0.5f;

					fragBullets = 3;
					fragBullet = new BasicBulletType(2.5f, 10f) {{
						lifetime = 18f;
						width = 5f;
						height = 10f;
						armorMultiplier = 2f;
						shrinkY = 0.75f;
						collidesAir = false;

						status = StatusEffects.freezing;
						statusDuration = 3f * Time.toSeconds;
						statusChance = 0.2f;

						frontColor = AstraPal.freezeFront;
						backColor = AstraPal.freezeBack;
						despawnEffect = Fx.none;
					}};
				}};
			}},
			new Weapon("astramod-strelitz-viper") {{
				reload = 40f;
				recoil = 1f;
				inaccuracy = 15f;
				rotate = true;

				mirror = false;
				x = -9f;
				y = -1.5f;
				shootY = 2f;
				controllable = false;
				autoTarget = true;
				useAttackRange = false;

				shootSound = Sounds.shootMissile;
				shootSoundVolume = 0.8f;

				bullet = new MissileBulletType(4f, 25) {{
					width = height = 8f;
					lifetime = 50f;
					homingPower = 0.4f;
					range = 60f;
					collidesGround = false;

					shootEffect = AstraFx.colorFlare(this);
				}};
			}});
		}};

		// region SONIC MECHS

		zenaida = new AstraUnitType("zenaida") {{
			health = 250;
			armor = 2f;
			hitSize = 10f;
			fogRadius = 8f;
			itemCapacity = 10;

			speed = 0.7f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			weapons.add(new AstraWeapon("astramod-zenaida-weapon") {{
				reload = 30f;
				recoil = 1.2f;
				alternate = false;

				top = false;
				x = 7.5f;
				y = 0.5f;
				shootX = -0.5f;
				shootY = 5f;
				heatColor = AstraPal.sonicHeat;

				shootSound = AstraSounds.shootSonic;
				shootSoundVolume = 0.7f;

				bullet = new SonicBulletType(6f, 40) {{
					width = 9f;
					height = 14f;
					lifetime = 15f;
				}};
			}});
		}};

		trexon = new AstraUnitType("trexon") {{
			health = 640;
			armor = 5f;
			hitSize = 12f;
			fogRadius = 8f;
			itemCapacity = 20;

			speed = 1f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			weapons.add(new AstraWeapon("astramod-trexon-weapon") {{
				reload = 15f;
				recoil = 1.2f;

				top = false;
				x = 9f;
				y = -1.25f;
				shootY = 7.25f;

				heatColor = AstraPal.sonicHeat;
				shootSound = AstraSounds.shootSonic;
				shootSoundVolume = 0.7f;

				bullet = new SonicBulletType(6f, 50) {{
					width = 9f;
					height = 14f;
					lifetime = 12f;
				}};
			}});
		}};

		oriolus = new AstraUnitType("oriolus") {{
			aiController = AbilityAI::new;
			singleTarget = true;

			health = 1100;
			armor = 6f;
			hitSize = 17f;
			fogRadius = 9f;
			itemCapacity = 30;

			speed = 1f;
			accel = 0.3f;
			rotateSpeed = 2.5f;
			stepSoundVolume = 1f;

			abilities.add(new DashAbility(4f, 0.5f * Time.toSeconds, 20f * Time.toSeconds) {{
				boostEffect = AstraFx.dynamicBurst(AstraPal.sonicShotFront, AstraPal.sonicShotBack, 3);
			}});

			weapons.add(new AstraWeapon("astramod-oriolus-weapon") {{
				reload = 100f;
				recoil = 2f;
				shoot.shots = 4;
				shoot.shotDelay = 6f;

				mirror = false;
				x = 0f;
				y = 1f;
				shootY = 9f;
				rotate = true;
				rotateSpeed = 2f;
				rotationLimit = 60f;

				heatColor = AstraPal.sonicHeat;
				shootSound = AstraSounds.shootSonic;

				bullet = new SonicBulletType(6f, 60, "astramod-sonic-shot-large") {{
					width = 11f;
					height = 18f;
					lifetime = 18f;
					shootEffect = AstraFx.sonicHit;
				}};
			}},
			new AstraWeapon("astramod-oriolus-mount") {{
				reload = 20f;
				recoil = 1.2f;
				alternate = false;
				rotate = true;
				rotateSpeed = 2f;
				rotationLimit = 90f;

				x = 10.25f;
				y = 0.25f;
				shootY = 5f;

				heatColor = AstraPal.sonicHeat;
				shootSound = AstraSounds.shootSonic;
				shootSoundVolume = 0.7f;

				bullet = new SonicBulletType(6f, 20) {{
					width = 9f;
					height = 14f;
					lifetime = 14f;
					shoot.firstShotDelay = 22f;
				}};
			}});
		}};

		// region ROCKET MECHS

		legion = new AstraUnitType("legion") {{
			aiController = RaiderAI::new;

			health = 220;
			armor = 1f;
			hitSize = 10f;
			fogRadius = 10f;
			itemCapacity = 10;

			speed = 0.5f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;
			canBoost = true;
			engineOffset = 4.5f;
			engineSize = 3.5f;
			engineColor = AstraPal.missileOrange;
			boostMultiplier = 1.7f;
			knockbackMultiplier = 1.4f;

			weapons.add(new AstraWeapon("astramod-legion-rocket-mount") {{
				reload = 100f;
				rotate = true;
				rotationLimit = 30f;
				inaccuracy = 4f;
				velocityRnd = 0.15f;
				shoot.firstShotDelay = 20f;

				mirror = false;
				x = -6.75f;
				y = 0.75f;
				shootY = 5f;
				heatColor = AstraPal.heat;
				cooldownTime = 90f;

				shootSound = Sounds.shootMissile;
				shootSoundVolume = 2f;

				bullet = new MissileBulletType(3f, 10, "astramod-torpedo") {{
					lifetime = 50f;
					width = height = 10f;
					splashDamageRadius = 2.4f * tilesize;
					splashDamage = 40;
					scaledSplashDamage = true;

					recoil = 4f;
					keepVelocity = false;
					scaleLife = true;
					homingPower = 0f;
					drag = -0.003f;
					weaveMag = 3f;
					weaveScale = 4f;

					frontColor = AstraPal.missileOrange;
					backColor = trailColor = AstraPal.missileOrangeBack;
					shootEffect = AstraFx.triFlare(this, 1.5f);
					hitEffect = AstraFx.dynamicExplosion(this, 7, 6);
					trailChance = 0f;
					trailInterval = 3f;
				}};
			}});
		}};

		decanus = new AstraUnitType("decanus") {{
			aiController = RaiderAI::new;

			health = 680;
			armor = 3f;
			hitSize = 12f;
			fogRadius = 12f;
			itemCapacity = 20;

			speed = 0.4f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			canBoost = true;
			engineOffset = 6.5f;
			engineSize = 5f;
			engineColor = AstraPal.missileOrange;
			boostMultiplier = 1.8f;

			weapons.add(new AstraWeapon("astramod-decanus-weapon") {{
				reload = 150f;
				recoil = 0.8f;
				inaccuracy = 6f;
				velocityRnd = 0.2f;
				alternate = false;
				shoot = new ShootPattern() {{
						shots = 4;
						shotDelay = 5f;
					}
					public void flip() {
						firstShotDelay = shotDelay * (shots + 1);
					}
				};

				top = false;
				x = 9.25f;
				y = 0.25f;
				shootY = 5.75f;
				heatColor = AstraPal.heat;
				cooldownTime = 160f;

				shootSound = Sounds.shootMissileShort;
				shootSoundVolume = 1.5f;

				bullet = new MissileBulletType(4f, 8, "astramod-torpedo") {{
					lifetime = 32f;
					width = height = 7f;
					splashDamageRadius = 1.6f * tilesize;
					splashDamage = 24;
					scaledSplashDamage = true;

					recoil = 0.6f;
					keepVelocity = false;
					scaleLife = true;
					homingPower = 0f;
					drag = -0.003f;

					frontColor = AstraPal.missileOrange;
					backColor = trailColor = AstraPal.missileOrangeBack;
					shootEffect = AstraFx.triFlare(this);
					hitEffect = AstraFx.dynamicExplosion(this, 6, 2);
					trailChance = 0f;
					trailInterval = 4f;
					trailParam = 1.6f;
				}};
			}});
		}};

		centurion = new AstraUnitType("centurion") {{
			aiController = RaiderAI::new;

			health = 1200;
			armor = 8f;
			hitSize = 18f;
			fogRadius = 18f;
			itemCapacity = 40;

			speed = 0.4f;
			accel = 0.3f;
			stepSoundVolume = 1f;

			canBoost = true;
			engineColor = AstraPal.missileOrange;
			boostMultiplier = 1.8f;
			setEnginesMirror(new UnitEngine(7f, -9.5f, 4.8f, 300f));

			weapons.add(
				new AstraWeapon("astramod-heavy-rocket-launcher") {{
					reload = 140f;
					recoil = 0.8f;
					alternate = false;
					shoot.shots = 3;
					shoot.shotDelay = 10f;
					accel = 0.3f;

					top = true;
					x = 11.25f;
					y = -0.25f;
					shootY = 5f;
					heatColor = AstraPal.heat;
					cooldownTime = 160f;

					shootSound = Sounds.shootMissileLarge;
					shootSoundVolume = 1f;

					bullet = new MissileBulletType(4f, 10, "astramod-torpedo") {{
						lifetime = 50f;
						width = height = 12f;
						splashDamageRadius = 3f * tilesize;
						splashDamage = 60;
						scaledSplashDamage = true;

						recoil = 0.8f;
						keepVelocity = false;
						scaleLife = true;
						homingPower = 0.8f;
						drag = -0.003f;

						frontColor = AstraPal.missileOrange;
						backColor = trailColor = AstraPal.missileOrangeBack;
						shootEffect = AstraFx.triFlare(this, 1.5f);
						hitEffect = AstraFx.dynamicExplosion(this, 12, 5);
						trailLength = 6;
						trailEffect = Fx.none;
					}};
				}},
				new AstraWeapon("astramod-centurion-repeater") {{
					reload = 160f;
					recoil = 2f;
					inaccuracy = 3f;

					top = false;
					mirror = false;
					x = 10.5f;
					y = 6.75f;
					shootY = 5.5f;
					layerOffset = -0.02f;
					ejectEffect = Fx.casing2;
					shoot = new ShootAlternate(1.5f) {{
						shots = 8;
						shotDelay = 8f;
					}};

					shootSound = Sounds.shootScatter;
					shootSoundVolume = 0.8f;

					bullet = new BasicBulletType(4f, 16) {{
						width = 7f;
						height = 12f;
						lifetime = 20f;
						trailLength = 4;
					}};
				}},
				new Weapon("astramod-centurion-puncher") {{
					reload = 100f;
					shootCone = 45f;
					recoil = -3f;

					top = false;
					mirror = false;
					x = -10.75f;
					y = 8f;
					shootY = 9f;
					layerOffset = -0.02f;
					shootSound = Sounds.none;

					bullet = new MeleeBulletType(50f) {{
						hitSize = 13f;
						rangeOverride = 15f;
						armorMultiplier = 0.5f;
						knockback = 20f;
						hitSoundVolume = 1.8f;
						hitEffect = AstraFx.dynamicBurst(AstraPal.missileOrange, AstraPal.missileOrangeBack, 1.4f);
					}};
				}}
			);
		}};

		// region STEALTH MECHS
		
		baeri = new AstraUnitType("baeri") {{
			aiController = () -> new GroundSpecialistAI(b -> b.block instanceof Radar, 120f * tilesize);
			hovering = true;
			singleTarget = true;
			targetAir = false;

			health = 220;
			armor = 2f;
			hitSize = 10f;
			range = 25f;
			maxRange = 15f;
			fogRadius = 8f;
			rotateSpeed = 4f;
			itemCapacity = 5;

			allowLegStep = true;
			legPhysicsLayer = false;
			speed = 0.9f;
			drag = 0.11f;
			knockbackMultiplier = 0.5f;

			immunities.add(StatusEffects.slow);

			lockLegBase = true;
			legContinuousMove = true;
			legCount = 4;
			legGroupSize = 2;
			legLength = 12f;
			legLengthScl = 1f;
			legForwardScl = 1.1f;
			legMinLength = 0.2f;
			legMaxLength = 1.1f;
			legExtension = -2.3f;
			legBaseOffset = 3f;

			shadowElevation = 0.1f;
			groundLayer = Layer.legUnit - 1f;
			rippleScale = 0.3f;
			stepShake = 0f;
			stepSound = Sounds.walkerStepTiny;
			stepSoundVolume = 0.4f;

			weapons.add(new Weapon("astramod-baeri-puncher") {{
				reload = 15f;
				shootCone = 45f;
				recoil = -3f;

				top = false;
				x = 7f;
				y = -0.25f;
				shootY = 9f;
				shootSound = Sounds.none;

				bullet = new MeleeBulletType(30) {{
					hitSize = 8f;
					rangeOverride = 15f;
					armorMultiplier = 0.5f;
				}};
			}},
			new DeathWeapon(new ExplosionBulletType(50f, 4f * tilesize) {{
				buildingDamageMultiplier = 0.75f;
				shieldDamageMultiplier = 1.5f;
			}}));
			deathExplosionEffect = AstraFx.dynamicExplosion(Pal.lightPyraFlame, Pal.darkPyraFlame, 32f, 5, 4, false);
		}};

		var smokeShell = new ArtilleryBulletType(2f, 0) {{
			lifetime = 40f;
			width = height = 8f;

			backColor = Pal.metalGrayDark;
			frontColor = hitColor = trailColor = AstraPal.smokeScreen;
			trailInterval = 6f;
			trailInterp = Interp.slope;
			shootEffect = Fx.shootSmallSmoke;
			hitEffect = despawnEffect = Fx.hitSquaresColor;

			fragBullets = 1;
			fragBullet = new AOEBulletType(5f * Time.toSeconds) {{
				effect = u -> {
					if (u instanceof Concealc c) c.concealment(20f);
				};
				splashDamageRadius = 5f * tilesize;
				collidesTeam = true;
				shootEffect = AstraFx.smokeScreen(5f);
				trailColor = AstraPal.smokeScreen;
				trailWidth = 2f;
			}};
		}};

		vorhies = new AstraUnitType("vorhies") {{
			aiController = AbilityAI::new;
			hovering = true;
			singleTarget = true;
			targetAir = false;

			health = 600;
			armor = 4f;
			hitSize = 14f;
			fogRadius = 10f;
			itemCapacity = 15;

			speed = 0.8f;
			rotateSpeed = 4f;

			immunities.addAll(StatusEffects.slow, StatusEffects.burning);

			lockLegBase = true;
			legContinuousMove = true;
			legCount = 6;
			legGroupSize = 3;
			legLength = 14f;
			legLengthScl = 0.95f;
			legForwardScl = 0.9f;
			legMinLength = 0.2f;
			legMaxLength = 1.1f;
			legExtension = -3f;
			legMoveSpace = 1f;
			legBaseOffset = 5f;

			shadowElevation = 0.2f;
			groundLayer = Layer.legUnit - 1f;
			stepSound = Sounds.walkerStepSmall;

			abilities.add(new WeaponAbility("astramod-vorhies-smoke-cannon") {{ rangeMultiplier = 1.3f; }});

			weapons.add(new Weapon("astramod-vorhies-flame-fang") {{
				reload = 16f;
				recoil = 3f;

				x = 2.25f;
				y = 5.5f;
				shootX = -0.75f;
				shootY = 3f;
				layerOffset = -0.002f;

				heatColor = AstraPal.heat;
				cooldownTime = 60f;
				rotate = false;

				shootSound = Sounds.shootFlame;

				bullet = new BulletType(4f, 45) {{
					hitSize = 7f;
					lifetime = 15f;
					pierce = pierceBuilding = true;
					pierceCap = 2;
					armorMultiplier = 0.5f;

					status = StatusEffects.burning;
					statusDuration = 5f * Time.toSeconds;

					collidesAir = false;
					keepVelocity = false;
					hittable = false;

					shootEffect = Fx.shootPyraFlame;
					hitEffect = Fx.hitFlameSmall;
					despawnEffect = Fx.none;
				}};
			}},
			new AbilityWeapon("astramod-vorhies-smoke-cannon") {{
				reload = 15f * Time.toSeconds;
				recoil = 3f;
				rotate = true;
				rotateSpeed = 2f;
				rotationLimit = 60f;

				mirror = false;
				x = -7.5f;
				y = -1f;
				shootY = 2.75f;
				cooldownTime = 60f;

				shootSound = Sounds.shootMerui;
				shootSoundVolume = 1.4f;

				bullet = smokeShell;
			}});
		}};

		// region GUNNER TANKS

		aculei = new AstraTankUnitType("aculei") {{
			aiController = GroundTankAI::new;

			health = 700;
			armor = 4f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 12;

			speed = 1f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.95f;
			knockbackMultiplier = 0.6f;

			treadPullOffset = 3;
			treadRects = new Rect[] { new Rect(-21f, -28f, 15f, 56f) };

			tankMoveSound = Sounds.tankMoveSmall;
			tankMoveVolume *= 0.4f;

			weapons.add(new Weapon("astramod-aculei-weapon") {{
				reload = 8f;
				inaccuracy = 12f;
				rotate = true;
				rotateSpeed = 2.5f;
				recoil = 0.8f;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 5.5f;
				layerOffset = 0.0001f;

				bullet = new BasicBulletType(5f, 10) {{
					lifetime = 18f;
					shrinkY = 0f;
				}};
			}});
		}};

		echidna = new AstraTankUnitType("echidna") {{
			aiController = GroundTankAI::new;

			health = 1600;
			armor = 8f;
			hitSize = 21f;
			fogRadius = 12f;
			itemCapacity = 25;

			speed = 0.8f;
			accel = 0.18f;
			rotateSpeed = 2.5f;
			floorMultiplier = 0.8f;
			knockbackMultiplier = 0.5f;
			crushFragile = true;
			crushDamage = 0.4f;

			treadPullOffset = 8;
			treadFrames = 16;
			treadRects = new Rect[] { new Rect(-35f, -44f, 18f, 88f) };

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			weapons.add(new Weapon("astramod-echidna-weapon") {{
				reload = 7f;
				inaccuracy = 12f;
				recoil = 0.8f;
				shoot = new ShootAlternate(5.2f);

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 10f;
				layerOffset = 0.0001f;
				rotate = true;
				rotateSpeed = 2f;
				shootCone = 2f;

				bullet = new RicochetBulletType(6f, 11) {{
					width = 8f;
					height = 10f;
					lifetime = 18f;

					shootSoundVolume = 1.5f;
					smokeEffect = Fx.shootBigSmoke;

					fragOnHit = false;
					fragBullet = new RicochetBulletType(this) {{
						lifetime = 18f;
						trailLength = 8;
					}};
				}};
			}});
		}};

		// region RANGER TANKS

		arbalest = new AstraTankUnitType("arbalest") {{
			aiController = GroundRangerAI::new;
			targetAir = false;

			health = 300;
			armor = 2f;
			hitSize = 13f;
			fogRadius = 12f;
			itemCapacity = 12;

			speed = 0.9f;
			accel = 0.2f;
			rotateSpeed = 1.5f;
			floorMultiplier = 0.95f;

			treadPullOffset = 3;
			treadRects = new Rect[] { new Rect(-25f, -28f, 16f, 56f) };

			tankMoveSound = Sounds.tankMoveSmall;
			tankMoveVolume *= 0.4f;

			weapons.add(new Weapon("astramod-arbalest-weapon") {{
				reload = 120f;
				rotate = true;
				rotateSpeed = 1.5f;
				recoil = 1.5f;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 5.5f;
				layerOffset = 0.0001f;
				shootCone = 0f;

				shootSound = AstraSounds.shootSniper;
				shootSoundVolume = 0.6f;

				bullet = new BasicBulletType(9f, 60, "astramod-high-caliber") {{
					width = 5f;
					height = 20f;
					lifetime = 22f;
					shrinkY = 0f;
					collidesAir = false;
					ejectEffect = Fx.casing3;
					trailLength = 15;
					trailWidth = 1f;

					smokeEffect = Fx.shootSmallSmoke;
					shootEffect = AstraFx.tFlare(this, Pal.missileYellowBack);
					hitEffect = AstraFx.hitSniper;
				}};
			}});
		}};

		bartizan = new AstraTankUnitType("bartizan") {{
			aiController = GroundRangerAI::new;
			targetAir = false;

			health = 800;
			armor = 4f;
			hitSize = 21f;
			fogRadius = 15f;
			itemCapacity = 25;

			speed = 0.7f;
			accel = 0.17f;
			rotateSpeed = 1.4f;
			floorMultiplier = 0.85f;

			treadPullOffset = 8;
			treadFrames = 16;
			treadRects = new Rect[] { new Rect(-36f, -44f, 22f, 88f) };

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			weapons.add(new Weapon("astramod-bartizan-weapon") {{
				reload = 140f;
				rotate = true;
				rotateSpeed = 1.2f;
				recoil = 4f;
				shake = 2f;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 7f;
				layerOffset = 0.0001f;
				ejectEffect = Fx.casing4;
				shootSound = Sounds.shootArtillery;

				bullet = new ArtilleryBulletType(4f, 25, "astramod-fragger-shell") {{
					width = height = 16f;
					lifetime = 55f;
					shoot.firstShotDelay = 10f;

					collides = true;
					collidesTiles = true;
					splashDamageRadius = 3f * tilesize;
					splashDamage = 60f;
					scaledSplashDamage = true;

					fragBullets = 10;
					fragRandomSpread = 100f;
					fragBullet = new BasicBulletType(4f, 8) {{
						hitSize = 2f;
						lifetime = 20f;
						width = 6f;
						height = 8f;
						armorMultiplier = 1.5f;
						shrinkY = 0.8f;

						collidesAir = false;
						pierce = true;
						pierceCap = 3;

						frontColor = Pal.blastAmmoFront;
						backColor = Pal.blastAmmoBack;
						hitEffect = AstraFx.hitColor(this);
						despawnEffect = Fx.none;
					}};

					frontColor = Pal.blastAmmoFront;
					backColor = trailColor = Pal.blastAmmoBack;
					trailLength = 15;
					trailScl = 3f;
					shootEffect = AstraFx.mortarShoot(this, 12, 4, 12f, 18f, true);
					hitEffect = AstraFx.dynamicExplosion(this, 10, 7);
					despawnEffect = AstraFx.hitColor(this);
					shootSoundVolume = 1.5f;
				}};
			}});
		}};

		// region SUPPORT TANKS

		hymeno = new AstraTankUnitType("hymeno") {{
			aiController = GroundCowardAI::new;
			targetPriority = -5f;

			health = 200;
			hitSize = 11f;
			range = 24f * tilesize;
			fogRadius = 30f;
			itemCapacity = 20;

			speed = 2.5f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.85f;
			knockbackMultiplier = 1.2f;

			treadPullOffset = 3;
			treadFrames = 8;
			treadRects = new Rect[] {
				new Rect(-21f, 5f, 14f, 23f),
				new Rect(-18f, -25f, 14f, 23f)
			};

			tankMoveSound = Sounds.tankMoveSmall;
			tankMoveVolume *= 0.3f;

			abilities.add(new ApplyStatusFieldAbility(StatusEffects.fast, 3f * Time.toSeconds, 2.5f * Time.toSeconds, 4f * tilesize) {{
				color = StatusEffects.fast.color;
			}});
		}};

		vitex = new AstraTankUnitType("vitex") {{
			aiController = GroundBacklineAI::new;

			health = 850;
			armor = 4f;
			hitSize = 20f;
			range = 16f * tilesize;
			fogRadius = 18f;
			itemCapacity = 40;

			speed = 1.8f;
			accel = 0.2f;
			rotateSpeed = 2.0f;
			floorMultiplier = 0.75f;

			buildSpeed = 0.2f;
			buildBeamOffset = 10f;

			treadPullOffset = 8;
			treadFrames = 16;
			treadRects = new Rect[] {
				new Rect(-37f, 3f, 26f, 41f),
				new Rect(-37f, -44f, 26f, 41f)
			};

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			abilities.add(new ShieldRegenFieldAbility(15f, 60f, 4f * Time.toSeconds, 6f * tilesize));

			weapons.add(new RepairBeamWeapon("astramod-vitex-repair-turret") {{
				x = 0;
				y = 5.75f;
				shootY = 3f;
				beamWidth = 0.6f;
				repairSpeed = 40f / Time.toSeconds;
				targetBuildings = true;

				bullet = new BulletType() {{
					maxRange = 8f * tilesize;
				}};
			}
			@Override public void flip() {
				y *= -1f;
				shoot = shoot.copy();
				display = false;
			}});
		}};

		// region ENERGY TANKS

		meissa = new AstraTankUnitType("meissa") {{
			aiController = GroundBacklineAI::new;

			health = 500;
			armor = 4f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 12;

			speed = 1.2f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.95f;

			treadPullOffset = 3;
			treadRects = new Rect[] { new Rect(-24f, -28f, 20f, 56f) };

			tankMoveVolume *= 0.4f;
			tankMoveSound = Sounds.tankMoveSmall;

			weapons.add(new Weapon("astramod-meissa-weapon") {{
				reload = 50f;
				rotate = true;
				rotateSpeed = 2.2f;
				recoil = 1f;
				shoot = new ShootHelix() {{ mag = 2f; scl = 2f; }};

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 5.5f;
				layerOffset = 0.0001f;
				heatColor = AstraPal.crystalGlow;
				cooldownTime = 15f;

				shootSound = Sounds.shootMissilePlasma;

				bullet = new EnergyBulletType(3.8f, 25, "circle-bullet") {{
					width = height = 6f;
					lifetime = 30f;
					pierceCap = 2;

					lightColor = AstraPal.crystalGlow;
					lightRadius = 40f;

					frontColor = AstraPal.crystalFront;
					backColor = trailColor = AstraPal.crystalBack;
					shootEffect = AstraFx.shootMeissa;
					despawnEffect = hitEffect = AstraFx.dynamicBurst(this);
					trailWidth = 1.5f;
					trailLength = 8;
				}};
			}});
		}};

		saiph = new AstraTankUnitType("saiph") {{
			health = 1000;
			armor = 6f;
			hitSize = 21f;
			fogRadius = 12f;
			itemCapacity = 25;

			speed = 0.8f;
			accel = 0.18f;
			rotateSpeed = 2.5f;
			floorMultiplier = 0.8f;
			crushFragile = true;
			crushDamage = 0.4f;

			treadPullOffset = 8;
			treadFrames = 16;
			treadRects = new Rect[] { new Rect(-43f, -44f, 34f, 88f) };

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			abilities.add(new ShieldRegenAbility(20f, 250f, 4f * Time.toSeconds));

			weapons.add(new Weapon("astramod-saiph-weapon") {{
				reload = 100f;
				rotate = true;
				rotateSpeed = 2f;
				recoil = 2f;
				shoot.firstShotDelay = 30f;

				mirror = false;
				shootY = -1f;
				x = 0f;
				y = 0f;
				heatColor = AstraPal.crystalGlow;
				cooldownTime = 100f;
				parentizeEffects = true;

				shootSound = Sounds.shootNavanax;
				shootSoundVolume = 0.8f;

				bullet = new EnergyBulletType(4.5f, 40) {{
					lifetime = 35f;
					pierceCap = 3;

					lightning = 2;
					lightningLength = 7;
					lightningDamage = 10;

					lightColor = AstraPal.crystalGlow;
					lightRadius = 50f;
					lightOpacity = 0.7f;

					frontColor = lightningColor = AstraPal.crystalFront;
					backColor = trailColor = AstraPal.crystalBack;
					chargeEffect = AstraFx.saiphCharge;
					shootEffect = AstraFx.saiphShoot;
					hitEffect = AstraFx.dynamicBurst(this);
					despawnEffect = AstraFx.dynamicBurst(this, 2);
					despawnOnRemove = true;
					despawnSound = hitSound = Sounds.explosionDull;

					height = width = 12f;
					trailWidth = 3f;
					trailLength = 12;

					bulletInterval = 4f;
					intervalDelay = 6f;
					intervalBullets = 2;
					intervalBullet = new LightningBulletType() {{
						damage = 2f;
						collides = false;
						lightningLength = 4;
						lightningColor = AstraPal.crystalBack;
						despawnEffect = hitEffect = Fx.none;
					}};
				}};
			}});
		}};

		// region DRAGON

		fledge = new AstraUnitType("fledge") {{
			aiController = () -> new GroundSpecialistAI(b -> b.block instanceof Conveyor, 100f * tilesize);
			hovering = true;
			canDrown = false;

			health = 100f;
			hitSize = 7f;
			fogRadius = 4f;
			itemCapacity = 5;

			omniMovement = false;
			drag = 0.06f;
			speed = 2.3f;
			accel = 0.4f;
			rotateSpeed = 6.5f;
			knockbackMultiplier = 2f;

			engineSize = -1f;
			useEngineElevation = false;
			shadowElevation = 0.1f;

			moveSound = Sounds.loopExtract;
			moveSoundVolume = 0.25f;
			moveSoundPitchMin = 0.7f;
			moveSoundPitchMax = 1.5f;

			parts.add(new HoverPart() {{
				radius = 7f;
				phase = 90f;
				stroke = 2f;
				layerOffset = -0.001f;
				color = Pal.sapBullet;
			}});

			abilities.add(new MoveEffectAbility(0f, -7f, null, Fx.missileTrailShort, 6f) {{ teamColor = true; }});

			weapons.add(new Weapon() {{
				reload = 15f;
				rotate = true;

				top = false;
				x = 1.2f;
				y = 0f;

				shootSound = Sounds.shootSap;

				bullet = new SapBulletType() {{
					lifetime = 35f;
					length = 20f;
					width = 0.5f;
					damage = 20f;
					knockback = -0.5f;
					sapStrength = 0.2f;
					status = StatusEffects.none;

					hitColor = color = Pal.sapBullet;
					shootEffect = AstraFx.energyFlare(Pal.sapBullet, Pal.sapBulletBack, AstraPal.sapBulletDarker);
					despawnEffect = Fx.none;
				}};
			}});
		}};

		newt = new AstraUnitType("newt") {{
			targetFlags = new BlockFlag[] { BlockFlag.drill, BlockFlag.factory, BlockFlag.repair, BlockFlag.battery, null };
			flying = true;

			health = 450;
			armor = 2f;
			hitSize = 12f;
			fogRadius = 6f;

			omniMovement = false;
			speed = 2.3f;
			accel = 0.8f;
			drag = 0.12f;
			knockbackMultiplier = 2f;

			lowAltitude = true;
			engineOffset = 6.75f;
			engineSize = 2.5f;

			weapons.add(new Weapon("astramod-newt-sapper") {{
				reload = 12.4f;
				rotate = true;

				x = 4f;
				y = 0.5f;
				shootY = 2.25f;
				shootX = -0.5f;

				shootSound = Sounds.shootSap;

				bullet = new SapBulletType() {{
					lifetime = 45f;
					length = 60f;
					width = 0.62f;
					damage = 35f;
					knockback = -0.6f;
					sapStrength = 0.35f;
					status = StatusEffects.none;

					hitColor = color = Pal.sapBullet;
					shootEffect = AstraFx.energyFlare(Pal.sapBullet, Pal.sapBulletBack, AstraPal.sapBulletDarker);
					despawnEffect = Fx.none;
				}};
			}},
			new AstraWeapon() {{
				reload = 15f;
				alwaysShooting = true;
				shoot.shots = 3;
				shootCone = 30f;
				minShootVelocity = 2f;

				top = false;
				mirror = false;
				x = 0;
				y = 0;

				shootSound = Sounds.shootArc;

				bullet = new LightningBulletType() {{
					lightningColor = hitColor = Pal.sapBullet;
					damage = 4f;
					createChance = 0.8f;
					lightningLength = 10;
					lightningLengthRand = 7;

					smokeEffect = shootEffect = Fx.none;
				}};
			}});
		}};

		// region GUNSHIP

		milvus = new AstraUnitType("milvus") {{
			targetFlags = new BlockFlag[] { BlockFlag.turret, BlockFlag.core, null };
			forceMultiTarget = true;
			flying = true;

			health = 230;
			armor = 2f;
			hitSize = 11f;
			fogRadius = 9f;
			itemCapacity = 12;

			speed = 0.9f;
			accel = 0.04f;
			drag = 0.016f;
			rotateSpeed = 3f;

			lowAltitude = true;
			engineOffset = 5.25f;
			engineSize = 3f;

			weapons.add(new Weapon("astramod-gunship-mount") {{
				reload = 40f;
				rotate = true;
				rotationLimit = 130f;
				shoot.shots = 2;
				shoot.shotDelay = 5f;

				x = 5.25f;
				y = 0.25f;
				shootX = -0.75f;
				shootY = 2f;

				bullet = new BasicBulletType(4f, 17) {{
					width = 7f;
					height = 12f;
					lifetime = 26f;
				}};
			}});
		}};

		pandion = new AstraUnitType("pandion") {{
			targetFlags = new BlockFlag[] { BlockFlag.turret, BlockFlag.storage, BlockFlag.repair, BlockFlag.core, null };
			flying = true;
			singleTarget = true;

			health = 720;
			armor = 4f;
			hitSize = 15f;
			fogRadius = 10f;
			itemCapacity = 30;

			speed = 0.9f;
			accel = 0.03f;
			drag = 0.020f;
			rotateSpeed = 2.5f;
			strafePenalty = 1f;

			lowAltitude = true;
			engineSize = -1f;

			loopSound = AstraSounds.loopHelicopter;
			loopSoundVolume = 1.4f;

			parts.add(new RotorPart(60f) {{ y = 1.75f; }});

			weapons.add(new Weapon("astramod-gunship-mount") {{
				reload = 60f;
				rotationLimit = 20f;
				shoot.shots = 3;
				shoot.shotDelay = 6f;
				alternate = false;

				x = 7.75f;
				y = 7.25f;
				shootX = -0.75f;
				shootY = 2f;
				top = false;
				layerOffset = -0.1f;

				bullet = new BasicBulletType(4f, 16) {{
					width = 7f;
					height = 12f;
					lifetime = 30f;
				}};
			}},
			new Weapon("astramod-gunship-flak-mount") {{
				reload = 50f;
				rotate = false;
				rotationLimit = 20f;
				alternate = false;
				mirror = false;

				x = 0f;
				y = 8.5f;
				shootY = 2.75f;
				top = false;
				layerOffset = -0.1f;

				shootSound = Sounds.shootScatter;

				bullet = new FlakBulletType(4f, 5) {{
					width = 8f;
					height = 9f;
					lifetime = 20f;
					splashDamage = 30f;
					splashDamageRadius = 2.2f * tilesize;
					collidesGround = true;

					trailLength = 10;
					trailScl = 3f;
					shootEffect = AstraFx.boltTurretShoot(this, false, true);
					hitEffect = AstraFx.dynamicExplosion(this);
				}};
			}});
		}};

		// region EXTRAS

		superBartizan = new AstraTankUnitType("super-bart") {{
			outlineColor = AstraPal.siegeMachineOutline;
			health = 10000000;
			armor = 1000f;
			hitSize = 21f;
			fogRadius = 100f;
			itemCapacity = 1000;
			useUnitCap = false;
			hideDetails = false;

			speed = 5f;
			accel = 1f;
			rotateSpeed = 10f;
			floorMultiplier = 0f;
			canDrown = false;

			treadPullOffset = 8;
			treadFrames = 16;
			treadRects = new Rect[] { new Rect(-36f, -44f, 22f, 88f) };

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			weapons.add(new Weapon("astramod-super-bart-weapon") {{
				reload = 10f;
				rotate = true;
				rotateSpeed = 10f;
				recoil = 1f;
				shake = 1f;
				ejectEffect = Fx.casing4;
				shootSound = Sounds.shootArtillery;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 7f;
				layerOffset = 0.0001f;

				bullet = new ArtilleryBulletType(10f, 10000000, "shell") {{
					width = height = 25f;
					lifetime = 100f;

					collides = true;
					collidesAir = true;
					collidesTiles = true;
					splashDamageRadius = 100f;
					splashDamage = 10000000;

					shootSound = Sounds.shootMissile;
					shootSoundVolume = 1.5f;
					hitEffect = Fx.reactorExplosion;
					shootEffect = Fx.shootBigColor;
					smokeEffect = Fx.shootBigSmoke;
					frontColor = AstraPal.testPinkDark;
					backColor = trailColor = AstraPal.testPink;
					trailLength = 15;
					trailScl = 3f;
				}};
			}});
		}};
	}
}