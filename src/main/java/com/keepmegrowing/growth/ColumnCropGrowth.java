
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.util.GrowthRandom;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public final class ColumnCropGrowth
extends AbstractCropGrowth {
    public ColumnCropGrowth(Material material, int n, boolean bl, int n2) {
        super(material, 2, false, n2);
    }

    @Override
    public final int a(BlockState blockState) {
        if (blockState.getBlock().getRelative(BlockFace.UP).getType() == this.c) {
            if (blockState.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == this.c) {
                return 2;
            }
            if (this.c == Material.CACTUS && blockState.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR && GrowthRandom.a(blockState = blockState.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getState(), Material.AIR)) {
                return 999;
            }
            return 1;
        }
        if (this.c == Material.CACTUS && blockState.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR && GrowthRandom.a(blockState = blockState.getBlock().getRelative(BlockFace.UP).getState(), Material.AIR)) {
            return 999;
        }
        return 0;
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        if (blockState.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
            blockState.getBlock().getRelative(BlockFace.UP).setType(this.c);
        }
        if (n2 >= 2 && blockState.getBlock().getRelative(BlockFace.UP).getType() == this.c && blockState.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.AIR) {
            blockState.getBlock().getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(this.c);
        }
        return false;
    }
}
