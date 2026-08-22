package astramod.content;

import arc.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.ui.*;
import astramod.ui.*;
import astramod.world.blocks.power.*;

public class AstraEvents {
	public static void load() {
		Log.info("Loading events");
		
		Events.on(AtlasPackEvent.class, e -> {
			Team.blue.name = "sentinels";
			Icons.packIcons();
		});

		Events.on(ClientLoadEvent.class, e -> {
			Icons.registerIcons();
			Team.blue.emoji = Fonts.getUnicodeStr("sentinels");
		});

		Events.on(BlockBuildEndEvent.class, e -> {
			if (e.tile.block().hasPower) WireRelay.updateWireConnectionAt(e.tile);
		});

		Events.on(ResetEvent.class, e -> { WireRelay.relayBuilds.clear(); });

		Events.run(Trigger.update, AstraWeathers::updateWind);

		Events.run(Trigger.newGame, AstraWeathers::setupWind);
	}
}
