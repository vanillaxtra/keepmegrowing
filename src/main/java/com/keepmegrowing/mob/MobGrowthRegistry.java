
package com.keepmegrowing.mob;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.KeepMeGrowingConfig;
import com.keepmegrowing.mob.MobGrowthSpec;
import com.keepmegrowing.mob.ShearMobGrowth;
import com.keepmegrowing.mob.PassiveMobGrowth;
import com.keepmegrowing.mob.TadpoleGrowth;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import org.bukkit.entity.EntityType;

public final class MobGrowthRegistry {
    private EnumMap<EntityType, List<MobGrowthSpec>> specsByEntityType;

    public final void registerMobs() {
        this.specsByEntityType = new EnumMap<>(EntityType.class);
        KeepMeGrowingConfig cfg = KeepMeGrowing.a().f();
        this.specsByEntityType.put(EntityType.CHICKEN, Arrays.asList(new PassiveMobGrowth(EntityType.CHICKEN, 24000, cfg.getMobGrowthMinutes("CHICKEN"))));
        this.specsByEntityType.put(EntityType.PIG, Arrays.asList(new PassiveMobGrowth(EntityType.PIG, 24000, cfg.getMobGrowthMinutes("PIG"))));
        this.specsByEntityType.put(EntityType.COW, Arrays.asList(new PassiveMobGrowth(EntityType.COW, 24000, cfg.getMobGrowthMinutes("COW"))));
        this.specsByEntityType.put(EntityType.SHEEP, Arrays.asList(new PassiveMobGrowth(EntityType.SHEEP, 24000, cfg.getMobGrowthMinutes("SHEEP")), new ShearMobGrowth(EntityType.SHEEP, 1, cfg.getMobGrowthMinutes("SHEEP_WOOL"))));
        try {
            this.specsByEntityType.put(EntityType.valueOf((String)"MUSHROOM_COW"), Arrays.asList(new PassiveMobGrowth(EntityType.valueOf((String)"MUSHROOM_COW"), 24000, cfg.getMobGrowthMinutes("MUSHROOM_COW"))));
        }
        catch (Exception exception) {}
        try {
            this.specsByEntityType.put(EntityType.valueOf((String)"MOOSHROOM"), Arrays.asList(new PassiveMobGrowth(EntityType.valueOf((String)"MOOSHROOM"), 24000, cfg.getMobGrowthMinutes("MUSHROOM_COW"))));
        }
        catch (Exception exception) {}
        this.specsByEntityType.put(EntityType.RABBIT, Arrays.asList(new PassiveMobGrowth(EntityType.RABBIT, 24000, cfg.getMobGrowthMinutes("RABBIT"))));
        this.specsByEntityType.put(EntityType.WOLF, Arrays.asList(new PassiveMobGrowth(EntityType.WOLF, 24000, cfg.getMobGrowthMinutes("WOLF"))));
        this.specsByEntityType.put(EntityType.OCELOT, Arrays.asList(new PassiveMobGrowth(EntityType.OCELOT, 24000, cfg.getMobGrowthMinutes("OCELOT"))));
        this.specsByEntityType.put(EntityType.HORSE, Arrays.asList(new PassiveMobGrowth(EntityType.HORSE, 24000, cfg.getMobGrowthMinutes("HORSE"))));
        this.specsByEntityType.put(EntityType.VILLAGER, Arrays.asList(new PassiveMobGrowth(EntityType.VILLAGER, 24000, cfg.getMobGrowthMinutes("VILLAGER"))));
        try {
            this.specsByEntityType.put(EntityType.POLAR_BEAR, Arrays.asList(new PassiveMobGrowth(EntityType.POLAR_BEAR, 24000, cfg.getMobGrowthMinutes("POLAR_BEAR"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.MULE, Arrays.asList(new PassiveMobGrowth(EntityType.MULE, 24000, cfg.getMobGrowthMinutes("MULE"))));
            this.specsByEntityType.put(EntityType.DONKEY, Arrays.asList(new PassiveMobGrowth(EntityType.DONKEY, 24000, cfg.getMobGrowthMinutes("DONKEY"))));
            this.specsByEntityType.put(EntityType.LLAMA, Arrays.asList(new PassiveMobGrowth(EntityType.LLAMA, 24000, cfg.getMobGrowthMinutes("LLAMA"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.TURTLE, Arrays.asList(new PassiveMobGrowth(EntityType.TURTLE, 24000, cfg.getMobGrowthMinutes("TURTLE"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.TRADER_LLAMA, Arrays.asList(new PassiveMobGrowth(EntityType.TRADER_LLAMA, 24000, cfg.getMobGrowthMinutes("TRADER_LLAMA"))));
            this.specsByEntityType.put(EntityType.FOX, Arrays.asList(new PassiveMobGrowth(EntityType.FOX, 24000, cfg.getMobGrowthMinutes("FOX"))));
            this.specsByEntityType.put(EntityType.CAT, Arrays.asList(new PassiveMobGrowth(EntityType.CAT, 24000, cfg.getMobGrowthMinutes("CAT"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.BEE, Arrays.asList(new PassiveMobGrowth(EntityType.BEE, 24000, cfg.getMobGrowthMinutes("BEE"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.PANDA, Arrays.asList(new PassiveMobGrowth(EntityType.PANDA, 24000, cfg.getMobGrowthMinutes("PANDA"))));
            this.specsByEntityType.put(EntityType.STRIDER, Arrays.asList(new PassiveMobGrowth(EntityType.STRIDER, 24000, cfg.getMobGrowthMinutes("STRIDER"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.GOAT, Arrays.asList(new PassiveMobGrowth(EntityType.GOAT, 24000, cfg.getMobGrowthMinutes("GOAT"))));
            this.specsByEntityType.put(EntityType.AXOLOTL, Arrays.asList(new PassiveMobGrowth(EntityType.AXOLOTL, 24000, cfg.getMobGrowthMinutes("AXOLOTL"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.FROG, Arrays.asList(new PassiveMobGrowth(EntityType.FROG, 24000, cfg.getMobGrowthMinutes("FROG"))));
            this.specsByEntityType.put(EntityType.TADPOLE, Arrays.asList(new TadpoleGrowth(EntityType.TADPOLE, 24000, cfg.getMobGrowthMinutes("TADPOLE"))));
        }
        catch (Throwable throwable) {}
        try {
            this.specsByEntityType.put(EntityType.SNIFFER, Arrays.asList(new PassiveMobGrowth(EntityType.SNIFFER, 48000, cfg.getMobGrowthMinutes("SNIFFER"))));
            this.specsByEntityType.put(EntityType.CAMEL, Arrays.asList(new PassiveMobGrowth(EntityType.CAMEL, 24000, cfg.getMobGrowthMinutes("CAMEL"))));
        }
        catch (Throwable throwable) {}
        try {
            EntityType armadillo = EntityType.valueOf("ARMADILLO");
            this.specsByEntityType.put(armadillo, Arrays.asList(new PassiveMobGrowth(armadillo, 24000, cfg.getMobGrowthMinutes("ARMADILLO"))));
        } catch (IllegalArgumentException ignored) {
        }
        return;
    }

    public final List<MobGrowthSpec> findSpecs(EntityType entityType) {
        return this.specsByEntityType.getOrDefault(entityType, null);
    }
}
