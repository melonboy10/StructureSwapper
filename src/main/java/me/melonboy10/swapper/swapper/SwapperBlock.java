package me.melonboy10.swapper.swapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import me.melonboy10.swapper.StructureSwapperPlugin;
import me.melonboy10.swapper.menuSystem.menus.SwapperMenu;
import me.melonboy10.swapper.schematics.Schematic;
import me.melonboy10.swapper.schematics.SwapperSchematic;
import me.melonboy10.swapper.utils.ParticleUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwapperBlock {

    StructureSwapperPlugin plugin;



    enum ArmorstandTypes {NAME, NAME_DIVIDER, MAX_COORDS, MIN_COORDS, BOUNDS, COORD_DIVIDER, SELECTED_SCHEMATIC;}
    public Block block;

    public SwapperData data;
    HashMap<ArmorstandTypes, ArmorStand> stands = new HashMap<>();
    BiMap<BlockFace, ArmorStand> boundingControls = HashBiMap.create();
    public boolean controlsVisible;
    public boolean advancedDisplay;
    BukkitTask runnable;
    public SwapperBlock(Block placedBlock, ItemStack item, StructureSwapperPlugin plugin) {
        this.plugin = plugin;

        this.block = placedBlock;
        BlockManager.add(block, this);

        setData(BlockManager.getDataFromItem(item, block.getLocation()));

        Structure blockState = (Structure) block.getState();
        blockState.getPersistentDataContainer().set(new NamespacedKey(plugin, "isSwapperBlock"), PersistentDataType.INTEGER, 1);
        blockState.setUsageMode(UsageMode.values()[RandomUtils.nextInt(4)]);
        blockState.setBoundingBoxVisible(false);
        blockState.update();


        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                BoundingBox box = data.getBoundingBox();
                ParticleUtils.drawBoundingBox(block.getWorld(), box, data.getDisplayMode(), SwapperData.dyeToColor.get(data.getColor()));
            }
        }.runTaskTimer(plugin, 0, 5);

        setHologram(ArmorstandTypes.NAME, data.getName());
        setHologram(ArmorstandTypes.BOUNDS, "L: " + data.getBoundingBox().getWidthX() + " W: " + data.getBoundingBox().getWidthZ() + " H: " + data.getBoundingBox().getHeight());
        updateHolograms();
    }

    private void setHologram(ArmorstandTypes type, String text) {

        if (text.equals("")) {
            stands.get(type).remove();
            stands.remove(type);
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
    }

    public void updateHolograms() {
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

    public void breakEvent() {
        block.getWorld().dropItemNaturally(block.getLocation(), BlockManager.createNewItem(data));
        stands.forEach((armorstandTypes, armorStand) -> armorStand.remove());
        boundingControls.forEach((blockFace, armorStand) -> armorStand.remove());
        runnable.cancel();
        block.setType(Material.AIR);
    }

    public void togglePlaceMode() {
        data.setPlaceMode(data.getPlaceMode().nextMode());
        System.out.println("toggleplace");
    }

    public void toggleBoundingBox() {
        data.setDisplayMode(data.getDisplayMode().nextMode());
    }

    public void toggleControls() {
        controlsVisible = !controlsVisible;
        if (controlsVisible) {
            BoundingBox box = data.getBoundingBox();
            addBoundingControl(BlockFace.NORTH, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 0.5, box.getCenterZ() + (box.getWidthZ() / 2 + 0.2), 0, 0));
            addBoundingControl(BlockFace.SOUTH, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 0.5, box.getCenterZ() - (box.getWidthZ() / 2 + 0.2), 180, 0));

            addBoundingControl(BlockFace.WEST, new Location(block.getWorld(), box.getCenterX() + (box.getWidthX() / 2 + 0.2), box.getCenterY() - 0.5, box.getCenterZ(), 270, 0));
            addBoundingControl(BlockFace.EAST, new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 0.5, box.getCenterZ(), 90, 0));

            addBoundingControl(BlockFace.DOWN, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() + (box.getHeight() / 2 - 0.8), box.getCenterZ(), 0, 1));
            addBoundingControl(BlockFace.UP, new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - (box.getHeight() / 2 + 0.6), box.getCenterZ(), 0, 180));
        } else {
            boundingControls.forEach((blockFace, armorStand) -> armorStand.remove());
        }
    }

    public void addBoundingControl(BlockFace face, Location location) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setSmall(true);
        stand.setInvulnerable(true);

        if (location.getPitch() > 0) {
            stand.getEquipment().setHelmet(new ItemStack(Material.SCAFFOLDING));

            stand.setHeadPose(new EulerAngle(Math.toRadians(location.getPitch()), 0, 0));
        } else {
            stand.getEquipment().setItemInMainHand(new ItemStack(Material.HOPPER));
            stand.getEquipment().setItemInOffHand(new ItemStack(Material.HOPPER));

            stand.setRotation(location.getYaw(), location.getPitch());
            stand.setRightArmPose(new EulerAngle(0, Math.toRadians(180), Math.toRadians(332)));
            stand.setLeftArmPose(new EulerAngle(0, Math.toRadians(180), Math.toRadians(28)));
        }

        stand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

        stand.getPersistentDataContainer().set(new NamespacedKey(plugin, "location"), PersistentDataType.INTEGER_ARRAY, new int[]{block.getX(), block.getY(), block.getZ()});

        boundingControls.put(face.getOppositeFace(), stand);
    }

    public void updateBoundingControls() {
        BoundingBox box = data.getBoundingBox();
        boundingControls.get(BlockFace.UP).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() + (box.getHeight() / 2 - 0.8), box.getCenterZ(), 0, 180));
        boundingControls.get(BlockFace.DOWN).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - (box.getHeight() / 2 + 0.2), box.getCenterZ(), 0, 1));

        boundingControls.get(BlockFace.SOUTH).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 0.5, box.getCenterZ() + (box.getWidthZ() / 2 + 0.2), 0, 0));
        boundingControls.get(BlockFace.NORTH).teleport(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 0.5, box.getCenterZ() - (box.getWidthZ() / 2 + 0.2), 180, 0));
        boundingControls.get(BlockFace.EAST).teleport(new Location(block.getWorld(), box.getCenterX() + (box.getWidthX() / 2 + 0.2), box.getCenterY() - 0.5, box.getCenterZ(), 270, 0));
        boundingControls.get(BlockFace.WEST).teleport(new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 0.5, box.getCenterZ(), 90, 0));

    }

    public void shrinkSideStand(ArmorStand rightClicked) {
        BlockFace face = boundingControls.inverse().getOrDefault(rightClicked, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.expand(face, -1);
        if (cloneBox.getWidthX() < 1 || cloneBox.getWidthZ() < 1 || cloneBox.getHeight() < 1) {
            return;
        }

        BoundingBox differenceBox = data.getBoundingBox().clone();
        switch (face) {
            case NORTH:
            case SOUTH:
                differenceBox.expand(face.getOppositeFace(), -cloneBox.getWidthZ());
                break;
            case EAST:
            case WEST:
                differenceBox.expand(face.getOppositeFace(), -cloneBox.getWidthX());
                break;
            case UP:
            case DOWN:
                differenceBox.expand(face.getOppositeFace(), -cloneBox.getHeight());
                break;
        }
        ParticleUtils.drawBoundingBox(block.getWorld(), differenceBox, SwapperData.BoundingDisplayMode.DOTTED, Particle.END_ROD);
        block.getWorld().playSound(rightClicked.getLocation(), "minecraft:block.wool.break", SoundCategory.BLOCKS, 1, 1);

        data.getBoundingBox().expand(face, -1);
        updateBoundingControls();
    }

    public void expandSideStand(ArmorStand rightClicked) {
        BlockFace face = boundingControls.inverse().getOrDefault(rightClicked, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.expand(face, 1);

        BoundingBox differenceBox = cloneBox.clone();
        switch (face) {
            case NORTH: case SOUTH:
                differenceBox.expand(face.getOppositeFace(), -data.getBoundingBox().getWidthZ());
                break;
            case EAST: case WEST:
                differenceBox.expand(face.getOppositeFace(), -data.getBoundingBox().getWidthX());
                break;
            case UP: case DOWN:
                differenceBox.expand(face.getOppositeFace(), -data.getBoundingBox().getHeight());
                break;
        }
        ParticleUtils.drawBoundingBox(block.getWorld(), differenceBox, SwapperData.BoundingDisplayMode.DOTTED, Particle.END_ROD);
        block.getWorld().playSound(rightClicked.getLocation(), "minecraft:block.wool.place", SoundCategory.BLOCKS, 1, 1);

        if (validateBounds(cloneBox)) {
            data.getBoundingBox().expand(face, 1);
            updateBoundingControls();
        }
    }

    public void shiftTowards(ArmorStand stand) {
        BlockFace face = boundingControls.inverse().getOrDefault(stand, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.shift(face.getDirection());

        if (validateBounds(cloneBox)) {
            data.getBoundingBox().shift(face.getDirection());
            block.getWorld().playSound(stand.getLocation(), "minecraft:block.piston.contract", SoundCategory.BLOCKS, 1, 1);
            updateBoundingControls();
        }
    }

    public void shiftAway(ArmorStand stand) {
        BlockFace face = boundingControls.inverse().getOrDefault(stand, null);
        if (face == null) return;

        BoundingBox cloneBox = data.getBoundingBox().clone();
        cloneBox.shift(face.getOppositeFace().getDirection());

        if (validateBounds(cloneBox)) {
            data.getBoundingBox().shift(face.getOppositeFace().getDirection());
            block.getWorld().playSound(stand.getLocation(), "minecraft:block.piston.extend", SoundCategory.BLOCKS, 1, 1);
            updateBoundingControls();
        }
    }

    private boolean validateBounds(BoundingBox box) {
        if (box.getWidthX() < 1 || box.getWidthZ() < 1 || box.getHeight() < 1) {
            return false;
        }

        AtomicBoolean foundBlock = new AtomicBoolean(false);
        BlockManager.getBlocks().forEach(block1 -> {
            if (!foundBlock.get() && box.contains(block1.getLocation().toVector())) {
                ParticleUtils.drawBoundingBox(block.getWorld(), box.intersection(new BoundingBox(block1.getX(), block1.getY(), block1.getZ(), block1.getX() + 1, block1.getY() + 1, block1.getZ() + 1)), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                foundBlock.set(true);
            }
        });
        if (foundBlock.get()) return false;

        AtomicBoolean foundBounds = new AtomicBoolean(false);
        BlockManager.getBlocks().stream().filter(block1 -> block1 != block).forEach(block1 -> {
            SwapperBlock block2 = BlockManager.get(block1);
            if (!foundBounds.get() && box.overlaps(block2.data.getBoundingBox())) {
                ParticleUtils.drawBoundingBox(block.getWorld(), box.intersection(block2.data.getBoundingBox()), SwapperData.BoundingDisplayMode.DOTTED, Color.RED);
                foundBounds.set(true);
            }
        });
        if (foundBounds.get()) return false;

        return true;
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
        updateHolograms();
    }

    public void changeSchematic(Schematic schematic) {
        changeSchematic(data.getSwapperSchematics().getOrDefault(schematic, new SwapperSchematic(schematic)));
    }

    public void changeSchematic(SwapperSchematic schematic) {
        if (data.getSwapperSchematic() != null) {
            data.getSwapperSchematic().save();
        }

        data.setSchematic(schematic);
        schematic.paste(data.getBoundingBox().getMin().toLocation(block.getWorld()));

    }
}
