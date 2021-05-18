package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.util.BoundingBox;

import java.util.HashMap;

public class SwapperData {

    String name;
    BoundingBox boundingBox;

    public SwapperData(String name, BoundingBox boundingBox) {
        this.name = name;
        this.boundingBox = boundingBox;
    }

//    ArrayList<Schematic> structures = new ArrayList<Schematic>();

    enum LocationModes {CENTER, BOTTOM_LEFT, TOP_RIGHT}

    public HashMap<String, String> serializeData() {
        HashMap<String, String> map = new HashMap<>();

        map.put("name", name);
        map.put("bounding_box", boundingBox.toString());

        return map;
    }

}
