package astramod.entities.part;

import arc.Core;
import arc.graphics.g2d.*;
import mindustry.Vars;
import mindustry.entities.part.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

public class LiquidPart extends DrawPart {
	public String suffix = "-liquid";
	public float layer = -1f, layerOffset = 0f;
	public float alpha = 1f;

	public TextureRegion region;

	public LiquidPart(String region) {
		suffix = region;
	}

	public LiquidPart() { };

	@Override public void load(String name) {
		super.load(name);
		region = Core.atlas.find(name + suffix);
	}

	@Override public void draw(PartParams params) {
		float z = Draw.z();
        if (layer > 0) Draw.z(layer);
        if (under && turretShading) Draw.z(z - 0.0001f);
        Draw.z(Draw.z() + layerOffset);

		Building build = Vars.world.buildWorld(params.x, params.y);
		Liquid drawn = build.liquids.current();
		Drawf.liquid(region, params.x, params.y, alpha * build.liquids.get(drawn) / build.block.liquidCapacity, drawn.color, params.rotation - 90f);

		Draw.z(z);
	}
}