package astramod.ui;

import arc.func.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.*;
import mindustry.ui.dialogs.*;
import mindustry.world.meta.StatUnit;
import astramod.game.*;

import static astramod.ui.CustomRulesSetter.*;

public class AstraCustomRules {
	public static void init() {
		CustomRulesDialog
			editorRulesDialog = Reflect.get(Reflect.<Object>get(Vars.ui.editor, "infoDialog"), "ruleInfo"),
			playtestRulesDialog = Reflect.get(Reflect.<Object>get(Vars.ui.editor, "playtestDialog"), "dialog"),
			customGameRulesDialog = Reflect.get(Reflect.<Object>get(Vars.ui.custom, "dialog"), "dialog"),
			pausedRulesDialog = Reflect.get(Vars.ui.paused, "rulesDialog");

		editorRulesDialog.additionalSetup.add(() -> addRules(editorRulesDialog));
		playtestRulesDialog.additionalSetup.add(() -> addRules(playtestRulesDialog));
		customGameRulesDialog.additionalSetup.add(() -> addRules(customGameRulesDialog));
		pausedRulesDialog.additionalSetup.add(() -> addRules(pausedRulesDialog));
	}

	public static void addRules(CustomRulesDialog dialog) {
		setMod("astramod-");
		setDialog(dialog);

		setCategory("environment");
		addRuleToggle("wind-enabled");
		cellCustomUpdate((Button e) -> e.setChecked(Vars.state.rules.tags.getBool("astramod-wind-enabled")));
		Boolf<Rules> windEnabled = r -> r.tags.getBool("astramod-wind-enabled") && !Vars.state.isGame();
		addRuleRange("wind-setupRand", StatUnit.percent, windEnabled, Math.round(WindLogic.defaultSetupRandMin * 100f), Math.round(WindLogic.defaultSetupRandMax * 100f), Math.round(WindLogic.rulesRandMin * 100f), Math.round(WindLogic.rulesRandMax * 100f));
		addRuleRange("wind-setupCounter", StatUnit.minutes, windEnabled, WindLogic.defaultSetupCounterMin, WindLogic.defaultSetupCounterMax, 0.1f, Float.MAX_VALUE);
		addRuleRange("wind-rand", StatUnit.percent, windEnabled, Math.round(WindLogic.defaultRandMin * 100f), Math.round(WindLogic.defaultRandMax * 100f), Math.round(WindLogic.rulesRandMin * 100f), Math.round(WindLogic.rulesRandMax * 100f));
		addRuleRange("wind-counter", StatUnit.minutes, windEnabled, WindLogic.defaultCounterMin, WindLogic.defaultCounterMax, 0.1f, Float.MAX_VALUE);
	}
}