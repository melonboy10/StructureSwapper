package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class SwapperBlock {

    Block block;
    SwapperData data;
    SwapperDisplay display;

    public SwapperBlock(Block placedBlock, ItemStack item, StructureStorage plugin) {
        this.block = placedBlock;
        CreatureSpawner blockState = (CreatureSpawner) block.getState();
        blockState.getPersistentDataContainer().set(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER, 1);
        blockState.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
        blockState.setSpawnRange(0);
        blockState.setRequiredPlayerRange(0);
        blockState.update();

        PersistentDataContainer meta = item.getItemMeta().getPersistentDataContainer();
        String name = meta.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
        int[] bb = meta.get(new NamespacedKey(plugin, "bounding_box"), PersistentDataType.INTEGER_ARRAY);

        setData(new SwapperData(name, new BoundingBox(bb[0], bb[1], bb[2], bb[3], bb[4], bb[5])));
        display.show(this.block.getLocation());
        display.setData(data);

        BlockManager.add(block, this);
    }

    public SwapperData getData() {
        return data;
    }

    public void setData(SwapperData data) {
        this.data = data;
    }

    public void clickEvent(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("Click");
    }

    public void breakEvent(BlockBreakEvent event) {
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), BlockManager.createNewItem(data));
        event.getBlock().setType(Material.AIR);
        display.remove();
    }

}
