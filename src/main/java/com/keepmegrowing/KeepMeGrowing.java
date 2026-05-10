package com.keepmegrowing;

import com.keepmegrowing.KeepMeGrowingConfig;
import com.keepmegrowing.database.DBSettings;
import com.keepmegrowing.bootstrap.ConfigDefaultsMerger;
import com.keepmegrowing.growth.util.VersionMaterials;
import com.keepmegrowing.command.KeepMeGrowingCommand;
import com.keepmegrowing.listener.WorldLoadListener;
import com.keepmegrowing.listener.ChunkEntityInitListener;
import com.keepmegrowing.storage.ChunkEntitySnapshotService;
import com.keepmegrowing.mob.MobGrowthRegistry;
import com.keepmegrowing.listener.ChunkUnloadPersistenceListener;
import com.keepmegrowing.listener.PlantBlockInteractListener;
import com.keepmegrowing.storage.PlantChunkStorage;
import com.keepmegrowing.growth.CropGrowthRegistry;
import com.keepmegrowing.integration.worldguard.KeepMeGrowingWorldGuard;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bstats.bukkit.Metrics;

public class KeepMeGrowing extends JavaPlugin {
    private static KeepMeGrowing pluginInstance;
    private PlantChunkStorage chunkStorage;
    private CropGrowthRegistry cropGrowthRegistry;
    private ChunkEntitySnapshotService chunkEntitySnapshotService;
    private MobGrowthRegistry mobGrowthRegistry;
    private DBSettings dbSettings;
    private KeepMeGrowingConfig growthConfig;
    private final GrowthRateController growthRateController = new GrowthRateController();
    private BukkitTask loadedCropGrowthTask;
    private BukkitTask loadedMobGrowthTask;

    @Override
    public void onLoad() {
        KeepMeGrowingWorldGuard.registerFlags();
    }

    public void onEnable() {
        pluginInstance = this;
        VersionMaterials.a();
        if (VersionMaterials.b()) {
            String err =
                    "This plugin requires minecraft >= 1.9 (Current: " + Bukkit.getBukkitVersion() + ")";
            this.getLogger().log(Level.SEVERE, err);
            this.getServer().getPluginManager().disablePlugin((Plugin) this);
            return;
        }
        this.saveDefaultConfig();
        File configFile = new File(this.getDataFolder(), "config.yml");
        ConfigDefaultsMerger.a(this, "config.yml", configFile, new ArrayList<>());
        this.reloadConfig();
        this.growthRateController.loadFromConfig(this.getConfig());
        this.growthConfig = new KeepMeGrowingConfig();
        this.growthConfig.loadFromConfig();
        this.dbSettings = new DBSettings();
        this.dbSettings.setup();
        this.cropGrowthRegistry = new CropGrowthRegistry();
        this.cropGrowthRegistry.registerCrops();
        this.chunkStorage = new PlantChunkStorage(this);
        final KeepMeGrowing self = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                self.chunkStorage.a();
            }
        }.runTaskLater(this, 1L);
        if (VersionMaterials.b) {
            this.getLogger()
                    .log(
                            Level.INFO,
                            "Mob offline simulation only runs on MC 1.10+. Disabling this feature...");
        } else {
            this.mobGrowthRegistry = new MobGrowthRegistry();
            this.mobGrowthRegistry.registerMobs();
            this.chunkEntitySnapshotService = new ChunkEntitySnapshotService();
            this.getServer()
                    .getPluginManager()
                    .registerEvents((Listener) new ChunkEntityInitListener(), (Plugin) this);
        }
        this.getServer().getPluginManager().registerEvents((Listener) new ChunkUnloadPersistenceListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new PlantBlockInteractListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new WorldLoadListener(), (Plugin) this);
        int backupPeriodTicks = 1200 * this.dbSettings.getBackupPeriod();
        Bukkit.getScheduler()
                .runTaskTimer(this, () -> this.chunkStorage.b(), backupPeriodTicks, backupPeriodTicks);
        KeepMeGrowingCommand commands = new KeepMeGrowingCommand();
        this.getCommand("keepmegrowing").setExecutor(commands);
        this.getCommand("keepmegrowing").setTabCompleter(commands);
        this.getCommand("growth").setExecutor(commands);
        this.getCommand("growth").setTabCompleter(commands);
        new Metrics(this, 31246);
        this.startLoadedGrowthSchedulers();
    }

    private void stopLoadedGrowthSchedulers() {
        if (loadedCropGrowthTask != null) {
            loadedCropGrowthTask.cancel();
            loadedCropGrowthTask = null;
        }
        if (loadedMobGrowthTask != null) {
            loadedMobGrowthTask.cancel();
            loadedMobGrowthTask = null;
        }
    }

    public void startLoadedGrowthSchedulers() {
        this.stopLoadedGrowthSchedulers();
        if (this.chunkStorage == null || this.growthConfig == null) {
            return;
        }
        int cropTicks = this.growthConfig.getLoadedChunkCropTickTicks();
        if (cropTicks > 0) {
            long dtWall = cropTicks * 50L;
            this.loadedCropGrowthTask =
                    Bukkit.getScheduler()
                            .runTaskTimer(
                                    this,
                                    () ->
                                            this.chunkStorage.runLoadedTrackedCropGrowth(
                                                    dtWall,
                                                    this.growthRateController.getEffectiveMultiplier()),
                                    cropTicks,
                                    cropTicks);
        }
        int mobTicks = this.growthConfig.getLoadedChunkMobTickTicks();
        if (mobTicks > 0) {
            long dtMob = mobTicks * 50L;
            this.loadedMobGrowthTask =
                    Bukkit.getScheduler()
                            .runTaskTimer(
                                    this,
                                    () ->
                                            this.chunkStorage.runLoadedPassiveMobGrowth(
                                                    dtMob,
                                                    this.growthRateController.getEffectiveMultiplier()),
                                    mobTicks,
                                    mobTicks);
        }
    }

    public void onDisable() {
        this.stopLoadedGrowthSchedulers();
        if (!VersionMaterials.b()) {
            this.chunkStorage.c();
            this.growthConfig.restoreSpawnChunkMemory();
        }
    }

    
    @Deprecated
    public static KeepMeGrowing a() {
        return pluginInstance;
    }

    public static KeepMeGrowing getPluginInstance() {
        return pluginInstance;
    }

    
    @Deprecated
    public final PlantChunkStorage b() {
        return this.chunkStorage;
    }

    public PlantChunkStorage getChunkStorage() {
        return this.chunkStorage;
    }

    
    @Deprecated
    public final CropGrowthRegistry c() {
        return this.cropGrowthRegistry;
    }

    public CropGrowthRegistry getCropGrowthRegistry() {
        return this.cropGrowthRegistry;
    }

    
    @Deprecated
    public final ChunkEntitySnapshotService d() {
        return this.chunkEntitySnapshotService;
    }

    public ChunkEntitySnapshotService getChunkEntitySnapshotService() {
        return this.chunkEntitySnapshotService;
    }

    
    @Deprecated
    public final MobGrowthRegistry e() {
        return this.mobGrowthRegistry;
    }

    public MobGrowthRegistry getMobGrowthRegistry() {
        return this.mobGrowthRegistry;
    }

    
    @Deprecated
    public final KeepMeGrowingConfig f() {
        return this.growthConfig;
    }

    public KeepMeGrowingConfig getGrowthConfig() {
        return this.growthConfig;
    }

    
    @Deprecated
    public final DBSettings g() {
        return this.dbSettings;
    }

    public DBSettings getDbSettings() {
        return this.dbSettings;
    }

    public GrowthRateController getGrowthRateController() {
        return this.growthRateController;
    }
}
