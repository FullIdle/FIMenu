package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.Main;
import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.HeadMatcher;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.RemoveHeadFactory;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.script.*;
import java.util.logging.Logger;

@Getter
public class JavaScriptAction implements IAction {
    private final String script;
    private final CompiledScript compile;

    public JavaScriptAction(String script) {
        if (engine == null) throw new IllegalStateException("JavaScript engine not initialized!");
        this.script = script;
        if (engine instanceof Compilable) {
            try {
                compile = ((Compilable) engine).compile(script);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        compile = null;
    }

    @Override
    public void execute(MenuConfig.MenuHolder menuHolder, Player player) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("player", player);
        bindings.put("menuHolder", menuHolder);
        try {
            if (this.compile != null){
                this.compile.eval(bindings);
                return;
            }
            engine.eval(this.script,bindings);
        } catch (ScriptException e) {
            throw new RuntimeException("error script: '" + this.script + "'", e);
        }
    }

    public static final HeadMatcher MATCHER = new HeadMatcher("js: ");

    public static final RemoveHeadFactory<JavaScriptAction> FACTORY = new RemoveHeadFactory<>("js: ", JavaScriptAction.class);

    private static ScriptEngine engine;

    public static void init(String engineName) {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName(engineName);
        Logger logger = Main.INSTANCE.getLogger();
        if (engine == null) {
            logger.warning("JavaScript engine '" + engineName + "' not found, Try using the 'JavaScript'");
            engine = manager.getEngineByName("JavaScript");
        }
        if (engine == null) throw new IllegalStateException("JavaScript engine not initialized!");
    }
}
