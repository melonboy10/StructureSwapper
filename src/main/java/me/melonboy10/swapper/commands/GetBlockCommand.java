package me.melonboy10.swapper.commands;

import com.jonahseguin.drink.annotation.Command;
import com.jonahseguin.drink.annotation.Sender;
import me.melonboy10.swapper.swapper.BlockManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetBlockCommand {

    @Command(name = "", desc = "Get the structure storage.")
    public void root(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            ((Player) sender).getInventory().addItem(BlockManager.createNewItem());
        }
    }

}
