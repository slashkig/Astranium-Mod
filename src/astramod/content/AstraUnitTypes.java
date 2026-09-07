package astramod.content;

import arc.graphics.Color;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.ExplosionEffect;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import astramod.ai.types.*;
import astramod.entities.abilities.*;
import astramod.entities.bullet.*;
import astramod.gen.ElevationMoveUnit;
import astramod.gen.MechUnit;
import astramod.gen.PayloadUnit;
import astramod.graphics.*;
import astramod.type.unit.*;
import astramod.type.weapons.*;
import mindustry.world.blocks.distribution.Conveyor.ConveyorBuild;

import static mindustry.Vars.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType
		manager, director;
	public static @EntityDef({ Unitc.class, Payloadc.class }) UnitType
		overseer;
	public static @EntityDef({ Unitc.class, BuildingTetherc.class }) UnitType
		gatherer, initiate, seeker, ward;
	public static @EntityDef({ Unitc.class, Mechc.class }) UnitType
		dicentra, achillion,
		zenaida, trexon, oriolus,
		legion, decanus;
	public static @EntityDef({ Unitc.class, Tankc.class	}) UnitType
		hymeno, vitex,
		aculei, echidna,
		arbalest, bartizan,
		meissa, alnitak,
		superBartizan;
	public static @EntityDef({ Unitc.class, ElevationMovec.class }) UnitType
		fledge;

	// TODO Smokec?

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
					buildingDamageMultiplier = 0.01f;
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
					buildingDamageMultiplier = 0.01f;
					homingPower = 0.03f;
				}};
			}});
		}};

		overseer = new AstraUnitType("overseer", PayloadUnit::create) {{
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
					buildingDamageMultiplier = 0.01f;
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

			health = 500f;
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

		dicentra = new AstraUnitType("dicentra", MechUnit::create) {{
			aiController = GroundRangerAI::new;

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

				bullet = new BasicBulletType(5.5f, 20) {{
					width = 7f;
					height = 12f;
					lifetime = 30f;
					knockback = 2f;
					status = StatusEffects.slow;
					statusDuration = 0.5f * Time.toSeconds;

					hitColor = backColor = AstraPal.ironBack;
					frontColor = AstraPal.ironFront;
				}};
			}});
		}};

		achillion = new AstraUnitType("achillion", MechUnit::create) {{
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
				velocityRnd = 0.2f;

				top = false;
				x = 7.96f;
				y = 0.34f;
				shootY = 6.5f;
				cooldownTime = 30f;
				heatColor = AstraPal.heat;
				ejectEffect = Fx.casing3;

				shootSound = Sounds.shootDiffuse;
				shootSoundVolume = 1.2f;

				bullet = new BasicBulletType(5f, 16) {{
					width = 5f;
					height = 6f;
					lifetime = 28f;
					knockback = 0.4f;

					status = StatusEffects.burning;
					statusDuration = 6f * Time.toSeconds;
					
					hitColor = backColor = AstraPal.fireBulletBack;
					frontColor = AstraPal.fireBulletFront;
					trailColor = AstraPal.fireBulletTrail;
					trailWidth = 1.5f;
					trailLength = 2;
					smokeEffect = Fx.shootBigSmoke;
					shootEffect = Fx.shootBigColor;
				}};
			}});
		}};

		// region SONIC MECHS

		zenaida = new AstraUnitType("zenaida", MechUnit::create) {{
			health = 250;
			armor = 2f;
			hitSize = 10f;
			fogRadius = 8f;
			itemCapacity = 10;

			speed = 0.8f;
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

				bullet = new SonicBulletType(6f, 40) {{
					width = 9f;
					height = 14f;
					lifetime = 15f;
				}};
			}});
		}};

		trexon = new AstraUnitType("trexon", MechUnit::create) {{
			health = 600;
			armor = 5f;
			hitSize = 12f;
			fogRadius = 8f;
			itemCapacity = 20;

			speed = 1.1f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			weapons.add(new AstraWeapon("astramod-trexon-weapon") {{
				reload = 15f;
				recoil = 1.2f;

				top = false;
				x = 9f;
				y = -1.25f;
				shootX = -0.5f;
				shootY = 5f;

				heatColor = AstraPal.sonicHeat;
				shootSound = AstraSounds.shootSonic;

				bullet = new SonicBulletType(6f, 50) {{
					width = 9f;
					height = 14f;
					lifetime = 12f;
				}};
			}});
		}};

		oriolus = new AstraUnitType("oriolus", MechUnit::create) {{
			health = 1100;
			armor = 6f;
			hitSize = 18f;
			fogRadius = 9f;
			itemCapacity = 30;

			speed = 1f;
			accel = 0.3f;
			rotateSpeed = 2.5f;
			stepSoundVolume = 1f;

			abilities.add(new DashAbility(4f, 0.5f * Time.toSeconds, 20f * Time.toSeconds));

			weapons.add(
				new AstraWeapon("astramod-oriolus-weapon") {{
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

					x = 10.25f;
					y = 0.25f;
					shootY = 5f;
					rotate = true;
					rotateSpeed = 2f;
					rotationLimit = 90f;

					heatColor = AstraPal.sonicHeat;
					shootSound = AstraSounds.shootSonic;

					bullet = new SonicBulletType(6f, 20) {{
						width = 9f;
						height = 14f;
						lifetime = 14f;
						shoot.firstShotDelay = 22f;
					}};
				}}
			);
		}};

		// region ROCKET MECHS

		legion = new AstraUnitType("legion", MechUnit::create) {{
			aiController = GroundRangerAI::new;
			health = 220;
			armor = 3f;
			hitSize = 10f;
			fogRadius = 8f;
			itemCapacity = 10;

			speed = 0.5f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;
			canBoost = true;
			engineOffset = 4.5f;
			engineSize = 3.5f;
			boostMultiplier = 1.7f;

			weapons.add(new AstraWeapon("astramod-legion-rocket-mount") {{
				reload = 110f;
				recoil = 3f;
				alternate = false;
				mirror = false;
				rotate = false;
				shoot.firstShotDelay = 20f;

				x = -6.75f;
				y = 0.75f;
				shootY = 5f;
				heatColor = AstraPal.heat;
				cooldownTime = 90f;

				shootSound = Sounds.shootMissile;
				shootSoundVolume = 2f;
				ejectEffect = Fx.casing4;
				shootSound = Sounds.shootMissileShort;
				bullet = new MissileBulletType(3f, 15, "missile"){{
					recoil = 6f;
					keepVelocity = false;
					width = 8f;
					height = 10f;
					shrinkY = 0f;
					drag = -0.003f;
					homingRange = 50f;
					splashDamageRadius = 30f;
					splashDamage = 40f;
					lifetime = 55f;
					frontColor = AstraPal.missileOrange;
					backColor = trailColor = AstraPal.missileOrangeBack;
					hitEffect = despawnEffect = Fx.blastExplosion;
					trailWidth = 2f;
					trailLength = 10;

					weaveMag = 0f;
				}};
			}});
		}};

		decanus = new AstraUnitType("decanus", MechUnit::create) {{
			health = 650;
			armor = 5f;
			hitSize = 12f;
			fogRadius = 8f;
			itemCapacity = 20;

			speed = 0.4f;
			accel = 0.3f;
			stepSoundVolume = 0.4f;

			canBoost = true;
			engineOffset = 6.5f;
			engineSize = 5f;
			boostMultiplier = 1.8f;

			weapons.add(new AstraWeapon("astramod-decanus-weapon") {{
				reload = 130f;
				recoil = 2f;
				shoot.shots = 4;
				shoot.shotDelay = 6f;
				inaccuracy = 6f;

				alternate = false;
				top = false;
				x = 9.25f;
				y = 0.25f;
				shootY = 5.75f;
				heatColor = AstraPal.heat;
				cooldownTime = 120f;

				shootSound = Sounds.shootMissile;
				shootSoundVolume = 2f;
				ejectEffect = Fx.casing4;
				shootSound = Sounds.shootMissileShort;

				bullet = new MissileBulletType(4f, 15, "missile"){{
					recoil = 1f;
					keepVelocity = false;
					width = 8f;
					height = 14f;
					shrinkY = 0f;
					drag = -0.003f;
					homingRange = 30f;
					splashDamageRadius = 25f;
					splashDamage = 30f;
					lifetime = 35f;
					frontColor = AstraPal.missileOrange;
					backColor = trailColor = AstraPal.missileOrangeBack;
					hitEffect = despawnEffect = Fx.blastExplosion;
					trailWidth = 2f;
					trailLength = 10;

					weaveMag = 0f;
				}};
			}});
		}};

		// region GUNNER TANKS

		aculei = new AstraTankUnitType("aculei") {{
			health = 600;
			armor = 4f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 12;

			speed = 1.2f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.95f;

			treadPullOffset = 3;
			treadRects = new Rect[] { new Rect(-21f, -28f, 15f, 56f) };

			tankMoveVolume *= 0.4f;
			tankMoveSound = Sounds.tankMoveSmall;

			weapons.add(new Weapon("astramod-aculei-weapon") {{
				reload = 8f;
				inaccuracy = 6f;
				rotate = true;
				rotateSpeed = 2.5f;
				recoil = 0.8f;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 5.5f;
				layerOffset = 0.0001f;

				bullet = new BasicBulletType(5f, 11) {{
					lifetime = 25f;
					shrinkY = 0f;
				}};
			}});
		}};

		echidna = new AstraTankUnitType("echidna") {{
			health = 1700;
			armor = 7f;
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
			treadRects = new Rect[] { new Rect(-35f, -44f, 18f, 88f) };

			tankMoveSound = Sounds.tankMove;
			tankMoveVolume *= 0.58f;

			weapons.add(new Weapon("astramod-echidna-weapon") {{
				reload = 7f;
				inaccuracy = 8f;
				rotate = true;
				rotateSpeed = 2f;
				shootCone = 2f;
				recoil = 0.8f;
				shoot = new ShootAlternate(5.2f);

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 10f;
				layerOffset = 0.0001f;

				bullet = new RicochetBulletType(6f, 18) {{
					width = 8f;
					height = 10f;
					lifetime = 25f;

					shootSoundVolume = 1.5f;
					smokeEffect = Fx.shootBigSmoke;

					fragOnHit = false;
					fragBullet = new RicochetBulletType(this) {{
						lifetime = 18f;
						frontColor = AstraPal.deflectFront;
						backColor = AstraPal.deflectBack;
					}};
				}};
			}});
		}};

		// region RANGER TANKS

		arbalest = new AstraTankUnitType("arbalest") {{
			aiController = GroundRangerAI::new;
			targetAir = false;

			health = 320;
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

				shootSound = AstraSounds.shootSniperSmall; // sound needs some work

				bullet = new BasicBulletType(9f, 60) {{
					width = 4f;
					height = 10f;
					lifetime = 22f;
					shrinkY = 0f;
					collidesAir = false;
					ejectEffect = Fx.casing3;

					smokeEffect = Fx.shootSmallSmoke;
					shootEffect = Fx.shootSmallColor;
				}};
			}});
		}};

		bartizan = new AstraTankUnitType("bartizan") {{
			aiController = GroundRangerAI::new;
			targetAir = false;

			health = 1000;
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

				bullet = new ArtilleryBulletType(4f, 25, "shell") {{
					width = height = 15f;
					lifetime = 55f;
					shoot.firstShotDelay = 10f;

					splashDamageRadius = 2f * tilesize;
					splashDamage = 40f;
					collides = true;
					collidesTiles = true;

					frontColor = Pal.blastAmmoFront;
					backColor = trailColor = Pal.blastAmmoBack;
					trailLength = 15;
					trailScl = 3f;
					hitEffect = Fx.blastExplosion;
					shootEffect = Fx.shootBigColor;
					smokeEffect = Fx.shootBigSmoke;
					shootSoundVolume = 1.5f;

					fragBullets = 10;
					fragRandomSpread = 100f;
					fragBullet = new BasicBulletType(4f, 6) {{
						lifetime = 20f;
						width = 6f;
						height = 8f;
						armorMultiplier = 1.5f;
						shrinkY = 0.8f;
						hitSize = 2f;

						collidesAir = false;
						pierce = true;
						pierceCap = 2;

						frontColor = Pal.blastAmmoFront;
						backColor = Pal.blastAmmoBack;
						despawnEffect = Fx.hitBulletColor;
					}};
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

			treadPullOffset = 3;
			treadFrames = 8;
			treadRects = new Rect[] {
				new Rect(-21f, 5f, 14, 23),
				new Rect(-18f, -25f, 14, 23)
			};

			tankMoveSound = Sounds.tankMoveSmall;
			tankMoveVolume *= 0.3f;

			abilities.add(new ApplyStatusFieldAbility(StatusEffects.fast, 3f * Time.toSeconds, 2.5f * Time.toSeconds, 4f * tilesize) {{
				color = StatusEffects.fast.color;
			}});
		}};

		vitex = new AstraTankUnitType("vitex") {{
			aiController = GroundRangerAI::new;

			health = 1000;
			armor = 5f;
			hitSize = 20f;
			range = 16f * tilesize;
			fogRadius = 18f;
			itemCapacity = 20;

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
					beamWidth = 0.4f;
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
				}}
			);
		}};

		// region ENERGY TANKS

		meissa = new AstraTankUnitType("meissa") {{
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
				inaccuracy = 0f;
				rotate = true;
				rotateSpeed = 2.5f;
				recoil = 1f;

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 5.5f;
				layerOffset = 0.0001f;
				heatColor = AstraPal.crystalGlow;
				cooldownTime = 15f;

				shootSound = Sounds.shootMissilePlasma;

				bullet = new MissileBulletType(3.9f, 20){{
					shoot = new ShootHelix(){{
						mag = 1f;
						scl = 5f;
					}};
					lifetime = 30f;
					keepVelocity = false;
					shootEffect = AstraFx.shootCrystal;
					smokeEffect = AstraFx.hitCrystal;
					splashDamage = 10f;
					splashDamageRadius = 10f;

					lightColor = AstraPal.crystalGlow;
					lightRadius = 40f;
					lightOpacity = 0.7f;

					frontColor = AstraPal.crystalFront;
					backColor = trailColor = AstraPal.crystalBack;
					hitSound = despawnSound = Sounds.explosion;
					trailWidth = 2f;
					trailLength = 10;

					despawnEffect = Fx.none;
					hitEffect = new ExplosionEffect(){{
						lifetime = 10f;
						waveStroke = 2f;
						waveColor = AstraPal.crystalBack;
						sparkColor = AstraPal.crystalFront;
						waveRad = 12f;
						smokeSize = 0f;
						smokeSizeBase = 0f;
						sparks = 9;
						sparkRad = 35f;
						sparkLen = 4f;
						sparkStroke = 1.5f;
					}};
				}};
			}});
		}};

		alnitak = new AstraTankUnitType("alnitak") {{
			health = 1500;
			armor = 11f;
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

			weapons.add(new Weapon("astramod-alnitak-weapon") {{
				shoot.firstShotDelay = 40f;
				shoot.shots = 2;
				shoot.shotDelay = 16f;

				mirror = false;
				shootY = -1.75f;
				x = 0f;
				y = 0f;
				rotateSpeed = 2f;
				reload = 120f;
				recoil = 4f;
				shootSound = Sounds.shootLancer;
				shadow = 20f;
				rotate = true;

				heatColor = AstraPal.crystalGlow;
				cooldownTime = 100f;

				shootStatusDuration = Time.toSeconds;
				shootStatus = StatusEffects.unmoving;

				bullet = new LaserBulletType(){{
					damage = 70f;
					recoil = 0f;
					sideAngle = 315f;
					sideWidth = 1f;
					sideLength = 60f;
					width = 25f;
					length = 150f;

					status = AstraStatusEffects.overcharged;
					statusDuration = Time.toSeconds;

					colors = new Color[]{AstraPal.crystalLazerBack.cpy().a(0.4f), AstraPal.crystalRed, AstraPal.crystalLazerLight};
					chargeEffect = new MultiEffect(AstraFx.alnitakLaserCharge, AstraFx.alnitakLaserChargeBegin);
					shootEffect = AstraFx.crystalShockwave;
				}};
			}});
		}};

		// region DRAGON

		fledge = new AstraUnitType("fledge", ElevationMoveUnit::create) {{
			aiController = () -> new GroundSpecialistAI(b -> b instanceof ConveyorBuild, 100f * tilesize);
			hovering = true;
			canDrown = false;

			health = 100f;
			hitSize = 7f;
			fogRadius = 4f;
			itemCapacity = 5;

			drag = 0.06f;
			speed = 2.3f;
			accel = 0.4f;
			rotateSpeed = 6.5f;

			engineSize = 0f;
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

			weapons.add(new Weapon("fledge-weapon") {{
				reload = 15f;
				rotate = true;

				top = false;
				mirror = true;
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
					shootEffect = Fx.shootSmall;
					despawnEffect = Fx.none;
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

			speed = 5f;
			accel = 1f;
			rotateSpeed = 10f;
			floorMultiplier = 0f;

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