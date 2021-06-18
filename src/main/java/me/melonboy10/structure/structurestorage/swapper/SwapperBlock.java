package me.melonboy10.structure.structurestorage.swapper;

import me.melonboy10.structure.structurestorage.StructureStorage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.*;

public class SwapperBlock {

    StructureStorage plugin;
    enum ArmorstandTypes {NAME, RELATIVE, BOUNDS, SELECTED_SCHEMATIC;}
    enum BoundingDisplayMode {NONE, CORNERS, DOTTED, SOLID;
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

    Block block;
    SwapperData data;
    HashMap<ArmorstandTypes, ArmorStand> stands = new HashMap<>();
    BoundingDisplayMode currentDisplayMode = BoundingDisplayMode.NONE;
    ArrayList<ArmorStand> boundingControls = new ArrayList<>();
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

        PersistentDataContainer meta = item.getItemMeta().getPersistentDataContainer();
        String name = meta.get(new NamespacedKey(plugin, "name"), PersistentDataType.STRING);
        int[] bb = meta.get(new NamespacedKey(plugin, "bounding_box"), PersistentDataType.INTEGER_ARRAY);
        setData(new SwapperData(name, block.getLocation().toVector(), new BoundingBox(bb[0], bb[1], bb[2], bb[3], bb[4], bb[5])));

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                BoundingBox box = data.getBoundingBox();
                switch (currentDisplayMode) {
                    case DOTTED:
                        for (int i = 0; i < box.getWidthX(); i++) {
                            makeParticle(box.getMax().subtract(new Vector(i + 0.5, 0, 0)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), 0)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(i + 0.5, 0, box.getWidthZ())).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), box.getWidthZ())).toLocation(block.getWorld()));
                        }
                        for (int i = 0; i < box.getWidthZ(); i++) {
                            makeParticle(box.getMax().subtract(new Vector(0, 0, i + 0.5)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(0, box.getHeight(), i + 0.5)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), 0, i + 0.5)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), box.getHeight(), i + 0.5)).toLocation(block.getWorld()));
                        }
                        for (int i = 0; i < box.getHeight(); i++) {
                            makeParticle(box.getMax().subtract(new Vector(0, i + 0.5, 0)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, 0)).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(0, i + 0.5, box.getWidthZ())).toLocation(block.getWorld()));
                            makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, box.getWidthZ())).toLocation(block.getWorld()));
                        }
                    case CORNERS:
                        makeParticle(box.getMax().toLocation(block.getWorld()));
                        makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), 0, 0)).toLocation(block.getWorld()));
                        makeParticle(box.getMax().subtract(new Vector(0, 0, box.getWidthZ())).toLocation(block.getWorld()));
                        makeParticle(box.getMax().subtract(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(block.getWorld()));

                        makeParticle(box.getMin().toLocation(block.getWorld()));
                        makeParticle(box.getMin().add(new Vector(box.getWidthX(), 0, 0)).toLocation(block.getWorld()));
                        makeParticle(box.getMin().add(new Vector(0, 0, box.getWidthZ())).toLocation(block.getWorld()));
                        makeParticle(box.getMin().add(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(block.getWorld()));
                        break;
                    case SOLID:
                        break;
                }

            }
        }.runTaskTimer(plugin, 0, 5);

        addHologram(ArmorstandTypes.NAME, name);
        addHologram(ArmorstandTypes.RELATIVE, Arrays.toString(bb));
    }

    private void addHologram(ArmorstandTypes type, String text) {
        ArmorStand nameStand = (ArmorStand) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 1 + (0.5 * stands.values().size()), 0.5), EntityType.ARMOR_STAND);
        nameStand.setVisible(false);
        nameStand.setCustomName(text);
        nameStand.setCustomNameVisible(true);
        nameStand.setMarker(true);

        stands.putIfAbsent(type, nameStand);

        int i = 0;
        for (ArmorStand stand : stands.values()) {
            stand.teleport(block.getLocation().clone().add(0.5, 1 + (0.5 * (stands.values().size() - i - 1)), 0.5));
            i++;
        }
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
        boundingControls.forEach(Entity::remove);
        runnable.cancel();
    }

    public void toggleBoundingBox() {
        currentDisplayMode = currentDisplayMode.nextMode();
    }


    public void makeParticle(Location location) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.GREEN, 1));
    }

    public void toggleControls() {
        if (controlsVisible) {
            boundingControls.forEach(Entity::remove);
        } else {
            BoundingBox box = data.getBoundingBox();
            addBoundingControl(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() + (box.getWidthZ() / 2 + 0.2), 0, 0));
            addBoundingControl(new Location(block.getWorld(), box.getCenterX(), box.getCenterY() - 1, box.getCenterZ() - (box.getWidthZ() / 2 + 0.2), 180, 0));

            addBoundingControl(new Location(block.getWorld(), box.getCenterX() + (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 270, 0));
            addBoundingControl(new Location(block.getWorld(), box.getCenterX() - (box.getWidthX() / 2 + 0.2), box.getCenterY() - 1, box.getCenterZ(), 90, 0));
        }
        controlsVisible = !controlsVisible;
    }

    public void addBoundingControl(Location location) {
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

        boundingControls.add(stand);
    }

    public void toggleAdvancedDisplay() {

    }

    public void shrinkSideStand(Entity rightClicked) {
    }

    public void expandSideStand(Entity rightClicked) {
    }


}
