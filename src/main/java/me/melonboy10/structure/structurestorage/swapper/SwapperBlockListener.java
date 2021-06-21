package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import me.melonboy10.structure.structurestorage.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.atomic.AtomicBoolean;

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

            AtomicBoolean foundBound = new AtomicBoolean(false);

            BlockManager.getBlocks().forEach(testedBlock -> {
                SwapperBlock swapperBlock = BlockManager.get(testedBlock);
                if (!foundBound.get()) {
                    if (swapperBlock.data.getBoundingBox().overlaps(block.getBoundingBox())) {
                        ParticleUtils.drawBoundingBox(block.getWorld(), block.getBoundingBox(), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                        foundBound.set(true);
                    } else {
                        SwapperData dataFromItem = BlockManager.getDataFromItem(item, block.getLocation());
                        if (swapperBlock.data.getBoundingBox().overlaps(dataFromItem.getBoundingBox())) {
                            ParticleUtils.drawBoundingBox(block.getWorld(), dataFromItem.getBoundingBox(), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                            foundBound.set(true);
                        }
                    }
                }
            });

            if (foundBound.get()) {
                event.setCancelled(true);
            } else {
                new SwapperBlock(block, item, plugin);
            }

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

    @EventHandler
    public void onControllerClick(PlayerInteractAtEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.HAND)) {
            if (event.getRightClicked() instanceof ArmorStand) {
                if (event.getRightClicked().getPersistentDataContainer().has(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY)) {
                    int[] locations = event.getRightClicked().getPersistentDataContainer().get(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY);
                    Location location = new Location(event.getRightClicked().getWorld(), locations[0], locations[1], locations[2]);

                    SwapperBlock block = BlockManager.get(BlockManager.getBlocks().stream().filter(block1 -> block1.getLocation().equals(location)).findFirst().get());
                    if (event.getPlayer().isSneaking()) {
                        block.shrinkSideStand(((ArmorStand) event.getRightClicked()));
                    } else {
                        block.expandSideStand(((ArmorStand) event.getRightClicked()));
                    }
                }
            }
        }
    }
}
