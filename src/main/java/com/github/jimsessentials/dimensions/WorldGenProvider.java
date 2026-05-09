package com.github.jimsessentials.dimensions;

import com.github.jimsessentials.JimsEssentials;
import com.github.jimsessentials.dimensions.wastelands.DIM_Wastelands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class WorldGenProvider extends DatapackBuiltinEntriesProvider
{
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, DIM_Wastelands.Instance()::bootstrapType)
            .add(Registries.LEVEL_STEM, DIM_Wastelands.Instance()::bootstrapDimension);
    public WorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries, BUILDER, Set.of(JimsEssentials.MODID));
    }
}
