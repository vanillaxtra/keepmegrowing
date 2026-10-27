package com.keepmegrowing.listener;

import com.keepmegrowing.update.ModrinthUpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class AdminJoinUpdateListener implements Listener {

    private final ModrinthUpdateChecker checker;

    public AdminJoinUpdateListener(ModrinthUpdateChecker checker) {
        this.checker = checker;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!checker.isUpdateAvailable()) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.isOp() && !player.hasPermission("keepmegrowing.admin")) {
            return;
        }
        player.sendMessage(
                ChatColor.GOLD
                        + "[KeepMeGrowing] "
                        + ChatColor.YELLOW
                        + "Update available: "
                        + checker.getInstalledVersion()
                        + " -> "
                        + checker.getLatestVersion());
        player.sendMessage(ChatColor.GRAY + ModrinthUpdateChecker.PROJECT_URL);
    }
}
