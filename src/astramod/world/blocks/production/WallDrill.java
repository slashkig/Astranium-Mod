package astramod.world.blocks.production;

import arc.Core;
import arc.util.*;
import arc.struct.*;
import arc.math.*;
import arc.math.geom.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.environment.*;
import astramod.world.modules.*;

import static mindustry.Vars.*;

/** Physical wall drill, can be multiboosted. */
public class WallDrill extends Block {
	// for countOre
	protected final ObjectIntMap<Item> oreCount = new ObjectIntMap<>();
	protected final Seq<Item> itemArray = new Seq<>();
	protected @Nullable Item returnItem;
	protected int returnCount;
	protected boolean invalidItem;

	public float drillTime = 200f;
	public int range = 5;
	public int tier = 1;
	public float hardnessDrillMultiplier = 50f;
	public ObjectFloatMap<Item> drillMultipliers = new ObjectFloatMap<>();

	public float liquidBoostIntensity = 2.5f;
	public ObjectFloatMap<Liquid> boostMultMap = new ObjectFloatMap<>();

	public Effect updateEffect = Fx.mineWallSmall;
	public float updateEffectChance = 0.01f;
	public float rotateSpeed = 2f;

	public TextureRegion topRegion;
	public TextureRegion rotatorRegion;
	public TextureRegion rotatorBottomRegion;

	public WallDrill(String name) {
		super(name);

		hasItems = true;
		rotate = true;
		update = true;
		solid = true;
		regionRotated1 = 1;

		envEnabled |= Env.space;
		flags = EnumSet.of(BlockFlag.drill);
		group = BlockGroup.drills;
	}

	@Override public void load() {
		super.load();
		topRegion = Core.atlas.find(name + "-top");
		rotatorRegion = Core.atlas.find(name + "-rotator");
		rotatorBottomRegion = Core.atlas.find(name + "-rotator-bottom");
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(Stat.drillTier, StatValues.drillables(drillTime, hardnessDrillMultiplier, size, drillMultipliers, b ->
			(b instanceof Floor f && f.wallOre && f.itemDrop != null && f.itemDrop.hardness <= tier) ||
			(b instanceof StaticWall w && w.itemDrop != null && w.itemDrop.hardness <= tier)
		));

		stats.add(Stat.drillSpeed, 60f / drillTime * size, StatUnit.itemsSecond);

		if (boostMultMap.size > 0) {
			stats.remove(Stat.booster);

			Seq<LiquidStack> boosters = new Seq<LiquidStack>(boostMultMap.size);
			for (ObjectFloatMap.Entry<Liquid> entry : boostMultMap) { boosters.add(new LiquidStack(entry.key, entry.value)); }
			boosters.sort(e -> e.amount);

			for (LiquidStack booster : boosters) {
				stats.add(Stat.booster, StatValues.speedBoosters(
					"{0}" + StatUnit.timesSpeed.localized(),
					((ConsumeLiquidFilter)findConsumer(f -> f instanceof ConsumeLiquidFilter)).amount,
					booster.amount * liquidBoostIntensity, false,
					l -> l == booster.liquid
				));
			}
		} else if (liquidBoostIntensity != 1 && findConsumer(f -> f instanceof ConsumeLiquidBase && f.booster) instanceof ConsumeLiquidBase consBase) {
			stats.remove(Stat.booster);
			stats.add(Stat.booster, StatValues.speedBoosters(
				"{0}" + StatUnit.timesSpeed.localized(),
				consBase.amount, liquidBoostIntensity, false,
				l -> (consumesLiquid(l) && (findConsumer(f -> f instanceof ConsumeLiquid).booster || ((ConsumeLiquid)findConsumer(f -> f instanceof ConsumeLiquid)).liquid != l))
			));
		}
	}

	@Override public void setBars() {
		super.setBars();

		if (findConsumer(f -> (f instanceof ConsumeLiquid && !f.booster)) != null) {
			addLiquidBar(build -> build.liquids.current());
		}
		addBar("drillspeed", (WallDrillBuild e) -> new Bar(() -> Core.bundle.format("bar.drillspeed", Strings.fixed(e.lastDrillSpeed * 60, 2)), () -> Pal.ammo, () -> e.warmup));
	}

	@Override public boolean outputsItems() {
		return true;
	}

	@Override public boolean rotatedOutput(int x, int y) {
		return false;
	}

	@Override public TextureRegion[] icons() {
		return new TextureRegion[] { region, topRegion };
	}

	public ConsumeLiquidFilter consumeLiquidBoosts(float boostCost, Object... liquidBoosts) {
		if (liquidBoosts.length % 2 != 0) {
			throw new IllegalArgumentException("Expected an even number of arguments, but got " + liquidBoosts.length);
		}

		for (int i = 0; i < liquidBoosts.length; i += 2) {
			boostMultMap.put((Liquid)liquidBoosts[i], (float)liquidBoosts[i + 1] / liquidBoostIntensity);
		}

		return ((ConsumeLiquidFilter)consume(new ConsumeLiquidFilter(liquid -> boostMultMap.containsKey(liquid), boostCost)).boost());
	}

	@Override public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
		Draw.rect(region, plan.drawx(), plan.drawy());
		Draw.rect(topRegion, plan.drawx(), plan.drawy(), plan.rotation * 90);
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		countOre(x, y, rotation);

		if (returnItem != null) {
			float width = drawPlaceText(Core.bundle.formatFloat("bar.drillspeed", 60f / getDrillTime(returnItem) * returnCount, 2), x, y, valid);
			float dx = x * tilesize + offset - width / 2f - 4f, dy = y * tilesize + offset + size * tilesize / 2f + 5, s = iconSmall / 4f;
			Draw.mixcol(Color.darkGray, 1f);
			Draw.rect(returnItem.fullIcon, dx, dy - 1, s, s);
			Draw.reset();
			Draw.rect(returnItem.fullIcon, dx, dy, s, s);
		} else if (invalidItem) {
			drawPlaceText(Core.bundle.get("bar.drilltierreq"), x, y, false);
		}
	}

	@Override public boolean canPlaceOn(Tile tile, Team team, int rotation) {
		for (int i = 0; i < size; i++) {
			nearbySide(tile.x, tile.y, rotation, i, Tmp.p1);
			Tile other = world.tile(Tmp.p1.x, Tmp.p1.y);
			if (other != null && other.solid()) {
				Item drop = other.wallDrop();
				if (drop != null && drop.hardness <= tier) {
					return true;
				}
			}
		}
		return false;
	}

	protected void countOre(int x, int y, int rotation) {
		returnItem = null;
		invalidItem = false;
		returnCount = 0;

		oreCount.clear();
		itemArray.clear();

		for (int i = 0; i < size; i++) {
			nearbySide(x, y, rotation, i, Tmp.p1);

			Tile other = world.tile(Tmp.p1.x, Tmp.p1.y);
			Item drop = other == null ? null : other.wallDrop();
			if (drop != null) {
				if (drop.hardness <= tier) {
					oreCount.increment(drop, 0, 1);
				} else {
					invalidItem = true;
				}
			}
		}

		for (Item item : oreCount.keys()) {
			itemArray.add(item);
		}

		itemArray.sort((item1, item2) -> {
			int type = Boolean.compare(!item1.lowPriority, !item2.lowPriority);
			if (type != 0) return type;
			int amounts = Integer.compare(oreCount.get(item1, 0), oreCount.get(item2, 0));
			if (amounts != 0) return amounts;
			return Integer.compare(item1.id, item2.id);
		});

		if (itemArray.size > 0) {
			returnItem = itemArray.peek();
			returnCount = oreCount.get(itemArray.peek(), 0);
		}
	}

	public float getDrillTime(Item item) {
		return (drillTime + hardnessDrillMultiplier * item.hardness) / drillMultipliers.get(item, 1f);
	}

	public class WallDrillBuild extends Building {
		public int itemsCount;
		public Item dominantItem;
		public float timeDrilled, timeRotate;
		public float lastDrillSpeed;
		public float warmup;

		protected float prevTime = 0f;

		@Override public Building create(Block block, Team team) {
			Building build = super.create(block, team);
			if (boostMultMap.size > 0) { liquids = new LiquidBoostModule(boostMultMap); }
			return build;
		}

		@Override public boolean shouldConsume() {
			return items.total() < itemCapacity && enabled;
		}

		@Override public void updateTile() {
			super.updateTile();

			if (dominantItem == null) {
				countOre(tile.x, tile.y, rotation);
				if (dominantItem == null) { return; }
				dominantItem = returnItem;
				itemsCount = returnCount;
			}
			warmup = Mathf.approachDelta(warmup, Mathf.num(efficiency > 0), 1f / 60f);
			float multiplier = Mathf.lerp(1f, liquidBoostIntensity, optionalEfficiency);
			float drillTime = getDrillTime(dominantItem);
			lastDrillSpeed = (itemsCount * multiplier * timeScale) / drillTime;

			timeDrilled += edelta() * multiplier;
			timeRotate += edelta() * warmup;

			if (timeDrilled >= drillTime) {
				items.add(dominantItem, itemsCount);
				produced(dominantItem);
				timeDrilled %= drillTime;
			}

			if (timer(timerDump, dumpTime)) {
				dump();
			}
		}

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			countOre(tile.x, tile.y, rotation);
			dominantItem = returnItem;
			itemsCount = returnCount;
		}

		@Override public void updateEfficiencyMultiplier() {
			float scale = efficiencyScale();
			efficiency *= scale;
			optionalEfficiency *= scale * boostMultMap.get(liquids.current(), 1f);
		}

		@Override public void draw() {
			Draw.rect(block.region, x, y);
			Draw.rect(topRegion, x, y, rotdeg());

			float ds = 0.6f, dx = Geometry.d4x(rotation) * ds, dy = Geometry.d4y(rotation) * ds;
			int bs = (rotation == 0 || rotation == 3) ? 1 : -1;
			int cornerX = tile.x - (size - 1) / 2, cornerY = tile.y - (size - 1) / 2;
			for (int i = 0; i < size; i++) {
				int rx = 0, ry = 0;

				switch (rotation) {
					case 0 -> {
						rx = cornerX + size;
						ry = cornerY + i;
					}
					case 1 -> {
						rx = cornerX + i;
						ry = cornerY + size;
					}
					case 2 -> {
						rx = cornerX - 1;
						ry = cornerY + i;
					}
					case 3 -> {
						rx = cornerX + i;
						ry = cornerY - 1;
					}
				}

				int sign = i >= size / 2 && size % 2 == 0 ? -1 : 1;
				float vx = (rx - dx) * tilesize, vy = (ry - dy) * tilesize;
				Draw.z(Layer.blockOver);
				Draw.rect(rotatorBottomRegion, vx, vy, timeRotate * rotateSpeed * sign * bs);
				Draw.rect(rotatorRegion, vx, vy);

				if (prevTime != Time.time) {
					Tile other = world.tile(rx, ry);
					Item drop = other == null ? null : other.wallDrop();
					if (drop == dominantItem && Mathf.chanceDelta(updateEffectChance * warmup)) {
						updateEffect.at(
							other.worldx() + Mathf.range(3f) - dx * tilesize,
							other.worldy() + Mathf.range(3f) - dy * tilesize,
							dominantItem.color.cpy().lerp(other.block().mapColor, 0.5f)
						);
					}
				}
			}
			prevTime = Time.time;
		}

		@Override public void drawSelect() {
			if (dominantItem != null) {
				float dx = x - size * tilesize / 2f, dy = y + size * tilesize / 2f, s = iconSmall / 4f;
				Draw.mixcol(Color.darkGray, 1f);
				Draw.rect(dominantItem.fullIcon, dx, dy - 1, s, s);
				Draw.reset();
				Draw.rect(dominantItem.fullIcon, dx, dy, s, s);
			}
		}
	}
}