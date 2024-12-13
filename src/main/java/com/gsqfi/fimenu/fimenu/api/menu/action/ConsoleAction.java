package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleAction implements Action {
    private final String commandContent;

    public ConsoleAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, this.commandContent));
    }
}
