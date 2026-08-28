package astramod.ui;

import arc.func.*;
import arc.graphics.Color;
import arc.scene.*;
import arc.scene.ui.layout.*;
import astramod.content.AstraStatusEffects;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.Pal;

public final class Displays {
	public static void init() {
		var cells = ((Table)((Stack)((Group)Vars.ui.hudGroup.find("status")).find(e -> e instanceof Stack)).find(e -> e instanceof Table)).getCells();
		Prov<Color> healthColor = () -> !Vars.player.dead() && Vars.player.unit().hasEffect(AstraStatusEffects.irradiated) ? AstraStatusEffects.irradiated.color : Pal.health;
		Element healthBar = ((Stack)cells.get(0).get()).getChildren().get(0);
		((Cell<?>)cells.get(0)).update(c -> healthBar.color.set(healthColor.get()));
		((Cell<?>)cells.get(2)).update(c -> c.color.set(Vars.player.displayAmmo() ? Pal.ammo : Vars.player.unit() instanceof Payloadc pay && !pay.payloads().isEmpty() ? Pal.items : healthColor.get()));
	}
}