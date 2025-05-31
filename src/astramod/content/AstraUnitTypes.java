package astramod.content;

import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.type.*;
import mindustry.type.weapons.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import astramod.ai.*;
import astramod.ai.types.*;
import astramod.gen.*;
import astramod.gen.UnitEntity;
import astramod.type.unit.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType manager, director;
	public static @EntityDef({ Unitc.class, BuildingTetherc.class }) UnitType gatherer, initiate, seeker, ward;

	public static void load() {
		Log.info("Loading units");

		// region CORE

		manager = new AstraUnitType("manager") {{
			constructor = UnitEntity::create;
			aiController = BuilderAI::new;
			flying = true;
			alwaysUnlocked = true;

			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 120f;
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
				reload = 20f;
				x = 5f;
				y = 3.75f;
				top = false;
				layerOffset = -0.1f;
				shootSound = Sounds.lasershoot;

				bullet = new LaserBoltBulletType(5f, 15) {{
					lifetime = 35f;
					keepVelocity = false;
					buildingDamageMultiplier = 0.01f;

					backColor = Pal.bulletYellowBack;
					frontColor = Pal.bulletYellow;
					smokeEffect = AstraFx.coreLaser;
					hitEffect = AstraFx.coreLaser;
					despawnEffect = AstraFx.coreLaser;
				}};
			}});
		}};

		director = new AstraUnitType("director") {{
			constructor = UnitEntity::create;
			aiController = BuilderAI::new;
			flying = true;

			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 160f;
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
				reload = 30f;
				shoot.shots = 3;
				shoot.shotDelay = 5f;
				inaccuracy = 6f;
				x = 5.5f;
				y = 2.5f;
				top = false;
				layerOffset = -0.1f;
				shootSound = Sounds.lasershoot;

				bullet = new LaserBoltBulletType(5.5f, 15) {{
					lifetime = 37.5f;
					keepVelocity = false;
					buildingDamageMultiplier = 0.01f;

					backColor = Pal.bulletYellowBack;
					frontColor = Pal.bulletYellow;
					smokeEffect = AstraFx.coreLaser;
					hitEffect = AstraFx.coreLaser;
					despawnEffect = AstraFx.coreLaser;
				}};
			}});
		}};

		// region MODULES

		gatherer = new AstraUnitType("gatherer") {{
			constructor = BuildingTetherUnit::create;
			aiController = () -> new AnchoredMinerAI();
			flying = true;

			playerControllable = false;
			logicControllable = false;
			isEnemy = false;
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

		initiate = new AstraUnitType("initiate") {{
			constructor = BuildingTetherUnit::create;
			aiController = () -> new AnchoredSupportAI();
			flying = true;

			playerControllable = false;
			logicControllable = false;
			isEnemy = false;	
			targetPriority = -5f;

			health = 150f;
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
				laserColor = Pal.accent;
				healColor = Pal.accent;

				bullet = new BulletType() {{
					maxRange = 30f;
				}};
			}});
		}};

		seeker =  new AstraUnitType("seeker") {{
			constructor = BuildingTetherUnit::create;
			controller = u -> u.team.isAI() && !u.team.rules().rtsAi ? aiController.get() : new CommandAI();
			aiController = () -> new AnchoredProtectorAI();
			commands = new UnitCommand[] { AstraUnitCommand.protect, AstraUnitCommand.combatFollow };
			defaultCommand = AstraUnitCommand.protect;
			flying = true;

			playerControllable = false;
			logicControllable = false;
			isEnemy = false;

			health = 200f;
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
				reload = 30f;
				shoot.shots = 4;
				shoot.shotDelay = 6f;
				inaccuracy = 1f;
				x = 0f;
				y = 3f;
				top = false;
				mirror = false;
				shootSound = Sounds.lasershoot;

				bullet = new LaserBoltBulletType(5f, 15) {{
					lifetime = 25f;
					keepVelocity = false;
					buildingDamageMultiplier = 0.5f;

					backColor = Pal.bulletYellowBack;
					frontColor = Pal.bulletYellow;
					smokeEffect = AstraFx.coreLaser;
					hitEffect = AstraFx.coreLaser;
					despawnEffect = AstraFx.coreLaser;
				}};
			}});
		}};

		ward = new AstraUnitType("warder") {{
			constructor = BuildingTetherUnit::create;
			controller = u -> u.team.isAI() && !u.team.rules().rtsAi ? aiController.get() : new CommandAI();
			aiController = () -> new AnchoredShieldAI();
			commands = new UnitCommand[] { AstraUnitCommand.shieldCore, AstraUnitCommand.shieldFollow };
			defaultCommand = AstraUnitCommand.shieldCore;
			flying = true;

			playerControllable = false;
			logicControllable = false;
			isEnemy = false;

			health = 150f;
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
				region = "astramod-warder-shield";
				radius = 8f;
				angle = 120f;
				width = 5f;
				cooldown = 60f * 10;
				max = 500f;
				whenShooting = false;
				drawArc = false;
			}});
		}};
	}
}