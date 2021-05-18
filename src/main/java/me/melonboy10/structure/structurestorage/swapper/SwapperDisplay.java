package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class SwapperDisplay {

    ArmorStand name;

    public SwapperDisplay(ArmorStand name) {
        this.name = name;
    }

    public void remove() {
        name.remove();
    }

    public void show(Location location) {
        name = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        name.setVisible(false);
        name.setGravity(false);
        name.setMarker(true);
        name.setCustomNameVisible(true);
    }

    public void setData(SwapperData data) {
        name.setCustomName(data.name);
    }
}
