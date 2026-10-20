package astramod.ui;

import arc.Core;
import arc.func.*;
import arc.graphics.*;
import arc.scene.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.ui.dialogs.*;
import mindustry.world.meta.StatUnit;

public class CustomRulesSetter {
	private static String modName;
	private static CustomRulesDialog dialog;
	private static Rules rules;

	public static void setMod(String mod) {
		modName = mod;
	}

	public static void setDialog(CustomRulesDialog rulesDialog) {
		dialog = rulesDialog;
		rules = Reflect.get(dialog, "rules");
	}

	public static void setCategory(String category) {
		int index = dialog.categoryNames.indexOf(name -> name == category);
		if (index == -1) {
			dialog.category(category);
		} else {
			dialog.current = dialog.categories.get(index);
			dialog.currentName = category;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Element> void cellCustomUpdate(Cons<T> update) {
		((Cell<T>)dialog.current.getCells().get(dialog.current.getCells().size - 1)).update(update);
	}

	public static void addRuleToggle(String rule) {
		addRuleToggle(rule, r -> true);
	}

	public static void addRuleToggle(String rule, Boolf<Rules> condition) {
		String tag = rule.startsWith(modName) ? rule : modName + rule;
		dialog.check("@rules." + tag, b -> rules.tags.put(tag, String.valueOf(b)), () -> rules.tags.getBool(tag), () -> condition.get(rules));
	}

	public static void addRuleFloat(String rule) {
		addRuleFloat(rule, 0f);
	}

	public static void addRuleFloat(String rule, float defaultVal) {
		addRuleFloat(rule, defaultVal, 0f, Float.MAX_VALUE);
	}

	public static void addRuleFloat(String rule, float defaultVal, float minVal, float maxVal) {
		addRuleFloat(rule, r -> true, defaultVal, minVal, maxVal);
	}

	public static void addRuleFloat(String rule, Boolf<Rules> condition) {
		addRuleFloat(rule, condition, 0f);
	}

	public static void addRuleFloat(String rule, Boolf<Rules> condition, float defaultVal) {
		addRuleFloat(rule, condition, defaultVal, 0f, Float.MAX_VALUE);
	}

	public static void addRuleFloat(String rule, Boolf<Rules> condition, float defaultVal, float minVal, float maxVal) {
		String tag = rule.startsWith(modName) ? rule : modName + rule;
		dialog.number("@rules." + tag, false, f -> rules.tags.put(tag, String.valueOf(f)),
			() -> rules.tags.getFloat(tag, defaultVal), () -> condition.get(rules), minVal, maxVal);
	}

	public static void addRuleRange(String rule, float lowVal, float highVal) {
		addRuleRange(rule, lowVal, highVal, lowVal, highVal);
	}

	public static void addRuleRange(String rule, float defaultLowVal, float defaultHighVal, float minVal, float maxVal) {
		addRuleRange(rule, StatUnit.none, r -> true, defaultLowVal, defaultHighVal, minVal, maxVal);
	}

	public static void addRuleRange(String rule, Boolf<Rules> condition, float lowVal, float highVal) {
		addRuleRange(rule, condition, lowVal, highVal, lowVal, highVal);
	}

	public static void addRuleRange(String rule, Boolf<Rules> condition, float defaultLowVal, float defaultHighVal, float minVal, float maxVal) {
		addRuleRange(rule, StatUnit.none, condition, defaultLowVal, defaultHighVal, minVal, maxVal);
	}

	public static void addRuleRange(String rule, StatUnit unit, float lowVal, float highVal) {
		addRuleRange(rule, unit, lowVal, highVal, lowVal, highVal);
	}

	public static void addRuleRange(String rule, StatUnit unit, float defaultLowVal, float defaultHighVal, float minVal, float maxVal) {
		addRuleRange(rule, unit, r -> true, defaultLowVal, defaultHighVal, minVal, maxVal);
	}

	public static void addRuleRange(String rule, StatUnit unit, Boolf<Rules> condition, float lowVal, float highVal) {
		addRuleRange(rule, unit, condition, lowVal, highVal, lowVal, highVal);
	}

	public static void addRuleRange(String rule, StatUnit unit, Boolf<Rules> condition, float defaultLowVal, float defaultHighVal, float minVal, float maxVal) {
		String ruleName = rule.startsWith(modName) ? rule : modName + rule;
		String lowTag = ruleName + "-low", highTag = ruleName + "-high";
		Floatp lowerProv = () -> rules.tags.getFloat(lowTag, defaultLowVal);
		Floatp upperProv = () -> rules.tags.getFloat(highTag, defaultHighVal);

		if (!Core.bundle.get("rules." + ruleName).toLowerCase().contains(dialog.ruleSearch)) return;
		var cell = dialog.current.table(t -> {
			t.left();
			t.add("@rules." + ruleName).left().padRight(5f).update(a -> a.setColor(condition.get(rules) ? Color.white : Color.gray));
			t.field(lowerProv.get() + "", s -> rules.tags.put(lowTag, s)).padRight(5f).update(a -> a.setDisabled(!condition.get(rules)))
				.valid(f -> Strings.parseFloat(f) >= minVal && Strings.parseFloat(f) <= upperProv.get()).width(120f).left();
			t.add(" - ").left().padRight(5f).update(a -> a.setColor(condition.get(rules) ? Color.white : Color.gray));
			t.field(upperProv.get() + "", s -> rules.tags.put(highTag, s)).padRight(5f).update(a -> a.setDisabled(!condition.get(rules)))
				.valid(f -> Strings.parseFloat(f) >= lowerProv.get() && Strings.parseFloat(f) <= maxVal).width(120f).left();
			t.add(unit.localized()).left().padRight(50f).update(a -> a.setColor(condition.get(rules) ? Color.white : Color.gray));
		}).padTop(0);
		dialog.ruleInfo(cell, "@rules." + ruleName);
		dialog.current.row();
	}
}