package me.melonboy10.structure.structurestorage.schematic.block;

import me.melonboy10.structure.structurestorage.schematic.WrongIdException;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

public abstract class NBTBlock {
    
    private final NBTTagCompound nbtTag;
    
    public NBTBlock(NBTTagCompound nbtTag) {
        this.nbtTag = nbtTag;
    }

    public NBTTagCompound getNbtTag() {
        return nbtTag;
    }

    public Vector getOffset() {
        NBTTagCompound compound = this.getNbtTag();
        int[] pos = compound.getIntArray("Pos");
        return new Vector(pos[0], pos[1], pos[2]);
    }

    public abstract void setData(BlockState state) throws WrongIdException;

    public abstract boolean isEmpty();
}
