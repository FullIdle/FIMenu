package com.gsqfi.fimenu.fimenu.api.commands;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractCommandExecutor implements TabExecutor {
    private final String name;
    private final AbstractCommandExecutor superCmd;
    private final Map<String, AbstractCommandExecutor> subCmd = new HashMap<>();

    protected AbstractCommandExecutor(String name, AbstractCommandExecutor superCmd) {
        this.name = name;
        this.superCmd = superCmd;
        if (this.superCmd != null) {
            this.superCmd.subCmd.put(name, this);
        }
    }

    public String[] rOa(String[] args) {
        ArrayList<String> list = Lists.newArrayList(args);
        if (list.isEmpty()) return args;
        list.remove(0);
        if (list.isEmpty()) return list.toArray(new String[0]);
        if (list.get(0).isEmpty()) list.remove(0);
        return list.toArray(new String[0]);
    }
}
