package com.gsqfi.fimenu.fimenu.api.commands;

import com.gsqfi.fimenu.fimenu.api.LangUtil;
import de.tr7zw.nbtapi.NBT;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        String nbtString = NBT.itemStackToNBT(item).toString();

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(LangUtil.INSTANCE.getString("tips.cmd.json.hover")).create());
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, nbtString);

        BaseComponent[] components = new ComponentBuilder(item.getType().name())
                .color(ChatColor.AQUA)
                .event(hoverEvent)
                .event(clickEvent)
                .create();
        player.spigot().sendMessage(components);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
