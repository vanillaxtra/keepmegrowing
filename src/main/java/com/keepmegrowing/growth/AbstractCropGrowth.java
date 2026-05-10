
package com.keepmegrowing.growth;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public abstract class AbstractCropGrowth {
    final Material c;
    final int d;
    final boolean e;
    final int f;
    Random g = new Random();

    public AbstractCropGrowth(Material material, int n, boolean bl, int n2) {
        this.c = material;
        this.d = n;
        this.e = bl;
        this.f = n2;
    }

    public abstract int a(BlockState var1);

    public abstract boolean a(BlockState var1, int var2, int var3, int var4);

    public boolean a(BlockState blockState, long l, double d) {
        if (this.f < 0) {
            return false;
        }
        System.currentTimeMillis();
        long l2 = System.currentTimeMillis() - l;
        int n = this.a(blockState);
        AbstractCropGrowth a2 = this;
        int n2 = a2.d;
        int n3 = (int)((double)this.f * d);
        if (n == 999) {
            return true;
        }
        if (n >= n2) {
            a2 = this;
            return a2.e;
        }
        float f = (float)(l2 / 1000L) / 60.0f * (float)n2 / (float)n3;
        int n4 = (int)f;
        if ((n3 = n4 + n) > n2) {
            n3 = n2;
        } else {
            f -= (float)n4;
            float f3 = this.g.nextFloat();
            if (f3 <= f) {
                ++n3;
            }
            if (n3 > n2) {
                n3 = n2;
            }
        }
        if (n3 <= 0 || n >= n3) {
            return false;
        }
        if (this.a(blockState, n, n3, n2)) {
            return true;
        }
        a2 = this;
        if (a2.e && n3 >= n2) {
            return true;
        }
        System.currentTimeMillis();
        return false;
    }
}
