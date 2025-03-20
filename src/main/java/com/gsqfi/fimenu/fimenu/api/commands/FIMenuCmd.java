package com.gsqfi.fimenu.fimenu.api.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class FIMenuCmd extends AbstractCommandExecutor {
    public static final FIMenuCmd INSTANCE = new FIMenuCmd();

    private FIMenuCmd() {
        super("fimenu", null);
        new HelpCmd(this);
        new OpenCmd(this);
        new ReloadCmd(this);
        new JsonCmd(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        AbstractCommandExecutor commandExecutor = args.length > 0 ? this.getSubCmd().get(args[0].toLowerCase()) : HelpCmd.INSTANCE;
        (commandExecutor == null ? HelpCmd.INSTANCE : commandExecutor).onCommand(sender, cmd, label, rOa(args));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) return new ArrayList<>(this.getSubCmd().keySet());
        String lowerCase = args[0].toLowerCase();
        if (args.length == 1)
            return this.getSubCmd().keySet().stream().filter(s -> s.startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        return this.getSubCmd().get(lowerCase).onTabComplete(sender, cmd, label, rOa(args));
    }
}
