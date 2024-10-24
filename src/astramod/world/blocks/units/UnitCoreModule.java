package astramod.world.blocks.units;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.meta.*;
import astramod.world.blocks.storage.*;

import static mindustry.Vars.*;

import java.util.Arrays;

public class UnitCoreModule extends CoreModule {
	public UnitType spawnedUnit;
	public int numUnits = 1;
	public float buildTime = 8f * 60f;

	public float polyStroke = 1.8f, polyRadius = 8f;
	public int polySides = 6;
	public float polyRotateSpeed = 1f;
	public Color polyColor = Pal.accent;

	public UnitCoreModule(String name, UnitType unit) {
		super(name);
		update = true;
		spawnedUnit = unit;
		ambientSound = Sounds.respawning;
	}

	@Override public void setStats() {
		super.setStats();

		stats.remove(Stat.buildTime);
		stats.add(Stat.unitType, table -> {
			table.row();
			table.table(Styles.grayPanel, b -> {
				b.image(spawnedUnit.uiIcon).size(40).pad(10f).left().scaling(Scaling.fit);
				b.table(info -> {
					info.add(spawnedUnit.localizedName).left();
					if (Core.settings.getBool("console")) {
						info.row();
						info.add(spawnedUnit.name).left().color(Color.lightGray);
					}
				});
				b.button("?", Styles.flatBordert, () -> ui.content.show(spawnedUnit)).size(40f).pad(10).right().grow().visible(() -> spawnedUnit.unlockedNow());
			}).growX().pad(5).row();
		});
	}

	@Override public void setBars() {
		super.setBars();

		addBar("units", (UnitCoreModuleBuild e) -> {
			int count = e.unitCount();
			return new Bar(
				() -> Core.bundle.format("bar.unitcap", Fonts.getUnicodeStr(spawnedUnit.name), count, numUnits),
				() -> Pal.power,
				() -> (float)count / numUnits
			);
		});
	}

	public class UnitCoreModuleBuild extends StorageBuild implements UnitTetherBlock {
		public float buildProgress, totalProgress;
		public float warmup, readyness;
		public Unit[] units = new Unit[numUnits];
		public int[] readUnitId = new int[numUnits];
		protected int targetIndex;

		@Override public Building create(Block block, Team team) {
			Arrays.fill(units, null);
			Arrays.fill(readUnitId, -1);
			return super.create(block, team);
		}

		@Override public void updateTile() {
			for (int i = 0; i < numUnits; i++) {
				// Unit was lost/destroyed
				if (units[i] != null && (units[i].dead || !units[i].isAdded())) {
					units[i] = null;
				}

				if (readUnitId[i] != -1) {
					units[i] = Groups.unit.getByID(readUnitId[i]);
					if (units[i] != null || !net.client()) {
						readUnitId[i] = -1;
					}
				}

				if (units[i] == null && targetIndex == -1) {
					targetIndex = i;
				}
			}

			warmup = Mathf.approachDelta(warmup, efficiency, 1f / 60f);
			readyness = Mathf.approachDelta(readyness, targetIndex != -1 ? 1f : 0f, 1f / 60f);

			if (targetIndex != -1) {
				buildProgress += edelta() / buildTime;
				totalProgress += edelta();

				if (buildProgress >= 1f) {
					if (!net.client()) {
						Unit unit = spawnedUnit.create(team);
						units[targetIndex] = unit;
						if(unit instanceof BuildingTetherc bt) {
							bt.building(this);
						}
						unit.set(x, y);
						unit.rotation = 90f;
						unit.add();
						Call.unitTetherBlockSpawned(tile, unit.id);
					}
				}
			}
		}

		public void spawned(int id) {
			Fx.spawn.at(x, y);
			buildProgress = 0f;
			if (net.client()) {
				readUnitId[targetIndex] = id;
			}
			targetIndex = -1;
		}

		@Override public boolean shouldActiveSound() {
			return shouldConsume() && warmup > 0.01f;
		}

		@Override public void draw() {
			Draw.rect(block.region, x, y);
			if (targetIndex != -1) {
				Draw.draw(Layer.blockOver, () -> {
					Drawf.construct(this, spawnedUnit.fullIcon, 0f, buildProgress, warmup, totalProgress);
				});
			} else {
				Draw.z(Layer.bullet - 0.01f);
				Draw.color(polyColor);
				Lines.stroke(polyStroke * readyness);
				Lines.poly(x, y, polySides, polyRadius, Time.time * polyRotateSpeed);
				Draw.reset();
				Draw.z(Layer.block);
			}
		}

		@Override public float totalProgress() {
			return totalProgress;
		}

		@Override public float progress() {
			return buildProgress;
		}

		public int unitCount() {
			if (targetIndex == -1) return numUnits;

			int count = 0;
			for (Unit unit : units) {
				if (unit != null) count++;
			}
			return count;
		}

		@Override public void write(Writes write) {
			super.write(write);

			for (Unit unit : units) {
				write.i(unit == null ? -1 : unit.id);
			}
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);

			for (int i = 0; i < numUnits; i++) {
				readUnitId[i] = read.i();
			}
		}
	}
}