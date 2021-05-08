package me.melonboy10.structure.structurestorage.commands;

import com.jonahseguin.drink.annotation.*;
import me.melonboy10.structure.structurestorage.swapper.BlockUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetBlockCommand {

    @Command(name = "", desc = "Get the structure storage.")
    public void root(@Sender CommandSender sender) {
        if (sender instanceof Player) {
            ((Player) sender).getInventory().addItem(BlockUtils.createNewItem());
        }
    }

}
