package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;

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
        /*
           +
           | 5m
           |
           +--15m---+
          /
         / 10m
        +

         Structures:
         - dd
         - jdj


         */

        meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER, 1);


        item.setItemMeta(meta);

        System.out.println("Giving item");
        return item;
    }

    public static ItemStack createNewItem() {
        return createNewItem(new SwapperData("New Structure Swapper", new BoundingBox(0, 0, 0, 0, 0, 0)));
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
}
