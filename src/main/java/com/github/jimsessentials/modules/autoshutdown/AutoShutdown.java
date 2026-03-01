package com.github.jimsessentials.modules.autoshutdown;

import com.github.jimsessentials.lib.ServerScheduler;
import com.github.jimsessentials.lib.interfaces.Module;
import com.github.jimsessentials.config.ServerConfig;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.jline.utils.Log;

public class AutoShutdown extends Module
{
    public static final String ID = "autoshutdown";
    private static AutoShutdown instance = null;

    private AutoShutdown()
    {
        super(ID);
    }

    /**
     * @return Singleton instance
     */
    public static AutoShutdown Instance()
    {
        if (instance == null)
            instance = new AutoShutdown();

        return instance;
    }


    private static ServerScheduler.JobInfo shutdown_job = null;

    /**
     * @param server Server instance to halt
     */
    private static void halt_server_later(MinecraftServer server)
    {
        if (server == null)
        {
            Log.error("Weird...  server == null.");
            Log.error("Is this the client side?");
            return;
        }

        if (shutdown_job != null)
            ServerScheduler.Instance().cancel(shutdown_job);

        shutdown_job = ServerScheduler.JobInfo.Builder()
                .job(() -> {
                    server.halt(false);
                })
                .delay(ServerConfig.AutoShutdown.delay())
                .build();

        ServerScheduler.Instance().schedule(shutdown_job);
    }

    /**
     * @apiNote Never call this method manually! It starts the countdown.
     */
    @SubscribeEvent
    private void server_started(ServerStartedEvent event)
    {
        halt_server_later(event.getServer());
    }

    /**
     * @apiNote Never call this method manually! It starts the countdown.
     */
    @SubscribeEvent
    private void player_leave(PlayerEvent.PlayerLoggedOutEvent event)
    {
        halt_server_later(event.getEntity().getServer());
    }

    /**
     * @apiNote Never call this method manually! It starts the countdown.
     */
    @SubscribeEvent
    private void player_join(PlayerEvent.PlayerLoggedInEvent event)
    {
        ServerScheduler.Instance().cancel(shutdown_job);
        shutdown_job = null;
    }
}
