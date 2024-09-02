package astramod.classes.blocks.defense;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.util.Time;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.meta.*;
import mindustry.logic.Ranged;
import mindustry.world.blocks.defense.Wall;
import mindustry.entities.Units;
import mindustry.gen.Building;

import static mindustry.Vars.*;

// Emits a damaging aura.
public class AuraWall extends Wall {
	public float auraDamage = 5f;
	public float auraRadius = 20f;
	public StatusEffect auraEffect = StatusEffects.none;
	public Color baseColor;
	public float auraStrength = 0.1f;

	public AuraWall(String name, Color color) {
		super(name);
		baseColor = color;
		update = true;
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(Stat.range, auraRadius);
		stats.add(Stat.damage, auraDamage);
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, auraRadius, baseColor);
	}
	
	public class AuraWallBuild extends WallBuild implements Ranged {
		
		public float range() {
			return auraRadius;
		}

		@Override public void updateTile() {
			Units.nearbyEnemies(team, x, y, auraRadius, unit -> {
				unit.damage(auraDamage * Time.delta / 60f);
			});
		}
		
        @Override public void drawSelect() {
			Drawf.dashCircle(x, y, auraRadius, baseColor);
        }

		@Override public void draw() {
			super.draw();

			Draw.color(baseColor, auraStrength);
			Draw.z(Layer.overlayUI);
			Fill.circle(x, y, auraRadius);
			Lines.circle(x, y, auraRadius);

			Draw.reset();
		}

		@Override public void drawLight() {
			Drawf.light(x, y, auraRadius, baseColor, 0.75f);
		}
	}
}
