package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.Location;
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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
            new SwapperBlock(block, item, plugin);
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
        event.getPlayer().sendMessage("Clicked");
        if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
            event.getPlayer().sendMessage("Clicked Stand");
            if (event.getRightClicked().getPersistentDataContainer().has(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY)) {
                event.getPlayer().sendMessage("Clicked Stand with Data");
                int[] locations = event.getRightClicked().getPersistentDataContainer().get(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY);
                Location location = new Location(event.getRightClicked().getWorld(), locations[0], locations[1], locations[2]);

                SwapperBlock block = BlockManager.get(BlockManager.getBlocks().stream().filter(block1 -> block1.getLocation().equals(location)).findFirst().get());
                if (event.getPlayer().isSneaking()) {
                    block.shrinkSideStand(event.getRightClicked());
                } else {
                    block.expandSideStand(event.getRightClicked());
                }
            }
        }
    }
}
