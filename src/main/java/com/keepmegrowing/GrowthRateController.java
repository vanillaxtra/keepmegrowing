package com.keepmegrowing;

import org.bukkit.configuration.file.FileConfiguration;

public final class GrowthRateController {
    private volatile boolean frozen;
    private volatile boolean sprintActive;
    private double baseMultiplier = 1.0;
    private double sprintExtraMultiplier = 5.0;

    public void loadFromConfig(FileConfiguration config) {
        baseMultiplier = config.getDouble("growth-rate.base-multiplier", 1.0);
        sprintExtraMultiplier = config.getDouble("growth-rate.sprint-multiplier", 5.0);
    }

    
    public double getEffectiveMultiplier() {
        if (frozen) {
            return 0.0;
        }
        double m = baseMultiplier;
        if (sprintActive) {
            m *= sprintExtraMultiplier;
        }
        return m;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isSprintActive() {
        return sprintActive;
    }

    public void setSprintActive(boolean sprintActive) {
        this.sprintActive = sprintActive;
    }

    public double getBaseMultiplier() {
        return baseMultiplier;
    }

    public void setBaseMultiplier(double baseMultiplier) {
        this.baseMultiplier = baseMultiplier;
    }

    public double getSprintExtraMultiplier() {
        return sprintExtraMultiplier;
    }
}
