package astramod.world.blocks.liquid;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Geometry;
import arc.util.*;
import mindustry.core.Renderer;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;

import static mindustry.Vars.*;

/** Liquid bridge that shows the liquid in the bridge. */
public class AstraLiquidBridge extends LiquidBridge {
	public float liquidPadding = 1f;

	public TextureRegion bottomRegion;
	public TextureRegion bridgeBotRegion;
	public TextureRegion bridgeLiquidRegion;

	public AstraLiquidBridge(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		bottomRegion = Core.atlas.find(name + "-bottom");
		bridgeBotRegion = Core.atlas.find(name + "-bridge-bottom");
		bridgeLiquidRegion = Core.atlas.find(name + "-bridge-liquid");
	}

    public class AstraLiquidBridgeBuild extends LiquidBridgeBuild {
		@Override public void draw() {
			Draw.rect(bottomRegion, x, y);

			if(liquids.currentAmount() > 0.001f){
				LiquidBlock.drawTiledFrames(size, x, y, liquidPadding, liquids.current(), liquids.currentAmount() / liquidCapacity);
			}

			Draw.rect(block.region, x, y);

			Tile other = world.tile(link);
			if (other != null) {
				Color liquidColor = Tmp.c1.set(liquids.current().color).a(liquids.currentAmount() / liquidCapacity * liquids.current().color.a);
				float angle = Angles.angle(x, y, other.drawx(), other.drawy()),
					cx = (x + other.drawx()) / 2f,
					cy = (y + other.drawy()) / 2f,
					len = Math.max(Math.abs(x - other.drawx()), Math.abs(y - other.drawy())) - size * tilesize;

				Draw.z(Layer.power - 1);
				Draw.alpha(Renderer.bridgeOpacity);
				Draw.rect(bridgeRegion, cx, cy, len, bridgeRegion.height * bridgeRegion.scl(), angle);
				if (liquidColor != null) {
					Draw.color(liquidColor, liquidColor.a * Renderer.bridgeOpacity);
					Draw.rect(bridgeLiquidRegion, cx, cy, len, bridgeLiquidRegion.height * bridgeLiquidRegion.scl(), angle);
					Draw.color();
					Draw.alpha(Renderer.bridgeOpacity);
				}
				if (bridgeBotRegion.found()) {
					Draw.color(0.4f, 0.4f, 0.4f, 0.4f * Renderer.bridgeOpacity);
					Draw.rect(bridgeBotRegion, cx, cy, len, bridgeBotRegion.height * bridgeBotRegion.scl(), angle);
					Draw.reset();
				}
				Draw.alpha(Renderer.bridgeOpacity);

				int i = relativeTo(other.x, other.y),
					dist = Math.max(Math.abs(other.x - tile.x), Math.abs(other.y - tile.y)) - 1,
					arrows = (int)(dist * tilesize / arrowSpacing),
					dx = Geometry.d4x(i),
					dy = Geometry.d4y(i);

				for (int a = 0; a < arrows; a++) {
					Draw.alpha(Mathf.absin(a - time / arrowTimeScl, arrowPeriod, 1f) * warmup * Renderer.bridgeOpacity);
					Draw.rect(arrowRegion,
						x + dx * (tilesize / 2f + a * arrowSpacing + arrowOffset),
						y + dy * (tilesize / 2f + a * arrowSpacing + arrowOffset),
						i * 90f
					);
				}

				Draw.reset();
			}
		}
	}
}