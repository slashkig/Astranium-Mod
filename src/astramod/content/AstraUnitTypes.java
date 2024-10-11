package astramod.content;

import arc.graphics.*;
import arc.util.Log;
import ent.anno.Annotations.*;
import mindustry.type.*;
import mindustry.ai.types.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import astramod.graphics.*;

public class AstraUnitTypes {
	public static @EntityDef({ Unitc.class }) UnitType manager, director;

	public static void load() {
		Log.info("Loading units");

		manager = new UnitType("manager") {{
			constructor = UnitEntity::create;
			aiController = BuilderAI::new;
			isEnemy = false;
			coreUnitDock = true;

			lowAltitude = true;
			flying = true;
			mineSpeed = 5f;
			mineTier = 1;
			mineWalls = true;
			buildSpeed = 0.6f;
			drag = 0.06f;
			speed = 4f;
			rotateSpeed = 15f;
			accel = 0.1f;
			fogRadius = 0;
			itemCapacity = 25;
			health = 120f;
			engineOffset = 7f;
			hitSize = 8f;
			alwaysUnlocked = true;
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
			isEnemy = false;
			coreUnitDock = true;

			lowAltitude = true;
			flying = true;
			mineSpeed = 6f;
			mineTier = 1;
			mineWalls = true;
			buildSpeed = 0.8f;
			drag = 0.07f;
			speed = 4.5f;
			rotateSpeed = 17.5f;
			accel = 0.1f;
			fogRadius = 0;
			itemCapacity = 40;
			health = 150f;
			engineOffset = 10f;
			engineSize = 3.5f;
			hitSize = 9f;
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
	}
}