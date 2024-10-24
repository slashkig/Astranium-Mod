package astramod.world.blocks.liquid;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.Geometry;
import arc.util.*;
import astramod.world.meta.AstraStat;
import mindustry.content.Fx;
import mindustry.core.Renderer;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.liquid.*;
import mindustry.world.meta.StatUnit;

import static mindustry.Vars.*;

public class PipelineBridge extends LiquidBridge {
	public float heatCapacity = 0.5f;

	public float liquidPadding = 1f;

	public TextureRegion bottomRegion;
	public TextureRegion bridgeBotRegion;
	public TextureRegion bridgeLiquidRegion;

	public PipelineBridge(String name) {
		super(name);
	}

	@Override public void load() {
		super.load();
		bottomRegion = Core.atlas.find(name + "-bottom");
		bridgeBotRegion = Core.atlas.find(name + "-bridge-bottom");
		bridgeLiquidRegion = Core.atlas.find(name + "-bridge-liquid");
	}

	@Override public void setStats() {
		super.setStats();
		stats.add(AstraStat.bridgeRange, range, StatUnit.blocks);
		stats.add(AstraStat.heatCapacity, heatCapacity * 100, StatUnit.percent);
	}

	@Override protected TextureRegion[] icons() {
		return new TextureRegion[] {bottomRegion, region};
	}

    public class PipelineBridgeBuild extends LiquidBridgeBuild {
		@Override public void update() {
			super.update();

			if (liquids.currentAmount() > 0.1f && liquids.current().temperature > heatCapacity) {
				float strength = liquids.currentAmount() * liquids.current().temperature / heatCapacity;
				damageContinuous(strength / 60f);
				if (Mathf.chanceDelta(strength / 100f)) Fx.fire.at(x, y);
			}
		}

		@Override public void draw() {
			Draw.rect(bottomRegion, x, y);

			if (liquids.currentAmount() > 0.001f) {
				LiquidBlock.drawTiledFrames(size, x, y, liquidPadding, liquids.current(), liquids.currentAmount() / liquidCapacity);
			}

			Draw.rect(block.region, x, y);

			Tile other = world.tile(link);
			if (other != null && other.build != null && other.build.block == this.block) {
				Color liquidColor = Tmp.c1.set(liquids.current().color).a(liquids.currentAmount() / liquidCapacity * liquids.current().color.a);
				float angle = Angles.angle(x, y, other.drawx(), other.drawy()),
					cx = (x + other.drawx()) / 2f,
					cy = (y + other.drawy()) / 2f,
					len = Math.max(Math.abs(x - other.drawx()), Math.abs(y - other.drawy())) - size * tilesize;

				Draw.z(Layer.power - 1);
				Draw.alpha(Renderer.bridgeOpacity);
				float dir = relativeTo(other.x, other.y) * 90f;
				Draw.rect(endRegion, x, y, dir + 90f);
				Draw.rect(endRegion, other.drawx(), other.drawy(), dir + 270f);
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