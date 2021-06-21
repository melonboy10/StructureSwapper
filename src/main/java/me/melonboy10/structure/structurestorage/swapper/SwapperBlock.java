package me.melonboy10.structure.structurestorage.swapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.melonboy10.structure.structurestorage.StructureStorage;
import me.melonboy10.structure.structurestorage.utils.ParticleUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwapperBlock {

    StructureStorage plugin;
    enum ArmorstandTypes {NAME, NAME_DIVIDER, MAX_COORDS, MIN_COORDS, BOUNDS, COORD_DIVIDER, SELECTED_SCHEMATIC}

    Block block;
    SwapperData data;
    HashMap<ArmorstandTypes, ArmorStand> stands = new HashMap<>();
    BiMap<BlockFace, ArmorStand> boundingControls = HashBiMap.create();
    public boolean controlsVisible;
    public boolean advancedDisplay;

    BukkitTask runnable;
    public SwapperBlock(Block placedBlock, ItemStack item, StructureStorage plugin) {
        this.plugin = plugin;

        this.block = placedBlock;
        BlockManager.add(block, this);

        CreatureSpawner blockState = (CreatureSpawner) block.getState();
        blockState.getPersistentDataContainer().set(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER, 1);
        blockState.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
        blockState.setSpawnRange(0);
        blockState.setRequiredPlayerRange(0);
        blockState.update();

        setData(BlockManager.getDataFromItem(item, block.getLocation()));

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                BoundingBox box = data.getBoundingBox();
                ParticleUtils.drawBoundingBox(block.getWorld(), box, data.getCurrentDisplayMode(), SwapperData.dyeToColor.get(data.getColor()));
            }
        }.runTaskTimer(plugin, 0, 5);

        setHologram(ArmorstandTypes.NAME, data.getName());
        setHologram(ArmorstandTypes.BOUNDS, "L: " + data.getBoundingBox().getWidthX() + " W: " + data.getBoundingBox().getWidthZ() + " H: " + data.getBoundingBox().getHeight());
    }

    private void setHologram(ArmorstandTypes type, String text) {

        if (text.equals("")) {
            stands.get(type).remove();
            return;
        }
        ArmorStand existingStand = stands.get(type);
        if (existingStand != null) {
            existingStand.setCustomName(text);
            return;
        }

        ArmorStand nameStand = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 0.5, 0.5), EntityType.ARMOR_STAND);
        nameStand.setVisible(false);
        nameStand.setCustomName(text);
        nameStand.setCustomNameVisible(true);
        nameStand.setMarker(true);

        stands.putIfAbsent(type, nameStand);

        new BukkitRunnable() {
            @Override
            public void run() {
                int i = 0;

                for (ArmorstandTypes standType : ArmorstandTypes.values()) {
                    ArmorStand stand = stands.get(standType);
                    if (stand != null) {
                        stand.teleport(block.getLocation().clone().add(0.5, 1 + (0.5 * (stands.values().size() - i - 1)), 0.5));
                        i++;
                    }
                }
            }
            }.runTaskLater(plugin, 5);
    }

    public void setData(SwapperData data) {
        this.data = data;
    }

    public void clickEvent(PlayerInteractEvent event) {
        new SwapperMenu(event.getPlayer(), this).open();
    }

    public void breakEvent(BlockBreakEvent event) {
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), BlockManager.createNewItem(data));
        stands.forEach((armorstandTypes, armorStand) -> armorStand.remove());
        boundingControls.forEach((blockFace, armorStand) -> armorStand.remove());
        runnable.cancel();
    }

    public void toggleBoundingBox() {
        data.setCurrentDisplayMode(data.getCurrentDisplayMode().nextMode());
    }

    public void toggleControls() {
        controlsVisible = !controlsVisible;
        if (controlsVisible) {
            BoundingBox box = data.getBoundingBox();
            addBoundingControl(BlockFace.NORTH, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() + (box.getWidthZ() / 2 + 0.2), 0, 0));
            addBoundingControl(BlockFace.SOUTH, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() - (box.getWidthZ() / 2 + 0.2), 180, 0));

            addBoundingControl(BlockFace.WEST, new Location(block.getWorld(), box.getCenterX() + (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 270, 0));
            addBoundingControl(BlockFace.EAST, new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 90, 0));

//            addBoundingControl(BlockFace.UP, new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 90, 0));
//            addBoundingControl(BlockFace.DOWN, new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 90, 0));
        } else {
            boundingControls.forEach((blockFace, armorStand) -> armorStand.remove());
        }
    }

    public void addBoundingControl(BlockFace face, Location location) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setInvulnerable(true);

        stand.getEquipment().setItemInMainHand(new ItemStack(Material.HOPPER));
        stand.getEquipment().setItemInOffHand(new ItemStack(Material.HOPPER));

        stand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

        stand.setRotation(location.getYaw(), location.getPitch());
        stand.setRightArmPose(new EulerAngle(0, Math.toRadians(180), Math.toRadians(332)));
        stand.setLeftArmPose(new EulerAngle(0, Math.toRadians(180), Math.toRadians(28)));

        stand.getPersistentDataContainer().set(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY, new int[]{block.getX(), block.getY(), block.getZ()});

        boundingControls.put(face.getOppositeFace(), stand);
    }

    public void updateBoundingControls() {
        BoundingBox box = data.getBoundingBox();
        boundingControls.get(BlockFace.SOUTH).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() + (box.getWidthZ() / 2 + 0.2), 0, 0));
        boundingControls.get(BlockFace.NORTH).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() - (box.getWidthZ() / 2 + 0.2), 180, 0));
        boundingControls.get(BlockFace.EAST).teleport(new Location(block.getWorld(), box.getCenterX() + (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 270, 0));
        boundingControls.get(BlockFace.WEST).teleport(new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 90, 0));
    }

    public void shrinkSideStand(ArmorStand rightClicked) {
        BlockFace face = boundingControls.inverse().getOrDefault(rightClicked, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.expand(face, -1);
        if (cloneBox.getWidthX() < 1 || cloneBox.getWidthZ() < 1 || cloneBox.getHeight() < 1) {
            return;
        }

        data.getBoundingBox().expand(face, -1);
        updateBoundingControls();
    }

    public void expandSideStand(ArmorStand rightClicked) {
        BlockFace face = boundingControls.inverse().getOrDefault(rightClicked, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.expand(face, 1);

        if (cloneBox.getWidthX() < 1 || cloneBox.getWidthZ() < 1 || cloneBox.getHeight() < 1) {
            return;
        }

        AtomicBoolean foundBlock = new AtomicBoolean(false);
        BlockManager.getBlocks().forEach(block1 -> {
            if (!foundBlock.get() && cloneBox.contains(block1.getLocation().toVector())) {
                ParticleUtils.drawBoundingBox(block.getWorld(), cloneBox.intersection(new BoundingBox(block1.getX(), block1.getY(), block1.getZ(), block1.getX() + 1, block1.getY() + 1, block1.getZ() + 1)), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                foundBlock.set(true);
            }
        });
        if (foundBlock.get()) return;

        AtomicBoolean foundBounds = new AtomicBoolean(false);
        BlockManager.getBlocks().stream().filter(block1 -> block1 != block).forEach(block1 -> {
            SwapperBlock block2 = BlockManager.get(block1);
            if (!foundBounds.get() && cloneBox.overlaps(block2.data.getBoundingBox())) {
                ParticleUtils.drawBoundingBox(block.getWorld(), cloneBox.intersection(block2.data.getBoundingBox()), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                foundBounds.set(true);
            }
        });
        if (foundBounds.get()) return;

        data.getBoundingBox().expand(face, 1);
        updateBoundingControls();
    }

    public void toggleColor(boolean back) {
        if (back) {
            data.setColor(SwapperData.nextColor.inverse().get(data.getColor()));
        } else {
            data.setColor(SwapperData.nextColor.get(data.getColor()));
        }
    }

    public void toggleAdvancedDisplay() {
        advancedDisplay = !advancedDisplay;
        if (advancedDisplay) {
            setHologram(ArmorstandTypes.NAME_DIVIDER,  ChatColor.GRAY + "-------");
            setHologram(ArmorstandTypes.MAX_COORDS, "Max: " + data.getBoundingBox().getMax());
            setHologram(ArmorstandTypes.MIN_COORDS, "Min: " + data.getBoundingBox().getMin());
            setHologram(ArmorstandTypes.BOUNDS, "L: " + data.getBoundingBox().getWidthX() + " W: " + data.getBoundingBox().getWidthZ() + " H: " + data.getBoundingBox().getHeight());
            setHologram(ArmorstandTypes.COORD_DIVIDER, ChatColor.GRAY + "-------");
//            setHologram(ArmorstandTypes.SELECTED_SCHEMATIC, data.getSelectedSchematics());
        } else {
            setHologram(ArmorstandTypes.NAME_DIVIDER,  "");
            setHologram(ArmorstandTypes.MAX_COORDS, "");
            setHologram(ArmorstandTypes.MIN_COORDS, "");
            setHologram(ArmorstandTypes.COORD_DIVIDER, "");
        }
    }


}
