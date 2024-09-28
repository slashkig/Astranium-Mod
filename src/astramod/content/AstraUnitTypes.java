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
	public static @EntityDef({ Unitc.class }) UnitType manager;

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
			fogRadius = 1;
			itemCapacity = 25;
			health = 120f;
			engineOffset = 7f;
			hitSize = 8f;
			alwaysUnlocked = true;

			weapons.add(new Weapon("astramod-manager-weapon") {{
				reload = 20f;
				x = 3f;
				y = 6f;
				top = false;
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
	}
}