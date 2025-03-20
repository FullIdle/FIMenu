package com.gsqfi.fimenu.fimenu.api;

import com.gsqfi.fimenu.fimenu.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class LangUtil extends YamlConfiguration {
    public static LangUtil INSTANCE;

    public static void init(File file){
        INSTANCE = new LangUtil(file);
    }

    private LangUtil(File file) {
        Validate.notNull(file, "File cannot be null");
        try {
            this.load(file);
        } catch (FileNotFoundException ignored) {
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, e);
        }
        INSTANCE = this;
    }

    @Override
    public String getString(String path, String def) {
        Object val = this.get(path, def);
        return rp(val != null ? val.toString() : def);
    }

    private String rp(String str) {
        return str.replace('&', '§');
    }

    public void sendMsg(CommandSender sender, String path, Object... args) {
        sender.sendMessage(String.format(this.getString(path), args));
    }

    public void sendPrefixMsg(CommandSender sender, String path, Object... args) {
        sender.sendMessage(Main.INSTANCE.getDescription().getPrefix() + String.format(this.getString(path), args));
    }
}
