
package com.keepmegrowing.growth;

import com.keepmegrowing.growth.util.GrowthRandom;
import com.keepmegrowing.growth.util.StemBlockHelper;
import com.keepmegrowing.growth.util.VersionMaterials;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockGrowEvent;

public final class StemFruitGrowth extends AbstractCropGrowth {
    private Material stemMaterial;
    private Material fruitMaterial;
    private static final List<BlockFace> CARDINAL_FACES =
            Collections.unmodifiableList(
                    Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST));

    public StemFruitGrowth(Material stemBlock, Material stemWhenReset, Material fruitType, int n, boolean bl, int n2) {
        super(stemBlock, 7, false, n2);
        this.stemMaterial = stemWhenReset;
        this.fruitMaterial = fruitType;
    }

    @Override
    public final boolean a(BlockState blockState, long lastTimeMs, double multiplier) {
        float f;
        int n;
        if (this.f < 0) {
            return false;
        }
        System.currentTimeMillis();
        if (VersionMaterials.a) {
            if (faceDisplaysFruit(blockState, this.fruitMaterial)) {
                return false;
            }
        }
        long l2 = System.currentTimeMillis() - lastTimeMs;
        int n2 = ((AbstractCropGrowth) this).a(blockState);
        StemFruitGrowth b2 = this;
        int n3 = b2.d;
        int n4 = (int) ((double) this.f * multiplier);
        int n5 = n2 >= n3 ? 1 : 0;
        if (n5 != 0) {
            n2 = 0;
            n3 = 1;
            if ((n4 = (int) (0.25 * (double) n4)) == 0) {
                n4 = 1;
            }
        }
        if ((n = (int) (f = (float) (l2 / 1000L) / 60.0f * (float) n3 / (float) n4)) + n2 >= n3) {
            n4 = n;
        } else {
            n4 = n + n2;
            f -= (float) n;
            float f3 = this.g.nextFloat();
            if (f3 <= f) {
                ++n4;
            }
        }
        if (n4 <= 0 || n2 >= n4) {
            return false;
        }
        int n6 = n4;
        n2 = n5;
        n4 = n3;
        int n7 = n6;
        StemFruitGrowth b3 = this;
        if (n2 != 0) {
            b3.trySpawnFruit(blockState);
        } else {
            n2 = n7;
            if (n7 > n4) {
                n2 = n4;
            }
            VersionMaterials.c.a(blockState, n2);
            if (n7 >= n4 + 2) {
                b3.trySpawnFruit(blockState);
            }
        }
        System.currentTimeMillis();
        return false;
    }

    private static boolean faceDisplaysFruit(BlockState blockState2, Material fruit) {
        return blockState2.getBlock().getRelative(BlockFace.NORTH).getType() == fruit
                ? true
                : (blockState2.getBlock().getRelative(BlockFace.EAST).getType() == fruit
                        ? true
                        : (blockState2.getBlock().getRelative(BlockFace.SOUTH).getType() == fruit
                                ? true
                                : blockState2.getBlock().getRelative(BlockFace.WEST).getType() == fruit));
    }

    @Override
    public final int a(BlockState blockState) {
        return VersionMaterials.c.a(blockState);
    }

    private void trySpawnFruit(BlockState stemState) {
        List<BlockFace> candidates = new ArrayList<>(4);
        for (BlockFace blockFace : CARDINAL_FACES) {
            if (stemState.getBlock().getRelative(blockFace).getType() != Material.AIR
                    || !isSoilForFruitBlock(
                            stemState.getBlock().getRelative(blockFace).getRelative(BlockFace.DOWN).getType())) {
                continue;
            }
            candidates.add(blockFace);
        }
        if (candidates.isEmpty()) {
            return;
        }
        BlockFace chosen = candidates.get(GrowthRandom.a(0, candidates.size() - 1));
        BlockState placeState = stemState.getBlock().getRelative(chosen).getState();
        placeState.setType(this.fruitMaterial);
        BlockGrowEvent growEvent =
                new BlockGrowEvent(stemState.getBlock().getRelative(chosen), placeState);
        Bukkit.getServer().getPluginManager().callEvent((Event) growEvent);
        if (!growEvent.isCancelled()) {
            stemState.getBlock().getRelative(chosen).setType(this.fruitMaterial);
            if (!VersionMaterials.a) {
                stemState.getBlock().setType(this.stemMaterial);
                StemBlockHelper cfr_ignored_0 = (StemBlockHelper) VersionMaterials.c;
                StemBlockHelper.a(stemState, chosen);
            }
        }
    }

    private static boolean isSoilForFruitBlock(Material material) {
        return VersionMaterials.c.a(material);
    }

    @Override
    public final boolean a(BlockState blockState, int n, int n2, int n3) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
