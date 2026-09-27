package astramod.io;

import arc.util.*;
import astramod.entities.abilities.ActivatedAbility;
import mindustry.gen.*;

import static astramod.AstraVars.*;

public class AstraNetHandlers {
	public static void init() {
		NetUtil.clientFloatHandler("wind-update", data -> windManager.globalWind = data);
		NetUtil.clientFloatsHandler("wind-change", data -> {
			windManager.randWind = data.get();
			windManager.windCounter = data.get();
		});
		NetUtil.clientFloatHandler("wind-delta", data -> {
			windManager.randWind -= data;
			windManager.tempWind += data;
		});
		NetUtil.clientStringHandler("wind-fade", data -> {
			WeatherState instance = Groups.weather.find(w -> w.weather.name.equals(data));
			if (instance != null) instance.life(windManager.fadeDelay);
			else Log.warn("Failed to fade weather: " + data);
		});
		NetUtil.serverIntsHandler("ability-activate", (player, data) -> {
			Unit unit = Groups.unit.getByID(data.get());
			int abilityId = data.get();
			ActivatedAbility ability = (ActivatedAbility)Structs.find(unit.abilities, a -> a instanceof ActivatedAbility aa && aa.id == abilityId);
			if (ability.canActivate(unit)) ability.activate(unit);
		});
	}
}