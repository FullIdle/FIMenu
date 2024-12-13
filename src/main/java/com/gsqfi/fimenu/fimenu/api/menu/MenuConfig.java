package com.gsqfi.fimenu.fimenu.api.menu;

import com.gsqfi.fimenu.fimenu.api.menu.action.Action;
import com.gsqfi.fimenu.fimenu.api.menu.action.ActionHelper;
import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import lombok.SneakyThrows;
import me.fullidle.ficore.ficore.common.SomeMethod;
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class MenuConfig {
    private static final Method getMaterialId;

    private final File file;
    private final YamlConfiguration config;

    public MenuConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 将MenuConfig的配置的菜单布局刻印到一个Inventory上。注意!!这只是刻印不会有处理程序
     *
     * @param holder 需要顺带刻印进到Inventory的框架holder对象
     */
    public Inventory engraveAnInv(InventoryHolder holder) {
        List<String> layout = color(config.getStringList("layout"));
        Inventory inv = Bukkit.createInventory(holder, layout.size() * 9, color(config.getString("title")));
        for (int y = 0; y < layout.size(); y++) {
            char[] chars = layout.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                inv.setItem((y * 9) + x, getConfigItemStack(chars[x]));
            }
        }
        return inv;
    }

    @SneakyThrows
    public ItemStack getConfigItemStack(char buttonId) {
        ConfigurationSection section = this.config.getConfigurationSection("button." + buttonId);
        String materialData = section.getString("material");
        ItemStack itemStack;

        if (materialData.endsWith("{")) {
            try {
                itemStack = NBT.itemStackFromNBT(NBT.parseNBT(materialData));
                if (itemStack == null) {
                    itemStack = new ItemStack(Material.STONE);
                }
            } catch (Exception e) {
                itemStack = new ItemStack(Material.STONE);
            }
        } else {
            String[] split = materialData.split(":");
            Material material;
            if (isDigits(split[0])) {
                if (getMaterialId == null)
                    throw new RuntimeException("Your server version does not support material with numeric IDs!");
                material = (Material) getMaterialId.invoke(null, Integer.parseInt(split[0]));
            } else {
                material = Material.getMaterial(split[0]);
            }
            itemStack = new ItemStack(material == null ? Material.STONE : material);
            if (split.length > 1) try {
                itemStack.setDurability(Short.parseShort(split[1]));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Your server version does not support material with numeric IDs!",e);
            }
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(color(section.getString("name")));
        itemMeta.setLore(color(section.getStringList("lore")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public MenuHolder newMenuHolder(Player player) {
        return new MenuHolder(this, player);
    }

    public static boolean isDigits(String str) {
        if (str == null || str.replace(" ", "").isEmpty()) {
            return false;
        } else {
            for (int i = 0; i < str.length(); ++i) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String color(String str) {
        return str.replace('&', '§');
    }

    public static List<String> color(Collection<String> collection) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : collection) {
            list.add(color(s));
        }
        return list;
    }

    @Getter
    public static class MenuHolder extends ListenerInvHolder {
        private static final Consumer<InventoryClickEvent> clickEventHandler;
        private static final Consumer<InventoryDragEvent> dragEventHandler;
        private static final Consumer<InventoryOpenEvent> openEventHandler;
        private static final Consumer<InventoryCloseEvent> closeEventHandler;

        /**/
        private final Player player;
        private final MenuConfig menuConfig;
        private final Inventory inventory;

        private MenuHolder(MenuConfig menuConfig, Player player) {
            this.menuConfig = menuConfig;
            this.player = player;
            this.inventory = this.menuConfig.engraveAnInv(this);

            this.onClick(clickEventHandler);
            this.onDrag(dragEventHandler);
            this.onOpen(openEventHandler);
            this.onClose(closeEventHandler);
        }


        public void open() {
            this.getPlayer().openInventory(this.inventory);
        }

        public void executeButtonActions(char id, ClickType clickType, MenuHolder menuHolder) {
            for (String actionText : this.menuConfig.config.getStringList(clickType.getButtonActionsPath(id))) {
                Action action;
                if ((action = ActionHelper.parse(actionText)) == null)
                    throw new RuntimeException("gui: " + this.menuConfig.file.getPath() + "| actions: '" + actionText + "' parsing error!");
                action.execute(menuHolder, this.player);
            }
        }

        static {
            clickEventHandler = event -> {
                event.setCancelled(true);
                Inventory inv = event.getClickedInventory();
                if (inv == null || inv instanceof PlayerInventory) return;
                MenuHolder holder = (MenuHolder) inv.getHolder();
                assert holder != null;
                YamlConfiguration config = holder.menuConfig.config;
                int slot = event.getSlot();
                char id = config.getStringList("layout").get(slot / 9).charAt(slot % 9);
                if (event.isLeftClick()) {
                    holder.executeButtonActions(id, ClickType.LEFT, holder);
                }
                if (event.isRightClick()) {
                    holder.executeButtonActions(id, ClickType.RIGHT, holder);
                }
                holder.executeButtonActions(id, ClickType.ALL, holder);
            };

            dragEventHandler = event -> {
                event.setCancelled(true);
            };

            openEventHandler = event -> {
            };

            closeEventHandler = event -> {
            };
        }


        @Getter
        public enum ClickType {
            ALL("all"), LEFT("left"), RIGHT("right");

            private final String tailPath;

            ClickType(String tailPath) {
                this.tailPath = tailPath;
            }

            public String getButtonActionsPath(char id) {
                return "button." + id + ".actions." + this.tailPath;
            }
        }
    }

    static {
        if (Integer.parseInt(SomeMethod.getMinecraftVersion().replace(".", "")) <= 1122) {
            try {
                getMaterialId = Material.class.getDeclaredMethod("getMaterial", int.class);
                getMaterialId.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } else {
            getMaterialId = null;
        }
    }
}
