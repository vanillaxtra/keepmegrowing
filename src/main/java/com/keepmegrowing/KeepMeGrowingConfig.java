package com.keepmegrowing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class KeepMeGrowingConfig {
    private Set<String> blacklistedWorlds;
    
    private boolean unloadSpawnChunks;
    private int loadedChunkCropTickTicks;
    private int loadedChunkMobTickTicks;
    private boolean scanChunkOnUnload;
    
    private Set<String> worldsSpawnRestored;
    private Map<String, Integer> plantGrowthMinutes;
    private Map<String, Integer> mobGrowthMinutes;

    public final void loadFromConfig() {
        if (this.worldsSpawnRestored != null) {
            for (String worldName : this.worldsSpawnRestored) {
                if (Bukkit.getWorld(worldName) == null) {
                    continue;
                }
                Bukkit.getWorld(worldName).setKeepSpawnInMemory(true);
            }
        }
        this.blacklistedWorlds = new HashSet<>();
        this.plantGrowthMinutes = new HashMap<>();
        this.mobGrowthMinutes = new HashMap<>();
        this.worldsSpawnRestored = new HashSet<>();
        FileConfiguration config = KeepMeGrowing.a().getConfig();
        for (String worldName : config.getStringList("blacklisted-worlds")) {
            this.blacklistedWorlds.add(worldName);
        }
        this.unloadSpawnChunks = config.getBoolean("disable-spawn-chunks", false);
        this.loadedChunkCropTickTicks = config.getInt("loaded-chunk-growth.crop-tick-interval-ticks", 120);
        this.loadedChunkMobTickTicks = config.getInt("loaded-chunk-growth.mob-tick-interval-ticks", 120);
        this.scanChunkOnUnload =
                config.getBoolean("unload-chunk-scan-crops.enabled", false);
        final KeepMeGrowingConfig self = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    self.applySpawnChunkUnloadForWorld(world);
                }
            }
        }.runTaskLater((Plugin) KeepMeGrowing.a(), 2L);
        this.plantGrowthMinutes.put("CARROTS", 20);
        this.plantGrowthMinutes.put("POTATOES", 20);
        this.plantGrowthMinutes.put("WHEAT", 20);
        this.plantGrowthMinutes.put("BEETROOTS", 16);
        this.plantGrowthMinutes.put("NETHER_WART", 14);
        this.plantGrowthMinutes.put("COCOA", 12);
        this.plantGrowthMinutes.put("OAK_SAPLING", 12);
        this.plantGrowthMinutes.put("SPRUCE_SAPLING", 12);
        this.plantGrowthMinutes.put("BIRCH_SAPLING", 12);
        this.plantGrowthMinutes.put("JUNGLE_SAPLING", 12);
        this.plantGrowthMinutes.put("ACACIA_SAPLING", 12);
        this.plantGrowthMinutes.put("DARK_OAK_SAPLING", 12);
        this.plantGrowthMinutes.put("CACTUS", 22);
        this.plantGrowthMinutes.put("SUGAR_CANE", 22);
        this.plantGrowthMinutes.put("MELON_STEM", 20);
        this.plantGrowthMinutes.put("PUMPKIN_STEM", 20);
        try {
            this.plantGrowthMinutes.put("KELP", 24);
        } catch (Throwable ignored) {
        }
        try {
            this.plantGrowthMinutes.put("SWEET_BERRY_BUSH", 18);
            this.plantGrowthMinutes.put("BAMBOO", 14);
        } catch (Throwable ignored) {
        }
        try {
            this.plantGrowthMinutes.put("WEEPING_VINES", 24);
            this.plantGrowthMinutes.put("TWISTING_VINES", 24);
        } catch (Throwable ignored) {
        }
        try {
            this.plantGrowthMinutes.put("CAVE_VINES", 20);
        } catch (Throwable ignored) {
        }
        try {
            this.plantGrowthMinutes.put("MANGROVE_PROPAGULE", 14);
        } catch (Throwable ignored) {
        }
        try {
            this.plantGrowthMinutes.put("CHERRY_SAPLING", 12);
            this.plantGrowthMinutes.put("TORCHFLOWER", 18);
            this.plantGrowthMinutes.put("PITCHER", 18);
        } catch (Throwable ignored) {
        }
        for (String cropKey : this.plantGrowthMinutes.keySet()) {
            if (!config.isInt("plants-minutes-to-full-growth." + cropKey)
                    || !config.isSet("plants-minutes-to-full-growth." + cropKey)) {
                continue;
            }
            this.plantGrowthMinutes.put(cropKey, config.getInt("plants-minutes-to-full-growth." + cropKey));
        }
        this.mobGrowthMinutes.put("CHICKEN", 20);
        this.mobGrowthMinutes.put("PIG", 20);
        this.mobGrowthMinutes.put("COW", 20);
        this.mobGrowthMinutes.put("SHEEP", 20);
        this.mobGrowthMinutes.put("SHEEP_WOOL", 10);
        this.mobGrowthMinutes.put("MUSHROOM_COW", 20);
        this.mobGrowthMinutes.put("RABBIT", 20);
        this.mobGrowthMinutes.put("WOLF", 20);
        this.mobGrowthMinutes.put("OCELOT", 20);
        this.mobGrowthMinutes.put("HORSE", 20);
        this.mobGrowthMinutes.put("VILLAGER", 20);
        this.mobGrowthMinutes.put("POLAR_BEAR", 20);
        this.mobGrowthMinutes.put("MULE", 20);
        this.mobGrowthMinutes.put("DONKEY", 20);
        this.mobGrowthMinutes.put("LLAMA", 20);
        this.mobGrowthMinutes.put("TURTLE", 20);
        this.mobGrowthMinutes.put("TRADER_LLAMA", 20);
        this.mobGrowthMinutes.put("FOX", 20);
        this.mobGrowthMinutes.put("CAT", 20);
        this.mobGrowthMinutes.put("BEE", 20);
        this.mobGrowthMinutes.put("PANDA", 20);
        this.mobGrowthMinutes.put("STRIDER", 20);
        this.mobGrowthMinutes.put("GOAT", 20);
        this.mobGrowthMinutes.put("AXOLOTL", 20);
        this.mobGrowthMinutes.put("FROG", 20);
        this.mobGrowthMinutes.put("TADPOLE", 20);
        this.mobGrowthMinutes.put("SNIFFER", 40);
        this.mobGrowthMinutes.put("CAMEL", 20);
        this.mobGrowthMinutes.put("ARMADILLO", 20);
        for (String mobKey : this.mobGrowthMinutes.keySet()) {
            if (!config.isInt("mobs-minutes-to-full-growth." + mobKey)
                    || !config.isSet("mobs-minutes-to-full-growth." + mobKey)) {
                continue;
            }
            this.mobGrowthMinutes.put(mobKey, config.getInt("mobs-minutes-to-full-growth." + mobKey));
        }
    }

    public final int getPlantGrowthMinutes(String cropKey) {
        return this.plantGrowthMinutes.get(cropKey);
    }

    public final int getMobGrowthMinutes(String mobKey) {
        return this.mobGrowthMinutes.get(mobKey);
    }

    public final boolean isWorldBlacklisted(String worldName) {
        return this.blacklistedWorlds.contains(worldName);
    }

    public final void restoreSpawnChunkMemory() {
        if (this.worldsSpawnRestored != null) {
            for (String worldName : this.worldsSpawnRestored) {
                if (Bukkit.getWorld(worldName) == null) {
                    continue;
                }
                Bukkit.getWorld(worldName).setKeepSpawnInMemory(true);
            }
        }
    }

    public int getLoadedChunkCropTickTicks() {
        return loadedChunkCropTickTicks;
    }

    public int getLoadedChunkMobTickTicks() {
        return loadedChunkMobTickTicks;
    }

    public boolean isUnloadChunkCropScanEnabled() {
        return scanChunkOnUnload;
    }

    public final void applySpawnChunkUnloadForWorld(World world) {
        if (this.unloadSpawnChunks
                && !this.isWorldBlacklisted(world.getName())
                && world.getKeepSpawnInMemory()) {
            world.setKeepSpawnInMemory(false);
            this.worldsSpawnRestored.add(world.getName());
        }
    }
}
