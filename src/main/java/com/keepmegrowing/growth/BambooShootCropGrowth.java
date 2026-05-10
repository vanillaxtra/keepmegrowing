
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.BambooStalkCropGrowth;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public final class BambooShootCropGrowth
extends BambooStalkCropGrowth {
    public BambooShootCropGrowth(Material material, int n, boolean bl, int n2) {
        super(material, 13, false, n2);
    }

    @Override
    public final int a(BlockState blockState) {
        return 0;
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        return super.a(blockState, n, n2, n3);
    }
}
