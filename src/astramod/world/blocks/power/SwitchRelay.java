package astramod.world.blocks.power;

import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.ui.*;

public class SwitchRelay extends PowerRelay {
	public SwitchRelay(String name) {
		super(name);
		drawDisabled = true;
		config(Boolean.class, (SwitchRelayBuild relay, Boolean state) -> relay.setActive(state));
	}

	public class SwitchRelayBuild extends PowerRelayBuild {
		public void setActive(boolean active) {
			enabled = active;
			if (active) {
				updatePowerGraph();
			} else {
				power.graph.remove(this);
			}
		}

		@Override public void buildConfiguration(Table table) {
			table.button(Icon.upOpen, Styles.cleari, () -> {
				configure(!enabled);
				deselect();
			}).update(b -> b.setChecked(enabled)).size(40f);
		}

		@Override public void updatePowerGraph() {
			if (enabled) {
				super.updatePowerGraph();
			}
		}

		@Override public boolean conductsTo(Building other) {
			return false;
		}

		@Override public float warmupTarget() {
			return enabled ? super.warmupTarget() : 0f;
		}
	}
}