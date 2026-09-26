package astramod.world.draw;

import arc.graphics.g2d.*;
import mindustry.entities.part.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild;
import mindustry.world.draw.*;
import astramod.world.blocks.defense.turrets.AstraItemTurret.AstraItemTurretBuild;

public class DrawBarrelTurret extends DrawTurret {
	public DrawBarrelTurret(String basePrefix) {
		super(basePrefix);
    }

	@Override public void draw(Building build) {
		Turret turret = (Turret)build.block;
		TurretBuild tb = (TurretBuild)build;

		Draw.rect(base, build.x, build.y);
		Draw.color();

		Draw.z(shadowLayer);

		Drawf.shadow(preview, build.x + tb.recoilOffset.x - turret.elevation, build.y + tb.recoilOffset.y - turret.elevation, tb.drawrot());

		Draw.z(turretLayer);

		drawTurret(turret, tb);
		drawHeat(turret, tb);

		if (parts.size > 0) {
			if (outline.found()) {
				Draw.z(turretLayer - 0.01f);
				Draw.rect(outline, build.x + tb.recoilOffset.x, build.y + tb.recoilOffset.y, tb.drawrot());
				Draw.z(turretLayer);
			}

			float progress = tb.progress();

			var params = DrawPart.params.set(build.warmup(), 1f - progress, 1f - progress, tb.heat, tb.curRecoil, tb.charge, tb.x + tb.recoilOffset.x, tb.y + tb.recoilOffset.y, tb.rotation);

			for (var part : parts) {
				params.setRecoil(part.recoilIndex >= 0 && tb.curRecoils != null ? tb.curRecoils[part.recoilIndex] : tb.curRecoil);
				if (part.weaponIndex >= 0 && tb instanceof AstraItemTurretBuild atb && atb.curHeats != null) params.heat = atb.curHeats[part.weaponIndex];
				part.draw(params);
			}
		}
	}
}