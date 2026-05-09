package com.github.jimsessentials.datagen;


import com.github.jimsessentials.JimsEssentials;
import com.github.jimsessentials.dimensions.WorldGenProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = JimsEssentials.MODID)
public class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupprovider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new WorldGenProvider(output, lookupprovider));
//        generator.addProvider(event.includeServer(), new StructureProvider(output, lookupprovider, fileHelper));
    }
}
