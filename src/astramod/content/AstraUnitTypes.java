package astramod.content;

import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import astramod.ai.types.*;
import astramod.entities.bullet.*;
import astramod.gen.PayloadUnit;
import astramod.graphics.*;
import astramod.type.unit.*;
import astramod.type.weapons.*;

import static mindustry.Vars.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType manager, director;
	public static @EntityDef({ Unitc.class, Payloadc.class }) UnitType overseer;
	public static @EntityDef({ Unitc.class, BuildingTetherc.class }) UnitType gatherer, initiate, seeker, ward;
	public static @EntityDef({ Unitc.class, Tankc.class	}) UnitType aculei;

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
				x = 5f;
				y = 3.75f;
				top = false;
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

		overseer = new AstraUnitType("overseer") {{
			constructor = PayloadUnit::create;
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
				x = 7.9f;
				y = 1.6f;
				top = false;
				layerOffset = -0.1f;
				shootSound = Sounds.shootLaser;
				shootY = 4.5f;

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
			targetPriority = -5f;

			health = 80f;
			hitSize = 6f;
			fogRadius = 6f;
			canAttack = false;
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
			itemCapacity = 10;

			drag = 0.06f;
			accel = 0.12f;
			speed = 2.8f;

			buildSpeed = 0.6f;
			buildRange = 100f;

			lowAltitude = true;
			engineOffset = 5.5f;
			engineSize = 2f;

			weapons.add(new RepairBeamWeapon() {{
				widthSinMag = 0.1f;
				reload = 20f;
				x = 0f;
				y = 4f;
				rotate = false;
				shootY = 0f;
				beamWidth = 0.5f;
				repairSpeed = 2.5f;
				aimDst = 0f;
				shootCone = 4f;
				mirror = false;

				targetUnits = false;
				targetBuildings = true;
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
			itemCapacity = 10;

			drag = 0.05f;
			accel = 0.1f;
			speed = 2.2f;

			lowAltitude = true;
			engineOffset = 5.6f;
			engineSize = 2.2f;

			weapons.add(new Weapon("astramod-seeker-weapon") {{
				reload = 60f;
				shoot.shots = 4;
				shoot.shotDelay = 6f;
				inaccuracy = 1f;
				x = 0f;
				y = 3f;
				top = false;
				mirror = false;
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
			fogRadius = 6f;
			itemCapacity = 12;
			range = 40f;

			drag = 0.05f;
			accel = 0.15f;
			speed = 2.5f;

			lowAltitude = true;
			engineOffset = 5.6f;
			engineSize = 2.2f;

			abilities.add(new ShieldArcAbility() {{
				radius = 10f;
				angle = 120f;
				width = 5f;
				cooldown = 10f * Time.toSeconds;
				max = 500f;
				regen = 0.15f;
				whenShooting = false;
			}});
		}};

		// region GUNNER TANKS

		aculei = new AstraTankUnitType("aculei") {{
			health = 500;
			armor = 4f;
			hitSize = 12f;
			fogRadius = 10f;
			itemCapacity = 10;

			speed = 0.6f;
			accel = 0.2f;
			rotateSpeed = 3f;
			floorMultiplier = 0.95f;

			treadRects = new Rect[] { new Rect(-21f, -28f, 42f, 56f) };
			treadPullOffset = 3;

			tankMoveVolume *= 0.4f;
			tankMoveSound = Sounds.tankMoveSmall;

			weapons.add(new Weapon("astramod-aculei-weapon") {{
				reload = 15f;
				inaccuracy = 5.5f;
				shootY = 5f;
				recoil = 1f;
				x = 0f;
				y = -0.75f;
				mirror = false;
				rotate = true;
				rotateSpeed = 2.2f;

				layerOffset = 0.0001f;
				heatColor = AstraPal.machineGunHeat;
				cooldownTime = 100f;

				bullet = new BasicBulletType(5f, 12) {{
					smokeEffect = Fx.shootBigSmoke;
					lifetime = 30f;
					hitSize = 4f;
					despawnEffect = hitEffect = Fx.hitBulletColor;
				}};
			}});
		}};
	}
}
