package me.melonboy10.swapper.menuSystem.menus;

import me.melonboy10.swapper.menuSystem.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class StructureEditMenu extends Menu {

    public StructureEditMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return null;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {

    }
}
