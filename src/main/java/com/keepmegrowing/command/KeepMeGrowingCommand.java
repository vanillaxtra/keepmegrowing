package com.keepmegrowing.command;

import com.keepmegrowing.GrowthRateController;
import com.keepmegrowing.KeepMeGrowing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public final class KeepMeGrowingCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUBCOMMANDS =
            Arrays.asList("reload", "debug", "sprint", "freeze", "unfreeze", "rate");

    private static boolean canUse(CommandSender sender) {
        return sender.isOp() || sender.hasPermission("keepmegrowing.admin");
    }

    private static void msg(CommandSender sender, String raw) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', raw));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!canUse(sender)) {
            msg(sender, "&cYou do not have permission.");
            return true;
        }
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin == null) {
            return true;
        }
        GrowthRateController rates = plugin.getGrowthRateController();

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            rates.loadFromConfig(plugin.getConfig());
            plugin.getGrowthConfig().loadFromConfig();
            plugin.getCropGrowthRegistry().registerCrops();
            if (plugin.getMobGrowthRegistry() != null) {
                plugin.getMobGrowthRegistry().registerMobs();
            }
            plugin.startLoadedGrowthSchedulers();
            msg(sender, "&a[KeepMeGrowing] Configuration reloaded.");
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("debug")) {
            msg(
                    sender,
                    "&a[KeepMeGrowing] toProcessQueue: " + plugin.getChunkStorage().chunkLoadQueue.size());
            msg(
                    sender,
                    "&a[KeepMeGrowing] setChunkUnloadAfterCompletition: "
                            + plugin.getChunkStorage().pendingUnloadAfterGrowth.size());
            msg(
                    sender,
                    "&a[KeepMeGrowing] inProcessQueue: "
                            + plugin.getChunkStorage().growthProcessingCooldown.size());
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("sprint")) {
            boolean next = !rates.isSprintActive();
            rates.setSprintActive(next);
            msg(
                    sender,
                    "&a[KeepMeGrowing] Sprint mode "
                            + (next ? "&eenabled" : "&7disabled")
                            + "&a (effective x"
                            + String.format("%.2f", rates.getEffectiveMultiplier())
                            + ").");
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("freeze")) {
            rates.setFrozen(true);
            msg(sender, "&a[KeepMeGrowing] Growth &cfrozen&a (offline crop/mob simulation paused).");
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("unfreeze")) {
            rates.setFrozen(false);
            msg(
                    sender,
                    "&a[KeepMeGrowing] Growth &aunfrozen&a (effective x"
                            + String.format("%.2f", rates.getEffectiveMultiplier())
                            + ").");
            return true;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("rate")) {
            try {
                double v = Double.parseDouble(args[1]);
                if (v <= 0.0 || Double.isNaN(v) || Double.isInfinite(v)) {
                    msg(sender, "&cRate must be a positive number.");
                    return true;
                }
                rates.setBaseMultiplier(v);
                msg(
                        sender,
                        "&a[KeepMeGrowing] Base multiplier set to &e"
                                + v
                                + "&a (effective x"
                                + String.format("%.2f", rates.getEffectiveMultiplier())
                                + ").");
            } catch (NumberFormatException e) {
                msg(sender, "&cInvalid number.");
            }
            return true;
        }

        msg(
                sender,
                "&e/"
                        + label
                        + " &7reload|debug|sprint|freeze|unfreeze|rate <n>");
        return true;
    }

    @Override
    public List<String> onTabComplete(
            CommandSender sender, Command command, String alias, String[] args) {
        if (!canUse(sender)) {
            return List.of();
        }
        if (args.length == 1) {
            String p = args[0].toLowerCase();
            List<String> out = new ArrayList<>();
            for (String s : SUBCOMMANDS) {
                if (s.startsWith(p)) {
                    out.add(s);
                }
            }
            return out;
        }
        return List.of();
    }
}
