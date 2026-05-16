package com.github.jimsessentials.dimensions.wastelands.portal;

import com.github.jimsessentials.JimsEssentials;
import com.github.jimsessentials.dimensions.wastelands.DIM_Wastelands;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.material.Fluids;


public class WastelandPortal
{
    private static WastelandPortal instance = null;

    private WastelandPortal()
    {
        BUILDER = CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.CHISELED_RED_SANDSTONE)
                .returnDim(BuiltinDimensionTypes.OVERWORLD_EFFECTS, false)
                .destDimID(ResourceLocation.fromNamespaceAndPath(JimsEssentials.MODID, DIM_Wastelands.ID))
                .tintColor(100, 44, 22)
                .lightWithFluid(Fluids.LAVA);
    }

    public static WastelandPortal Instance()
    {
        if (instance == null)
            instance = new WastelandPortal();
        return instance;
    }


    private CustomPortalBuilder BUILDER = null;

    public void register()
    {
        BUILDER.registerPortal();
    }
}
