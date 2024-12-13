package com.gsqfi.fimenu.fimenu.api.commands;

import com.gsqfi.fimenu.fimenu.api.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JsonCmd extends AbstractCommandExecutor {
    protected JsonCmd(AbstractCommandExecutor superCmd) {
        super("json",superCmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            LangUtil.INSTANCE.sendPrefixMsg(sender,"tips.cmd.console.not.available");
            return false;
        }
        ////////////
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
