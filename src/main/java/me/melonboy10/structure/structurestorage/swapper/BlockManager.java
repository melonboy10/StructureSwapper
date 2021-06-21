package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BlockManager {

    private static final HashMap<Block, SwapperBlock> blocks = new HashMap<Block, SwapperBlock>();
    private static StructureStorage plugin;

    public BlockManager(StructureStorage plugin) {
        BlockManager.plugin = plugin;
    }

    public static ItemStack createNewItem(SwapperData swapperData) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Structure Swapper");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Storage Block");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GRAY + "   +");
        lore.add(ChatColor.DARK_GRAY + "   | " +
                ChatColor.YELLOW +
                (swapperData.getBoundingBox().getMaxY() - swapperData.getBoundingBox().getMinY()) +
                ChatColor.GOLD + "m");
        lore.add(ChatColor.DARK_GRAY + "   |");
        lore.add(ChatColor.GRAY + "   +" + ChatColor.DARK_GRAY +
                "--" + ChatColor.YELLOW +
                (swapperData.getBoundingBox().getMaxX() - swapperData.getBoundingBox().getMinX()) +
                ChatColor.GOLD +
                "m" + ChatColor.DARK_GRAY +
                "--" + ChatColor.GRAY + "+");
        lore.add(ChatColor.DARK_GRAY + "  /");
        lore.add(ChatColor.DARK_GRAY + " / " +
                ChatColor.YELLOW +
                (swapperData.getBoundingBox().getMaxZ() - swapperData.getBoundingBox().getMinZ()) +
                ChatColor.GOLD + "m");
        lore.add(ChatColor.GRAY + "+");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.YELLOW + "Schematics:");
        lore.add(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Schem1");
        lore.add(ChatColor.DARK_GRAY + " - " + ChatColor.AQUA + "Schem2");
        lore.add(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Schem3");
        lore.add(ChatColor.DARK_GRAY + " - " + ChatColor.GRAY + "Schem4");

        meta.setLore(lore);

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "name"), PersistentDataType.STRING, swapperData.getName());
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bounding_box"), PersistentDataType.INTEGER_ARRAY, intParseBoundingBox(swapperData.getRelativeBoundingBox()));
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "color"), PersistentDataType.STRING, swapperData.getColor().toString());
        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "display_mode"), PersistentDataType.STRING, swapperData.getCurrentDisplayMode().toString());

        item.setItemMeta(meta);

        return item;
    }

    public static SwapperData getDataFromItem(ItemStack itemStack, Location location) {
        PersistentDataContainer meta = itemStack.getItemMeta().getPersistentDataContainer();
        String name = meta.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
        int[] bb = meta.get(new NamespacedKey(plugin, "bounding_box"), PersistentDataType.INTEGER_ARRAY);
        String color = meta.get(new NamespacedKey(plugin, "color"), PersistentDataType.STRING);
        String displayMode = meta.get(new NamespacedKey(plugin, "display_mode"), PersistentDataType.STRING);

        return new SwapperData(name, location.toVector(), new BoundingBox(bb[0], bb[1], bb[2], bb[3], bb[4], bb[5]), DyeColor.valueOf(color), SwapperData.BoundingDisplayMode.valueOf(displayMode));
    }

    private static int[] intParseBoundingBox(BoundingBox boundingBox) {
        return new int[]{(int) boundingBox.getMinX(),(int) boundingBox.getMinY(),(int) boundingBox.getMinZ(),(int) boundingBox.getMaxX(),(int) boundingBox.getMaxY(),(int) boundingBox.getMaxZ()};
    }

    public static ItemStack createNewItem() {
        return createNewItem(new SwapperData("New Structure Swapper", new Vector(0, 0, 0), new BoundingBox(1, 2, 1, 0, 1, 0), DyeColor.GREEN, SwapperData.BoundingDisplayMode.NONE));
    }

    public static SwapperBlock get(Block block) {
        return blocks.getOrDefault(block, null);
    }

    public static void add(Block block, SwapperBlock swapperBlock) {
        blocks.put(block, swapperBlock);
    }

    public static void remove(Block block) {
        blocks.remove(block);
    }

    public static Set<Block> getBlocks() {
        return blocks.keySet();
    }
}
