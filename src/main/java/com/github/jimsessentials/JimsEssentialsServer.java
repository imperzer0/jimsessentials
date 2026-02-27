package com.github.jimsessentials;

import com.github.jimsessentials.modules.autoshutdown.AutoShutdown;
import config.ServerConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;


// This class will not load on clients. Accessing server side code from here is safe.
@Mod(value = JimsEssentials.MODID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = JimsEssentials.MODID, value = Dist.DEDICATED_SERVER)
public class JimsEssentialsServer
{
    public JimsEssentialsServer(IEventBus jim_event_bus, ModContainer container)
    {
        // Register server config before using it
        ServerConfig.register(container);


        // Server Only modules

        // Event Handlers:
        if (ServerConfig.AutoShutdown.enabled.get()) NeoForge.EVENT_BUS.register(AutoShutdown.Instance());
    }
}