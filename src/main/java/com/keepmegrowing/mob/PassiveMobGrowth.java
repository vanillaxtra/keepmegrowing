package com.keepmegrowing.mob;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public final class PassiveMobGrowth extends MobGrowthSpec {

    public PassiveMobGrowth(EntityType entityType, int ageScaleNumerator, int minutesToFullGrowth) {
        super(entityType, ageScaleNumerator, minutesToFullGrowth);
    }

    @Override
    public void applyGrowth(Entity entity, long lastUnloadedTimeMs, double rateMultiplier) {
        Ageable ageable = (Ageable) entity;
        if (minutesToFullGrowth < 0 || ageable.getAge() == 0) {
            return;
        }
        long elapsedMs =
                (long) ((double) (System.currentTimeMillis() - lastUnloadedTimeMs) * rateMultiplier);
        int n = ageable.getAge();
        float f;
        if (!ageable.isAdult()) {
            f =
                    (float) (elapsedMs / 1000L)
                            / 60.0f
                            * (float) ageScaleNumerator
                            / (float) minutesToFullGrowth;
        } else {
            f =
                    (float) (elapsedMs / 1000L)
                            / 60.0f
                            * 6000.0f
                            / ((float) minutesToFullGrowth
                                    / (entity.getType().toString().equals("SNIFFER") ? 8.0f : 4.0f));
            f = -f;
        }
        int n2 = (int) f;
        n2 += n;
        if (!ageable.isAdult()) {
            if (n2 > 0) {
                n2 = 0;
            }
        } else if (n2 < 0) {
            n2 = 0;
        }
        ageable.setAge(n2);
    }
}
