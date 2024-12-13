package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class CommandAction implements Action {
    private final String commandContent;

    public CommandAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Bukkit.dispatchCommand(player, PlaceholderAPI.setPlaceholders(player, commandContent));
    }
}
