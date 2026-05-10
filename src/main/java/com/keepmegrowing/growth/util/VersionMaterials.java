
package com.keepmegrowing.growth.util;

import com.keepmegrowing.growth.util.GrowthStageCapable;
import com.keepmegrowing.growth.util.SaplingPlacementHelper;
import com.keepmegrowing.growth.util.StemBlockHelper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public final class VersionMaterials {
    private static List<String> d;
    private static Map<String, Integer> e;
    public static boolean a;
    public static boolean b;
    public static GrowthStageCapable c;

    public static void a() {
        d = new ArrayList<String>();
        d.add("1.8");
        d.add("1.9");
        d.add("1.10");
        d.add("1.11");
        d.add("1.12");
        d.add("1.13");
        d.add("1.14");
        d.add("1.15");
        d.add("1.16");
        d.add("1.17");
        d.add("1.18");
        d.add("1.19");
        d.add("1.20");
        d.add("1.21");
        if (VersionMaterials.b()) {
            return;
        }
        String string = VersionMaterials.c();
        e = new LinkedHashMap<String, Integer>();
        int n = 0;
        for (String string2 : d) {
            e.put(string2, n);
            ++n;
        }
        if (e.get(string) < e.get("1.13")) {
            c = new SaplingPlacementHelper();
            a = true;
        } else {
            c = new StemBlockHelper();
        }
        if (e.get(string) < e.get("1.10")) {
            b = true;
        }
    }

    private static String c() {
        for (int i = d.size() - 1; i >= 0; --i) {
            String string = d.get(i);
            if (!Bukkit.getBukkitVersion().contains(string)) continue;
            return string;
        }
        return null;
    }

    public static boolean b() {
        return VersionMaterials.c() == null;
    }

    public static Material a(String string) {
        if (a) {
            if (string.equals("CARROTS")) {
                return Material.CARROT;
            }
            if (string.equals("POTATOES")) {
                return Material.POTATO;
            }
            if (string.equals("BEETROOTS")) {
                return Material.valueOf((String)"BEETROOT_BLOCK");
            }
            if (string.equals("NETHER_WART")) {
                return Material.valueOf((String)"NETHER_WARTS");
            }
            if (string.equals("WHEAT")) {
                return Material.valueOf((String)"CROPS");
            }
            if (string.equals("POTATOES")) {
                return Material.POTATO;
            }
            if (string.equals("SUGAR_CANE")) {
                return Material.valueOf((String)"SUGAR_CANE_BLOCK");
            }
            if (string.equals("ATTACHED_MELON_STEM")) {
                return Material.valueOf((String)"MELON_STEM");
            }
            if (string.equals("ATTACHED_PUMPKIN_STEM")) {
                return Material.valueOf((String)"PUMPKIN_STEM");
            }
            if (string.equals("MELON")) {
                return Material.valueOf((String)"MELON_BLOCK");
            }
            return Material.valueOf((String)string);
        }
        return Material.valueOf((String)string);
    }
}
