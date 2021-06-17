package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SwapperMenu implements InventoryHolder {

    protected Player player;
    protected Inventory inventory;
    protected ItemStack fillerGlass = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");
    protected SwapperBlock block;

    public SwapperMenu(Player player, SwapperBlock swapperBlock) {
        this.player = player;
        this.block = swapperBlock;
    }

    public void clickEvent(InventoryClickEvent event) {

        ItemStack item = event.getCurrentItem();
        switch (item.getType()) {
            case PAPER:
                System.out.println("paper");
                break;
            case GLASS:
                block.toggleBoundingBox();
                setMenuItems();
                break;
            case SCAFFOLDING:
                block.toggleControls();
                setMenuItems();
                break;
            case OBSERVER:
                block.toggleAdvancedDisplay();
                setMenuItems();
                break;
            case BARRIER:
                event.getWhoClicked().closeInventory();
                break;
        }
    }

    public void setMenuItems() {
        inventory.setItem(22, makeItem(Material.BARRIER, ChatColor.RED + "Close"));

        inventory.setItem(10, makeItem(Material.PAPER, ChatColor.YELLOW + "Change Name",
                ChatColor.DARK_GRAY + "Becomes item name",
                "",
                ChatColor.AQUA + "Current Name:",
                ChatColor.GRAY + block.getData().getName(),
                "",
                ChatColor.YELLOW + "Click to change name!"));

        inventory.setItem(12, makeItem(Material.GLASS, ChatColor.YELLOW + "Toggle Bounding Box",
                ChatColor.DARK_GRAY + "Displays Particles",
                "",
                ChatColor.AQUA + "Current Display Mode:",
                ChatColor.GRAY + " - " + (block.currentDisplayMode == SwapperBlock.BoundingDisplayMode.NONE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "None",
                ChatColor.GRAY + " - " + (block.currentDisplayMode == SwapperBlock.BoundingDisplayMode.CORNERS ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Corners",
                ChatColor.GRAY + " - " + (block.currentDisplayMode == SwapperBlock.BoundingDisplayMode.DOTTED ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Dotted",
                ChatColor.GRAY + " - " + (block.currentDisplayMode == SwapperBlock.BoundingDisplayMode.SOLID ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Solid",
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
