
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.VerticalColumnCropGrowth;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public final class KelpColumnCropGrowth
extends VerticalColumnCropGrowth {
    public KelpColumnCropGrowth(Material material, int n, boolean bl, int n2, BlockFace blockFace, Material material2, Material material3) {
        super(material, 25, false, n2, blockFace, material2, material3);
    }

    @Override
    public final int a(BlockState blockState) {
        int n = 0;
        blockState = blockState.getBlock().getRelative(this.a).getState();
        while (blockState.getType() != this.c) {
            KelpColumnCropGrowth self = this;
            if (n >= self.d) break;
            blockState = blockState.getBlock().getRelative(this.a).getState();
            ++n;
        }
        if (blockState.getType() == this.c) {
            return super.a(blockState);
        }
        return this.d;
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        int n4 = 0;
        blockState = blockState.getBlock().getRelative(this.a).getState();
        while (blockState.getType() != this.c) {
            KelpColumnCropGrowth self = this;
            if (n4 >= self.d) break;
            blockState = blockState.getBlock().getRelative(this.a).getState();
            ++n4;
        }
        if (blockState.getType() == this.c) {
            super.a(blockState, n, n2, n3);
        }
        return false;
    }
}
