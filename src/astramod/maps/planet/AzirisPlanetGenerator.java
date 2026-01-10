package astramod.maps.planet;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.noise.*;
import astramod.content.AstraWeathers;
import mindustry.game.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.type.Weather.WeatherEntry;

public class AzirisPlanetGenerator extends PlanetGenerator {
    Color c1 = Color.valueOf("5057a6"), c2 = Color.valueOf("272766"), out = new Color();

	@Override public float getHeight(Vec3 position) {
		return 0;
	}

	@Override public Color getColor(Vec3 position) {
		float depth = Simplex.noise3d(seed, 2, 0.56, 1.7f, position.x, position.y, position.z) / 2f;
		return c1.write(out).lerp(c2, Mathf.clamp(Mathf.round(depth, 0.15f))).a(0.2f);
	}

	@Override public void addWeather(Sector sector, Rules rules) {
		super.addWeather(sector, rules);

		rules.weather.add(new WeatherEntry(AstraWeathers.windy), new WeatherEntry(AstraWeathers.heavyWind));
	}
}