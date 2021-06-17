package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SwapperBlockListener implements Listener {

    StructureStorage plugin;

    public SwapperBlockListener(StructureStorage plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack item = event.getItemInHand();
        PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();

        if (itemData.has(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER)) {
            SwapperBlock swapperBlock = new SwapperBlock(block, item, plugin);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if (block.getState() instanceof CreatureSpawner) {
                CreatureSpawner blockState = (CreatureSpawner) block.getState();
                if (blockState.getPersistentDataContainer().has(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER)) {
                    event.setCancelled(true);
                    SwapperBlock swapperBlock = BlockManager.get(block);
                    if (swapperBlock != null) {
                        swapperBlock.clickEvent(event);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getState() instanceof CreatureSpawner) {
            CreatureSpawner blockState = (CreatureSpawner) block.getState();
            if (blockState.getPersistentDataContainer().has(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER)) {
//                event.setCancelled(true);
                SwapperBlock swapperBlock = BlockManager.get(block);
                if (swapperBlock != null) {
                    BlockManager.remove(block);
                    swapperBlock.breakEvent(event);
                }
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof TileState) {
            if (((TileState) block.getState()).getPersistentDataContainer().has(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER_ARRAY))
                event.setCancelled(true);
        }
    }
}
