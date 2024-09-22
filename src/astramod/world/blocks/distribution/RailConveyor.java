package astramod.world.blocks.distribution;

import arc.math.*;
import mindustry.world.blocks.distribution.StackConveyor;

public class RailConveyor extends StackConveyor {
	public RailConveyor(String name) {
		super(name);
	}

	public class RailConveyorBuild extends StackConveyorBuild {
		
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
					// TODO Typecasting
					if (front() instanceof RailConveyorBuild) {
						RailConveyorBuild e = (RailConveyorBuild)front();
						if (e.team == team && e.link == -1) {
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

		/*@Override public void onProximityUpdate() {
			this.noSleep();

			int lastState = state;
			state = stateMove;

			int[] bits = buildBlending(tile, rotation, null, true);
			if (bits[0] == 0 && blends(tile, rotation, 0) && (!blends(tile, rotation, 2) || back() instanceof StackConveyorBuild b && b.state == stateUnload)) state = stateLoad;  // a 0 that faces into a conveyor with none behind it
			if (outputRouter && bits[0] == 0 && !blends(tile, rotation, 0) && blends(tile, rotation, 2)) state = stateUnload; // a 0 that faces into none with a conveyor behind it
			if (!outputRouter && !(front() instanceof StackConveyorBuild)) state = stateUnload; // a 0 that faces into none with a conveyor behind it

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

			//update other conveyor state when this conveyor's state changes
			if (state != lastState) {
				proxUpdating = true;
				for(Building near : proximity){
					if(!(near instanceof StackConveyorBuild b && b.proxUpdating && b.state != stateUnload)){
						near.onProximityUpdate();
					}
				}
				proxUpdating = false;
			}
		}*/
	}
}
