package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class SwapperMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof SwapperMenu) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }

            SwapperMenu menu = (SwapperMenu) holder;
            menu.clickEvent(event);
        }
    }

}
