package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import org.bukkit.entity.Player;

public interface IAction {
    void execute(MenuConfig.MenuHolder menuHolder, Player player);
}
