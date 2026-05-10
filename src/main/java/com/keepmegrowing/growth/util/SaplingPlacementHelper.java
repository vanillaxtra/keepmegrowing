package com.keepmegrowing.growth.util;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.block.data.type.Sapling;

public final class SaplingPlacementHelper implements GrowthStageCapable {
    @Override
    public int a(BlockState blockState) {
        BlockData bd = blockState.getBlockData();
        if (bd instanceof Cocoa cocoa) {
            return cocoa.getAge();
        }
        if (bd instanceof Sapling sapling) {
            return sapling.getStage();
        }
        return Tag.SAPLINGS.isTagged(blockState.getType()) ? 0 : 999;
    }

    @Override
    public void a(BlockState blockState, int n) {
        BlockData bd = blockState.getBlockData();
        if (bd instanceof Cocoa cocoa) {
            cocoa.setAge(Math.min(Math.max(n, 0), cocoa.getMaximumAge()));
            blockState.setBlockData(cocoa);
            blockState.update(true);
            return;
        }
        if (bd instanceof Sapling sapling) {
            int max = sapling.getMaximumStage();
            sapling.setStage(Math.min(Math.max(n, 0), max));
            blockState.setBlockData(sapling);
            blockState.update(true);
        }
    }

    @Override
    public String b(BlockState blockState) {
        Material m = blockState.getType();
        if (Tag.SAPLINGS.isTagged(m)) {
            return m.name();
        }
        return "NONE";
    }

    @Override
    public void a(BlockState blockState, String string) {
        Material m = Material.matchMaterial(string);
        if (m == null || !Tag.SAPLINGS.isTagged(m)) {
            m = Material.OAK_SAPLING;
        }
        blockState.setType(m);
        BlockData bd = blockState.getBlockData();
        if (bd instanceof Sapling sapling) {
            sapling.setStage(0);
            blockState.setBlockData(sapling);
        }
        blockState.update(true);
    }

    @Override
    public int c(BlockState blockState) {
        return 0;
    }

    @Override
    public boolean a(Material material) {
        return material == Material.DIRT || material == Material.GRASS_BLOCK;
    }
}
