package astramod.world.blocks.distribution;

import arc.util.io.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.BufferedItemBridge;

/** Made solely to override a single hardcoded 4 :( */
public class AstraBufferedItemBridge extends BufferedItemBridge {
	public float bufferSpeed = 4f;

	public AstraBufferedItemBridge(String name) {
		super(name);
	}

	public class AstraBufferedItemBridgeBuild extends BufferedItemBridgeBuild {
		ItemBuffer buffer = new ItemBuffer(bufferCapacity);

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
