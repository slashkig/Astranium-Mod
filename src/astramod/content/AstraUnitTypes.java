package astramod.content;

import arc.graphics.*;
import arc.struct.*;
import arc.util.*;
import ent.anno.Annotations.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import astramod.graphics.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType manager, director;
	public static @EntityDef({ Unitc.class, BuildingTetherc.class }) UnitType gatherer;

	public static void load() {
		Log.info("Loading units");

		// region CORE

		manager = new UnitType("manager") {{
			constructor = UnitEntity::create;
			aiController = BuilderAI::new;
			flying = true;
			alwaysUnlocked = true;

			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 120f;
			hitSize = 10f;
			fogRadius = 0;
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
			outlineColor = Pal.darkOutline;

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

		director = new UnitType("director") {{
			constructor = UnitEntity::create;
			aiController = BuilderAI::new;
			flying = true;

			isEnemy = false;
			coreUnitDock = true;
			targetPriority = -2.5f;

			health = 150f;
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
			outlineColor = Pal.darkOutline;

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

		gatherer = new UnitType("gatherer") {{
			constructor = UnitEntity::create;
			controller = u -> new MinerAI();
			flying = true;

			defaultCommand = UnitCommand.mineCommand;
			playerControllable = false;
			logicControllable = false;
			isEnemy = false;
			targetPriority = -5f;

			health = 200f;
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
			mineItems = Seq.with(AstraItems.hematite, Items.copper, Items.lead, Items.graphite);

			engineOffset = 5.5f;
			engineSize = 2f;
		}};
	}
}