package astramod.content;

import arc.*;
import arc.util.Log;
import astramod.world.blocks.power.*;
import mindustry.game.EventType.*;

public class AstraEvents {
	public static void load() {
		Log.info("Loading events");

		Events.on(BlockBuildEndEvent.class, e -> {
			if (e.tile.block().hasPower) WireRelay.updateWireConnectionAt(e.tile);
		});

		Events.on(ResetEvent.class, e -> { WireRelay.relayBuilds.clear(); });

		Events.run(Trigger.update, AstraWeathers::updateWind);

		Events.run(Trigger.newGame, AstraWeathers::setupWind);
	}
}
