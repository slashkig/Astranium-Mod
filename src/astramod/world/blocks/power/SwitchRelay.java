package astramod.world.blocks.power;

import arc.scene.ui.layout.*;
import arc.struct.Seq;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.world.blocks.power.*;

public class SwitchRelay extends PowerRelay {
	public SwitchRelay(String name) {
		super(name);
		drawDisabled = true;
		config(Boolean.class, (SwitchRelayBuild relay, Boolean state) -> relay.setActive(state));
	}

	public static boolean isInactiveSwitch(Building build) {
		return build instanceof SwitchRelayBuild && !build.enabled;
	}

	public class SwitchRelayBuild extends PowerRelayBuild {
		public void setActive(boolean active) {
			if (active) {
				enabled = true;
				updatePowerGraph();
			} else {
				power.graph.remove(this);
				enabled = false;
				PowerGraph newGraph = new PowerGraph();
				newGraph.reflow(this);
			}
		}

		@Override public void buildConfiguration(Table table) {
			table.button(Icon.upOpen, Styles.cleari, () -> {
				configure(!enabled);
				deselect();
			}).update(b -> b.setChecked(enabled)).size(40f);
		}

		@Override public void updatePowerGraph() {
			if (enabled) super.updatePowerGraph();
		}

		@Override public Seq<Building> getPowerConnections(Seq<Building> out) {
			return enabled ? super.getPowerConnections(out) : new Seq<Building>();
		}

		@Override public boolean conductsTo(Building other) {
			return false;
		}

		@Override public float warmupTarget() {
			return enabled ? super.warmupTarget() : 0f;
		}
	}
}