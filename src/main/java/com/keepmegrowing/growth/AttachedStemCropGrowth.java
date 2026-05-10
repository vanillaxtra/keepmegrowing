
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public final class AttachedStemCropGrowth
extends AbstractCropGrowth {
    public AttachedStemCropGrowth(Material material) {
        super(material, -1, false, -1);
    }

    @Override
    public final boolean a(BlockState blockState, long l, double d) {
        return false;
    }

    @Override
    public final int a(BlockState blockState) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
