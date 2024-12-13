package com.gsqfi.fimenu.fimenu.api.menu.action;

public class ActionHelper {
    public static Action parse(String actionText) {
        if (actionText.startsWith("command: ")) {
            return new CommandAction(actionText.substring(9));
        }
        if (actionText.startsWith("console: ")) {
            return new ConsoleAction(actionText.substring(9));
        }
        if (actionText.startsWith("op: ")) {
            return new OpAction(actionText.substring(4));
        }
        if (actionText.equalsIgnoreCase("close")) {
            return new CloseAction();
        }
        if (actionText.startsWith("js: ")){
            return new JavaScriptAction(actionText.substring(4));
        }
        return null;
    }
}
