
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.util.VersionMaterials;
import com.keepmegrowing.growth.BambooSectionGrowth;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public final class AgeableCropGrowth
extends AbstractCropGrowth {
    public AgeableCropGrowth(Material material, int n, boolean bl, int n2) {
        super(material, n, bl, n2);
        BambooSectionGrowth.c = "96356758";
    }

    @Override
    public final int a(BlockState blockState) {
        return VersionMaterials.c.a(blockState);
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        if (blockState.getType().toString().equals("TORCHFLOWER_CROP") && n2 == 2) {
            blockState.setType(Material.valueOf((String)"TORCHFLOWER"));
            blockState.update(true, false);
        } else {
            VersionMaterials.c.a(blockState, n2);
        }
        return false;
    }
}
