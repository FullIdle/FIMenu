package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.IFactory;
import com.gsqfi.fimenu.fimenu.api.menu.action.IMatcher;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CloseAction implements IAction {
    @Override
    public void execute(MenuConfig.MenuHolder menuHolder, Player player) {
        Inventory inv = player.getOpenInventory().getTopInventory();
        if (inv.getHolder() != null && inv.getHolder().equals(menuHolder)) {
            player.closeInventory();
        }
    }

    public static final IMatcher MATCHER = desc -> desc.equalsIgnoreCase("close");

    public static final IFactory<CloseAction> FACTORY = desc -> new CloseAction();
}
