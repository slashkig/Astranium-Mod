package astramod.io;

import arc.util.*;
import mindustry.gen.*;
import astramod.entities.abilities.ActivatedAbility;

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
			else Log.warn("Failed to find weather: " + data);
		});
		NetUtil.serverIntsHandler("ability-activate", (player, data) -> {
			int unitId = data.get();
			Unit unit = Groups.unit.getByID(unitId);
			int abilityId = data.get();
			if (unit == null) {
				Log.warn("Failed to find unit id: " + unitId);
				return;
			}
			ActivatedAbility ability = (ActivatedAbility)Structs.find(unit.abilities, a -> a instanceof ActivatedAbility aa && aa.id == abilityId);
			if (ability != null && ability.canActivate(unit)) ability.activate(unit);
		});
		NetUtil.clientIntsHandler("weapon-shoot", data -> {
			int unitId = data.get();
			Unit unit = Groups.unit.getByID(unitId);
			if (unit == null) Log.warn("Failed to find unit id: " + unitId);
			else unit.mounts[data.get()].shoot = data.get() != 0;
		});
	}
}