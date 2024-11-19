package astramod.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import astramod.graphics.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

public class WireRelay extends PowerBlock {
	public static final int[] blendSprites = {3, 0, 0, 1, 0, 0, 1, 2, 0, 1, 0, 2, 1, 2, 2, 3};
	public static final int[] blendRotation = {0, 0, 90, 0, 180, 0, 90, 180, 270, 270, 90, 90, 180, 0, 270, 0};

	public float warmupSpeed = 0.02f;
	public int wireRange = 10;
	public ItemStack wireCost = new ItemStack(Items.copper, 1);

	public String wireName = "astramod-copper-cable-";
	public TextureRegion[] wireRegions = new TextureRegion[5];
	public TextureRegion glowRegion;

	protected Rect boundsRect;
	protected Rect sourceRect;

	public WireRelay(String name) {
		super(name);
		destructible = true;
		consumesPower = false;
		outputsPower = false;
		canOverdrive = false;
		drawDisabled = false;
		schematicPriority = -10;

		configurable = true;
		config(Point2.class, (WireRelayBuild relay, Point2 pos) -> {
			if (relay.getWireAt(pos.x, pos.y) != null) {
				relay.team.items().add(wireCost.item, wireCost.amount);
				relay.setWiring(pos, false);
			} else if (relay.team.items().get(wireCost.item) >= wireCost.amount) {
				relay.team.items().remove(wireCost);
				relay.setWiring(pos, true);
			}
		});
	}

	@Override public void init() {
		super.init();
		clipSize = Math.max(clipSize, wireRange * tilesize);
		boundsRect = new Rect(0, 0, wireRange * 2 - 0.5f, wireRange * 2 - 0.5f);
		sourceRect = new Rect(wireRange - 1, wireRange - 1, size - 0.5f, size - 0.5f);
	}

	@Override public void load() {
		super.load();

		glowRegion = Core.atlas.find(name + "-glow");
		for (int i = 0; i < 5; i++) {
			wireRegions[i] = Core.atlas.find(wireName + i);
		}
	}

    @Override public void setStats() {
		super.setStats();
		stats.add(Stat.powerRange, wireRange, StatUnit.blocks);
		stats.add(AstraStat.wireCost, StatValues.items(false, wireCost));
	}
	
	@Override public void setBars() {
		super.setBars();

		addBar("power", entity -> new Bar(
			() -> Core.bundle.format("bar.powerbalance", ((entity.power.graph.getPowerBalance() >= 0 ? "+" : "") + UI.formatAmount((long)(entity.power.graph.getPowerBalance() * 60)))),
			() -> Pal.powerBar,
			() -> Mathf.clamp(entity.power.graph.getLastPowerProduced() / entity.power.graph.getLastPowerNeeded())
		));

		addBar("batteries", entity -> new Bar(
			() -> Core.bundle.format("bar.powerstored", UI.formatAmount((long)entity.power.graph.getLastPowerStored()), UI.formatAmount((long)entity.power.graph.getLastCapacity())),
			() -> Pal.powerBar,
			() -> Mathf.clamp(entity.power.graph.getLastPowerStored() / entity.power.graph.getLastCapacity())
		));
	}

	@Override public void drawPlace(int x, int y, int rotation, boolean valid) {
		super.drawPlace(x, y, rotation, valid);

		Lines.stroke(1f);
		Draw.color(Pal.placing);
		Lines.square(x * tilesize + offset, y * tilesize + offset, wireRange * tilesize);
		Draw.reset();
	}

	public class WireRelayBuild extends Building {
		public float warmup = 0f;

		public int numWires = 0;
		protected Wire[][] wiring = new Wire[wireRange * 2][wireRange * 2];

		protected final Seq<Building> connectionChanges = new Seq<>();
		protected final Queue<Point2> checkQueue = new Queue<>();

		@Override public void draw() {
			Draw.rect(region, x, y);
			if (Mathf.zero(Renderer.laserOpacity) || isPayload()) return;

			Draw.z(Layer.blockOver);
			warmup = Mathf.approachDelta(warmup, power.graph.getSatisfaction(), warmupSpeed);
			Draw.color(AstraPal.powerGlow);
			Draw.alpha(warmup);
			Draw.rect(glowRegion, x, y);
		}

		@Override public void drawSelect() {
			super.drawSelect();
			drawWiring();
		}

		@Override public void drawConfigure() {
			super.drawConfigure();
			drawWiring();
		}

		public void drawWiring() {
			for (int cx = -wireRange; cx < wireRange; cx++) {
				for (int cy = -wireRange; cy < wireRange; cy++) {
					Wire wire = getWireAt(cx, cy);
					if (wire != null) {
						Draw.rect(wireRegions[blendSprites[wire.blendState]], x + (cx + 0.5f) * tilesize, y + (cy + 0.5f) * tilesize, blendRotation[wire.blendState]);
					}
				}
			}
			Drawf.square(x, y, wireRange * tilesize * Mathf.sqrt2, 0f);
		}

		public Wire getWireAt(int xOffset, int yOffset) {
			return wiring[xOffset + wireRange][yOffset + wireRange];
		}

		public void setWiring(Point2 offset, boolean add) {
			Point2 target = new Point2(offset.x + wireRange, offset.y + wireRange);
			if (add) {
				// Add wire
				if (wiring[target.x][target.y] == null) numWires++;
				wiring[target.x][target.y] = new Wire(this, target.x, target.y);

				// Check if connected
				boolean hasConnection = false;
				for (Point2 dir : Geometry.d4) {
					int adjX = target.x + dir.x, adjY = target.y + dir.y;
					if (boundsRect.contains(adjX, adjY) && (isConnectedAt(adjX, adjY) || sourceRect.contains(adjX, adjY))) {
						hasConnection = true;
						break;
					}
				}

				if (hasConnection) {
					checkQueue.add(target);
				
					// Flood fill connect
					floodConnect(true, true);

					// Connect affected buildings
					for (Building other : connectionChanges) {
						power.links.addUnique(other.pos());
						other.power.links.addUnique(pos());
						power.graph.addGraph(other.power.graph);
					}
					connectionChanges.clear();
				}
			} else {
				// Remove wire
				if (wiring[target.x][target.y] != null) numWires--;
				if (isConnectedAt(target.x, target.y)) {
					checkQueue.add(target);

					// Flood fill disconnect
					floodConnect(false, true);

					wiring[target.x][target.y] = null;

					// Add starting points from source
					for (Point2 edge : Edges.getEdges(size)) {
						edge = new Point2(wireRange + edge.x - 1, wireRange + edge.y - 1);
						if (isDisconnectedAt(edge.x, edge.y)) checkQueue.add(edge);
					}

					// Flood fill connect
					floodConnect(true, false);

					// Disconnect affected buildings
					for (Building other : connectionChanges) {
						power.links.removeValue(other.pos());
						other.power.links.removeValue(pos());

						PowerGraph newgraph = new PowerGraph();
						newgraph.reflow(this);

						if (other.power.graph != newgraph) {
							PowerGraph origin = new PowerGraph();
							origin.reflow(other);
						}
					}
					connectionChanges.clear();
				}
				else wiring[target.x][target.y] = null;

				// Update blending
				for (int i = 0; i < 4; i++) {
					int adjX = target.x + Geometry.d4x(i), adjY = target.y + Geometry.d4y(i);
					if (boundsRect.contains(adjX, adjY)) {
						Wire other = wiring[adjX][adjY];
						if (other != null) {
							other.blendState &= ~(1 << (i + 2) % 4);
						}
					}
				}	
			}
		}

		/** Performs a flood fill on the wire grid connection status. Either adds or removes the affected buildings to {@code connectionChanges}. */
		public void floodConnect(boolean connect, boolean add) {
			Point2 current;
			int newX, newY;
			while (!checkQueue.isEmpty()) {
				current = checkQueue.removeFirst();
				wiring[current.x][current.y].setConnected(connect, add);
				for (Point2 dir : Geometry.d4) {
					newX = current.x + dir.x;
					newY = current.y + dir.y;
					if (boundsRect.contains(newX, newY) && (connect ? isDisconnectedAt(newX, newY) : isConnectedAt(newX, newY))) {
						checkQueue.addLast(new Point2(newX, newY));
					}
				}
			}
		}

		/** Uses array coords. */
		public boolean isConnectedAt(int posX, int posY) {
			return wiring[posX][posY] != null && wiring[posX][posY].connected;
		}

		/** Uses array coords. */
		public boolean isDisconnectedAt(int posX, int posY) {
			return wiring[posX][posY] != null && !wiring[posX][posY].connected;
		}

		@Override public float warmup() {
			return warmup;
		}

		@Override public boolean conductsTo(Building other) {
			return false;
		}

		@Override public Seq<Building> getPowerConnections(Seq<Building> out) {
			super.getPowerConnections(out);
			out.removeAll(SwitchRelay::isInactiveSwitch);
			return out;
		}

		@Override public boolean onConfigureTapped(float x, float y) {
			if (world.buildWorld(x, y) == this) return false;

			Point2 offset = new Point2(Mathf.round(x / tilesize) - tile.x - 1, Mathf.round(y / tilesize) - tile.y - 1);
			if (!(offset.x >= -wireRange && offset.x < wireRange && offset.y >= -wireRange && offset.y < wireRange)) return false;
			configure(offset);
			return true;
		}

		// TODO onDeconstructed()

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			numWires = read.i();
			for (int i = 0; i < numWires; i++) {
				int arrayX = read.i(), arrayY = read.i();
				wiring[arrayX][arrayY] = new Wire(this, arrayX, arrayY);
			}

			// Set wire connections
			for (Point2 edge : Edges.getEdges(size)) {
				edge = new Point2(wireRange + edge.x - 1, wireRange + edge.y - 1);
				if (wiring[edge.x][edge.y] != null) checkQueue.add(edge);
			}
			floodConnect(true, false);
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.i(numWires);
			for (Wire[] row : wiring) {
				for (Wire wire : row) {
					if (wire != null) wire.write(write);
				}
			}
		}
	}

	public class Wire {
		public WireRelayBuild build;
		public int arrayX, arrayY;
		public boolean connected = false;
		public int blendState = 0;
		private int packedPos;

		public Wire(WireRelayBuild parent, int x, int y) {
			build = parent;
			arrayX = x;
			arrayY = y;
			packedPos = Point2.pack(build.tile.x + arrayX - wireRange + 1, build.tile.y + arrayY - wireRange + 1);
			updateBlending();
		}

		public void setConnected(boolean connect, boolean add) {
			connected = connect;
			Building linked = world.build(packedPos);
			if (linked != null && linked.power != null && linked.team == build.team) {
				if (add) build.connectionChanges.addUnique(linked);
				else build.connectionChanges.remove(linked);
			}
		}

		public void updateBlending() {
			blendState = 0;
			for (int i = 0; i < 4; i++) {
				int adjX = arrayX + Geometry.d4x(i), adjY = arrayY + Geometry.d4y(i);
				if (boundsRect.contains(adjX, adjY)) {
					Wire other = build.wiring[adjX][adjY];
					if (other != null) {
						blendState |= 1 << i;
						other.blendState |= 1 << (i + 2) % 4;
					} else if (sourceRect.contains(adjX, adjY)) {
						blendState |= 1 << i;
					}
				}
			}
		}

		public void write(Writes write) {
			write.i(arrayX);
			write.i(arrayY);
		}
	}
}