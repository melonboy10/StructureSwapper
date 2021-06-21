package me.melonboy10.structure.structurestorage.swapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.HashMap;

public class SwapperData {

    public enum BoundingDisplayMode {NONE, CORNERS, DOTTED, SOLID;
        public BoundingDisplayMode nextMode() {
            switch(this) {
                case NONE:
                    return CORNERS;
                case CORNERS:
                    return DOTTED;
                case DOTTED:
                    return SOLID;
                case SOLID:
                    return NONE;
            }
            return NONE;
        }

        public boolean visible() {
            return this == CORNERS || this == DOTTED || this == SOLID;
        }
    }

    private String name;
    private DyeColor color = DyeColor.LIME;
    private BoundingBox boundingBox;
    private final Vector location;
    private BoundingDisplayMode currentDisplayMode = BoundingDisplayMode.NONE;

    public static HashMap<DyeColor, Color> dyeToColor = new HashMap<>() {{
        put(DyeColor.RED,        Color.RED);
        put(DyeColor.ORANGE,     Color.ORANGE);
        put(DyeColor.YELLOW,     Color.YELLOW);
        put(DyeColor.LIME,       Color.LIME);
        put(DyeColor.GREEN,      Color.GREEN);
        put(DyeColor.LIGHT_BLUE, Color.fromRGB(42, 198, 222));
        put(DyeColor.BLUE,       Color.BLUE);
        put(DyeColor.CYAN,       Color.TEAL);
        put(DyeColor.PURPLE,     Color.PURPLE);
        put(DyeColor.MAGENTA,    Color.FUCHSIA);
        put(DyeColor.PINK,       Color.fromRGB(255, 143, 227));
        put(DyeColor.WHITE,      Color.WHITE);
        put(DyeColor.GRAY,       Color.GRAY);
        put(DyeColor.LIGHT_GRAY, Color.fromRGB(161, 161, 161));
        put(DyeColor.BLACK,      Color.BLACK);
        put(DyeColor.BROWN,      Color.MAROON);
    }};
    public static BiMap<DyeColor, DyeColor> nextColor = HashBiMap.create(new HashMap<>() {{
        put(DyeColor.RED,        DyeColor.ORANGE    );
        put(DyeColor.ORANGE,     DyeColor.YELLOW    );
        put(DyeColor.YELLOW,     DyeColor.LIME      );
        put(DyeColor.LIME,       DyeColor.GREEN     );
        put(DyeColor.GREEN,      DyeColor.CYAN      );
        put(DyeColor.CYAN,       DyeColor.LIGHT_BLUE);
        put(DyeColor.LIGHT_BLUE, DyeColor.BLUE      );
        put(DyeColor.BLUE,       DyeColor.PURPLE    );
        put(DyeColor.PURPLE,     DyeColor.MAGENTA   );
        put(DyeColor.MAGENTA,    DyeColor.PINK      );
        put(DyeColor.PINK,       DyeColor.WHITE     );
        put(DyeColor.WHITE,      DyeColor.LIGHT_GRAY);
        put(DyeColor.LIGHT_GRAY, DyeColor.GRAY      );
        put(DyeColor.GRAY,       DyeColor.BLACK     );
        put(DyeColor.BLACK,      DyeColor.BROWN     );
        put(DyeColor.BROWN,      DyeColor.RED       );
    }});

    public SwapperData(String name, Vector location, BoundingBox boundingBox, DyeColor color, BoundingDisplayMode displayMode) {
        this.name = name;
        this.location = location;
        this.boundingBox = boundingBox.shift(location);
        this.color = color;
        this.currentDisplayMode = displayMode;
    }

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

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public BoundingDisplayMode getCurrentDisplayMode() {
        return currentDisplayMode;
    }

    public void setCurrentDisplayMode(BoundingDisplayMode currentDisplayMode) {
        this.currentDisplayMode = currentDisplayMode;
    }
}
