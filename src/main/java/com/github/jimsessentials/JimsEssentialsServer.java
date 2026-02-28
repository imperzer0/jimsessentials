package com.github.jimsessentials;

import com.github.jimsessentials.modules.autoshutdown.AutoShutdown;
import config.ServerConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;


// This class will not load on clients. Accessing server side code from here is safe.
@Mod(value = JimsEssentials.MODID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = JimsEssentials.MODID, value = Dist.DEDICATED_SERVER)
public class JimsEssentialsServer
{
    public JimsEssentialsServer(IEventBus jim_event_bus, ModContainer container)
    {
        // Register server config before using it
        ServerConfig.register(container);
    }

    @SubscribeEvent
    private static void once_loaded(ServerAboutToStartEvent event)
    {
        // We need to access config,
        // so we load modules after the mod is loaded.

        // Register AutoShutdown event handlers
        if (ServerConfig.AutoShutdown.enabled()) NeoForge.EVENT_BUS.register(AutoShutdown.Instance());
    }
}