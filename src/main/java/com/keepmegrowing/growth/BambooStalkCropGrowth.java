
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.BambooSectionGrowth;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bamboo;

public class BambooStalkCropGrowth
extends AbstractCropGrowth {
    private static List<List<BambooSectionGrowth>> a;

    public BambooStalkCropGrowth(Material material, int n, boolean bl, int n2) {
        super(material, n, bl, n2);
        a = new ArrayList<>(16);
        List<BambooSectionGrowth> row = new ArrayList<>(1);
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(2);
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.SMALL));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(3);
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.SMALL));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(4);
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(5);
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(0, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(6);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(7);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(8);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(9);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(10);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(11);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(12);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(13);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(14);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
        row = new ArrayList<BambooSectionGrowth>(15);
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.NONE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.SMALL));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        row.add(new BambooSectionGrowth(1, Bamboo.Leaves.LARGE));
        a.add(row);
    }

    @Override
    public int a(BlockState object) {
        int n;
        object = object.getBlock().getRelative(BlockFace.UP).getState();
        for (n = 0; object.getType() == this.c && n < 13; ++n) {
            object = object.getBlock().getRelative(BlockFace.UP).getState();
        }
        if (object.getType() != Material.AIR) {
            return this.d;
        }
        return n;
    }

    @Override
    public boolean a(BlockState blockState, int n, int n2, int n3) {
        n2 -= n;
        BlockState blockState2 = blockState;
        if (n > 0) {
            blockState2 = blockState.getWorld().getBlockAt(blockState.getX(), blockState.getY() + n, blockState.getZ()).getState();
        }
        int n4 = 0;
        for (int i = 0; i < n2 && (blockState2 = blockState2.getBlock().getRelative(BlockFace.UP).getState()).getType() == Material.AIR; ++i) {
            ++n4;
        }
        n2 = n4;
        if ((n2 += n) <= 0 || n >= n2) {
            return false;
        }
        List<BambooSectionGrowth> list = a.get(n2);
        blockState2 = blockState;
        for (int i = 0; i < n2 + 1; ++i) {
            Bamboo bamboo;
            BlockState blockState3 = blockState2;
            BambooSectionGrowth u2 = list.get(i);
            boolean bl = false;
            if (blockState3.getType() != Material.BAMBOO) {
                blockState3.setType(Material.BAMBOO);
                bl = true;
            }
            if ((bamboo = (Bamboo)blockState3.getBlockData()).getAge() != u2.a) {
                bamboo.setAge(u2.a);
                bl = true;
            }
            if (bamboo.getLeaves() != u2.b) {
                bamboo.setLeaves(u2.b);
                bl = true;
            }
            if (bl) {
                blockState3.setBlockData((BlockData)bamboo);
                blockState3.update(true, false);
            }
            blockState2 = blockState2.getBlock().getRelative(BlockFace.UP).getState();
        }
        return false;
    }
}
