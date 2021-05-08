package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockManager {

    private static List<Block> blocks = new ArrayList<>();

    public static void loadFromConfig() {

    }

    public static ItemStack createNewItem() {
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



         */



        item.setItemMeta(meta);

        return item;
    }

}
