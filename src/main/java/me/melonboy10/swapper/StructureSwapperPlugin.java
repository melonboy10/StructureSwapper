package me.melonboy10.swapper;

import me.melonboy10.swapper.menuSystem.MenuListener;
import me.melonboy10.swapper.schematics.Schematic;
import me.melonboy10.swapper.schematics.SchematicManager;
import me.melonboy10.swapper.swapper.BlockManager;
import me.melonboy10.swapper.swapper.SwapperBlock;
import me.melonboy10.swapper.swapper.SwapperBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class StructureSwapperPlugin extends JavaPlugin {

    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "SS" + ChatColor.GRAY + "]" + ChatColor.WHITE + " ";
    public static StructureSwapperPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

//        CommandService drink = Drink.get(this);
//        drink.register(new GetBlockCommand(), "getblock");
//        drink.registerCommands();

        registerEvents();
        loadStructures();
        loadBlocks();

        System.out.println(pluginPrefix + "Plugin is loaded!");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getInventory().addItem(BlockManager.createNewItem());
        }
    }

    @Override
    public void onDisable() {
        for (Block block : BlockManager.getBlocks()) {
            SwapperBlock swapperBlock = BlockManager.get(block);
            swapperBlock.breakEvent();
        }
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new SwapperBlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    public void loadStructures() {
        new SchematicManager(this);
        File folder = new File(Bukkit.getWorlds().get(0).getWorldFolder() + "/generated/minecraft/structures");

        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                SchematicManager.addSchematic(new Schematic(file));
            }
        }
    }

    public void loadBlocks() {
        new BlockManager(this);
    }

}
