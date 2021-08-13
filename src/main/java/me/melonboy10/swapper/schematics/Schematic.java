package me.melonboy10.swapper.schematics;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.melonboy10.swapper.menuSystem.Menu;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.io.File;

public class Schematic {

    protected final File file;
    protected Material item;
    protected String name;

    protected int width;
    protected int height;
    protected int length;

    public Schematic(File file) {
        System.out.println("file = " + file);
        this.file = file;
        this.item = Material.GRASS_BLOCK;
        read();
    }

    public ItemStack getItem(boolean pinned, boolean selected) {
        return Menu.makeItem(item, ChatColor.YELLOW + name + (selected ? "" : (pinned ? " ⚲" : "")), 1, "file-name", file.getName(),
                ChatColor.DARK_GRAY + "Schematic",
                "",
                ChatColor.AQUA + "File Name:",
                ChatColor.GRAY + file.getName(),
                "",
                ChatColor.AQUA + "Dimensions:",
                ChatColor.GRAY + "    +",
                ChatColor.DARK_GRAY + "    |",
                ChatColor.YELLOW + "    " + (height) + ChatColor.GOLD + "m",
                ChatColor.DARK_GRAY + "    |",
                ChatColor.GRAY + "    +" + ChatColor.DARK_GRAY +
                        "--" + ChatColor.YELLOW +
                        (width) +
                        ChatColor.GOLD +
                        "m" + ChatColor.DARK_GRAY +
                        "--" + ChatColor.GRAY + "+",
                ChatColor.DARK_GRAY + "   /",
                ChatColor.YELLOW + "  " + (length) + ChatColor.GOLD + "m",
                ChatColor.GRAY + " /",
                ChatColor.GRAY + "+",
                "",
                (selected ? "" : ChatColor.YELLOW + "Click to select!"),
                (selected ? "" : ChatColor.YELLOW + "Right-Click to " + (pinned ? "un" : "") + "pin!"),
                ChatColor.YELLOW + "Shift-Left-Click to edit Schematic",
                ChatColor.YELLOW + "Shift-Right-Click to edit Palette!"
        );
    }

    private void read() {
        if (file != null) {
            try {
                name = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, file.getName().substring(0, file.getName().indexOf(".")));

                NamedTag tag = NBTUtil.read(file);
                JsonObject json = new JsonParser().parse(tag.getTag().valueToString()).getAsJsonObject();
                System.out.println("json = " + json);

                JsonArray size = json.getAsJsonObject("size").getAsJsonObject("value").getAsJsonArray("list");
                width = size.get(0).getAsInt();
                height = size.get(1).getAsInt();
                length = size.get(2).getAsInt();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {

    }

    public String getName() {
        return name;
    }

    public String getPathName() {
        return file.getName();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public File getFile() {
        return file;
    }

    public BoundingBox getBounds() {
        return new BoundingBox(0, 0, 0, width, height, length);
    }
}
