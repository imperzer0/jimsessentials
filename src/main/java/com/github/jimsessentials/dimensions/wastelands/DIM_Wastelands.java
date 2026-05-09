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
import net.minecraft.world.level.levelgen.NoiseSettings;

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


//    public final ResourceKey<Biome> WASTELANDS_OCEAN = ResourceKey.create(Registries.BIOME,
//            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_ocean"));
//    public final ResourceKey<Biome> WASTELANDS_STONY_PEAKS = ResourceKey.create(Registries.BIOME,
//            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_stony_peaks"));
//    public final ResourceKey<Biome> WASTELANDS_BADLANDS = ResourceKey.create(Registries.BIOME,
//            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_badlands"));
//    public final ResourceKey<Biome> WASTELANDS_DESERT = ResourceKey.create(Registries.BIOME,
//            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_desert"));
//    public final ResourceKey<Biome> WASTELANDS_NETHER_WASTES = ResourceKey.create(Registries.BIOME,
//            ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, "wastelands_nether_wastes"));


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
    public void bootstrapType(BootstrapContext<DimensionType> context)
    {
        context.register(TYPE, new DimensionType(
                OptionalLong.of(6000), // fixedTime - infinite noon
                true, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                64.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -128, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                0.0f, // ambientLight
                new DimensionType.MonsterSettings(true, false, ConstantInt.of(0), 0)));
    }

    @Override
    public void bootstrapDimension(BootstrapContext<LevelStem> context)
    {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);


        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-0.15f, 0.2f), // t
                                        Climate.Parameter.span(-1f, 1f), // h
                                        Climate.Parameter.span(-2f, 2f), // C
                                        Climate.Parameter.span(0.4f, 0.5f), // E
                                        Climate.Parameter.point(0.f), // D
                                        Climate.Parameter.span(-2f, 2f), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(0.25f, 1.f), // t
                                        Climate.Parameter.span(-2.f, -0.15f), // h
                                        Climate.Parameter.span(0.f, 1.f), // C
                                        Climate.Parameter.span(-2.f, -0.7799f), // E
                                        Climate.Parameter.point(0.f), // D
                                        Climate.Parameter.span(-0.5f, 1.f), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(Biomes.BADLANDS)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-2f, -0.9f), // t
                                        Climate.Parameter.span(-1.f, -0.35f), // h
                                        Climate.Parameter.span(0.03f, 2.f), // C
                                        Climate.Parameter.span(-1f, -0.7799f), // E
                                        Climate.Parameter.point(0.f), // D
                                        Climate.Parameter.span(0.4f, 0.5f), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(Biomes.STONY_PEAKS)),
                                Pair.of(Climate.parameters(
                                        Climate.Parameter.span(-2f, 2f), // t
                                        Climate.Parameter.span(-1.5f, -0.6f), // h
                                        Climate.Parameter.span(0.5f, 2.f), // C
                                        Climate.Parameter.span(-2.f, 0.f), // E
                                        Climate.Parameter.point(0.f), // D
                                        Climate.Parameter.span(-1.f, 0.5f), // w
                                        0.f /* offset */), biomeRegistry.getOrThrow(Biomes.DESERT))
                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(TYPE), noiseBasedChunkGenerator);

        context.register(KEY, stem);
    }
}
