package com.keepmegrowing.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keepmegrowing.KeepMeGrowing;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public final class ModrinthUpdateChecker {

    public static final String PROJECT_URL = "https://modrinth.com/plugin/keep-me-growing";
    private static final String VERSIONS_URL =
            "https://api.modrinth.com/v2/project/keep-me-growing/version";

    private final KeepMeGrowing plugin;
    private volatile String latestVersion;
    private volatile boolean updateAvailable;

    public ModrinthUpdateChecker(KeepMeGrowing plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer()
                .getScheduler()
                .runTaskAsynchronously(plugin, this::fetchAndAnnounce);
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getInstalledVersion() {
        return plugin.getDescription().getVersion();
    }

    private void fetchAndAnnounce() {
        String installed = getInstalledVersion();
        String latest;
        try {
            latest = fetchLatestVersion();
        } catch (Exception e) {
            plugin.getLogger()
                    .log(Level.WARNING, "Could not check Modrinth for updates: " + e.getMessage());
            return;
        }
        if (latest == null || latest.isEmpty()) {
            return;
        }
        latestVersion = latest;
        updateAvailable = compareVersions(latest, installed) > 0;
        plugin.getServer()
                .getScheduler()
                .runTask(
                        plugin,
                        () -> {
                            if (!plugin.isEnabled()) {
                                return;
                            }
                            if (updateAvailable) {
                                plugin.getLogger()
                                        .info(
                                                "Update available: "
                                                        + installed
                                                        + " -> "
                                                        + latest
                                                        + "  "
                                                        + PROJECT_URL);
                            } else {
                                plugin.getLogger()
                                        .info("Up to date (" + installed + ").");
                            }
                        });
    }

    private String fetchLatestVersion() throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(VERSIONS_URL).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty(
                "User-Agent",
                "KeepMeGrowing/" + plugin.getDescription().getVersion() + " (" + PROJECT_URL + ")");
        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IllegalStateException("Modrinth HTTP " + code);
        }
        try (InputStreamReader reader =
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)) {
            JsonArray versions = JsonParser.parseReader(reader).getAsJsonArray();
            if (versions.size() == 0) {
                return null;
            }
            JsonObject first = versions.get(0).getAsJsonObject();
            return first.has("version_number") ? first.get("version_number").getAsString() : null;
        } finally {
            conn.disconnect();
        }
    }

    static int compareVersions(String a, String b) {
        int[] left = parseInts(a);
        int[] right = parseInts(b);
        int n = Math.max(left.length, right.length);
        for (int i = 0; i < n; i++) {
            int l = i < left.length ? left[i] : 0;
            int r = i < right.length ? right[i] : 0;
            if (l != r) {
                return Integer.compare(l, r);
            }
        }
        return 0;
    }

    private static int[] parseInts(String version) {
        String[] parts = version.split("[^0-9]+");
        int[] out = new int[parts.length];
        int n = 0;
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            try {
                out[n++] = Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
            }
        }
        if (n == out.length) {
            return out;
        }
        int[] trimmed = new int[n];
        System.arraycopy(out, 0, trimmed, 0, n);
        return trimmed;
    }
}
