package me.melonboy10.swapper.utils;

import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BoundingBoxUtils {

    public static Vector getDimensionsVector(BoundingBox boundingBox) {
        return new Vector(boundingBox.getWidthX(), boundingBox.getHeight(), boundingBox.getWidthZ());
    }

}
