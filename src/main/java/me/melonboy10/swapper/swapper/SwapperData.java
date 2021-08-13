package me.melonboy10.swapper.swapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.melonboy10.swapper.schematics.Schematic;
import me.melonboy10.swapper.schematics.SwapperSchematic;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
    public enum StructurePlaceMode {NEGATIVE_CORNER, CENTER, POSITIVE_CORNER;
        public StructurePlaceMode nextMode() {
            switch (this) {
                case NEGATIVE_CORNER:
                    return CENTER;
                case CENTER:
                    return POSITIVE_CORNER;
                case POSITIVE_CORNER:
                    return NEGATIVE_CORNER;
            }
            return CENTER;
        }
    }

    public static HashMap<DyeColor, Color> dyeToColor = new HashMap<DyeColor, Color>() {{
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
    public static BiMap<DyeColor, DyeColor> nextColor = HashBiMap.create(new HashMap<DyeColor, DyeColor>() {{
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
    private String name;

    private DyeColor color;
    private BoundingBox boundingBox;
    private final Vector location;
    private BoundingDisplayMode displayMode;
    private StructurePlaceMode placeMode;
    private SwapperSchematic schematic;
    private HashMap<Schematic, SwapperSchematic> swapperSchematics = new HashMap<>();
    private ArrayList<Schematic> pinnedSchematics = new ArrayList<>();
    private ArrayList<Schematic> recentSchematics = new ArrayList<>();
    public SwapperData(String name, Vector location, BoundingBox boundingBox, DyeColor color, BoundingDisplayMode displayMode, StructurePlaceMode placeMode) {
        this.name = name;
        this.location = location;
        this.boundingBox = boundingBox.shift(location);
        this.color = color;
        this.displayMode = displayMode;
        this.placeMode = placeMode;
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

    public void setRelativeBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox.shift(location);
    }

    public BoundingBox getRelativeBoundingBox() {
        return this.boundingBox.clone().shift(location.clone().multiply(-1));
    }

    public BoundingBox getAbsoluteBoundingBox() {
        return  this.boundingBox.clone().shift(boundingBox.getMin().multiply(-1));
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public BoundingDisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(BoundingDisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public StructurePlaceMode getPlaceMode() {
        return placeMode;
    }

    public void setPlaceMode(StructurePlaceMode placeMode) {
        this.placeMode = placeMode;
    }

    public HashMap<Schematic, SwapperSchematic> getSwapperSchematics() {
        return swapperSchematics;
    }

    public SwapperSchematic getSwapperSchematic() {
        return schematic;
    }

    @Nullable
    public Schematic getSchematic() {
        return schematic != null ? schematic.getSchematic() : null;
    }

    public void setSchematic(SwapperSchematic schematic) {
        this.schematic = schematic;
    }

    public ArrayList<Schematic> getPinnedSchematics() {
        return pinnedSchematics;
    }

    public ArrayList<Schematic> getRecentSchematics() {
        return recentSchematics;
    }
}
