package me.melonboy10.swapper.schematics;

import me.melonboy10.swapper.StructureSwapperPlugin;

import java.util.HashMap;

public class SchematicManager {

    static StructureSwapperPlugin plugin;
    static HashMap<String, Schematic> schematics = new HashMap<>();

    public SchematicManager(StructureSwapperPlugin plugin) {
        SchematicManager.plugin = plugin;
    }

    public static HashMap<String, Schematic> getStructures() {
        return schematics;
    }

    public static void addSchematic(Schematic schematic) {
        schematics.put(schematic.getPathName(), schematic);
    }

    public static Schematic getSchematic(String fileName) {
        return schematics.getOrDefault(fileName, null);
    }
}
