
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;

public class VerticalColumnCropGrowth
extends AbstractCropGrowth {
    BlockFace a;
    private Material h;
    private Material i;
    public static String b;

    public VerticalColumnCropGrowth(Material material, int n, boolean bl, int n2, BlockFace blockFace, Material material2, Material material3) {
        super(material, n, bl, n2);
        this.a = blockFace;
        this.i = material2;
        this.h = material3;
    }

    @Override
    public int a(BlockState blockState) {
        return ((Ageable)blockState.getBlockData()).getAge();
    }

    private static boolean columnAllowsGrow(BlockState aboveState, VerticalColumnCropGrowth g) {
        if (aboveState.getType() != g.h) {
            return false;
        }
        if (g.h == Material.WATER) {
            int lev = ((Levelled) aboveState.getBlockData()).getLevel();
            return lev == 0 || lev == 8;
        }
        return true;
    }

    @Override
    public boolean a(BlockState rootState, int currentAge, int targetAge, int maxAge) {
        int targetSaved = targetAge;
        int steps = targetAge - currentAge;
        VerticalColumnCropGrowth g = this;
        BlockState cursor = rootState;
        BlockState above = cursor.getBlock().getRelative(g.a).getState();
        for (int i = 0; i < steps; ++i) {
            if (!columnAllowsGrow(above, g)) {
                cursor.setType(g.c);
                Ageable ageable = (Ageable) cursor.getBlockData();
                ageable.setAge(maxAge);
                cursor.setBlockData((BlockData) ageable);
                cursor.update(true);
                break;
            }
            if (i == 0) {
                cursor.getBlock().setType(g.i);
            }
            cursor = above;
            above = cursor.getBlock().getRelative(g.a).getState();
            if (i == steps - 1) {
                cursor.setType(g.c);
                Ageable top = (Ageable) cursor.getBlockData();
                top.setAge(targetSaved);
                cursor.setBlockData((BlockData) top);
                cursor.update(true);
                break;
            }
            cursor.getBlock().setType(g.i);
        }
        return false;
    }
}
