package astramod.world.blocks.distribution;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.distribution.StackConveyor;

import static mindustry.Vars.*;

/** Stack conveyor with stricter rules. */
public class RailConveyor extends StackConveyor {
	public RailConveyor(String name) {
		super(name);
		outputRouter = false;
	}

	@Override public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
		if (tile.build instanceof StackConveyorBuild b) {
			int state = b.state;
			if (state == stateLoad) { // standard conveyor mode
				return otherblock.outputsItems() && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
			}else if (state == stateUnload && !outputRouter) { // router mode
				return otherblock.acceptsItems &&
					(!otherblock.noSideBlend || lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock)) &&
					(notLookingAt(tile, rotation, otherx, othery, otherrot, otherblock) ||
					(otherblock instanceof StackConveyor && facing(otherx, othery, otherrot, tile.x, tile.y))) &&
					!(world.build(otherx, othery) instanceof StackConveyorBuild s && s.state == stateUnload) &&
					!(world.build(otherx, othery) instanceof StackConveyorBuild s2 && s2.state == stateMove &&
						!facing(otherx, othery, otherrot, tile.x, tile.y));
			}
		}
		return otherblock.outputsItems() && blendsArmored(tile, rotation, otherx, othery, otherrot, otherblock) && otherblock instanceof StackConveyor;
	}

	public class RailConveyorBuild extends StackConveyorBuild {
		boolean proxUpdating = false;

		@Override public void draw() {
			Draw.z(Layer.block - 0.2f);

			Draw.rect(regions[state], x, y, rotdeg());

			for(int i = 0; i < 4; i++){
				if((blendprox & (1 << i)) == 0){
					Draw.rect(edgeRegion, x, y, (rotation - i) * 90);
				}
			}

			//draw inputs
			if (state == stateLoad) {
				for (int i = 0; i < 4; i++) {
					int dir = rotation - i;
					var near = nearby(dir);
					if ((blendprox & (1 << i)) != 0 && i != 0 && near != null && !near.block.squareSprite) {
						Draw.rect(sliced(regions[0], SliceMode.bottom), x + Geometry.d4x(dir) * tilesize * 0.75f, y + Geometry.d4y(dir) * tilesize * 0.75f, (float)(dir * 90));
					}
				}
			} else if(state == stateUnload) { //front unload
				//TOOD hacky front check
				if ((blendprox & (1)) != 0 && !front().block.squareSprite) {
					Draw.rect(sliced(regions[0], SliceMode.top), x + Geometry.d4x(rotation) * tilesize * 0.75f, y + Geometry.d4y(rotation) * tilesize * 0.75f, rotation * 90f);
				}
			}

			Draw.z(Layer.block - 0.1f);

			Tile from = world.tile(link);

			if (glowRegion.found() && power != null && power.status > 0f) {
				Draw.z(Layer.blockAdditive);
				Draw.color(glowColor, glowAlpha * power.status);
				Draw.blend(Blending.additive);
				Draw.rect(glowRegion, x, y, rotation * 90);
				Draw.blend();
				Draw.color();
				Draw.z(Layer.block - 0.1f);
			}

			if (link == -1 || from == null || lastItem == null) return;

			int fromRot = from.build == null ? rotation : from.build.rotation;

			//offset
			Tmp.v1.set(from.worldx(), from.worldy());
			Tmp.v2.set(x, y);
			Tmp.v1.interpolate(Tmp.v2, 1f - cooldown, Interp.linear);

			//rotation
			float a = (fromRot % 4) * 90;
			float b = (rotation % 4) * 90;
			if ((fromRot % 4) == 3 && (rotation % 4) == 0) a = -1 * 90;
			if ((fromRot % 4) == 0 && (rotation % 4) == 3) a =  4 * 90;

			if (glowRegion.found()) {
				Draw.z(Layer.blockAdditive + 0.01f);
			}

			//stack
			Draw.rect(stackRegion, Tmp.v1.x, Tmp.v1.y, Mathf.lerp(a, b, Interp.smooth.apply(1f - Mathf.clamp(cooldown * 2, 0f, 1f))));

			//item
			float size = itemSize * Mathf.lerp(Math.min((float)items.total() / itemCapacity, 1), 1f, 0.4f);
			Drawf.shadow(Tmp.v1.x, Tmp.v1.y, size * 1.2f);
			Draw.rect(lastItem.fullIcon, Tmp.v1.x, Tmp.v1.y, size, size, 0);
		}

		@Override public void onProximityUpdate() {
			super.onProximityUpdate();

			int lastState = state;

			state = stateMove;

			int[] bits = buildBlending(tile, rotation, null, true);
			if(bits[0] == 0 && blends(tile, rotation, 0) && (!blends(tile, rotation, 2) || back() instanceof StackConveyorBuild b && b.state == stateUnload)) state = stateLoad;  // a 0 that faces into a conveyor with none behind it
			if(outputRouter && bits[0] == 0 && !blends(tile, rotation, 0) && blends(tile, rotation, 2)) state = stateUnload; // a 0 that faces into none with a conveyor behind it
			if(!outputRouter && !(front() instanceof StackConveyorBuild)) state = stateUnload; // a 0 that faces into none with a conveyor behind it

			if(!headless){
				blendprox = 0;

				for(int i = 0; i < 4; i++){
					if(blends(tile, rotation, i) && (state != stateUnload || outputRouter || i == 0 || nearby(Mathf.mod(rotation - i, 4)) instanceof StackConveyorBuild)){
						blendprox |= (1 << i);
					}
				}
			}

			//cannot load when facing
			if(state == stateLoad){
				for(Building near : proximity){
					if(near instanceof StackConveyorBuild && near.front() == this){
						state = stateMove;
						break;
					}
				}
			}

			// update other conveyor state when this conveyor's state changes
			if (state != lastState) {
				proxUpdating = true;
				for (Building near : proximity) {
					if (!(near instanceof RailConveyorBuild r && r.proxUpdating && r.state != stateUnload)) {
						near.onProximityUpdate();
					}
				}
				proxUpdating = false;
			}
		}

		@Override public void updateTile() {
			float eff = enabled ? (efficiency + baseEfficiency) : 0f;

			if(cooldown > 0f) cooldown = Mathf.clamp(cooldown - speed * eff * delta(), 0f, recharge);

			if(link == -1) return;

			if (cooldown > 0f) return;

			if (lastItem == null || !items.has(lastItem)) {
				lastItem = items.first();
			}

			if (!enabled) return;

			if (state == stateUnload) {
				while (lastItem != null && (!outputRouter ? moveForward(lastItem) : dump(lastItem))) {
					if (!outputRouter) {
						items.remove(lastItem, 1);
					}

					if (items.empty()) {
						poofOut();
						lastItem = null;
					}
				}
			} else {
				if (state != stateLoad || (items.total() >= getMaximumAccepted(lastItem))) {
					if (front() instanceof RailConveyorBuild e && e.team == team && e.link == -1) {
						e.items.add(items);
						e.lastItem = lastItem;
						e.link = tile.pos();

						link = -1;
						items.clear();

						cooldown = recharge;
						e.cooldown = 1;
					}
				}
			}
		}
	}
}
