package com.keepmegrowing.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class MobGrowthSpec {

    private final EntityType entityType;
    
    protected final int ageScaleNumerator;
    
    protected final int minutesToFullGrowth;

    protected MobGrowthSpec(EntityType entityType, int ageScaleNumerator, int minutesToFullGrowth) {
        this.entityType = entityType;
        this.ageScaleNumerator = ageScaleNumerator;
        this.minutesToFullGrowth = minutesToFullGrowth;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public abstract void applyGrowth(Entity entity, long lastUnloadedTimeMs, double rateMultiplier);
}
