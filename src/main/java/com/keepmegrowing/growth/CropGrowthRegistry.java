
package com.keepmegrowing.growth;

import com.keepmegrowing.KeepMeGrowing;
import com.keepmegrowing.KeepMeGrowingConfig;
import com.keepmegrowing.growth.AbstractCropGrowth;
import com.keepmegrowing.growth.StemFruitGrowth;
import com.keepmegrowing.growth.SaplingCropGrowth;
import com.keepmegrowing.growth.AgeableCropGrowth;
import com.keepmegrowing.growth.ColumnCropGrowth;
import com.keepmegrowing.growth.util.VersionMaterials;
import com.keepmegrowing.growth.BambooStalkCropGrowth;
import com.keepmegrowing.growth.BambooShootCropGrowth;
import com.keepmegrowing.growth.AttachedStemCropGrowth;
import com.keepmegrowing.growth.VerticalColumnCropGrowth;
import com.keepmegrowing.growth.KelpColumnCropGrowth;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;

public class CropGrowthRegistry {
    private EnumMap<Material, AbstractCropGrowth> growthByMaterial;

    public final void registerCrops() {
        this.growthByMaterial = new EnumMap<>(Material.class);
        KeepMeGrowingConfig cfg = KeepMeGrowing.a().f();
        this.growthByMaterial.put(VersionMaterials.a("CARROTS"), new AgeableCropGrowth(VersionMaterials.a("CARROTS"), 7, true, cfg.getPlantGrowthMinutes("CARROTS")));
        this.growthByMaterial.put(VersionMaterials.a("WHEAT"), new AgeableCropGrowth(VersionMaterials.a("WHEAT"), 7, true, cfg.getPlantGrowthMinutes("WHEAT")));
        this.growthByMaterial.put(VersionMaterials.a("POTATOES"), new AgeableCropGrowth(VersionMaterials.a("POTATOES"), 7, true, cfg.getPlantGrowthMinutes("POTATOES")));
        try {
            this.growthByMaterial.put(VersionMaterials.a("BEETROOTS"), new AgeableCropGrowth(VersionMaterials.a("BEETROOTS"), 3, true, cfg.getPlantGrowthMinutes("BEETROOTS")));
        }
        catch (Throwable throwable) {}
        this.growthByMaterial.put(VersionMaterials.a("NETHER_WART"), new AgeableCropGrowth(VersionMaterials.a("NETHER_WART"), 3, true, cfg.getPlantGrowthMinutes("NETHER_WART")));
        this.growthByMaterial.put(Material.COCOA, new AgeableCropGrowth(Material.COCOA, 2, true, cfg.getPlantGrowthMinutes("COCOA")));
        this.growthByMaterial.put(Material.CACTUS, new ColumnCropGrowth(Material.CACTUS, 2, false, cfg.getPlantGrowthMinutes("CACTUS")));
        this.growthByMaterial.put(VersionMaterials.a("SUGAR_CANE"), new ColumnCropGrowth(VersionMaterials.a("SUGAR_CANE"), 2, false, cfg.getPlantGrowthMinutes("SUGAR_CANE")));
        if (VersionMaterials.a) {
            this.growthByMaterial.put(Material.valueOf((String)"SAPLING"), new SaplingCropGrowth(Material.valueOf((String)"SAPLING"), 1, true, cfg.getPlantGrowthMinutes("OAK_SAPLING")));
        } else {
            for (Material material : Tag.SAPLINGS.getValues()) {
                if (!material.toString().equals("OAK_SAPLING") && !material.toString().equals("SPRUCE_SAPLING") && !material.toString().equals("BIRCH_SAPLING") && !material.toString().equals("JUNGLE_SAPLING") && !material.toString().equals("ACACIA_SAPLING") && !material.toString().equals("DARK_OAK_SAPLING")) continue;
                this.growthByMaterial.put(material, new SaplingCropGrowth(material, 2, true, cfg.getPlantGrowthMinutes(material.toString())));
            }
        }
        this.growthByMaterial.put(Material.MELON_STEM, new StemFruitGrowth(Material.MELON_STEM, VersionMaterials.a("ATTACHED_MELON_STEM"), VersionMaterials.a("MELON"), 7, false, cfg.getPlantGrowthMinutes("MELON_STEM")));
        this.growthByMaterial.put(Material.PUMPKIN_STEM, new StemFruitGrowth(Material.PUMPKIN_STEM, VersionMaterials.a("ATTACHED_PUMPKIN_STEM"), Material.PUMPKIN, 7, false, cfg.getPlantGrowthMinutes("PUMPKIN_STEM")));
        if (!VersionMaterials.a) {
            this.growthByMaterial.put(Material.ATTACHED_MELON_STEM, new AttachedStemCropGrowth(Material.ATTACHED_MELON_STEM));
            this.growthByMaterial.put(Material.ATTACHED_PUMPKIN_STEM, new AttachedStemCropGrowth(Material.ATTACHED_PUMPKIN_STEM));
        }
        try {
            this.growthByMaterial.put(Material.KELP_PLANT, new KelpColumnCropGrowth(Material.KELP, 25, false, cfg.getPlantGrowthMinutes("KELP"), BlockFace.UP, Material.KELP_PLANT, Material.WATER));
            this.growthByMaterial.put(Material.KELP, new VerticalColumnCropGrowth(Material.KELP, 25, false, cfg.getPlantGrowthMinutes("KELP"), BlockFace.UP, Material.KELP_PLANT, Material.WATER));
        }
        catch (Throwable throwable) {}
        try {
            this.growthByMaterial.put(Material.SWEET_BERRY_BUSH, new AgeableCropGrowth(Material.SWEET_BERRY_BUSH, 3, false, cfg.getPlantGrowthMinutes("SWEET_BERRY_BUSH")));
            this.growthByMaterial.put(Material.BAMBOO, new BambooStalkCropGrowth(Material.BAMBOO, 13, false, cfg.getPlantGrowthMinutes("BAMBOO")));
            this.growthByMaterial.put(Material.BAMBOO_SAPLING, new BambooShootCropGrowth(Material.BAMBOO_SAPLING, 13, false, cfg.getPlantGrowthMinutes("BAMBOO")));
        }
        catch (Throwable throwable) {}
        try {
            this.growthByMaterial.put(Material.WEEPING_VINES_PLANT, new KelpColumnCropGrowth(Material.WEEPING_VINES, 25, false, cfg.getPlantGrowthMinutes("WEEPING_VINES"), BlockFace.DOWN, Material.WEEPING_VINES_PLANT, Material.AIR));
            this.growthByMaterial.put(Material.WEEPING_VINES, new VerticalColumnCropGrowth(Material.WEEPING_VINES, 25, false, cfg.getPlantGrowthMinutes("WEEPING_VINES"), BlockFace.DOWN, Material.WEEPING_VINES_PLANT, Material.AIR));
            this.growthByMaterial.put(Material.TWISTING_VINES_PLANT, new KelpColumnCropGrowth(Material.TWISTING_VINES, 25, false, cfg.getPlantGrowthMinutes("TWISTING_VINES"), BlockFace.UP, Material.TWISTING_VINES_PLANT, Material.AIR));
            this.growthByMaterial.put(Material.TWISTING_VINES, new VerticalColumnCropGrowth(Material.TWISTING_VINES, 25, false, cfg.getPlantGrowthMinutes("TWISTING_VINES"), BlockFace.UP, Material.TWISTING_VINES_PLANT, Material.AIR));
        }
        catch (Throwable throwable) {}
        try {
            this.growthByMaterial.put(Material.CAVE_VINES_PLANT, new KelpColumnCropGrowth(Material.CAVE_VINES, 25, false, cfg.getPlantGrowthMinutes("CAVE_VINES"), BlockFace.DOWN, Material.CAVE_VINES_PLANT, Material.AIR));
            this.growthByMaterial.put(Material.CAVE_VINES, new VerticalColumnCropGrowth(Material.CAVE_VINES, 25, false, cfg.getPlantGrowthMinutes("CAVE_VINES"), BlockFace.DOWN, Material.CAVE_VINES_PLANT, Material.AIR));
        }
        catch (Throwable throwable) {}
        try {
            this.growthByMaterial.put(Material.MANGROVE_PROPAGULE, new SaplingCropGrowth(Material.MANGROVE_PROPAGULE, 2, true, cfg.getPlantGrowthMinutes("MANGROVE_PROPAGULE")));
        }
        catch (Throwable throwable) {}
        try {
            this.growthByMaterial.put(Material.CHERRY_SAPLING, new SaplingCropGrowth(Material.CHERRY_SAPLING, 2, true, cfg.getPlantGrowthMinutes("CHERRY_SAPLING")));
            this.growthByMaterial.put(Material.TORCHFLOWER_CROP, new AgeableCropGrowth(Material.TORCHFLOWER_CROP, 2, true, cfg.getPlantGrowthMinutes("TORCHFLOWER")));
            this.growthByMaterial.put(Material.PITCHER_CROP, new AgeableCropGrowth(Material.PITCHER_CROP, 4, true, cfg.getPlantGrowthMinutes("PITCHER")));
            return;
        }
        catch (Throwable throwable) {
            return;
        }
    }

    public final AbstractCropGrowth findGrowth(Material material) {
        return this.growthByMaterial.getOrDefault(material, null);
    }

    public Set<Material> getRegisteredMaterials() {
        return Collections.unmodifiableSet(this.growthByMaterial.keySet());
    }
}
