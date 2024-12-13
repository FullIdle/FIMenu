package com.gsqfi.fimenu.fimenu.api.commands;

import com.gsqfi.fimenu.fimenu.Main;
import com.gsqfi.fimenu.fimenu.api.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCmd extends AbstractCommandExecutor {
    protected ReloadCmd(AbstractCommandExecutor superCmd) {
        super("reload", superCmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Main.INSTANCE.reloadConfig();
        LangUtil.INSTANCE.sendPrefixMsg(sender,"tips.cmd.reload");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
