
package com.keepmegrowing.growth.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    private static final Pattern MODERN_BUKKIT_VERSION =
            Pattern.compile("(?:^|[\\s(])(\\d{2,})\\.(\\d+)");

    private static String resolveGameVersion() {
        String fromMc = readMinecraftVersion();
        String normalized = normalizeGameVersion(fromMc);
        if (normalized != null) {
            return normalized;
        }
        String fromPaperBuild = readPaperMinecraftVersionId();
        normalized = normalizeGameVersion(fromPaperBuild);
        if (normalized != null) {
            return normalized;
        }
        normalized = matchModernBukkitVersion(Bukkit.getBukkitVersion());
        if (normalized != null) {
            return normalized;
        }
        return matchLegacyBukkitVersion(Bukkit.getBukkitVersion());
    }

    /** e.g. 26.3, 26.3-rc2 -> 26.3 for feature gates */
    static String normalizeGameVersion(String raw) {
        int[] parts = parseVersionParts(raw);
        if (parts == null) {
            return null;
        }
        return parts[0] + "." + parts[1];
    }

    private static String readPaperMinecraftVersionId() {
        try {
            Class<?> info = Class.forName("io.papermc.paper.ServerBuildInfo");
            Method buildInfo = info.getMethod("buildInfo");
            Object build = buildInfo.invoke(null);
            Method versionId = build.getClass().getMethod("minecraftVersionId");
            Object value = versionId.invoke(build);
            if (value instanceof String s && !s.isEmpty()) {
                return s;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static String matchModernBukkitVersion(String bukkitVersion) {
        if (bukkitVersion == null || bukkitVersion.isEmpty()) {
            return null;
        }
        Matcher matcher = MODERN_BUKKIT_VERSION.matcher(bukkitVersion);
        if (!matcher.find()) {
            return null;
        }
        try {
            int major = Integer.parseInt(matcher.group(1));
            int minor = Integer.parseInt(matcher.group(2));
            if (major >= 26) {
                return major + "." + minor;
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
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
        if (version == null || version.isEmpty()) {
            return null;
        }
        String[] parts = version.split("\\.");
        if (parts.length < 2) {
            return null;
        }
        int major = leadingInt(parts[0]);
        int minor = leadingInt(parts[1]);
        if (major < 0 || minor < 0) {
            return null;
        }
        return new int[] {major, minor};
    }

    private static int leadingInt(String segment) {
        if (segment == null || segment.isEmpty()) {
            return -1;
        }
        int end = 0;
        while (end < segment.length() && Character.isDigit(segment.charAt(end))) {
            end++;
        }
        if (end == 0) {
            return -1;
        }
        try {
            return Integer.parseInt(segment.substring(0, end));
        } catch (NumberFormatException e) {
            return -1;
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
