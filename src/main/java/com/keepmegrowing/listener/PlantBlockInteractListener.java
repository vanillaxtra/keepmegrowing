package com.keepmegrowing.listener;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.CropGrowthRegistry;
import com.keepmegrowing.integration.worldguard.KeepMeGrowingWorldGuard;
import com.keepmegrowing.storage.PlantChunkStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class PlantBlockInteractListener implements Listener {
    private static final CropGrowthRegistry cropRegistry = KeepMeGrowing.a().c();
    private static final PlantChunkStorage chunkStorage = KeepMeGrowing.a().b();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        maybeTrackCrop(event.getBlockPlaced().getType(), event.getBlockPlaced());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        maybeTrackCrop(event.getTo(), event.getBlock());
    }

    private static void maybeTrackCrop(Material material, Block block) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(block.getWorld().getName())) {
            return;
        }
        if (!KeepMeGrowingWorldGuard.allowsGrowth(block.getLocation())) {
            return;
        }
            AbstractCropGrowth growth = cropRegistry.findGrowth(material);
        if (growth != null) {
            switch (material.toString()) {
                case "CACTUS":
                case "SUGAR_CANE":
                case "KELP":
                case "TWISTING_VINES": {
                    Material below = block.getRelative(BlockFace.DOWN).getType();
                    if (below != material) break;
                    return;
                }
                case "WEEPING_VINES": {
                    Material above = block.getRelative(BlockFace.UP).getType();
                    if (above != material) break;
                    return;
                }
                default:
                    break;
            }
            chunkStorage.a(block.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (KeepMeGrowing.a().f().isWorldBlacklisted(event.getPlayer().getWorld().getName())) {
            return;
        }
        AbstractCropGrowth growth = cropRegistry.findGrowth(event.getBlock().getType());
        if (growth != null) {
            chunkStorage.b(event.getBlock().getLocation());
        }
    }
}
