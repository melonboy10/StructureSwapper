package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

public class SwapperData {

    String name;
    int id;
    BoundingBox boundingBox;
    ArrayList<StoredStructure> structures = new ArrayList<StoredStructure>();

}
