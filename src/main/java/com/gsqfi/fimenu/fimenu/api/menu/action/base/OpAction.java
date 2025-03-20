package com.gsqfi.fimenu.fimenu.api.menu.action.base;

import com.gsqfi.fimenu.fimenu.api.FakeOpPlayerUtil;
import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.HeadMatcher;
import com.gsqfi.fimenu.fimenu.api.menu.action.IAction;
import com.gsqfi.fimenu.fimenu.api.menu.action.RemoveHeadFactory;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class OpAction implements IAction {
    private final String commandContent;

    public OpAction(String commandContent) {
        this.commandContent = commandContent;
    }

    @Override
    public void execute(MenuConfig.MenuHolder holder, Player player) {
        Player proxy = FakeOpPlayerUtil.getINSTANCE().createProxy(player);
        Bukkit.dispatchCommand(proxy, PlaceholderAPI.setPlaceholders(proxy, commandContent));
    }

    public static final HeadMatcher MATCHER = new HeadMatcher("op: ");

    public static final RemoveHeadFactory<OpAction> FACTORY = new RemoveHeadFactory<>("op: ", OpAction.class);
}
