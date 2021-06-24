package me.melonboy10.swapper.menuSystem.menus;

import me.melonboy10.swapper.menuSystem.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class SchematicsListMenu extends PaginatedMenu {

    public SchematicsListMenu(Player player) {
        super(player);
    }

    @Override
    public String getMenuName() {
        return "World's Schematics";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void clickEvent(InventoryClickEvent event) {

    }

    @Override
    public void closeMenu(InventoryCloseEvent event) {

    }

    @Override
    public void setMenuItems() {
        addMenuBorder(true);
    }
}
