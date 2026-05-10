
package com.keepmegrowing.growth.util;

import com.keepmegrowing.growth.util.GrowthStageCapable;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Sapling;

public final class StemBlockHelper
implements GrowthStageCapable {
    @Override
    public final int a(BlockState blockState) {
        return ((Ageable)blockState.getBlockData()).getAge();
    }

    @Override
    public final void a(BlockState blockState, int n) {
        Ageable ageable = (Ageable)blockState.getBlockData();
        ageable.setAge(n);
        blockState.setBlockData((BlockData)ageable);
        blockState.update();
    }

    @Override
    public final String b(BlockState blockState) {
        return blockState.getType().toString();
    }

    @Override
    public final void a(BlockState blockState, String string) {
        blockState.setType(Material.valueOf((String)string));
        blockState.update(true);
    }

    @Override
    public final int c(BlockState blockState) {
        return ((Sapling)blockState.getBlockData()).getStage();
    }

    @Override
    public final boolean a(Material material) {
        return material == Material.DIRT || material == Material.GRASS_BLOCK || material == Material.FARMLAND || material == Material.PODZOL || material == Material.COARSE_DIRT;
    }

    public static void a(BlockState blockState, BlockFace blockFace) {
        Directional directional = (Directional)blockState.getBlock().getBlockData();
        directional.setFacing(blockFace);
        blockState.getBlock().setBlockData((BlockData)directional);
    }
}
