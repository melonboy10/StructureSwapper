package me.melonboy10.swapper;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.swapper.commands.GetBlockCommand;
import me.melonboy10.swapper.menuSystem.MenuListener;
import me.melonboy10.swapper.structures.StructureManager;
import me.melonboy10.swapper.swapper.BlockManager;
import me.melonboy10.swapper.swapper.SwapperBlock;
import me.melonboy10.swapper.swapper.SwapperBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class StructureSwapperPlugin extends JavaPlugin {

    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "SS" + ChatColor.GRAY + "]" + ChatColor.WHITE + " ";

    @Override
    public void onEnable() {

        CommandService drink = Drink.get(this);
        drink.register(new GetBlockCommand(), "getblock");
        drink.registerCommands();

        registerEvents();
        loadStructures();
        loadBlocks();

        System.out.println(pluginPrefix + "Plugin is loaded!");
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
        new StructureManager(this);
        File folder = new File(Bukkit.getWorlds().get(0).getWorldFolder() + "/generated/minecraft/structures");
        StructureManager.loadStructures(Arrays.asList(folder.listFiles()));
    }

    public void loadBlocks() {
        new BlockManager(this);
    }

}
