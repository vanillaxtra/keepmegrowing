
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.util.BigTreePlacement;
import com.keepmegrowing.growth.util.StemBlockHelper;
import com.keepmegrowing.growth.util.VersionMaterials;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.Event;
import org.bukkit.event.world.StructureGrowEvent;

public final class SaplingCropGrowth
extends AbstractCropGrowth {
    public SaplingCropGrowth(Material material, int n, boolean bl, int n2) {
        super(material, n, true, n2);
    }

    @Override
    public final int a(BlockState blockState) {
        return VersionMaterials.c.c(blockState);
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        if (!VersionMaterials.a && n2 == 1) {
            StemBlockHelper cfr_ignored_0 = (StemBlockHelper)VersionMaterials.c;
            n = n2;
            Sapling sapling = (Sapling)blockState.getBlock().getBlockData();
            sapling.setStage(n);
            blockState.setBlockData((BlockData)sapling);
            blockState.update(true);
            return false;
        }
        ArrayList<BlockState> states = new ArrayList<>();
        states.add(blockState);
        StructureGrowEvent structureGrowEvent =
                new StructureGrowEvent(blockState.getLocation(), TreeType.TREE, false, null, states);
        Bukkit.getServer().getPluginManager().callEvent((Event)structureGrowEvent);
        boolean bl = !structureGrowEvent.isCancelled() ? BigTreePlacement.a(blockState.getBlock()) : false;
        return bl;
    }

    @Override
    public final boolean a(BlockState blockState, long l, double d) {
        if (this.f < 0) {
            return false;
        }
        System.currentTimeMillis();
        long l2 = System.currentTimeMillis() - l;
        int n = ((AbstractCropGrowth)this).a(blockState);
        SaplingCropGrowth c = this;
        int n2 = c.d;
        int n3 = (int)((double)this.f * d);
        if (n == 999) {
            return true;
        }
        if (n >= n2) {
            c = this;
            return c.e;
        }
        float f = (float)(l2 / 1000L) / 60.0f * (float)n2 / (float)n3;
        int n4 = (int)f;
        if ((n3 = n4 + n) > n2) {
            n3 = n2;
        } else {
            f -= (float)n4;
            float f3 = this.g.nextFloat();
            if (f3 <= f) {
                ++n3;
            }
            if (n3 > n2) {
                n3 = n2;
            }
        }
        if (n3 <= 0 || n >= n3) {
            return false;
        }
        boolean bl = ((AbstractCropGrowth)this).a(blockState, n, n3, n2);
        if (bl) {
            return true;
        }
        c = this;
        if (c.e && n3 >= n2) {
            return !(this instanceof SaplingCropGrowth) || bl;
        }
        System.currentTimeMillis();
        return false;
    }
}
