package me.melonboy10.structure.structurestorage;

import com.jonahseguin.drink.CommandService;
import com.jonahseguin.drink.Drink;
import me.melonboy10.structure.structurestorage.commands.GetBlockCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class StructureStorage extends JavaPlugin {

    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.BLUE + "SS" + ChatColor.GRAY + "]" + ChatColor.WHITE + " ";

    @Override
    public void onEnable() {

        CommandService drink = Drink.get(this);
        drink.register(new GetBlockCommand(), "getblock");
        drink.registerCommands();

        this.saveDefaultConfig();

        System.out.println(pluginPrefix + "Plugin is loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
