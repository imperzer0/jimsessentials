package com.github.jimsessentials.modules.autoshutdown;

import com.github.jimsessentials.lib.ServerScheduler;
import com.github.jimsessentials.lib.interfaces.Module;
import config.ServerConfig;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jline.utils.Log;

public class AutoShutdown extends Module
{
    public static final String ID = "autoshutdown";
    private static AutoShutdown instance = null;

    private AutoShutdown()
    {
        super(ID);
    }

    public static AutoShutdown Instance()
    {
        if (AutoShutdown.instance == null)
            AutoShutdown.instance = new AutoShutdown();

        return AutoShutdown.instance;
    }


    private static ServerScheduler.JobInfo shutdown_job = null;

    @SubscribeEvent
    private static void player_leave(PlayerEvent.PlayerLoggedOutEvent event)
    {
        MinecraftServer server = event.getEntity().getServer();
        if (server == null)
        {
            Log.error(event.getClass().getCanonicalName() + "'s getEntity().getServer() returned null.");
            Log.error("Is this client side?");
            return;
        }

        if (shutdown_job != null)
            ServerScheduler.Instance().cancel(shutdown_job);

        shutdown_job = ServerScheduler.JobInfo.Builder()
                .job(() -> {
                    server.halt(false);
                })
                .delay(ServerConfig.AutoShutdown.delay.get())
                .build();

        ServerScheduler.Instance().schedule(shutdown_job);
    }

    @SubscribeEvent
    private static void player_join(PlayerEvent.PlayerLoggedInEvent event)
    {
        ServerScheduler.Instance().cancel(shutdown_job);
        shutdown_job = null;
    }
}
