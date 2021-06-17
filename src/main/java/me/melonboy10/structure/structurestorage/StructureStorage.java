package me.melonboy10.structure.structurestorage;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.structure.structurestorage.commands.GetBlockCommand;
import me.melonboy10.structure.structurestorage.swapper.BlockManager;
import me.melonboy10.structure.structurestorage.swapper.SwapperBlockListener;
import me.melonboy10.structure.structurestorage.swapper.SwapperMenu;
import me.melonboy10.structure.structurestorage.swapper.SwapperMenuListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class StructureStorage extends JavaPlugin {

    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "SS" + ChatColor.GRAY + "]" + ChatColor.WHITE + " ";

    @Override
    public void onEnable() {

        CommandService drink = Drink.get(this);
        drink.register(new GetBlockCommand(), "getblock");
        drink.registerCommands();

        registerEvents();
        new BlockManager(this);

        System.out.println(pluginPrefix + "Plugin is loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new SwapperBlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new SwapperMenuListener(), this);
    }
}
