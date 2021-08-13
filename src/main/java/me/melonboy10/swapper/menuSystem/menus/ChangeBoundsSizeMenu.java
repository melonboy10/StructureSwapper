package me.melonboy10.swapper.menuSystem.menus;

import me.melonboy10.swapper.menuSystem.Menu;
import me.melonboy10.swapper.schematics.Schematic;
import me.melonboy10.swapper.swapper.SwapperBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.util.BoundingBox;

public class ChangeBoundsSizeMenu extends Menu {

    protected SwapperMenu menu;
    protected SwapperBlock block;
    protected Schematic schematic;

    public ChangeBoundsSizeMenu(Player player, SwapperMenu menu, SwapperBlock block, Schematic schematic) {
        super(player);
        this.menu = menu;
        this.block = block;
        this.schematic = schematic;
    }

    @Override
    public String getMenuName() {
        return "Change Bounds Size";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            switch (event.getSlot()) {
                case 11:
                    BoundingBox newBox = block.data.getBoundingBox().clone();
                    newBox.expand(0, 0, 0, 0, schematic.getHeight() - newBox.getHeight(), 0);
                    switch (block.data.getPlaceMode()) {
                        case POSITIVE_CORNER:
                            newBox.expand(schematic.getWidth() - newBox.getWidthX(), 0, schematic.getLength() - newBox.getWidthZ(), 0, 0, 0);
                            break;
                        case CENTER:
                            int[] tempWidth = divide((int) (schematic.getWidth() - newBox.getWidthX()));
                            int[] tempLength = divide((int) (schematic.getWidth() - newBox.getWidthX()));
                            newBox.expand(tempWidth[0], 0, tempLength[0], tempWidth[1], 0, tempLength[1]);
                            break;
                        case NEGATIVE_CORNER:
                            newBox.expand(0, 0, 0, schematic.getWidth() - newBox.getWidthX(), 0, schematic.getLength() - newBox.getWidthZ());
                            break;
                    }
                    menu.block.data.setBoundingBox(newBox);
                    menu.block.changeSchematic(schematic);
                case 15:
                    player.closeInventory();
                    menu.open();
                    break;
            }
        }
    }

    private int[] divide(int number) {
        double divided = (double) number / 2;
        int dividedInt = (int) divided;
        if (divided > dividedInt) {
            return new int[]{dividedInt + 1, dividedInt};
        }
        return new int[]{dividedInt, dividedInt};
    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        inventory.setItem(11, makeItem(Material.LIME_TERRACOTTA, ChatColor.GREEN + "Change Size"));

        inventory.setItem(13, makeItem(Material.BIRCH_SIGN, ChatColor.YELLOW + "Change Size?",
                "        " + ChatColor.GRAY + "+",
                "        " + ChatColor.WHITE + "|",
                "       " + ChatColor.AQUA + "+2" + ChatColor.DARK_AQUA + "m                      " + ChatColor.YELLOW + "Info:",
                "        " + ChatColor.WHITE + "|                         " + ChatColor.GRAY + "--",
                "        " + ChatColor.GRAY + "+                        " + ChatColor.WHITE + "The Schematic you selected",
                "        " + ChatColor.DARK_GRAY + "|                         " + ChatColor.WHITE + "is too large to fit in",
                "        " + ChatColor.YELLOW + "8" + ChatColor.GOLD + "m                      " + ChatColor.WHITE + "the current bounds of the",
                "        " + ChatColor.DARK_GRAY + "|                        " + ChatColor.WHITE + "swapper block. Would you like",
                "        " + ChatColor.GRAY + "*" + ChatColor.DARK_GRAY + "--" + ChatColor.YELLOW + "8" + ChatColor.GOLD + "m" + ChatColor.DARK_GRAY + "--" + ChatColor.GRAY + "+" +
                        ChatColor.WHITE + "--" + ChatColor.AQUA + "+2" + ChatColor.DARK_AQUA + "m" + ChatColor.WHITE + "--" + ChatColor.GRAY + "+  " +
                        ChatColor.WHITE + "to scale the bounds to fit",
                "       " + ChatColor.DARK_GRAY + "/                         " + ChatColor.WHITE + "the Schematic.",
                "      " + ChatColor.YELLOW + "8" + ChatColor.GOLD + "m",
                "     " + ChatColor.DARK_GRAY + "/",
                "    " + ChatColor.GRAY + "+",
                "   " + ChatColor.DARK_GRAY + "/                       " + ChatColor.YELLOW + "--- " + ChatColor.GRAY + ": " + ChatColor.WHITE + "Current Size",
                " " + ChatColor.AQUA + "+2" + ChatColor.DARK_AQUA + "m                      " + ChatColor.AQUA + "--- " + ChatColor.GRAY + ": " + ChatColor.WHITE + "Expanded Size",
                " " + ChatColor.DARK_GRAY + "/",
                ChatColor.GRAY + "+"
                ));

        inventory.setItem(15, makeItem(Material.RED_TERRACOTTA, ChatColor.RED + "Cancel"));
    }
}
