package astramod.world.blocks.modules;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import astramod.world.draw.DrawTeamTurret;
import mindustry.entities.units.BuildPlan;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

import static mindustry.Vars.*;

public class TurretCoreModule extends ItemTurret {
	private Item[] ammoPriority;

	public TurretCoreModule(String name) {
		super(name);
		maxAmmo = 1;
		playerControllable = false;
	}

	@Override public void init() {
		super.init();

		ammoPriority = new Item[ammoTypes.size];
		int i = ammoTypes.size - 1;
		for (Item ammo : ammoTypes.keys()) {
			ammoPriority[i] = ammo;
			i--;
		}
	}

	@Override public void setBars() {
		super.setBars();
		removeBar("ammo");
		addBar("ammo", (TurretCoreModuleBuild entity) ->
			new Bar(
				() -> entity.hasAmmo() ? entity.currentAmmo.localizedName : Core.bundle.format("stat.ammo"),
				() -> entity.hasAmmo() ? entity.currentAmmo.color : Pal.ammo,
				() -> entity.hasAmmo() ? 1 : 0
			)
		);
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (Point2 edge : Edges.getEdges(size)) {
			if (world.build(tile.x + edge.x, tile.y + edge.y) instanceof CoreBuild) return true;
		}
		return false;
	}

	@Override public void drawDefaultPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		if (plan.worldContext && player != null && teamRegion != null && teamRegion.found() && drawer instanceof DrawTeamTurret drawTurret) {
			Draw.rect(drawTurret.base, plan.drawx(), plan.drawy());
			Draw.color(player.team().color);
			Draw.rect(teamRegion, plan.drawx(), plan.drawy());
			Draw.color();
			Draw.rect(drawTurret.preview, plan.drawx(), plan.drawy());
			Draw.color(player.team().color);
			Draw.rect(drawTurret.turretTeam, plan.drawx(), plan.drawy());
			Draw.color();
		} else {
			Draw.rect(getPlanRegion(plan, list), plan.drawx(), plan.drawy(), !rotate || !rotateDraw ? 0 : plan.rotation * 90);
		}

		drawPlanConfig(plan, list);
	}

	public class TurretCoreModuleBuild extends ItemTurretBuild implements CoreModuleBlock {
		protected @Nullable Building linkedCore;

		protected Item currentAmmo;

		@Override public void drawSelect() {
			if (linkedCore != null) {
				linkedCore.drawSelect();
			}
			super.drawSelect();
		}

		@Override public boolean acceptItem(Building source, Item item) {
			return linkedCore != null && linkedCore.acceptItem(source, item);
		}

		@Override public int acceptStack(Item item, int amount, Teamc source) {
			return linkedCore == null ? 0 : linkedCore.acceptStack(item, amount, source);
		}

		@Override public void handleItem(Building source, Item item) {
			linkedCore.handleItem(source, item);
		}

		@Override public void handleStack(Item item, int amount, Teamc source) {
			linkedCore.handleStack(item, amount, source);
		}

		@Override public int removeStack(Item item, int amount) {
			return linkedCore == null ? 0 : linkedCore.removeStack(item, amount);
		}

		@Override public void setLinkedCore(Building core) {
			linkedCore = core;
		}

		@Override @Nullable public Building getLinkedCore() {
			return linkedCore;
		}

		@Override protected void updateReload() {
			super.updateReload();
			if (!hasAmmo() && linkedCore != null) {
				for (Item ammo : ammoPriority) {
					if (ammoTypes.get(ammo) != null && linkedCore.items.has(ammo)) {
						linkedCore.items.remove(ammo, 1);
						super.handleItem(linkedCore, ammo);
						currentAmmo = ammo;
						return;
					}
				}
			}
		}

		public Item getCurrentAmmo() {
			return currentAmmo;
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			currentAmmo = ammo.peek() instanceof ItemEntry e ? e.item : null;
		}
	}
}