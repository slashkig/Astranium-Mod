package astramod.ui;

import arc.func.*;
import arc.graphics.*;
import arc.scene.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import astramod.content.*;
import astramod.entities.abilities.*;
import astramod.gen.*;
import astramod.graphics.*;

import static mindustry.Vars.*;

public final class Displays {
	public static void init() {
		var cells = ui.hudGroup.<Group>find("status").<Group>find(e -> e instanceof Stack).<Table>find(e -> e instanceof Table).getCells();

		Prov<Color> healthColor = () ->
			player.dead() ? Color.black :
			player.unit().hasEffect(AstraStatusEffects.irradiated) ? AstraStatusEffects.irradiated.color :
			player.unit() instanceof Concealc con && con.concealment() > 0f ? AstraPal.smokescreen :
			Pal.health;
		Element healthBar = ((Stack)cells.get(0).get()).getChildren().get(0);
		((Cell<?>)cells.get(0)).update(c -> healthBar.color.set(healthColor.get()));

		Cell<?> infoCell = cells.get(2);
		infoCell.update(c -> c.color.set(
			player.dead() ? Color.black :
			player.displayAmmo() ? Pal.ammo :
			Structs.contains(player.unit().abilities, a -> a instanceof ActivatedAbility) ? Pal.accent :
			player.unit() instanceof Payloadc pay && !pay.payloads().isEmpty() ? Pal.items :
			healthColor.get()
		));
		// Reflection trick for the annoying local class
		try {
			var field = infoCell.get().getClass().getDeclaredField("amount");
			field.setAccessible(true);
			field.set(infoCell.get(), (Floatp) () ->
				player.dead() ? 0f :
				player.displayAmmo() ? player.unit().ammof() :
				Structs.find(player.unit().abilities, a -> a instanceof ActivatedAbility) instanceof ActivatedAbility a ? a.getProgress(player.unit()) :
				player.unit() instanceof Payloadc pay && !pay.payloads().isEmpty() ? pay.payloadUsed() / player.unit().type().payloadCapacity :
				player.unit().healthf()
			);
		} catch (ReflectiveOperationException e) { throw new RuntimeException(e); }
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

	public static void setLabel(Label label, String text) {
		label.getText().setLength(0);
		label.getText().append(text);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Element> T getElement(Table table, int index) {
		if (index < 0) index += table.getCells().size;
		return (T)table.getCells().get(index).get();
	}
}