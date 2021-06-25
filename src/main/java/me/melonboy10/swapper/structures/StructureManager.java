package me.melonboy10.swapper.structures;

import me.melonboy10.swapper.StructureSwapperPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StructureManager {

    static StructureSwapperPlugin plugin;
    static ArrayList<Structure> structures = new ArrayList<>();

    public StructureManager(StructureSwapperPlugin plugin) {
        StructureManager.plugin = plugin;
    }

    public static void loadStructures(List<File> structureFiles) {
        if (structureFiles.isEmpty()) {
            return;
        }

        for (File structureFile : structureFiles) {
            structures.add(new Structure(structureFile));
        }
    }

    public static ArrayList<Structure> getStructures() {
        return structures;
    }

    public static void addStructure(Structure structure) {
        structures.add(structure);
    }

}
