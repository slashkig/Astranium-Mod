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
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import astramod.ai.types.*;
import astramod.entities.bullet.*;
import astramod.gen.MechUnit;
import astramod.gen.PayloadUnit;
import astramod.gen.ElevationMoveUnit;
import astramod.graphics.*;
import astramod.type.unit.*;
import astramod.type.weapons.*;
import mindustry.world.meta.BlockFlag;

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
		zenaida, oriolus;
	public static @EntityDef({ Unitc.class, Tankc.class	}) UnitType
		hymeno,
		aculei, echidna;
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

		// region SCOUTS

		hymeno = new AstraTankUnitType("hymeno") {{
			aiController = GroundCowardAI::new;

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
				new Rect(-18f, -25f, 14, 23),
				new Rect(-21f, 5f, 14, 23)
			};

			tankMoveSound = Sounds.tankMoveSmall;
			tankMoveVolume *= 0.3f;
		}};

		// region OFFENSIVE MECHS

		dicentra = new AstraUnitType("dicentra", MechUnit::create) {{
			aiController = GroundRangerAI::new;

			health = 250;
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
					knockback = 4f;
					status = StatusEffects.slow;
					statusDuration = 4f * Time.toSeconds;

					hitColor = backColor = AstraPal.ironBack;
					frontColor = AstraPal.ironFront;
				}};
			}});
		}};

		achillion = new AstraUnitType("achillion", MechUnit::create) {{
			health = 600;
			armor = 3f;
			hitSize = 13f;
			fogRadius = 12f;
			itemCapacity = 25;

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
					knockback = 0.5f;

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
			health = 300;
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

				bullet = new SonicBulletType(6f, 36) {{
					width = 9f;
					height = 14f;
					lifetime = 15f;
				}};
			}});
		}};

		oriolus = new AstraUnitType("oriolus", MechUnit::create) {{
			health = 700;
			armor = 4f;
			hitSize = 17f;
			fogRadius = 9f;
			itemCapacity = 25;

			speed = 1f;
			accel = 0.3f;
			rotateSpeed = 2.5f;
			stepSoundVolume = 0.8f;

			weapons.add(new AstraWeapon("astramod-oriolus-weapon") {{
				reload = 80f;
				recoil = 3f;
				shoot.shots = 3;
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

				bullet = new SonicBulletType(6f, 60) {{
					sprite = "astramod-sonic-shot-large";

					width = 11f;
					height = 16f;
					lifetime = 18f;

					shootEffect = AstraFx.sonicHit;
				}};
			}});
		}};

		// region GUNNER TANKS

		aculei = new AstraTankUnitType("aculei") {{
			health = 600;
			armor = 4f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 10;

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
				y = -0.75f;
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
			itemCapacity = 20;

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

		// region DRAGON

		fledge = new AstraUnitType("fledge", ElevationMoveUnit::create) {{
			hovering = true;
			canDrown = false;
			shadowElevation = 0.1f;

			// TODO: fix unit not targeting drills and conveyors
			targetFlags = new BlockFlag[]{BlockFlag.drill, null};

			drag = 0.06f;
			speed = 2.3f;
			rotateSpeed = 6.5f;

			accel = 0.4f;
			health = 500f;
			armor = 2f;
			hitSize = 7f;
			engineOffset = 6.5f;
			engineSize = 2f;
			itemCapacity = 0;
			useEngineElevation = false;
			researchCostMultiplier = 0;
			moveSound = Sounds.loopExtract;
			moveSoundVolume = 0.25f;
			moveSoundPitchMin = 0.7f;
			moveSoundPitchMax = 1.5f;

			abilities.add(new MoveEffectAbility(0f, -7f, Pal.sapBulletBack, Fx.missileTrailShort, 4f){{
				teamColor = true;
			}});

			parts.add(new HoverPart(){{
				radius = 7f;
				phase = 90f;
				stroke = 2f;
				layerOffset = -0.001f;
				color = Color.valueOf("bf92f9");
			}});

			weapons.add(new Weapon("fledge-weapon"){{
				shootSound = Sounds.shootSap;
				top = false;
				mirror = true;
				reload = 12f;
				rotate = true;

				x = 1.2f;
				y = 0f;

				bullet = new SapBulletType(){{
					sapStrength = 1f;
					length = 20f;
					damage = 30f;
					shootEffect = Fx.shootSmall;
					hitColor = color = Color.valueOf("bf92f9");
					despawnEffect = Fx.none;
					width = 0.54f;
					lifetime = 35f;
					knockback = -1.24f;
				}};
			}});
		}};
	}
}