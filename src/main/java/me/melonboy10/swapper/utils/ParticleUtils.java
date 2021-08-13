package me.melonboy10.swapper.utils;

import me.melonboy10.swapper.swapper.SwapperData;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.*;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ParticleUtils {

    public static void drawBoundingBox(World world, BoundingBox box, SwapperData.BoundingDisplayMode displayMode, Color... colors) {
        drawBoundingBox(world, box, displayMode, Particle.REDSTONE, colors);
    }

    public static void drawBoundingBox(World world, BoundingBox box, SwapperData.BoundingDisplayMode displayMode, Particle particle, Color... colors) {
        Color color;
        if (colors.length < 1) {
            color = Color.GREEN;
        } else {
            color = colors[0];
            if (color.equals(Color.MAROON)) {
                color = (Color.fromBGR(RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255)));
            }
        }
        switch (displayMode) {
            case DOTTED:
                for (int i = 0; i < box.getWidthX(); i++) {
                    makeParticle(particle, box.getMax().subtract(new Vector(i + 0.5, 0, 0)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), 0)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(i + 0.5, 0, box.getWidthZ())).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), box.getWidthZ())).toLocation(world), color);
                }
                for (int i = 0; i < box.getWidthZ(); i++) {
                    makeParticle(particle, box.getMax().subtract(new Vector(0, 0, i + 0.5)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(0, box.getHeight(), i + 0.5)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), 0, i + 0.5)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), box.getHeight(), i + 0.5)).toLocation(world), color);
                }
                for (int i = 0; i < box.getHeight(); i++) {
                    makeParticle(particle, box.getMax().subtract(new Vector(0, i + 0.5, 0)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, 0)).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(0, i + 0.5, box.getWidthZ())).toLocation(world), color);
                    makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, box.getWidthZ())).toLocation(world), color);
                }
            case CORNERS:
                makeParticle(particle, box.getMax().toLocation(world), color);
                makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), 0, 0)).toLocation(world), color);
                makeParticle(particle, box.getMax().subtract(new Vector(0, 0, box.getWidthZ())).toLocation(world), color);
                makeParticle(particle, box.getMax().subtract(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(world), color);

                makeParticle(particle, box.getMin().toLocation(world), color);
                makeParticle(particle, box.getMin().add(new Vector(box.getWidthX(), 0, 0)).toLocation(world), color);
                makeParticle(particle, box.getMin().add(new Vector(0, 0, box.getWidthZ())).toLocation(world), color);
                makeParticle(particle, box.getMin().add(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(world), color);
                break;
            case SOLID:
                break;
        }
    }

    public static void makeParticle(Particle particle, Location location, Color color) {
        if (particle.equals(Particle.REDSTONE)) {
            makeParticle(location, color);
        } else {
            location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0);
        }
    }

    public static void makeParticle(Location location, Color color) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(color, 1));
    }

    public static void buildBoundingBox(World world, BoundingBox box, SwapperData.BoundingDisplayMode displayMode, Material material) {
        switch (displayMode) {
            case DOTTED:
                for (int i = 0; i < box.getWidthX(); i++) {
                    world.getBlockAt(box.getMax().subtract(new Vector(i + 0.5, 0, 0)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), 0)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(i + 0.5, 0, box.getWidthZ())).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(i + 0.5, box.getHeight(), box.getWidthZ())).toLocation(world)).setType(material);
                }
                for (int i = 0; i < box.getWidthZ(); i++) {
                    world.getBlockAt(box.getMax().subtract(new Vector(0, 0, i + 0.5)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(0, box.getHeight(), i + 0.5)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), 0, i + 0.5)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), box.getHeight(), i + 0.5)).toLocation(world)).setType(material);
                }
                for (int i = 0; i < box.getHeight(); i++) {
                    world.getBlockAt(box.getMax().subtract(new Vector(0, i + 0.5, 0)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, 0)).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(0, i + 0.5, box.getWidthZ())).toLocation(world)).setType(material);
                    world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), i + 0.5, box.getWidthZ())).toLocation(world)).setType(material);
                }
            case CORNERS:
                world.getBlockAt(box.getMax().toLocation(world)).setType(material);
                world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), 0, 0)).toLocation(world)).setType(material);
                world.getBlockAt(box.getMax().subtract(new Vector(0, 0, box.getWidthZ())).toLocation(world)).setType(material);
                world.getBlockAt(box.getMax().subtract(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(world)).setType(material);

                world.getBlockAt(box.getMin().toLocation(world)).setType(material);
                world.getBlockAt(box.getMin().add(new Vector(box.getWidthX(), 0, 0)).toLocation(world)).setType(material);
                world.getBlockAt(box.getMin().add(new Vector(0, 0, box.getWidthZ())).toLocation(world)).setType(material);
                world.getBlockAt(box.getMin().add(new Vector(box.getWidthX(), 0, box.getWidthZ())).toLocation(world)).setType(material);
                break;
            case SOLID:
                break;
        }
    }

}
