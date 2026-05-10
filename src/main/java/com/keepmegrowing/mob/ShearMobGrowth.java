package com.keepmegrowing.mob;

import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public final class ShearMobGrowth extends MobGrowthSpec {

    private final Random random = new Random();

    public ShearMobGrowth(EntityType entityType, int ageScaleNumerator, int minutesToFullGrowth) {
        super(entityType, ageScaleNumerator, minutesToFullGrowth);
    }

    @Override
    public void applyGrowth(Entity entity, long lastUnloadedTimeMs, double rateMultiplier) {
        if (!(entity instanceof Sheep sheep)) {
            return;
        }
        if (minutesToFullGrowth < 0 || !sheep.isSheared()) {
            return;
        }
        long elapsedMs =
                (long) ((double) (System.currentTimeMillis() - lastUnloadedTimeMs) * rateMultiplier);
        float f =
                (float) (elapsedMs / 1000L)
                        / 60.0f
                        * (float) ageScaleNumerator
                        / (float) minutesToFullGrowth;
        int n = (int) f;
        boolean regrow = false;
        if (n >= ageScaleNumerator) {
            regrow = true;
            f -= (float) n;
            if (random.nextFloat() <= f) {
                regrow = true;
            }
        }
        if (regrow) {
            sheep.setSheared(false);
        }
    }
}
