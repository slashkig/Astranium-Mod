package astramod.world.draw;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild;
import mindustry.world.draw.*;

public class DrawTeamTurret extends DrawTurret {
	public TextureRegion turretTeam;

	public DrawTeamTurret(String basePrefix) {
		super(basePrefix);
	}

	public DrawTeamTurret() { }

	@Override public void load(Block block) {
		super.load(block);

		turretTeam = Core.atlas.find(block.name + "-turret-team");
		block.outlinedIcon = 2;
	}

	@Override public TextureRegion[] icons(Block block) {
		return top.found() ? new TextureRegion[] { base, block.teamRegions[Team.sharded.id], preview, top } : new TextureRegion[] { base, block.teamRegions[Team.sharded.id], preview };
	}

	@Override public void draw(Building build) {
		if (build.block.teamRegion.found()) {
			Draw.z(Layer.block + 0.1f);
			Draw.color(build.team().color);
			Draw.rect(build.block.teamRegion, build.x, build.y);
			Draw.color();
		}

		Draw.z(Layer.block);
		super.draw(build);
	}

	@Override public void drawTurret(Turret block, TurretBuild build) {
		super.drawTurret(block, build);

		if (turretTeam.found()) {
			Draw.color(build.team().color);
			Draw.rect(turretTeam, build.x + build.recoilOffset.x, build.y + build.recoilOffset.y, build.drawrot());
			Draw.color();
		}
	}
}
