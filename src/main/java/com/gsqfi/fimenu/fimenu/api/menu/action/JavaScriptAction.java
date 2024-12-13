package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

@Getter
public class JavaScriptAction implements Action {
    private final String script;

    public JavaScriptAction(String script) {
        this.script = script;
    }

    @Override
    public void execute(MenuConfig.MenuHolder menuHolder, Player player) {
        try (Context cx = Context.enter()){
            ScriptableObject scope = cx.initStandardObjects();
            scope.put("player", scope, player);
            scope.put("menuHolder", scope, menuHolder);
            cx.evaluateString(scope, this.script, "FIMenuJSAction", 1, null);
        } catch (Exception e) {
            throw new RuntimeException("error script: '"+this.script+"'",e);
        }
    }
}
