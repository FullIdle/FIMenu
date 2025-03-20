package com.gsqfi.fimenu.fimenu;

import com.gsqfi.fimenu.fimenu.api.FakeOpPlayerUtil;
import com.gsqfi.fimenu.fimenu.api.LangUtil;
import com.gsqfi.fimenu.fimenu.api.commands.FIMenuCmd;
import com.gsqfi.fimenu.fimenu.api.menu.MenuConfig;
import com.gsqfi.fimenu.fimenu.api.menu.action.base.JavaScriptAction;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main extends JavaPlugin {
    public static final Map<String, MenuConfig> cache = new HashMap<>();
    public static Main INSTANCE;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        FakeOpPlayerUtil.getINSTANCE();
    }

    @Override
    public void onEnable() {
        reloadConfig();
        PluginCommand command = getCommand("fimenu");
        assert command != null;
        command.setExecutor(FIMenuCmd.INSTANCE);
        command.setTabCompleter(FIMenuCmd.INSTANCE);
        LangUtil.INSTANCE.sendPrefixMsg(Bukkit.getConsoleSender(), "info.plugin.enable");
    }

    @Override
    public void onDisable() {
    }

    @SneakyThrows
    @Override
    public void reloadConfig() {
        this.saveDefaultConfig();
        super.reloadConfig();

        FileConfiguration config = this.getConfig();

        {
            String langTag = config.getString("language");
            File folder = new File(this.getDataFolder(), "lang");
            if (!folder.exists() && folder.mkdirs()){
                JarFile jarFile = new JarFile(this.getFile());
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith("lang/")) {
                        String[] split = name.split("/");
                        if (split.length > 1) {
                            String fileName = split[1];
                            InputStream stream = this.getClass().getResourceAsStream("/lang/" + fileName);
                            Path path = new File(folder, fileName).toPath();
                            assert stream != null;
                            Files.copy(stream, path);
                        }
                    }
                }
                jarFile.close();
            }
            LangUtil.init(new File(folder, langTag + ".yml"));
        }

        {
            JavaScriptAction.init(config.getString("javascript-engine"));
        }

        {
            File folder = new File(this.getDataFolder(), "menus");
            if (!folder.exists()) {
                if (folder.mkdirs()) {
                    InputStream stream = this.getClass().getResourceAsStream("/menus/example.yml");
                    Path path = new File(folder, "example.yml").toPath();
                    assert stream != null;
                    Files.copy(stream, path);
                }
            }
            cache.clear();
            for (MenuConfig menuConfig : parseMenuConfig(folder))
                cache.put(menuConfig.getFile().getName(), menuConfig);
            LangUtil.INSTANCE.sendPrefixMsg(Bukkit.getConsoleSender(), "info.menu.load", cache.size());
        }
    }

    private static List<MenuConfig> parseMenuConfig(File file) {
        ArrayList<MenuConfig> list = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) list.addAll(parseMenuConfig(f));
            }
            return list;
        }
        list.add(new MenuConfig(file));
        return list;
    }
}