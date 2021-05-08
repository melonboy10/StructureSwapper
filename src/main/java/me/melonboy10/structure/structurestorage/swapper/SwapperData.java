package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.schematic.Schematic;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

public class SwapperData {

    String name;
    int id;
    BoundingBox boundingBox;
    ArrayList<Schematic> structures = new ArrayList<Schematic>();

}
