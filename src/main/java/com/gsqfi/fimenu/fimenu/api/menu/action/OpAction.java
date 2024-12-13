package com.gsqfi.fimenu.fimenu.api.menu.action;

import com.gsqfi.fimenu.fimenu.api.FakeOpPlayerUtil;
import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class OpAction implements Action {
    private final String commandContent;

    public OpAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Player proxy = FakeOpPlayerUtil.getINSTANCE().createProxy(player);
        Bukkit.dispatchCommand(proxy, PlaceholderAPI.setPlaceholders(proxy, commandContent));
    }
}
