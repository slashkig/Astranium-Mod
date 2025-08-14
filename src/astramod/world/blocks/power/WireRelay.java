package astramod.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.*;
import arc.input.KeyCode;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.*;
import astramod.graphics.*;
import astramod.math.*;
import astramod.world.meta.*;

import static mindustry.Vars.*;

/** Uses player-placed wires to connect buildings to the power grid. */
public class WireRelay extends PowerBlock {
	public static final int[] blendSprites = {0, 1, 1, 3, 1, 2, 3, 4, 1, 3, 2, 4, 3, 4, 4, 5};
	public static final int[] blendRotation = {0, 180, 270, 0, 0, 0, 90, 180, 90, 270, 90, 90, 180, 0, 270, 0};
	/** Cycle time in frames of the wire selection circle. */
	public static final float selectCircleTime = 120f;

	/** Stores all the relays on the map, divided into 32-tile chunks for easier searching. */
	public static final GridMap<Seq<WireRelayBuild>> relayBuilds = new GridMap<>();
	/** The last wire selected by the player. */
	protected static Point2 lastSelected;

	public float warmupSpeed = 0.02f;
	public int wireRange = 10;
	public ItemStack wireCost = new ItemStack(Items.copper, 1);

	public String wireName = "astramod-copper-cable-";
	public TextureRegion[] wireRegions = new TextureRegion[6];
	public TextureRegion glowRegion;

	/** Represents the full range of the relay. */
	protected Rect boundsRect;
	/** Represents the wirespace occupied by the relay block itself. */
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

		// Single wire placement
		config(Point2.class, (WireRelayBuild relay, Point2 pos) -> {
			if (relay.getWireAt(pos.x, pos.y) != null) {
				relay.team.items().add(wireCost.item, wireCost.amount);
				relay.setWiring(pos, false);
			} else if (relay.team.items().get(wireCost.item) >= wireCost.amount) {
				relay.team.items().remove(wireCost);
				relay.setWiring(pos, true);
			}
		});

		// Multi wire placement
		config(Long.class, (WireRelayBuild relay, Long packed) -> {
			WireConfig config = WireConfig.unpack(packed);
			if (config.adding) {
				int wiresAdded = relay.multiWireAdd(config.start, config.end);
				relay.team.items().remove(wireCost.item, wireCost.amount * wiresAdded);
			} else {
				int wiresRemoved = relay.multiWireRemove(config.start, config.end);
				relay.team.items().add(wireCost.item, wireCost.amount * wiresRemoved);
			}
		});

		// Rebuilding and new block placement
		config(Point2[].class, (WireRelayBuild relay, Point2[] positions) -> {
			relay.generateWiring(positions);
			relay.team.items().remove(wireCost.item, wireCost.amount * positions.length);
		});
	}

	@Override public void init() {
		super.init();
		clipSize = Math.max(clipSize, wireRange * tilesize * 2);
		boundsRect = Mathx.squareRect((size + 1) % 2 - wireRange - 1, wireRange * 2 - (size + 1) % 2 + 0.1f);
		sourceRect = Mathx.squareRect(-(size + 1) / 2, size - 1);
	}

	@Override public void load() {
		super.load();

		glowRegion = Core.atlas.find(name + "-glow");
		for (int i = 0; i < 6; i++) {
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

		addBar("wires", entity -> new Bar(
			() -> Core.bundle.format("bar.wires", ((WireRelayBuild)entity).wiring.size()),
			() -> wireCost.item.color,
			() -> ((WireRelayBuild)entity).wiring.size() > 0 ? 1 : 0
		));

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

	/** Checks adjacent chunks for connections for a newly placed block. */
	public static void updateWireConnectionAt(Tile tile) {
		Rect rect = new Rect().setSize(tile.block().size);
		updateConnectionForChunk(tile, new Point2(0, 0), rect);
		for (Point2 dir : Geometry.d8) {
			updateConnectionForChunk(tile, dir, rect);
		}
	}

	/** Checks if relays in a chunk should connect to a block. */
	protected static void updateConnectionForChunk(Tile tile, Point2 dir, Rect rect) {
		Seq<WireRelayBuild> chunk = relayBuilds.get((tile.x >> 5) + dir.x, (tile.y >> 5) + dir.y);
		float offset = (rect.width + 1) / 2;
		if (chunk == null) return;
		for (WireRelayBuild build : chunk) {
			rect.setPosition(tile.x - build.tile.x - offset, tile.y - build.tile.y - offset);
			if (((WireRelay)build.block).boundsRect.overlaps(rect) && build.isConnectedWithin(rect)) {
				build.connectionChanges.add(tile.build);
				build.connectBuildings();
			}
		}
	}

	public class WireRelayBuild extends Building {
		public float warmup = 0f;

		protected GridMap<Wire> wiring = new GridMap<>();
		protected GridMap<Integer> sourceBlending = new GridMap<>();

		protected final Seq<Building> connectionChanges = new Seq<>();
		protected final Queue<Point2> checkQueue = new Queue<>();
		protected boolean configuring = false;

		@Override public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {
			// Add to corresponding chunk
			Seq<WireRelayBuild> chunk = relayBuilds.get(tile.x >> 5, tile.y >> 5);
			if (chunk == null) {
				chunk = new Seq<>();
				relayBuilds.put(tile.x >> 5, tile.y >> 5, chunk);
			}
			chunk.add(this);

			// Generate source blending
			for (Point2 edge : Edges.getInsideEdges(size)) {
				sourceBlending.put(edge.x - 1, edge.y - 1, 0);
			}

			return super.init(tile, team, shouldAdd, rotation);
		}

		@Override public void remove() {
			Seq<WireRelayBuild> chunk = relayBuilds.get(tile.x >> 5, tile.y >> 5);
			if (chunk != null) chunk.remove(this);
			super.remove();
		}

		@Override public void draw() {
			Draw.z(Layer.block);
			Draw.rect(region, x, y);
			if (Mathf.zero(Renderer.laserOpacity) || isPayload()) return;

			Draw.z(Layer.blockOver);
			warmup = Mathf.approachDelta(warmup, power.graph.getSatisfaction(), warmupSpeed);
			Draw.color(AstraPal.powerGlow);
			Draw.alpha(warmup);
			Draw.rect(glowRegion, x, y);

			if (control.input.config.getSelected() != this) {
				Draw.z(Layer.blockUnder);
				Draw.alpha(1f);
				drawWiring();
			}

			Draw.reset();
		}

		@Override public void drawSelect() {
			super.drawSelect();
			drawWiring();
			Drawf.square(x, y, wireRange * tilesize * Mathf.sqrt2, 0f);
		}

		@Override public void drawConfigure() {
			Draw.z(Layer.blockUnder + 0.1f);
			Draw.color(Pal.accentBack, 0.2f);
			Fill.square(x, y, wireRange * tilesize);
			Draw.color();
			Draw.z(Layer.blockOver);
			drawWiring();
			Draw.rect(region, x, y);
			super.drawConfigure();
			Drawf.square(x, y, wireRange * tilesize * Mathf.sqrt2, 0f);
			if (lastSelected != null) {
				Draw.color(configuring ? Pal.accentBack : Pal.accent);
				Lines.circle(
					wireDrawX(lastSelected.x), wireDrawY(lastSelected.y),
					0.75f * tilesize * (1 + (float)Math.sin(Time.time / selectCircleTime * Math.PI) / 8)
				);
			}
			Draw.reset();
		}

		public void drawWiring() {
			for (Wire wire : wiring.values()) {
				Draw.rect(wireRegions[blendSprites[wire.blendState]], wireDrawX(wire.rx), wireDrawY(wire.ry), blendRotation[wire.blendState]);
			}
			int blend;
			for (Point2 edge : Edges.getInsideEdges(size)) {
				blend = sourceBlending.get(edge.x - 1, edge.y - 1);
				if (blend != 0) Draw.rect(wireRegions[blendSprites[blend]], wireDrawX(edge.x - 1), wireDrawY(edge.y - 1), blendRotation[blend]);
			}
		}

		public float wireDrawX(float rx) {
			return x + (rx + 0.5f) * tilesize;
		}

		public float wireDrawY(float ry) {
			return y + (ry + 0.5f) * tilesize;
		}

		/** Offsets x-coordinates for arrays (no negative values). */
		public int offsetX(int rx) {
			return rx - (int)boundsRect.x;
		}

		/** Offsets y-coordinates for arrays (no negative values). */
		public int offsetY(int ry) {
			return ry - (int)boundsRect.y;
		}

		/** @return The wire at the specified coordinates. */
		public Wire getWireAt(int rx, int ry) {
			return wiring.get(rx, ry);
		}

		/** Adds a new wire at the given coordinates if there wasn't a wire already present.
		 *  @return If a new wire was added. */
		protected boolean addNewWireAt(int atX, int atY) {
			if (!wiring.containsKey(atX, atY)) {
				wiring.put(atX, atY, new Wire(this, atX, atY));
				return true;
			} else return false;
		}

		/** Creates an array storing the locations of all wires in the relay. */
		public Point2[] packWiring() {
			Point2[] wires = new Point2[wiring.size()];
			int c = 0;
			for (Wire wire : wiring.values()) {
				wires[c] = new Point2(wire.rx, wire.ry);
				c++;
			}
			return wires;
		}

		/** Creates wires at the positions in the inputted array. */
		public void generateWiring(Point2[] positions) {
			// Create wires
			for (Point2 pos : positions) {
				addNewWireAt(pos.x, pos.y);
			}
			floodFromSource(true);
			connectBuildings();
		}

		/** Adds or removes a wire. */
		public void setWiring(Point2 at, boolean add) {
			configuring = true;
			if (add) {
				// Add wire
				addNewWireAt(at.x, at.y);

				// Check if connected
				for (Point2 dir : Geometry.d4) {
					int adjX = at.x + dir.x, adjY = at.y + dir.y;
					if (isConnectedAt(adjX, adjY) || sourceRect.contains(adjX, adjY)) {
						// Flood fill connect
						checkQueue.add(at);
						floodConnect(true, true);
						connectBuildings();
						break;
					}
				}
			} else {
				// Remove wire
				removeWirePath(at);
			}
			configuring = false;
		}

		/** Adds lines of wires along both axes to connect {@code start} to {@code end}, adding the longer axis first.
		 *  @return Number of wires added. */
		public int multiWireAdd(Point2 start, Point2 end) {
			configuring = true;
			int wiresAdded = 0;
			if (Math.abs(start.x - end.x) > Math.abs(start.y - end.y)) {
				// X is longer
				wiresAdded += addHorizontalWires(start.x, start.y, end.y);
				wiresAdded += addVerticalWires(start.x, end.x, end.y);
			} else {
				// Y is longer
				wiresAdded += addVerticalWires(start.x, end.x, start.y);
				wiresAdded += addHorizontalWires(end.x, start.y, end.y);
			}

			// Flood fill connect
			checkQueue.add(start);
			floodConnect(true, true);
			connectBuildings();

			configuring = false;
			return wiresAdded;
		}

		/** Finds and removes the shortest contiguous path of wires from {@code start} to {@code end}.
		 *  @return Number of wires removed. */
		public int multiWireRemove(Point2 start, Point2 end) {
			if (wiring.size() == 0) return 0;
			configuring = true;

			Queue<Point2> searchQueue = new Queue<>();
			boolean[][] checked = new boolean[(int)boundsRect.width + 1][(int)boundsRect.height + 1];
			GridMap<Point2> parents = new GridMap<>();
			Point2 current = null, target = null;
			int newX, newY;
			boolean pathFound = false;

			// Search for a valid path to remove
			searchQueue.add(start);
			checked[offsetX(start.x)][offsetY(start.y)] = true;
			while (!pathFound) {
				// Target closest unchecked wire to starting point
				while (!searchQueue.isEmpty()) {
					current = searchQueue.removeFirst();
					if (wiring.containsKey(current.x, current.y)) {
						target = current;
						break;
					} else {
						for (Point2 dir : Geometry.d4) {
							newX = current.x + dir.x;
							newY = current.y + dir.y;
							if (boundsRect.contains(newX, newY) && !checked[offsetX(newX)][offsetY(newY)]) {
								searchQueue.addLast(new Point2(newX, newY));
								checked[offsetX(newX)][offsetY(newY)] = true;
							}
						}
					}
				}

				// If true, could not find valid target
				if (target == null) {
					configuring = false;
					return 0;
				}

				// Create directional map outwards from the target, until either the endpoint is found or all wires are checked
				checkQueue.add(target);
				while (!checkQueue.isEmpty()) {
					current = checkQueue.removeFirst();
					if (current.equals(end)) { // Match
						checkQueue.clear();
						pathFound = true;
					} else { // Continue searching
						for (Point2 dir : Geometry.d4) {
							newX = current.x + dir.x;
							newY = current.y + dir.y;
							if (wiring.containsKey(newX, newY) && !parents.containsKey(newX, newY)) {
								checkQueue.addLast(new Point2(newX, newY));
								parents.put(newX, newY, current);
							}
						}
					}
				}
				target = null;
			}

			// Construct path
			Seq<Point2> path = new Seq<>(Point2.class);
			while (current != target) {
				path.add(current);
				current = parents.get(current.x, current.y);
			}
			path.add(target);

			// Remove wires
			removeWirePath(path.toArray());
			configuring = false;
			return path.size;
		}

		/** Adds a vertical line of wire from {@code startX} up to, but not including, {@code endX}.
		 *  @return Number of wires added. */
		protected int addVerticalWires(int startX, int endX, int wy) {
			int wiresAdded = 0;
			if (startX < endX) {
				// Moving right
				for (int wx = startX; wx < endX; wx++) {
					if (addNewWireAt(wx, wy)) wiresAdded++;
				}
			} else {
				// Moving left
				for (int wx = startX; wx > endX; wx--) {
					if (addNewWireAt(wx, wy)) wiresAdded++;
				}
			}
			return wiresAdded;
		}

		/** Adds a horizontal line of wire from {@code startY} up to, but not including, {@code endY}.
		 *  @return Number of wires added. */
		protected int addHorizontalWires(int wx, int startY, int endY) {
			int wiresAdded = 0;
			if (startY < endY) {
				// Moving up
				for (int wy = startY; wy < endY; wy++) {
					if (addNewWireAt(wx, wy)) wiresAdded++;
				}
			} else {
				// Moving down
				for (int wy = startY; wy > endY; wy--) {
					if (addNewWireAt(wx, wy)) wiresAdded++;
				}
			}
			return wiresAdded;
		}


		/** Removes the wires at the position(s) in {@code path}. */
		protected void removeWirePath(Point2... path) {
			Point2 target = path[0];
			boolean connected = isConnectedAt(target.x, target.y);

			if (connected) {
				// Flood fill disconnect
				checkQueue.add(target);
				floodConnect(false, true);
			}

			Wire other;
			int adjX, adjY;
			for (Point2 pos : path) {
				wiring.remove(pos.x, pos.y);

				// Update blending
				for (int i = 0; i < 4; i++) {
					adjX = pos.x + Geometry.d4x(i);
					adjY = pos.y + Geometry.d4y(i);
					other = wiring.get(adjX, adjY);
					if (other != null) {
						other.blendState &= ~(1 << (i + 2) % 4);
					} else if (sourceRect.contains(adjX, adjY)) {
						sourceBlending.put(adjX, adjY, sourceBlending.get(adjX, adjY) & ~(1 << (i + 2) % 4));
					}
				}
			}

			if (connected) {
				floodFromSource(false);
				disconnectBuildings();
			}
		}

		/** Flood connects the wire grid starting from the source block.
		 * @param add - Whether buildings should be added to or removed from {@code connectionChanges}. */
		public void floodFromSource(boolean add) {
			for (Point2 edge : Edges.getEdges(size)) {
				edge = new Point2(edge.x - 1, edge.y - 1);
				if (isDisconnectedAt(edge.x, edge.y)) checkQueue.add(edge);
			}
			floodConnect(true, add);
		}

		/** Performs a flood fill on the wire grid connection status. Either adds or removes the affected buildings to/from {@code connectionChanges}.
		 * @param add - Whether buildings should be added to or removed from {@code connectionChanges}. */
		public void floodConnect(boolean connect, boolean add) {
			Point2 current;
			int newX, newY;
			while (!checkQueue.isEmpty()) {
				current = checkQueue.removeFirst();
				wiring.get(current.x, current.y).setConnected(connect, add);	
				for (Point2 dir : Geometry.d4) {
					newX = current.x + dir.x;
					newY = current.y + dir.y;
					if (boundsRect.contains(newX, newY) && (connect ? isDisconnectedAt(newX, newY) : isConnectedAt(newX, newY))) {
						checkQueue.addLast(new Point2(newX, newY));
					}
				}
			}
		}

		/** @return Whether the wire at the specified coordinates is connected to the source. */
		public boolean isConnectedAt(int posX, int posY) {
			return wiring.containsKey(posX, posY) && wiring.get(posX, posY).connected;
		}

		/** @return Whether the wire at the specified coordinates is NOT connected to the source. */
		public boolean isDisconnectedAt(int posX, int posY) {
			return wiring.containsKey(posX, posY) && !wiring.get(posX, posY).connected;
		}

		/** @return Whether there is a wire within the {@code rect} bounds that is connected to the source. */
		public boolean isConnectedWithin(Rect rect) {
			for (int posX = (int)Math.max(rect.x, boundsRect.x); posX < Math.min(rect.x + rect.width, boundsRect.x + boundsRect.width + 1); posX++) {
				for (int posY = (int)Math.max(rect.y, boundsRect.y); posY < Math.min(rect.y + rect.height, boundsRect.y + boundsRect.height + 1); posY++) {
					if (isConnectedAt(posX, posY)) return true;
				}
			}
			return false;
		}

		/** Creates power links for all buildings in {@code connectionChanges}. */
		protected void connectBuildings() {
			for (Building other : connectionChanges) {
				power.links.addUnique(other.pos());
				other.power.links.addUnique(pos());
				power.graph.addGraph(other.power.graph);
			}
			connectionChanges.clear();
		}

		/** Removes power links for all buildings in {@code connectionChanges}. */
		protected void disconnectBuildings() {
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

		@Override public float warmup() {
			return warmup;
		}

		@Override public boolean conductsTo(Building other) {
			return false;
		}

		@Override public boolean canPickup() {
			return wiring.size() == 0;
		}

		@Override public Seq<Building> getPowerConnections(Seq<Building> out) {
			super.getPowerConnections(out);
			out.removeAll(SwitchRelay::isInactiveSwitch);
			return out;
		}

		@Override public boolean onConfigureTapped(float x, float y) {
			if (world.buildWorld(x, y) == this) {
				deselect();
				return true;
			}

			Point2 origin = new Point2(Mathf.round(x / tilesize) - tile.x - 1, Mathf.round(y / tilesize) - tile.y - 1);
			if (!boundsRect.contains(origin.x, origin.y)) {
				lastSelected = null;
				return false;
			}

			if (!configuring) {
				if (Core.input.keyDown(KeyCode.shiftLeft) || Core.input.keyDown(KeyCode.shiftRight)) {
					if (!wiring.containsKey(origin.x, origin.y)) {
						boolean[][] checked = new boolean[(int)boundsRect.width + 1][(int)boundsRect.height + 1];
						checkQueue.add(origin);
						checked[offsetX(origin.x)][offsetY(origin.y)] = true;

						Point2 current;
						int newX, newY;
						while (!checkQueue.isEmpty()) {
							current = checkQueue.removeFirst();
							if (isConnectedAt(current.x, current.y) || sourceRect.contains(current.x, current.y)) {
								checkQueue.clear();
								if (current != origin) configure(WireConfig.pack(origin, current, true));
								break;
							} else {
								for (Point2 dir : Geometry.d4) {
									newX = current.x + dir.x;
									newY = current.y + dir.y;
									if (boundsRect.contains(newX, newY) && !checked[offsetX(newX)][offsetY(newY)]) {
										checkQueue.addLast(new Point2(newX, newY));
										checked[offsetX(newX)][offsetY(newY)] = true;
									}
								}
							}
						}
					} else if (lastSelected != null) configure(WireConfig.pack(lastSelected, origin, false));
				} else {
					configure(origin);
				}
			}
			lastSelected = origin;
			return true;
		}

		@Override public void deselect() {
			lastSelected = null;
			super.deselect();
		}

		@Override public Object config() {
			return packWiring();
		}

		// TODO onDeconstructed()

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			int numWires = read.i();
			for (int i = 0; i < numWires; i++) {
				int wx = read.i(), wy = read.i();
				wiring.put(wx, wy, new Wire(this, wx, wy));
			}

			floodFromSource(false);
		}

		@Override public void write(Writes write) {
			super.write(write);
			write.i(wiring.size());
			for (Wire wire : wiring.values()) {
				wire.write(write);
			}
		}
	}

	public class Wire {
		public WireRelayBuild build;
		public int rx, ry;
		public boolean connected = false;
		public int blendState = 0;

		public Wire(WireRelayBuild parent, int x, int y) {
			build = parent;
			rx = x;
			ry = y;
			updateBlending();
		}

		/** @param connect - Whether the wire should be set to connected or disconnected.
		 * @param add - Whether the wire's corresponding building (if any) should be added to or removed from {@code connectionChanges}. */
		public void setConnected(boolean connect, boolean add) {
			connected = connect;
			Building linked = world.build(build.tile.x + rx, build.tile.y + ry);
			if (linked != null && linked.power != null && linked.team == build.team) {
				if (add) build.connectionChanges.addUnique(linked);
				else build.connectionChanges.remove(linked);
			}
		}

		/** Updates the blending of the wire and its neighbors. */
		public void updateBlending() {
			blendState = 0;
			for (int i = 0; i < 4; i++) {
				int adjX = rx + Geometry.d4x(i), adjY = ry + Geometry.d4y(i);
				if (boundsRect.contains(adjX, adjY)) {
					Wire other = build.wiring.get(adjX, adjY);
					if (other != null) {
						blendState |= 1 << i;
						other.blendState |= 1 << (i + 2) % 4;
					} else if (sourceRect.contains(adjX, adjY)) {
						blendState |= 1 << i;
						build.sourceBlending.put(adjX, adjY, build.sourceBlending.get(adjX, adjY) | (1 << (i + 2) % 4));
					}
				}
			}
		}

		public void write(Writes write) {
			write.i(rx);
			write.i(ry);
		}

		@Override public String toString() {
			return String.format("%sconnected wire (%d, %d)", connected ? "" : "dis", rx, ry);
		}
	}

	/** Data container for multi wire configs. */
	public static class WireConfig {
		public final Point2 start, end;
		public final boolean adding;

		public WireConfig(Point2 start, Point2 end, boolean adding) {
			this.start = start;
			this.end = end;
			this.adding = adding;
		}

		public static long pack(Point2 start, Point2 end, boolean adding) {
			// Removes the end.x sign bit to make room for the adding boolean
			return (start.pack() & 0xFFFFFFFFL) | ((end.pack() & 0x7FFFFFFFL) << 32) | ((adding ? 1L : 0L) << 63);
		}

		public static WireConfig unpack(long packed) {
			return new WireConfig(
				Point2.unpack((int)(packed & 0xFFFFFFFF)),
				Point2.unpack((int)((packed >>> 32) & 0x7FFFFFFF | (packed >>> 31) & 0x80000000)), // Recovers the end.x sign bit
				packed >>> 63 == 1
			);
		}
	}
}