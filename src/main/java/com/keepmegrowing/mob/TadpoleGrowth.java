package com.keepmegrowing.mob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Tadpole;

public final class TadpoleGrowth extends MobGrowthSpec {

    public TadpoleGrowth(EntityType entityType, int ageScaleNumerator, int minutesToFullGrowth) {
        super(entityType, ageScaleNumerator, minutesToFullGrowth);
    }

    @Override
    public void applyGrowth(Entity entity, long lastUnloadedTimeMs, double rateMultiplier) {
        if (minutesToFullGrowth < 0) {
            return;
        }
        if (!(entity instanceof Tadpole tadpole)) {
            return;
        }
        long elapsedMs =
                (long) ((double) (System.currentTimeMillis() - lastUnloadedTimeMs) * rateMultiplier);
        float f =
                (float) (elapsedMs / 1000L)
                        / 60.0f
                        * (float) ageScaleNumerator
                        / (float) minutesToFullGrowth;
        int n = tadpole.getAge();
        int n2 = (int) f;
        if ((n = n2 + n) > ageScaleNumerator) {
            n = ageScaleNumerator;
        }
        tadpole.setAge(n);
    }
}
