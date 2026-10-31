package com.keepmegrowing.updates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keepmegrowing.KeepMeGrowing;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class UpdateChecker implements Listener {

    private static final String MODRINTH_SLUG = "keep-me-growing";
    private static final String GITHUB_REPO = "vanillaxtra/keepmegrowing";
    private static final String USER_AGENT = "KeepMeGrowing-UpdateChecker/1.0";
    private static final String PERMISSION = "keepmegrowing.update.notify";

    private static final String CONSOLE_RESET = "\u001B[0m";
    private static final String CONSOLE_BRIGHT_GREEN = "\u001B[92m";
    private static final String CONSOLE_YELLOW = "\u001B[33m";
    private static final String CONSOLE_BRIGHT_BLUE = "\u001B[94m";

    private final KeepMeGrowing plugin;
    private final String currentVersion;
    private final Map<UUID, LocalDate> notifiedPlayers = new HashMap<>();

    private volatile boolean updateAvailable;
    private volatile String latestVersion = "";
    private volatile String modrinthUrl = "";
    private volatile String githubUrl = "";

    private volatile RemoteVersion modrinthResult;
    private volatile RemoteVersion githubResult;

    public UpdateChecker(KeepMeGrowing plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        checkForUpdates()
                .thenAccept(
                        hasUpdate -> {
                            if (hasUpdate) {
                                displayConsoleUpdateMessage();
                            }
                        })
                .exceptionally(
                        ex -> {
                            plugin.getLogger().warning("Failed to check for updates: " + ex.getMessage());
                            return null;
                        });
    }

    public CompletableFuture<Boolean> checkForUpdates() {
        modrinthResult = null;
        githubResult = null;
        return CompletableFuture.allOf(fetchModrinthVersion(), fetchGitHubVersion())
                .thenApply(
                        ignored -> {
                            Version current = new Version(currentVersion);
                            Version latest = Version.ZERO;
                            String bestVersionLabel = "";

                            RemoteVersion modrinth = modrinthResult;
                            RemoteVersion github = githubResult;

                            if (modrinth != null && modrinth.version.compareTo(latest) > 0) {
                                latest = modrinth.version;
                                bestVersionLabel = modrinth.versionLabel;
                            }
                            if (github != null && github.version.compareTo(latest) > 0) {
                                latest = github.version;
                                bestVersionLabel = github.versionLabel;
                            }

                            if (modrinth != null) {
                                modrinthUrl = modrinth.pageUrl;
                            }
                            if (github != null) {
                                githubUrl = github.pageUrl;
                            }

                            if (latest.compareTo(current) > 0) {
                                latestVersion =
                                        bestVersionLabel.isEmpty() ? latest.toString() : bestVersionLabel;
                                updateAvailable = true;
                                if (plugin.getConfig().getBoolean("debug", false)) {
                                    plugin.getLogger()
                                            .info(
                                                    "[UpdateChecker] update available: current="
                                                            + current
                                                            + " latest="
                                                            + latest);
                                }
                                return true;
                            }
                            updateAvailable = false;
                            if (plugin.getConfig().getBoolean("debug", false)) {
                                plugin.getLogger()
                                        .info("[UpdateChecker] up to date: current=" + current);
                            }
                            return false;
                        });
    }

    private CompletableFuture<Void> fetchModrinthVersion() {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        URL url =
                                new URL(
                                        "https://api.modrinth.com/v2/project/"
                                                + MODRINTH_SLUG
                                                + "/version");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setRequestProperty("User-Agent", USER_AGENT);
                        connection.setRequestProperty("Accept", "application/json");
                        if (connection.getResponseCode() != 200) {
                            plugin.getLogger()
                                    .warning(
                                            "Modrinth update check failed. HTTP "
                                                    + connection.getResponseCode());
                            return;
                        }
                        JsonArray versions =
                                JsonParser.parseString(readResponse(connection)).getAsJsonArray();
                        JsonObject chosen = pickNewestBySemver(versions, true);
                        if (chosen == null) {
                            chosen = pickNewestBySemver(versions, false);
                        }
                        if (chosen == null || !chosen.has("version_number")) {
                            return;
                        }
                        String versionNumber = chosen.get("version_number").getAsString();
                        String versionId = chosen.get("id").getAsString();
                        String pageUrl =
                                "https://modrinth.com/plugin/"
                                        + MODRINTH_SLUG
                                        + "/version/"
                                        + versionId;
                        modrinthResult =
                                new RemoteVersion(new Version(versionNumber), versionNumber, pageUrl);
                    } catch (Exception e) {
                        plugin.getLogger().warning("Modrinth update check error: " + e.getMessage());
                    }
                });
    }

    private CompletableFuture<Void> fetchGitHubVersion() {
        return CompletableFuture.runAsync(
                () -> {
                    try {
                        URL url =
                                new URL(
                                        "https://api.github.com/repos/" + GITHUB_REPO + "/releases/latest");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setRequestProperty("User-Agent", USER_AGENT);
                        connection.setRequestProperty("Accept", "application/vnd.github+json");
                        if (connection.getResponseCode() != 200) {
                            plugin.getLogger()
                                    .warning(
                                            "GitHub update check failed. HTTP "
                                                    + connection.getResponseCode());
                            return;
                        }
                        JsonObject release =
                                JsonParser.parseString(readResponse(connection)).getAsJsonObject();
                        if (!release.has("tag_name")) {
                            return;
                        }
                        String tagName = release.get("tag_name").getAsString();
                        String versionLabel = tagName.replaceAll("^v", "");
                        String pageUrl =
                                release.has("html_url")
                                        ? release.get("html_url").getAsString()
                                        : "https://github.com/" + GITHUB_REPO + "/releases/latest";
                        githubResult =
                                new RemoteVersion(new Version(versionLabel), versionLabel, pageUrl);
                    } catch (Exception e) {
                        plugin.getLogger().warning("GitHub update check error: " + e.getMessage());
                    }
                });
    }

    private static JsonObject pickNewestBySemver(JsonArray versions, boolean releaseOnly) {
        JsonObject newest = null;
        Version newestVersion = Version.ZERO;
        for (JsonElement element : versions) {
            JsonObject version = element.getAsJsonObject();
            if (releaseOnly
                    && version.has("version_type")
                    && !"release".equals(version.get("version_type").getAsString())) {
                continue;
            }
            if (!version.has("version_number")) {
                continue;
            }
            Version candidate = new Version(version.get("version_number").getAsString());
            if (newest == null || candidate.compareTo(newestVersion) > 0) {
                newest = version;
                newestVersion = candidate;
            }
        }
        return newest;
    }

    private static String readResponse(HttpURLConnection connection) throws Exception {
        try (BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } finally {
            connection.disconnect();
        }
    }

    private void displayConsoleUpdateMessage() {
        plugin.getLogger()
                .info(
                        CONSOLE_BRIGHT_BLUE
                                + "────────────────────────────────────────────────────"
                                + CONSOLE_RESET);
        plugin.getLogger()
                .info(CONSOLE_BRIGHT_GREEN + "         KeepMeGrowing Update Available" + CONSOLE_RESET);
        plugin.getLogger()
                .info(
                        CONSOLE_BRIGHT_BLUE
                                + "────────────────────────────────────────────────────"
                                + CONSOLE_RESET);
        plugin.getLogger()
                .info("Current version: " + CONSOLE_YELLOW + currentVersion + CONSOLE_RESET);
        plugin.getLogger()
                .info("Latest version:  " + CONSOLE_BRIGHT_GREEN + latestVersion + CONSOLE_RESET);
        if (!modrinthUrl.isEmpty()) {
            plugin.getLogger().info("Modrinth: " + CONSOLE_BRIGHT_GREEN + modrinthUrl + CONSOLE_RESET);
        }
        if (!githubUrl.isEmpty()) {
            plugin.getLogger().info("GitHub:  " + CONSOLE_BRIGHT_GREEN + githubUrl + CONSOLE_RESET);
        }
        plugin.getLogger()
                .info(
                        CONSOLE_BRIGHT_BLUE
                                + "────────────────────────────────────────────────────"
                                + CONSOLE_RESET);
    }

    private void sendUpdateNotification(Player player) {
        if (!updateAvailable || !player.hasPermission(PERMISSION)) {
            return;
        }
        TextColor blue = TextColor.fromHexString("#3B82F6");
        TextColor green = TextColor.fromHexString("#22C55E");
        TextColor red = TextColor.fromHexString("#EF4444");
        TextColor orange = TextColor.fromHexString("#F97316");
        TextColor white = TextColor.fromHexString("#F3F4F6");

        if (!(player instanceof Audience audience)) {
            return;
        }
        audience.sendMessage(Component.empty());
        audience.sendMessage(Component.text("----- KeepMeGrowing Update -----").color(blue));
        audience.sendMessage(Component.text("New update available!").color(green));
        audience.sendMessage(
                Component.text("Current: ")
                        .color(white)
                        .append(Component.text(currentVersion).color(red))
                        .append(Component.text("  Latest: ").color(white))
                        .append(Component.text(latestVersion).color(green)));
        if (!modrinthUrl.isEmpty()) {
            audience.sendMessage(
                    Component.text("[Download on Modrinth]")
                            .color(orange)
                            .clickEvent(ClickEvent.openUrl(modrinthUrl))
                            .hoverEvent(
                                    HoverEvent.showText(
                                            Component.text("Open Modrinth release ")
                                                    .color(white)
                                                    .append(Component.text(latestVersion).color(green)))));
        }
        if (!githubUrl.isEmpty()) {
            audience.sendMessage(
                    Component.text("[View on GitHub]")
                            .color(orange)
                            .clickEvent(ClickEvent.openUrl(githubUrl))
                            .hoverEvent(
                                    HoverEvent.showText(
                                            Component.text("Open GitHub release ")
                                                    .color(white)
                                                    .append(Component.text(latestVersion).color(green)))));
        }
        audience.sendMessage(Component.text("--------------------------------").color(blue));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(PERMISSION)) {
            return;
        }
        UUID playerId = player.getUniqueId();
        LocalDate today = LocalDate.now();
        notifiedPlayers.entrySet().removeIf(entry -> entry.getValue().isBefore(today));
        if (today.equals(notifiedPlayers.get(playerId))) {
            return;
        }
        if (updateAvailable) {
            plugin.getServer()
                    .getScheduler()
                    .runTaskLater(
                            plugin,
                            () -> {
                                if (player.isOnline()) {
                                    sendUpdateNotification(player);
                                    notifiedPlayers.put(playerId, today);
                                }
                            },
                            40L);
            return;
        }
        checkForUpdates()
                .thenAccept(
                        hasUpdate -> {
                            if (!hasUpdate) {
                                return;
                            }
                            plugin.getServer()
                                    .getScheduler()
                                    .runTask(
                                            plugin,
                                            () -> {
                                                if (player.isOnline()) {
                                                    sendUpdateNotification(player);
                                                    notifiedPlayers.put(playerId, today);
                                                }
                                            });
                        });
    }

    private record RemoteVersion(Version version, String versionLabel, String pageUrl) {}
}
