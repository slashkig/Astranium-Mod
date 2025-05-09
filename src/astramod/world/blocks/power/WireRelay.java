package astramod.world.blocks.power;

import arc.Core;
import arc.graphics.g2d.*;
import arc.input.KeyCode;
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
	public static final int[] blendSprites = {0, 1, 1, 3, 1, 2, 3, 4, 1, 3, 2, 4, 3, 4, 4, 5};
	public static final int[] blendRotation = {0, 180, 270, 0, 0, 0, 90, 180, 90, 270, 90, 90, 180, 0, 270, 0};

	protected static Point2 lastSelected;

	public float warmupSpeed = 0.02f;
	public int wireRange = 10;
	public ItemStack wireCost = new ItemStack(Items.copper, 1);

	public String wireName = "astramod-copper-cable-";
	public TextureRegion[] wireRegions = new TextureRegion[6];
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

		config(WireConfig.class, (WireRelayBuild relay, WireConfig config) -> {
			if (config.adding) {
				int wiresAdded = relay.multiWireAdd(config.start, config.end);
				relay.team.items().remove(wireCost.item, wireCost.amount * wiresAdded);
			} /* else {
				int wiresRemoved = relay.multiWireRemove(config.start, config.end);
				relay.team.items().add(wireCost.item, wireCost.amount * wiresRemoved);
			}*/
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
			super.drawConfigure();
			drawWiring();
			Drawf.square(x, y, wireRange * tilesize * Mathf.sqrt2, 0f);
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
		}

		public Wire getWireAt(int xOffset, int yOffset) {
			return wiring[xOffset + wireRange][yOffset + wireRange];
		}

		public void setWiring(Point2 offset, boolean add) {
			Point2 target = new Point2(offset.x + wireRange, offset.y + wireRange);
			if (add) {
				// Add wire
				if (addNewWireAt(target.x, target.y)) numWires++;

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
					// Flood fill connect
					checkQueue.add(target);
					floodConnect(true, true);
					connectBuildings();
				}
			} else {
				// Remove wire
				if (wiring[target.x][target.y] != null) numWires--;
				removeWirePath(target);
			}
		}

		/** Adds lines of wires along both axes to connect {@code start} to {@code end}, adding the longer axis first.
		 *  @return Number of wires added. */
		public int multiWireAdd(Point2 start, Point2 end) {
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

			numWires += wiresAdded;
			return wiresAdded;
		}

		/** Finds and removes the shortest contiguous path of wires from a start point to an end point.
		 *  @return Number of wires removed. */
		public int multiWireRemove(Point2 start, Point2 end) {
			Queue<Point2> searchQueue = new Queue<>();
			boolean[][] checked = new boolean[wiring.length][wiring[0].length];
			GridMap<Point2> parents = new GridMap<>();
			Point2 current = null;
			Point2 target = null;
			int newX, newY;
			boolean pathFound = false;

			// Search 
			searchQueue.add(start);
			checked[start.x][start.y] = true;
			while (!pathFound) {
				// Find closest wire to starting point
				while (!searchQueue.isEmpty()) {
					current = searchQueue.removeFirst();
					if (wiring[current.x][current.y] != null) {
						target = current;
						break;
					} else {
						for (Point2 dir : Geometry.d4) {
							newX = current.x + dir.x;
							newY = current.y + dir.y;
							if (boundsRect.contains(newX, newY) && !checked[newX][newY]) {
								searchQueue.addLast(new Point2(newX, newY));
								checked[newX][newY] = true;
							}
						}
					}
				}

				// If true, could not find valid target
				if (target == null) return 0;

				// Create directional wiring map outwards from the target
				checkQueue.add(target);
				while (!checkQueue.isEmpty()) {
					current = checkQueue.removeFirst();
					if (current.equals(end)) {
						checkQueue.clear();
					} else {
						for (Point2 dir : Geometry.d4) {
							newX = current.x + dir.x;
							newY = current.y + dir.y;
							if (boundsRect.contains(newX, newY) && wiring[newX][newY] != null && !parents.containsKey(newX, newY)) {
								checkQueue.addLast(new Point2(newX, newY));
								parents.put(newX, newY, current);
							}
						}
					}
				}
			}

			// Construct path
			Seq<Point2> path = new Seq<>();
			while (current != target) {
				path.add(current);
				current = parents.get(current.x, current.y);
			}
			path.add(target);

			// Remove wires
			numWires -= path.size;
			removeWirePath(path.toArray());
			return path.size;
		}

		/** Adds a vertical line of wire from {@code startX} up to, but not including, {@code endX}.
		 *  @return Number of wires added. */
		protected int addVerticalWires(int startX, int endX, int y) {
			int wiresAdded = 0;
			if (startX < endX) {
				// Moving right
				for (int x = startX; x < endX; x++) {
					if (addNewWireAt(x, y)) wiresAdded++;
				}
			} else {
				// Moving left
				for (int x = startX; x > endX; x--) {
					if (addNewWireAt(x, y)) wiresAdded++;
				}
			}
			return wiresAdded;
		}

		/** Adds a horizontal line of wire from {@code startY} up to, but not including, {@code endY}.
		 *  @return Number of wires added. */
		protected int addHorizontalWires(int x, int startY, int endY) {
			int wiresAdded = 0;
			if (startY < endY) {
				// Moving up
				for (int y = startY; y < endY; y++) {
					if (addNewWireAt(x, y)) wiresAdded++;
				}
			} else {
				// Moving down
				for (int y = startY; y > endY; y--) {
					if (addNewWireAt(x, y)) wiresAdded++;
				}
			}
			return wiresAdded;
		}

		/** Adds a new wire at the given coordinates if there wasn't a wire already present.
		 *  @return If a new wire was added. */
		protected boolean addNewWireAt(int atX, int atY) {
			if (wiring[atX][atY] == null) {
				wiring[atX][atY] = new Wire(this, atX, atY);
				return true;
			} else return false;
		}

		protected void removeWirePath(Point2... path) {
			Point2 target = path[0];
			boolean connected = isConnectedAt(target.x, target.y);

			if (connected) {
				// Flood fill disconnect
				checkQueue.add(target);
				floodConnect(false, true);
			}

			int adjX, adjY;
			Wire other;
			for (Point2 pos : path) {
				wiring[pos.x][pos.y] = null;

				// Update blending
				for (int i = 0; i < 4; i++) {
					adjX = pos.x + Geometry.d4x(i);
					adjY = pos.y + Geometry.d4y(i);
					if (boundsRect.contains(adjX, adjY)) {
						other = wiring[adjX][adjY];
						if (other != null) {
							other.blendState &= ~(1 << (i + 2) % 4);
						}
					}
				}
			}

			if (connected) {
				// Add starting points from source
				for (Point2 edge : Edges.getEdges(size)) {
					edge = new Point2(wireRange + edge.x - 1, wireRange + edge.y - 1);
					if (isDisconnectedAt(edge.x, edge.y)) checkQueue.add(edge);
				}

				// Flood fill connect
				floodConnect(true, false);
				disconnectBuildings();
			}
		}

		/** Performs a flood fill on the wire grid connection status. Either adds or removes the affected buildings to/from {@code connectionChanges}. */
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

		/** Uses array coords.
		 * @return Whether the wire at the specified coordinates is connected to the source.
		 */
		public boolean isConnectedAt(int posX, int posY) {
			return wiring[posX][posY] != null && wiring[posX][posY].connected;
		}

		/** Uses array coords.
		 * @return Whether the wire at the specified coordinates is NOT connected to the source.
		 */
		public boolean isDisconnectedAt(int posX, int posY) {
			return wiring[posX][posY] != null && !wiring[posX][posY].connected;
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

			Point2 offset = new Point2(Mathf.round(x / tilesize) - tile.x - 1, Mathf.round(y / tilesize) - tile.y - 1);
			if (!(offset.x >= -wireRange && offset.x < wireRange && offset.y >= -wireRange && offset.y < wireRange)) return false;

			Point2 origin = new Point2(offset.x + wireRange, offset.y + wireRange);
			if (Core.input.keyDown(KeyCode.shiftLeft) || Core.input.keyDown(KeyCode.shiftRight)) {
				if (wiring[origin.x][origin.y] == null) {
					boolean[][] checked = new boolean[wiring.length][wiring[0].length];
					checkQueue.add(origin);
					checked[origin.x][origin.y] = true;

					Point2 current;
					int newX, newY;
					while (!checkQueue.isEmpty()) {
						current = checkQueue.removeFirst();
						if (isConnectedAt(current.x, current.y) || sourceRect.contains(current.x, current.y)) {
							checkQueue.clear();
							if (current != origin) configure(new WireConfig(origin, current, true));
							return true;
						} else {
							for (Point2 dir : Geometry.d4) {
								newX = current.x + dir.x;
								newY = current.y + dir.y;
								if (boundsRect.contains(newX, newY) && !checked[newX][newY]) {
									checkQueue.addLast(new Point2(newX, newY));
									checked[newX][newY] = true;
								}
							}
						}
					}
				} else {
					configure(new WireConfig(lastSelected, origin, false));
				}
			} else {
				configure(offset);
			}
			lastSelected = origin;
			return true;
		}

		@Override public void deselect() {
			lastSelected = null;
			super.deselect();
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

	// Multi wire config data container
	public record WireConfig(Point2 start, Point2 end, boolean adding) { }
}