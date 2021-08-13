package me.melonboy10.swapper.schematics;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import com.github.shynixn.structureblocklib.api.enumeration.StructureMirror;
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation;
import org.bukkit.Location;

import static me.melonboy10.swapper.schematics.SchematicManager.plugin;

public class SwapperSchematic{

    protected Schematic schematic;

    protected StructureRotation rotation;
    protected StructureMirror mirror;

    protected int offsetX;
    protected int offsetY;
    protected int offsetZ;

    public SwapperSchematic(Schematic schematic) {
        this.schematic = schematic;
        rotation = StructureRotation.NONE;
        mirror = StructureMirror.NONE;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public void paste(Location location) {
        StructureBlockLibApi.INSTANCE.
                loadStructure(plugin).
                at(location).
                rotation(rotation).
                mirror(mirror).
                includeEntities(true)
                .loadFromFile(schematic.getFile());
    }

    public void save() {
        schematic.save();
        //write to config file
    }

    public StructureRotation getRotation() {
        return rotation;
    }

    public void setRotation(StructureRotation rotation) {
        this.rotation = rotation;
    }

    public StructureMirror getMirror() {
        return mirror;
    }

    public void setMirror(StructureMirror mirror) {
        this.mirror = mirror;
    }
}
