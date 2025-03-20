package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.HeadMatcher;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.RemoveHeadFactory;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class CommandAction implements IAction {
    private final String commandContent;

    public CommandAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Bukkit.dispatchCommand(player, PlaceholderAPI.setPlaceholders(player, commandContent));
    }

    public static final HeadMatcher MATCHER = new HeadMatcher("command: ");

    public static final RemoveHeadFactory<CommandAction> FACTORY = new RemoveHeadFactory<>("command: ", CommandAction.class);
}
