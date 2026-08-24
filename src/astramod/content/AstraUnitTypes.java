package astramod.content;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import astramod.ai.types.*;
import astramod.entities.bullet.*;
import astramod.gen.PayloadUnit;
import astramod.type.unit.*;
import astramod.type.weapons.*;

import static mindustry.Vars.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType manager, director;
	public static @EntityDef({ Unitc.class, Payloadc.class }) UnitType overseer;
	public static @EntityDef({ Unitc.class, BuildingTetherc.class }) UnitType gatherer, initiate, seeker, ward;
	public static @EntityDef({ Unitc.class, Tankc.class	}) UnitType aculei, echidna;

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
			armor = 2f;
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
			armor = 3f;
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
			armor = 4f;
			hitSize = 17f;
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
			}});
		}};

		// region MODULES

		gatherer = new AstraAnchoredUnitType("gatherer") {{
			aiController = () -> new AnchoredMinerAI();
			flying = true;
			canAttack = false;
			targetPriority = -5f;

			health = 80f;
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
			aiController = () -> new AnchoredSupportAI();
			flying = true;
			targetPriority = -5f;

			health = 150f;
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
			aiController = () -> new AnchoredAttackerAI();
			flying = true;

			health = 200f;
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
			flying = true;
			targetPriority = -4f;

			health = 150f;
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

		// region GUNNER TANKS

		aculei = new AstraTankUnitType("aculei") {{
			health = 600;
			armor = 5f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 10;

			speed = 1.2f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.95f;

			treadPullOffset = 3;
			treadRects = new Rect[] {
				new Rect(-21f, -28f, 15f, 56f)
			};

			tankMoveVolume *= 0.4f;
			tankMoveSound = Sounds.tankMoveSmall;

			weapons.add(new Weapon("astramod-aculei-weapon") {{
				reload = 12f;
				inaccuracy = 6f;
				rotate = true;
				rotateSpeed = 2.5f;
				recoil = 0.8f;

				mirror = false;
				x = 0f;
				y = -0.75f;
				shootY = 5.5f;
				layerOffset = 0.0001f;

				bullet = new BasicBulletType(5f, 12) {{
					lifetime = 30f;
					hitSize = 4f;
					shrinkY = 0f;
				}};
			}});
		}};

		echidna = new AstraTankUnitType("echidna") {{
			health = 1900;
			armor = 8f;
			hitSize = 19f;
			fogRadius = 12f;
			itemCapacity = 20;

			speed = 0.8f;
			accel = 0.18f;
			rotateSpeed = 2.5f;
			floorMultiplier = 0.8f;

			treadPullOffset = 5;
			treadRects = new Rect[] {
				new Rect(-35f, -38f, 18, 79),
				new Rect(-12f, -44f, 24, 88)
			};

			tankMoveVolume *= 0.58f;
			tankMoveSound = Sounds.tankMove;

			weapons.add(new Weapon("astramod-echidna-weapon") {{
				reload = 7.5f;
				inaccuracy = 8f;
				rotate = true;
				rotateSpeed = 2f;
				shootCone = 2f;
				recoil = 0.8f;
				shoot = new ShootAlternate(4f);

				mirror = false;
				x = 0f;
				y = 0f;
				shootY = 10f;
				layerOffset = 0.0001f;

				bullet = new BasicBulletType(5f, 16) {{
					width = 6f;
					height = 8.5f;
					lifetime = 35f;
					hitSize = 5f;
					shrinkY = 0f;
					smokeEffect = Fx.shootBigSmoke;

					fragBullets = 1;
					fragOnHit = false;
					fragBullet = new BasicBulletType(5f, 16) {{
						width = 6f;
						height = 8.5f;
						lifetime = 15f;
						hitSize = 5f;
						shrinkY = 0f;
					}};
				}};
			}});
		}};
	}
}
