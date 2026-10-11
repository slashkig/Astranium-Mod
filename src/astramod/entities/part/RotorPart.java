package astramod.entities.part;

import arc.graphics.g2d.*;
import arc.math.Mathf;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.part.*;

public class RotorPart extends RegionPart {
	public RegionPart top;

	public RotorPart(float spinTime) {
		super("-rotor-blur");
		outline = false;
		layerOffset = 1f;
		moveRot = 360f;
		progress = PartProgress.time.loop(spinTime);

		top = new RegionPart("-rotor-top");
		top.layerOffset = 2f;
	}

	@Override public void load(String name) {
		super.load(name);
		top.load(name);
	}

	@Override public void draw(PartParams params) {
		super.draw(params);

		int len = mirror && params.sideOverride == -1 ? 2 : 1;
		for (int s = 0; s < len; s++) {
			int i = (params.sideOverride == -1 ? s : params.sideOverride);
			float sign = (i == 1 ? -1 : 1) * params.sideMultiplier;
			Tmp.v1.set(x * sign, y).rotateRadExact((params.rotation - 90f) * Mathf.degRad);

			childParam.set(params.warmup, params.reload, params.smoothReload, params.heat, params.recoil, params.charge, params.x + Tmp.v1.x, params.y + Tmp.v1.y, params.rotation);
			childParam.sideMultiplier = params.sideMultiplier;
			childParam.life = params.life;
			childParam.sideOverride = i;
			top.draw(childParam);
		}
	}

	@Override public void getOutlines(Seq<TextureRegion> out) {
		super.getOutlines(out);
		top.getOutlines(out);
	}
}