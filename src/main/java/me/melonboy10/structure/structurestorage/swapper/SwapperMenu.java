package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class SwapperMenu implements InventoryHolder {

    protected Player player;
    protected Inventory inventory;
    protected ItemStack fillerGlass = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    protected SwapperBlock block;

    static HashMap<DyeColor, Material> colorToDye = new HashMap<>() {{
       put(DyeColor.RED,        Material.RED_DYE);
       put(DyeColor.ORANGE,     Material.ORANGE_DYE);
       put(DyeColor.YELLOW,     Material.YELLOW_DYE);
       put(DyeColor.LIME,       Material.LIME_DYE);
       put(DyeColor.GREEN,      Material.GREEN_DYE);
       put(DyeColor.LIGHT_BLUE, Material.LIGHT_BLUE_DYE);
       put(DyeColor.BLUE,       Material.BLUE_DYE);
       put(DyeColor.CYAN,       Material.CYAN_DYE);
       put(DyeColor.PURPLE,     Material.PURPLE_DYE);
       put(DyeColor.MAGENTA,    Material.MAGENTA_DYE);
       put(DyeColor.PINK,       Material.PINK_DYE);
       put(DyeColor.WHITE,      Material.WHITE_DYE);
       put(DyeColor.GRAY,       Material.GRAY_DYE);
       put(DyeColor.LIGHT_GRAY, Material.LIGHT_GRAY_DYE);
       put(DyeColor.BLACK,      Material.BLACK_DYE);
       put(DyeColor.BROWN,      Material.NAUTILUS_SHELL);
    }};

    public SwapperMenu(Player player, SwapperBlock swapperBlock) {
        this.player = player;
        this.block = swapperBlock;
    }

    public void clickEvent(InventoryClickEvent event) {

        ItemStack item = event.getCurrentItem();
        switch (item.getType()) {
            case PAPER -> System.out.println("paper");
            case RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, LIGHT_BLUE_DYE, BLUE_DYE, CYAN_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE, WHITE_DYE, GRAY_DYE, LIGHT_GRAY_DYE, BLACK_DYE, NAUTILUS_SHELL -> {
                if (event.getClick().isRightClick()) {
                    block.toggleColor(true);
                } else {
                    block.toggleColor(false);
                }
                setMenuItems();
            }
            case GLASS -> {
                block.toggleBoundingBox();
                setMenuItems();
            }
            case SCAFFOLDING -> {
                block.toggleControls();
                setMenuItems();
            }
            case OBSERVER -> {
                block.toggleAdvancedDisplay();
                setMenuItems();
            }
            case BARRIER -> event.getWhoClicked().closeInventory();
        }
    }

    public void setMenuItems() {
        inventory.setItem(22, makeItem(Material.BARRIER, ChatColor.RED + "Close"));

        inventory.setItem(10, makeItem(Material.PAPER, ChatColor.YELLOW + "Change Name",
                ChatColor.DARK_GRAY + "Becomes item name",
                "",
                ChatColor.AQUA + "Current Name:",
                ChatColor.GRAY + block.data.getName(),
                "",
                ChatColor.YELLOW + "Click to change name!"));

        DyeColor color = block.data.getColor();
        inventory.setItem(11, makeItem(colorToDye.get(color), ChatColor.YELLOW + "Change Color",
                ChatColor.DARK_GRAY + "In 16 brand new colors!",
                "",
                ChatColor.AQUA + "Current Color:",
                ChatColor.GRAY + " - " + (color == DyeColor.RED ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Red",
                ChatColor.GRAY + " - " + (color == DyeColor.ORANGE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Orange",
                ChatColor.GRAY + " - " + (color == DyeColor.YELLOW ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Yellow",
                ChatColor.GRAY + " - " + (color == DyeColor.LIME ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Lime",
                ChatColor.GRAY + " - " + (color == DyeColor.GREEN ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Green",
                ChatColor.GRAY + " - " + (color == DyeColor.CYAN ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Cyan",
                ChatColor.GRAY + " - " + (color == DyeColor.LIGHT_BLUE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Light Blue",
                ChatColor.GRAY + " - " + (color == DyeColor.BLUE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Blue",
                ChatColor.GRAY + " - " + (color == DyeColor.PURPLE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Purple",
                ChatColor.GRAY + " - " + (color == DyeColor.MAGENTA ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Magenta",
                ChatColor.GRAY + " - " + (color == DyeColor.PINK ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Pink",
                ChatColor.GRAY + " - " + (color == DyeColor.WHITE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "White",
                ChatColor.GRAY + " - " + (color == DyeColor.LIGHT_GRAY ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Light Gray",
                ChatColor.GRAY + " - " + (color == DyeColor.GRAY ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Gray",
                ChatColor.GRAY + " - " + (color == DyeColor.BLACK ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Black",
                ChatColor.GRAY + " - " + (color == DyeColor.BROWN ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Rainbow",
                "",
                ChatColor.YELLOW + "Click to toggle!"
                ));

        inventory.setItem(13, makeItem(Material.GLASS, ChatColor.YELLOW + "Toggle Bounding Box",
                ChatColor.DARK_GRAY + "Displays Particles",
                "",
                ChatColor.AQUA + "Current Display Mode:",
                ChatColor.GRAY + " - " + (block.data.getCurrentDisplayMode() == SwapperData.BoundingDisplayMode.NONE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "None",
                ChatColor.GRAY + " - " + (block.data.getCurrentDisplayMode() == SwapperData.BoundingDisplayMode.CORNERS ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Corners",
                ChatColor.GRAY + " - " + (block.data.getCurrentDisplayMode() == SwapperData.BoundingDisplayMode.DOTTED ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Dotted",
                ChatColor.GRAY + " - " + (block.data.getCurrentDisplayMode() == SwapperData.BoundingDisplayMode.SOLID ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Solid",
                "",
                ChatColor.YELLOW + "Click to toggle!"
                ));

        inventory.setItem(14, makeItem(Material.SCAFFOLDING, ChatColor.YELLOW + "Show Bounding Box Controls",
                ChatColor.DARK_GRAY + "Adds clickable controls",
                "",
                ChatColor.AQUA + "Current displaying?:",
                ChatColor.GRAY + "" + block.controlsVisible,
                "",
                ChatColor.YELLOW + "Click to toggle!"
                ));

        inventory.setItem(16, makeItem(Material.OBSERVER, ChatColor.YELLOW + "Advanced Display Mode",
                ChatColor.DARK_GRAY + "Shows more detail",
                "",
                ChatColor.AQUA + "Visible?:",
                ChatColor.GRAY + "" + block.advancedDisplay,
                "",
                ChatColor.YELLOW + "Click to toggle!"
                ));

        setFillerGlass();
    }

    public void open() {
        inventory = Bukkit.createInventory(this, 27, "Swapper Block Settings");
        this.setMenuItems();
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass() {
        for (int i = 0; i < 27; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, fillerGlass);
            }
        }
    }

    public static ItemStack makeItem(Material material, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack makeItem(Material material, int amount, String name, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);

        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        item.setAmount(amount);

        return item;
    }

    public static ItemStack makeItem(Material material, String name, int amount) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + name);

        item.setItemMeta(itemMeta);

        item.setAmount(amount);

        return item;
    }
}
