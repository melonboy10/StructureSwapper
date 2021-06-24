package me.melonboy10.swapper.menuSystem.menus;

import me.melonboy10.swapper.menuSystem.Menu;
import me.melonboy10.swapper.swapper.SwapperBlock;
import me.melonboy10.swapper.swapper.SwapperData;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SwapperMenu extends Menu {

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
        super(player);
        this.block = swapperBlock;
    }

    @Override
    public String getMenuName() {
        return "Structure Swapper Settings";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {
        if (!block.block.getWorld().getBlockAt(block.block.getLocation()).equals(block.block)) {
            player.closeInventory();
        }

        ItemStack item = event.getCurrentItem();
        switch (event.getSlot()) {
            case 30, 31, 32, 33, 34 -> {
                if (!item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {

                }
            }
            case 10 -> System.out.println("paper");
            case 11 -> {
                block.toggleColor(event.getClick().isRightClick());
                setMenuItems();
            }
            case 13 -> {
                block.toggleBoundingBox();
                setMenuItems();
            }
            case 14 -> {
                block.toggleControls();
                setMenuItems();
            }
            case 16 -> {
                block.toggleAdvancedDisplay();
                setMenuItems();
            }
            case 49 -> player.closeInventory();
        }
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    public void setMenuItems() {

        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.RED + "Close"));

        inventory.setItem(10, makeItem(Material.NAME_TAG, ChatColor.YELLOW + "Change Name",
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

        inventory.setItem(28, makeItem(Material.PAPER, ChatColor.YELLOW + "Selected Schematic",
                ChatColor.DARK_GRAY + "Choose a schematic",
                "",
                ChatColor.AQUA + "Current Schematic:",
                ChatColor.GRAY + (block.data.getSchematic() == null ? "None" : block.data.getSchematic().toString()),
                "",
                ChatColor.GRAY + "   +",
                ChatColor.DARK_GRAY + "   | " +
                ChatColor.YELLOW +
                    (block.data.getBoundingBox().getMaxY() - block.data.getBoundingBox().getMinY()) +
                ChatColor.GOLD + "m",
                ChatColor.DARK_GRAY + "   |",
                ChatColor.GRAY + "   +" + ChatColor.DARK_GRAY +
                    "--" + ChatColor.YELLOW +
                    (block.data.getBoundingBox().getMaxX() - block.data.getBoundingBox().getMinX()) +
                    ChatColor.GOLD +
                    "m" + ChatColor.DARK_GRAY +
                    "--" + ChatColor.GRAY + "+",
                ChatColor.DARK_GRAY + "  /",
                ChatColor.DARK_GRAY + " / " +
                ChatColor.YELLOW +
                    (block.data.getBoundingBox().getMaxZ() - block.data.getBoundingBox().getMinZ()) +
                ChatColor.GOLD + "m",
                ChatColor.GRAY + "+",
                ""
                ));

        inventory.setItem(30, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        inventory.setItem(31, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        inventory.setItem(32, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        inventory.setItem(33, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        inventory.setItem(34, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));

        setFillerGlass();
    }
}
