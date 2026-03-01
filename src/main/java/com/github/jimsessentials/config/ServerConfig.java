package com.github.jimsessentials.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class ServerConfig
{
    private static final ModConfigSpec.Builder Builder = new ModConfigSpec.Builder();

    public static class AutoShutdown
    {
        private static final String ID = com.github.jimsessentials.modules.autoshutdown.AutoShutdown.ID;


        private static ModConfigSpec.BooleanValue enabled;

        public static Boolean enabled()
        {
            return enabled.get();
        }


        private static ModConfigSpec.ConfigValue<Integer> delay;

        public static Integer delay()
        {
            return delay.get();
        }
    }

    private static final ModConfigSpec spec;

    static
    {
        // It turns out that I need to manually define the initialisation order
        // if I don't it will call .build() first
        //
        // It's complicated, but I have a sneaking suspicion that java calls static initialisations
        // right before the field is about to be used, and the way my class was set up made sure
        // that it calls .build() and then my .define() methods
        // Thus, manual static init block it is!
        AutoShutdown.enabled = Builder
                .define("modules." + AutoShutdown.ID + ".enabled", false);
        AutoShutdown.delay = Builder
                .comment(" Keep the server running for this many ticks after the last player has left.",
                        " The default is 10 minutes.")
                .define("modules." + AutoShutdown.ID + ".delay", 12000); // 10 min
        spec = Builder.build();
    }

    public static void register(@NotNull ModContainer container)
    {
        container.registerConfig(ModConfig.Type.SERVER, spec);
    }
}
