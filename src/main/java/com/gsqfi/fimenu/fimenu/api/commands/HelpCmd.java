package com.gsqfi.fimenu.fimenu.api.commands;

import com.gsqfi.fimenu.fimenu.api.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCmd extends AbstractCommandExecutor {
    public static HelpCmd INSTANCE;

    protected HelpCmd(AbstractCommandExecutor superCmd) {
        super("help", superCmd);
        INSTANCE = this;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        LangUtil.INSTANCE.sendPrefixMsg(sender, "tips.cmd.help");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}
