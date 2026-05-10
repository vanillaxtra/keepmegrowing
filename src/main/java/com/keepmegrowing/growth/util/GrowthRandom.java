
package com.keepmegrowing.growth.util;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public final class GrowthRandom {
    public static int a(int n, int n2) {
        return ThreadLocalRandom.current().nextInt(0, n2 + 1);
    }

    public static boolean a(BlockState blockState, Material material) {
        if (blockState.getBlock().getRelative(BlockFace.NORTH).getType() != material) {
            return true;
        }
        if (blockState.getBlock().getRelative(BlockFace.EAST).getType() != material) {
            return true;
        }
        if (blockState.getBlock().getRelative(BlockFace.SOUTH).getType() != material) {
            return true;
        }
        return blockState.getBlock().getRelative(BlockFace.WEST).getType() != material;
    }
}
