package com.gsqfi.fimenu.fimenu.api.commands;

import com.gsqfi.fimenu.fimenu.Main;
import com.gsqfi.fimenu.fimenu.api.LangUtil;
import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenCmd extends AbstractCommandExecutor {
    protected OpenCmd(AbstractCommandExecutor superCmd) {
        super("open", superCmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            LangUtil.INSTANCE.sendPrefixMsg(sender, "tips.cmd.missing.args");
            return false;
        }
        MenuConfig menuConfig = Main.cache.get(args[0]);
        if (menuConfig == null) {
            LangUtil.INSTANCE.sendPrefixMsg(sender, "tips.cmd.error.args", args[0]);
            return false;
        }
        Player player = null;
        if (args.length > 1) {
            player = Bukkit.getPlayer(args[1]);
        }
        if (player == null) {
            if (!(sender instanceof Player)) {
                LangUtil.INSTANCE.sendPrefixMsg(sender, "tips.cmd.console.not.available", menuConfig.getFile().getName());
                return false;
            }
            player = (Player) sender;
        }
        menuConfig.newMenuHolder(player).open();
        LangUtil.INSTANCE.sendPrefixMsg(sender, "tips.cmd.menu.open", player.getName(), menuConfig.getFile().getName());
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) return new ArrayList<>(Main.cache.keySet());
        if (args.length < 2)
            return Main.cache.keySet().stream().filter(key -> key.startsWith(args[0])).collect(Collectors.toList());
        return null;
    }
}
