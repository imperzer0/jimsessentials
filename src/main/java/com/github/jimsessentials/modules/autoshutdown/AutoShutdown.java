package com.github.jimsessentials.modules.autoshutdown;

import com.github.jimsessentials.lib.ServerScheduler;
import com.github.jimsessentials.lib.interfaces.Module;
import com.github.jimsessentials.config.ServerConfig;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.Objects;

import static com.github.jimsessentials.JimsEssentials.Log;

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
        Log.debug("server = {}", server);
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
                    Log.debug("PlayerCount = {}", server.getPlayerCount());
                    if (server.getPlayerCount() == 0) // only if there are no players
                    {
                        Log.info("Halting the server, because there are no players online.");
                        server.halt(false);
                    }
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
        Log.debug("Started counting down. If no players connect in the meantime the server will shut down.");
        halt_server_later(event.getServer());
    }

    /**
     * @apiNote Never call this method manually! It starts the countdown.
     */
    @SubscribeEvent
    private void player_leave(PlayerEvent.PlayerLoggedOutEvent event)
    {
        Log.debug("Started counting down. If there will be no players until it's over the server will shut down.");
        Log.debug("Currently there are {} players.", Objects.requireNonNull(event.getEntity().getServer()).getPlayerCount());
        halt_server_later(event.getEntity().getServer());
    }

    /**
     * @apiNote Never call this method manually! It resets the countdown.
     */
    @SubscribeEvent
    private void player_join(PlayerEvent.PlayerLoggedInEvent event)
    {
        ServerScheduler.Instance().cancel(shutdown_job);
        shutdown_job = null;
    }
}
