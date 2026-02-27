package com.github.jimsessentials;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.github.jimsessentials.JimsEssentials.Log;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = JimsEssentials.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = JimsEssentials.MODID, value = Dist.CLIENT)
public class JimsEssentialsClient
{
    public JimsEssentialsClient(ModContainer container)
    {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event)
    {
    }
}
