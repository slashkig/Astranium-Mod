package astramod.graphics;

import arc.graphics.g2d.*;
import arc.util.*;
import mindustry.graphics.Shaders.*;

public class AstraShaders {
	public static PhaseShader phase;

	public static void init() {
		phase = new PhaseShader();
	}

	public static class PhaseShader extends LoadShader {
		public TextureRegion region;
		public float alpha;

		public PhaseShader() {
			super("phase", "default");
		}

		@Override public void apply() {
			setUniformf("u_time", Time.time);
			setUniformf("u_alpha", alpha);
			setUniformf("u_uv", region.u, region.v);
			setUniformf("u_uv2", region.u2, region.v2);
			setUniformf("u_texsize", region.texture.width, region.texture.height);
		}
	}
}