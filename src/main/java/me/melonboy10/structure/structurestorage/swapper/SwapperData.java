package me.melonboy10.structure.structurestorage.swapper;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class SwapperData {

    private String name;
    private BoundingBox boundingBox;
    private final Vector location;

    public SwapperData(String name, Vector location, BoundingBox boundingBox) {
        this.name = name;
        this.location = location;
        this.boundingBox = boundingBox.shift(location);
    }


//    ArrayList<Schematic> structures = new ArrayList<Schematic>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public BoundingBox getRelativeBoundingBox() {
        return this.boundingBox.shift(location.multiply(-1));
    }

}
