package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CloseAction implements Action {
    @Override
    public void execute(MenuConfig.MenuHolder menuHolder, Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() != null && inv.getHolder().equals(menuHolder)) {
            player.closeInventory();
        }
    }
}
