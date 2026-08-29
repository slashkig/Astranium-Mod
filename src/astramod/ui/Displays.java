package astramod.ui;

import arc.func.*;
import arc.graphics.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import astramod.content.AstraStatusEffects;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;

public final class Displays {
	public static void init() {
		var cells = Vars.ui.hudGroup.<Group>find("status").<Group>find(e -> e instanceof Stack).<Table>find(e -> e instanceof Table).getCells();
		Prov<Color> healthColor = () -> !Vars.player.dead() && Vars.player.unit().hasEffect(AstraStatusEffects.irradiated) ? AstraStatusEffects.irradiated.color : Pal.health;
		Element healthBar = ((Stack)cells.get(0).get()).getChildren().get(0);
		((Cell<?>)cells.get(0)).update(c -> healthBar.color.set(healthColor.get()));
		((Cell<?>)cells.get(2)).update(c -> c.color.set(Vars.player.displayAmmo() ? Pal.ammo : Vars.player.unit() instanceof Payloadc pay && !pay.payloads().isEmpty() ? Pal.items : healthColor.get()));
	}

	/** Searches {@code table} for a Label containing {@code str}. If the string is found, it is replaced with {@code replacement}. */
	public static void replaceLabelText(Table table, String str, String replacement) {
		int ind;
		for (Element e : table.getChildren()) {
			if (e instanceof Label l && (ind = l.getText().indexOf(str)) != -1) {
				l.getText().replace(ind, ind + str.length(), replacement);
				return;
			}
		}
	}
}