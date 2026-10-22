package astramod.graphics;

import arc.graphics.g2d.*;
import mindustry.graphics.*;

public final class DrawUtil {
	public static void drawPhase(TextureRegion region, float x, float y, float alpha) {
		Draw.draw(Layer.blockOver, () -> {
			AstraShaders.phase.region = region;
			AstraShaders.phase.alpha = alpha;

			Draw.shader(AstraShaders.phase);
			Draw.rect(region, x, y);
			Draw.shader();
		});
	}
}