package com.keepmegrowing.mob;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.integration.worldguard.KeepMeGrowingWorldGuard;
import java.util.List;
import org.bukkit.entity.Entity;

public final class EntityGrowthProcessor {

    private static MobGrowthRegistry registry() {
        KeepMeGrowing p = KeepMeGrowing.getPluginInstance();
        return p == null ? null : p.getMobGrowthRegistry();
    }

    public static void applyOfflineGrowth(Entity entity) {
        KeepMeGrowing plugin = KeepMeGrowing.getPluginInstance();
        if (plugin == null || plugin.getGrowthRateController().getEffectiveMultiplier() <= 0.0) {
            return;
        }
        MobGrowthRegistry reg = registry();
        if (reg == null) {
            return;
        }
        if (!KeepMeGrowingWorldGuard.allowsGrowth(entity.getLocation())) {
            return;
        }
        List<MobGrowthSpec> specs = reg.findSpecs(entity.getType());
        if (specs == null) {
            return;
        }
        long lastUnloaded = -1L;
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith("keepmegrowing_last_unloaded:")) continue;
            lastUnloaded = Long.parseLong(tag.split(":")[1]);
            break;
        }
        long lastLoaded = 0L;
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith("keepmegrowing_last_loaded:")) continue;
            lastLoaded = Long.parseLong(tag.split(":")[1]);
            break;
        }
        if (lastUnloaded == -1L || System.currentTimeMillis() - lastLoaded < 10000L) {
            return;
        }
        String duplicateLoadedTag = null;
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith("keepmegrowing_last_loaded:")) continue;
            duplicateLoadedTag = tag;
            break;
        }
        if (duplicateLoadedTag != null) {
            entity.removeScoreboardTag(duplicateLoadedTag);
        }
        entity.addScoreboardTag("keepmegrowing_last_loaded:" + System.currentTimeMillis());
        double mult = plugin.getGrowthRateController().getEffectiveMultiplier();
        for (MobGrowthSpec spec : specs) {
            spec.applyGrowth(entity, lastUnloaded, mult);
        }
    }

    public static void tickLoadedPassiveMobs(Entity entity, long dtWallMs, double mult) {
        if (dtWallMs <= 0L || mult <= 0.0) {
            return;
        }
        MobGrowthRegistry reg = registry();
        if (reg == null) {
            return;
        }
        if (!KeepMeGrowingWorldGuard.allowsGrowth(entity.getLocation())) {
            return;
        }
        List<MobGrowthSpec> specs = reg.findSpecs(entity.getType());
        if (specs == null) {
            return;
        }
        long ref = System.currentTimeMillis() - dtWallMs;
        for (MobGrowthSpec spec : specs) {
            spec.applyGrowth(entity, ref, mult);
        }
    }

    public static void markEntityUnloaded(Entity entity) {
        MobGrowthRegistry reg = registry();
        if (reg == null || reg.findSpecs(entity.getType()) == null) {
            return;
        }
        String existing = null;
        for (String tag : entity.getScoreboardTags()) {
            if (!tag.startsWith("keepmegrowing_last_unloaded:")) continue;
            existing = tag;
            break;
        }
        if (existing != null) {
            entity.removeScoreboardTag(existing);
        }
        entity.addScoreboardTag("keepmegrowing_last_unloaded:" + System.currentTimeMillis());
    }
}
