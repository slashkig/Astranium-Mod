package astramod.world.blocks.distribution;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import arc.util.io.*;
import astramod.world.meta.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.*;

public class AstraItemBridge extends BufferedItemBridge {
	public float bufferSpeed = 4f;

	public AstraItemBridge(String name) {
		super(name);

		configurations.clear();
		config(Point2.class, (ItemBridgeBuild build, Point2 p) -> {
			build.link = Point2.pack(p.x + build.tileX(), p.y + build.tileY());
			build.rotation = Math.round(Angles.angle(p.x, p.y) / 90f);
			build.updateProximity();
		});
		config(Integer.class, (ItemBridgeBuild build, Integer i) -> {
			build.link = i;
			if (i != -1) {
				Tmp.p1.set(Point2.unpack(i));
				build.rotation = Math.round(Angles.angle(build.tileX(), build.tileY(), Tmp.p1.x, Tmp.p1.y) / 90f);
			}
			build.updateProximity();
		});
	}

	@Override public void setStats() {
		super.setStats();

		stats.add(AstraStat.bridgeRange, range, StatUnit.blocks);
	}

	@Override public boolean rotatedOutput(int x, int y) {
		return ((ItemBridgeBuild)Vars.world.build(x, y)).link != -1;
	}

	public class AstraItemBridgeBuild extends BufferedItemBridgeBuild {
		protected ItemBuffer buffer = new ItemBuffer(bufferCapacity);

		@Override public void updateTransport(Building other) {
			if (buffer.accepts() && items.total() > 0) {
				buffer.accept(items.take());
			}

			Item item = buffer.poll(speed / timeScale);
			if (timer(timerAccept, bufferSpeed / timeScale) && item != null && other.acceptItem(this, item)) {
				moved = true;
				other.handleItem(this, item);
				buffer.remove();
			}
		}

		@Override public void write(Writes write) {
			super.write(write);
			buffer.write(write);
		}

		@Override public void read(Reads read, byte revision) {
			super.read(read, revision);
			buffer.read(read);
		}
	}
}
