package me.melonboy10.swapper.menuSystem.menus;

import me.melonboy10.swapper.StructureSwapperPlugin;
import me.melonboy10.swapper.menuSystem.Menu;
import me.melonboy10.swapper.schematics.Schematic;
import me.melonboy10.swapper.schematics.SchematicManager;
import me.melonboy10.swapper.swapper.BlockManager;
import me.melonboy10.swapper.swapper.SwapperBlock;
import me.melonboy10.swapper.swapper.SwapperData;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwapperMenu extends Menu {

    protected SwapperBlock block;

    static HashMap<DyeColor, Material> colorToDye = new HashMap<DyeColor, Material>() {{
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
            case 30: case 31: case 32: case 33: case 34: case 39: case 40: case 41: case 42:
                if (!item.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) {
                    if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(StructureSwapperPlugin.plugin, "file-name"), PersistentDataType.STRING)) {
                        Schematic schematic = SchematicManager.getSchematic(item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(StructureSwapperPlugin.plugin, "file-name"), PersistentDataType.STRING));
                        switch (event.getClick()) {
                            case LEFT: case MIDDLE:
                                if (block.data.getSchematic() != schematic) {
                                    AtomicBoolean foundMatch = new AtomicBoolean(false);
                                    BlockManager.getBlocks().stream().map(BlockManager::get).filter(swapperBlock -> !swapperBlock.equals(block)).forEach(swapperBlock -> {
                                        if (!foundMatch.get() && swapperBlock.data.getSchematic() != schematic) {
                                            foundMatch.set(true);
                                        }
                                    });
                                    if (!foundMatch.get()) {
                                        if (block.data.getAbsoluteBoundingBox().contains(schematic.getBounds())) {
                                            block.changeSchematic(schematic);
                                        } else {
                                            new ChangeBoundsSizeMenu(player, this, block, schematic).open();
                                        }
                                    }
                                }
                                break;
                            case RIGHT:
                                if (block.data.getPinnedSchematics().contains(schematic)) {
                                    block.data.getPinnedSchematics().remove(schematic);
                                } else {
                                    block.data.getPinnedSchematics().add(schematic);
                                }
                                break;
                            case SHIFT_RIGHT:
                                break;
                            case SHIFT_LEFT:
                                break;
                        }
                        setMenuItems();
                    }
                }
                break;
            case 10:
                System.out.println("paper");
                break;
            case 11:
                block.toggleColor(event.getClick().isRightClick());
                setMenuItems();
                break;
            case 12:
                block.toggleBoundingBox();
                setMenuItems();
                break;
            case 13:
                block.toggleControls();
                setMenuItems();
                break;
            case 14:
                block.togglePlaceMode();
                setMenuItems();
                break;
            case 16:
                block.toggleAdvancedDisplay();
                setMenuItems();
                break;
            case 37:
                switch (event.getClick()) {
                    case LEFT: case SHIFT_LEFT: case MIDDLE:
                        if (block.data.getSwapperSchematic() != null) {
                            block.data.getSwapperSchematic().save();
                        }
                        break;
                    case RIGHT: case SHIFT_RIGHT:

                        break;
                }
                break;
            case 49:
                player.closeInventory();
                break;
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

        inventory.setItem(12, makeItem(Material.GLASS, ChatColor.YELLOW + "Toggle Bounding Box",
                ChatColor.DARK_GRAY + "Displays Particles",
                "",
                ChatColor.AQUA + "Current Display Mode:",
                ChatColor.GRAY + " - " + (block.data.getDisplayMode() == SwapperData.BoundingDisplayMode.NONE ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "None",
                ChatColor.GRAY + " - " + (block.data.getDisplayMode() == SwapperData.BoundingDisplayMode.CORNERS ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Corners",
                ChatColor.GRAY + " - " + (block.data.getDisplayMode() == SwapperData.BoundingDisplayMode.DOTTED ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Dotted",
                ChatColor.GRAY + " - " + (block.data.getDisplayMode() == SwapperData.BoundingDisplayMode.SOLID ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Solid",
                "",
                ChatColor.YELLOW + "Click to toggle!"
        ));

        inventory.setItem(13, makeItem(Material.SCAFFOLDING, ChatColor.YELLOW + "Show Bounding Box Controls",
                ChatColor.DARK_GRAY + "Adds clickable controls",
                "",
                ChatColor.AQUA + "Current displaying?:",
                ChatColor.GRAY + "" + block.controlsVisible,
                "",
                ChatColor.YELLOW + "Click to toggle!"
        ));

        inventory.setItem(14, makeItem(Material.SHULKER_SHELL, ChatColor.YELLOW + "Toggle Placement Mode",
                ChatColor.DARK_GRAY + "Placement location",
                "",
                ChatColor.AQUA + "Current Display Mode:",
                ChatColor.GRAY + " - " + (block.data.getPlaceMode().equals(SwapperData.StructurePlaceMode.NEGATIVE_CORNER) ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Negative-Corner (North-West)",
                ChatColor.GRAY + " - " + (block.data.getPlaceMode() == SwapperData.StructurePlaceMode.CENTER ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Centered",
                ChatColor.GRAY + " - " + (block.data.getPlaceMode() == SwapperData.StructurePlaceMode.POSITIVE_CORNER ? ChatColor.DARK_AQUA : ChatColor.GRAY) + "Positive-Corner (South-East)",
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

        inventory.setItem(28, block.data.getSchematic() != null ? block.data.getSchematic().getItem(false, true) :
                makeItem(Material.PAPER,
                        ChatColor.YELLOW + "No Current Schematic",
                        ChatColor.DARK_GRAY + "Schematic",
                        "",
                        ChatColor.YELLOW + "Select a schematic on the right",
                        ChatColor.YELLOW + "or Click below to save a new one!"
                ));

        inventory.setItem(37, makeItem(Material.WRITABLE_BOOK, ChatColor.YELLOW + "Save Schematic",
                ChatColor.DARK_GRAY + "Write to File",
                "",
                ChatColor.AQUA + "Currently saved as:",
                ChatColor.GRAY + (block.data.getSchematic() != null ? block.data.getSchematic().getPathName() : "No schematic is selected!"),
                "",
                ChatColor.YELLOW + "Click to save!",
                ChatColor.YELLOW + "Right-Click to save as new Schematic!"
        ));

        /*
         * Schematic listings
         */

        int pinnedSize = block.data.getPinnedSchematics().size();
        Set<Schematic> tempSchematics = new LinkedHashSet<>();
        tempSchematics.addAll(block.data.getPinnedSchematics());
        tempSchematics.addAll(block.data.getRecentSchematics());
        tempSchematics.addAll(SchematicManager.getStructures().values());

        ArrayList<Schematic> schematics = new ArrayList<>(tempSchematics);

        int size = schematics.size() - 1;
        for (int i = 0; i < 9; i++) {
            int slot = 30 + (i >= 5 ? i + 4 : i);
            if (i > size) {
                inventory.setItem(slot, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
            } else {
                Schematic schematic = schematics.get(i);
                inventory.setItem(slot, schematic.getItem(i < pinnedSize, false));
            }
        }



        setFillerGlass();
    }
}
