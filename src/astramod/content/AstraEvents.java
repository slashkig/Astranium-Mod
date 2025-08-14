package astramod.content;

import arc.*;
import astramod.world.blocks.power.*;
import mindustry.game.EventType.*;

@SuppressWarnings("unused")
public class AstraEvents {
	public static void load() {
		Events.on(BlockBuildEndEvent.class, e -> {
			if (e.tile.block().hasPower) WireRelay.updateWireConnectionAt(e.tile);
		});

		Events.on(ResetEvent.class, e -> {
			WireRelay.relayBuilds.clear();
		});
	}
}
