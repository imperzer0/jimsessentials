package com.github.jimsessentials.lib.interfaces;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public interface Dimension
{
    void bootstrapType(BootstrapContext<DimensionType> context);

    void bootstrapDimension(BootstrapContext<LevelStem> context);
}
