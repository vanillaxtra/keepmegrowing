
package com.keepmegrowing.growth.util;

import com.keepmegrowing.growth.util.VersionMaterials;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class BigTreePlacement {
    private static final List<BlockFace> a = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.NORTH_EAST, BlockFace.SELF);
    private static final List<BlockFace> b = Arrays.asList(BlockFace.NORTH_WEST, BlockFace.SELF, BlockFace.WEST, BlockFace.NORTH);
    private static final List<BlockFace> c = Arrays.asList(BlockFace.WEST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.SELF);
    private static final List<BlockFace> d = Arrays.asList(BlockFace.SELF, BlockFace.SOUTH_EAST, BlockFace.EAST, BlockFace.SOUTH);
    private static final List<List<BlockFace>> e = Arrays.asList(a, b, c, d);
    private static final Map<String, TreeType> f;
    private static final Map<String, TreeType> g;

    public static boolean a(Block block2) {
        block10: {
            Block block3;
            String string;
            block9: {
                boolean bl;
                block8: {
                    string = VersionMaterials.c.b(block2.getState());
                    if (!g.containsKey(string)) break block9;
                    block3 = block2;
                    String string2 = VersionMaterials.c.b(block3.getState());
                    TreeType treeType = g.get(string2);
                    for (List<BlockFace> list : e) {
                        if (!list.stream().map(arg_0 -> ((Block)block3).getRelative(arg_0)).allMatch(block -> VersionMaterials.c.b(block.getState()).equals(string2))) continue;
                        list.stream().map(arg_0 -> ((Block)block3).getRelative(arg_0)).forEach(block -> block.setType(Material.AIR));
                        Location location = block3.getRelative(list.get(0)).getLocation();
                        if (block3.getWorld().generateTree(location, treeType)) {
                            bl = true;
                            break block8;
                        }
                        list.stream().map(arg_0 -> ((Block)block3).getRelative(arg_0)).forEach(block -> VersionMaterials.c.a(block.getState(), string2));
                    }
                    bl = false;
                }
                if (bl) break block10;
            }
            if (f.containsKey(string)) {
                TreeType tallType = f.getOrDefault(string, TreeType.TREE);
                block2.setType(Material.AIR);
                if (!block2.getWorld().generateTree(block2.getLocation(), tallType)) {
                    VersionMaterials.c.a(block2.getState(), string);
                    return false;
                }
            }
        }
        return true;
    }

    static {
        HashMap<String, TreeType> hashMap = new HashMap<String, TreeType>();
        hashMap.put("ACACIA_SAPLING", TreeType.ACACIA);
        hashMap.put("BIRCH_SAPLING", TreeType.BIRCH);
        hashMap.put("JUNGLE_SAPLING", TreeType.SMALL_JUNGLE);
        hashMap.put("OAK_SAPLING", TreeType.TREE);
        hashMap.put("SPRUCE_SAPLING", TreeType.REDWOOD);
        try {
            hashMap.put("MANGROVE_PROPAGULE", TreeType.MANGROVE);
        }
        catch (Throwable throwable) {}
        try {
            hashMap.put("CHERRY_SAPLING", TreeType.CHERRY);
        }
        catch (Throwable throwable) {}
        f = Collections.unmodifiableMap(hashMap);
        hashMap = new HashMap<String, TreeType>();
        hashMap.put("DARK_OAK_SAPLING", TreeType.DARK_OAK);
        hashMap.put("SPRUCE_SAPLING", TreeType.MEGA_REDWOOD);
        hashMap.put("JUNGLE_SAPLING", TreeType.JUNGLE);
        g = Collections.unmodifiableMap(hashMap);
    }
}
