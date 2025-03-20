package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.HeadMatcher;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.RemoveHeadFactory;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleAction implements IAction {
    private final String commandContent;

    public ConsoleAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, this.commandContent));
    }

    public static final HeadMatcher MATCHER = new HeadMatcher("console: ");

    public static final RemoveHeadFactory<ConsoleAction> FACTORY = new RemoveHeadFactory<>("console: ", ConsoleAction.class);
}
