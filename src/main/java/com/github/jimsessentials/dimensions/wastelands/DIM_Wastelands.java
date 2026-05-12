package com.github.jimsessentials.dimensions.wastelands;

import com.github.jimsessentials.JimsEssentials;
import com.github.jimsessentials.lib.interfaces.Dimension;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalLong;

public class DIM_Wastelands implements Dimension
{
    public static final String ID = "wastelands";
    public final ResourceKey<LevelStem> KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, ID));
    public final ResourceKey<Level> LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, ID));
    public final ResourceKey<DimensionType> TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, ID + "_t"));


    public final ResourceKey<Biome> WASTELANDS_STONY_PEAKS = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_stony_peaks"));
    public final ResourceKey<Biome> WASTELANDS_BADLANDS = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_badlands"));
    public final ResourceKey<Biome> WASTELANDS_DESERT = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_desert"));
    public final ResourceKey<Biome> WASTELANDS_DRIPSTONE_CAVES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_dripstone_caves"));
    public final ResourceKey<Biome> WASTELANDS_LUSH_CAVES = ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_lush_caves"));


    private DIM_Wastelands()
    {
    }

    private static DIM_Wastelands instance = null;

    public static DIM_Wastelands Instance()
    {
        if (instance == null)
            instance = new DIM_Wastelands();
        return instance;
    }


    @Override
    public void bootstrapType(@NotNull BootstrapContext<DimensionType> context)
    {
        context.register(TYPE, new DimensionType(
                OptionalLong.of(18000), // fixedTime - infinite midnight
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                64.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -128, // minY
                384, // height
                384, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 3)));
    }

    @Override
    public void bootstrapDimension(@NotNull BootstrapContext<LevelStem> context)
    {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);


        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-1, 0), // t
                                        Climate.Parameter.point(0), // h
                                        Climate.Parameter.span(0, 1), // C
                                        Climate.Parameter.point(0), // E
                                        Climate.Parameter.point(0), // D
                                        Climate.Parameter.point(0), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(WASTELANDS_BADLANDS)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-2, -0.1f), // t
                                        Climate.Parameter.point(0), // h
                                        Climate.Parameter.span(0.7f, 2), // C
                                        Climate.Parameter.point(0), // E
                                        Climate.Parameter.point(0), // D
                                        Climate.Parameter.point(0), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(WASTELANDS_STONY_PEAKS)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(0.2f, 2), // t
                                        Climate.Parameter.point(0), // h
                                        Climate.Parameter.span(-2, 0.7f), // C
                                        Climate.Parameter.point(0), // E
                                        Climate.Parameter.point(0), // D
                                        Climate.Parameter.point(0), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(WASTELANDS_DESERT)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-0.5f, 0.5f), // t
                                        Climate.Parameter.point(0), // h
                                        Climate.Parameter.point(0), // C
                                        Climate.Parameter.point(0.5f), // E
                                        Climate.Parameter.point(0.3f), // D
                                        Climate.Parameter.point(0), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(WASTELANDS_DRIPSTONE_CAVES)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-0.5f, 0.5f), // t
                                        Climate.Parameter.point(0), // h
                                        Climate.Parameter.point(-0.5f), // C
                                        Climate.Parameter.point(0), // E
                                        Climate.Parameter.point(0.4f), // D
                                        Climate.Parameter.point(0), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(WASTELANDS_LUSH_CAVES))
                        ))),
                noiseGenSettings.getOrThrow(ResourceKey.create(Registries.NOISE_SETTINGS, ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands"))));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(TYPE), noiseBasedChunkGenerator);

        context.register(KEY, stem);
    }
}
