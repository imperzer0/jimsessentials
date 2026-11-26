package com.github.jimsessentials.modules.glisteringmelon;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber(modid = "jimsessentials")
public class EdibleGlisteringMelons
{
    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event)
    {
        event.modify(Items.GLISTERING_MELON_SLICE, builder -> builder.set(
                DataComponents.FOOD,
                new FoodProperties.Builder()
                        .nutrition(6)                         // Restores 3 hunger shanks
                        .saturationModifier(14.4f)            // Saturation coefficient
                        .alwaysEdible()                       // Permits consumption at full satiety
                        .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0f) // Instant Health I
                        .build()
        ));
    }
}