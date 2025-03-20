package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.action.base.*;

import java.util.HashMap;
import java.util.Map;

public class ActionHelper {
    public static final Map<IMatcher,IFactory<? extends IAction>> registered;

    public static IAction parseAndCreate(String desc) {
        IFactory<? extends IAction> factory = parseFactory(desc);
        if (factory != null) return factory.create(desc);
        return null;
    }

    public static IFactory<? extends IAction> parseFactory(String desc) {
        for (Map.Entry<IMatcher, IFactory<? extends IAction>> entry : registered.entrySet())
            if (entry.getKey().matcher(desc)) return entry.getValue();
        return null;
    }

    static {
        registered = new HashMap<>();
        registered.put(CloseAction.MATCHER, CloseAction.FACTORY);
        registered.put(CommandAction.MATCHER, CommandAction.FACTORY);
        registered.put(ConsoleAction.MATCHER, ConsoleAction.FACTORY);
        registered.put(JavaScriptAction.MATCHER, JavaScriptAction.FACTORY);
        registered.put(OpAction.MATCHER, OpAction.FACTORY);
    }
}
