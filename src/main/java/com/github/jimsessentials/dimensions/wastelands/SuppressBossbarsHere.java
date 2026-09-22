package com.github.jimsessentials.dimensions.wastelands;

import com.github.jimsessentials.JimsEssentials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = JimsEssentials.MODID, value = Dist.CLIENT)
public class SuppressBossbarsHere
{
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event)
    {
        if (event.getName().equals(VanillaGuiLayers.BOSS_OVERLAY))
        {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;

            if (level.dimension().location().equals(DIM_Wastelands.Instance().KEY.location()))
            {
                event.setCanceled(true);
            }
        }
    }
}