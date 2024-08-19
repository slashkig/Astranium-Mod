import arc.graphics.*;
import mindustry.content.*;
import mindustry.graphics.*;
import Mindustry.world.meta.*;
import mindustry.gen.Building;

public class AuraWall extends Wall {
	public float auraDamage = 10f;
	public float auraRadius = 10f;
	public float auraEffect = StatusEffect.none;
	public Color baseColor;

	public AuraWall(String name, Color color) {
		super(name);
		baseColor = color;
		update = true;
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(Stat.range, auraRadius, Stat.damage, auraDamage);
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Drawf.dashCircle(x * tilesize + offset, y * tilesize + offset, range, baseColor);
	}
	
	public class AuraWallBuild extends WallBuild implements Ranged {
		
		@Override public float range() {
			return auraRadius;
		}

		@Override public void updateTile() {
			Units.nearbyEnemies(team, x, y, auraRadius, unit -> {
				unit.damage(auraDamage);
			});
		}
		
        @Override public void drawSelect() {
			Drawf.dashCircle(x, y, auraRadius, baseColor);
        }

		@Override public void draw() {
			super.draw();

			Draw.color(baseColor);
			Lines.circle(x, y, auraRadius);
			draw.reset();
		}

		@Override public void drawLight() {
			Drawf.light(x, y, auraRadius, baseColor, 0.75f);
		}
	}
}
