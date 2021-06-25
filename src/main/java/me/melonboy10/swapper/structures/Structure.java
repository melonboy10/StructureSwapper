package me.melonboy10.swapper.structures;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.io.File;

public class Structure {

    private final File file;
    private Material item;

    public Structure(File file) {
        this.file = file;
        this.item = Material.GRASS_BLOCK;
    }

    public Material getItem() {
        return item;
    }
}
