package config;

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
        public static final ModConfigSpec.BooleanValue enabled = Builder
                .comment("Enable " + ID + " module")
                .define("modules." + ID + ".enabled", false);
        public static final ModConfigSpec.ConfigValue<Integer> delay = Builder
                .comment("Keep server running for this many ticks after the last player has left (default: 20t/s * 600s = 12000t)")
                .define("modules." + ID + ".delay", 12000); // 10 min
    }

    public static void register(@NotNull ModContainer container)
    {
        container.registerConfig(ModConfig.Type.SERVER, Builder.build());
    }
}
