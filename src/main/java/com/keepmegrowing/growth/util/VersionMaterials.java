
package com.keepmegrowing.growth.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public final class VersionMaterials {
    private static final List<String> LEGACY_VERSIONS = new ArrayList<>();

    static {
        LEGACY_VERSIONS.add("1.8");
        LEGACY_VERSIONS.add("1.9");
        LEGACY_VERSIONS.add("1.10");
        LEGACY_VERSIONS.add("1.11");
        LEGACY_VERSIONS.add("1.12");
        LEGACY_VERSIONS.add("1.13");
        LEGACY_VERSIONS.add("1.14");
        LEGACY_VERSIONS.add("1.15");
        LEGACY_VERSIONS.add("1.16");
        LEGACY_VERSIONS.add("1.17");
        LEGACY_VERSIONS.add("1.18");
        LEGACY_VERSIONS.add("1.19");
        LEGACY_VERSIONS.add("1.20");
        LEGACY_VERSIONS.add("1.21");
    }

    public static boolean a;
    public static boolean b;
    public static GrowthStageCapable c;

    private static String resolvedGameVersion;
    private static int[] resolvedParts;
    private static boolean versionKnown;

    public static void a() {
        resolvedGameVersion = resolveGameVersion();
        versionKnown = resolvedGameVersion != null;
        resolvedParts = versionKnown ? parseVersionParts(resolvedGameVersion) : null;
        if (!versionKnown) {
            return;
        }
        if (usesLegacyMaterials(resolvedGameVersion)) {
            c = new SaplingPlacementHelper();
            a = true;
        } else {
            c = new StemBlockHelper();
            a = false;
        }
        b = !supportsMobGrowth(resolvedGameVersion);
    }

    public static boolean b() {
        return !versionKnown;
    }

    public static String getResolvedGameVersion() {
        return resolvedGameVersion;
    }

    public static boolean supportsEntityLoadEvents() {
        if (resolvedParts == null) {
            return false;
        }
        return resolvedParts[0] >= 26 || (resolvedParts[0] == 1 && resolvedParts[1] >= 17);
    }

    private static String resolveGameVersion() {
        String fromMc = readMinecraftVersion();
        if (fromMc != null && parseVersionParts(fromMc) != null) {
            return fromMc;
        }
        return matchLegacyBukkitVersion(Bukkit.getBukkitVersion());
    }

    private static String readMinecraftVersion() {
        try {
            Method method = Bukkit.class.getMethod("getMinecraftVersion");
            Object value = method.invoke(null);
            if (value instanceof String s && !s.isEmpty()) {
                return s;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static String matchLegacyBukkitVersion(String bukkitVersion) {
        if (bukkitVersion == null || bukkitVersion.isEmpty()) {
            return null;
        }
        for (int i = LEGACY_VERSIONS.size() - 1; i >= 0; i--) {
            String token = LEGACY_VERSIONS.get(i);
            if (containsLegacyToken(bukkitVersion, token)) {
                return token;
            }
        }
        return null;
    }

    private static boolean containsLegacyToken(String bukkitVersion, String token) {
        int idx = bukkitVersion.indexOf(token);
        if (idx < 0) {
            return false;
        }
        if (idx > 0 && Character.isDigit(bukkitVersion.charAt(idx - 1))) {
            return false;
        }
        int after = idx + token.length();
        return after >= bukkitVersion.length() || !Character.isDigit(bukkitVersion.charAt(after));
    }

    private static int[] parseVersionParts(String version) {
        String[] parts = version.split("\\.");
        if (parts.length < 2) {
            return null;
        }
        try {
            return new int[] {Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean usesLegacyMaterials(String version) {
        int[] parts = parseVersionParts(version);
        if (parts == null) {
            return false;
        }
        if (parts[0] >= 26) {
            return false;
        }
        return parts[0] == 1 && parts[1] < 13;
    }

    private static boolean supportsMobGrowth(String version) {
        int[] parts = parseVersionParts(version);
        if (parts == null) {
            return false;
        }
        if (parts[0] >= 26) {
            return true;
        }
        return parts[0] > 1 || parts[1] >= 10;
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
