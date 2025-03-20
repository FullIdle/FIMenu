package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.HeadMatcher;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.RemoveHeadFactory;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

@Getter
public class JavaScriptAction implements IAction {
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

    public static final HeadMatcher MATCHER = new HeadMatcher("js: ");

    public static final RemoveHeadFactory<JavaScriptAction> FACTORY = new RemoveHeadFactory<>("js: ", JavaScriptAction.class);
}
