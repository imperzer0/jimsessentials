package com.github.jimsessentials.modules.disable_alexscaves_magnets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

@Pseudo
@Mixin(targets = "com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil", remap = false)
public abstract class MagnetUtilMixin
{

    @Inject(method = "getNearbyAttractingMagnets", at = @At("HEAD"), cancellable = true, require = 0)
    private static void cancelAttractingMagnetScan(BlockPos pos, ServerLevel level, int range, CallbackInfoReturnable<Stream<BlockPos>> cir)
    {
        cir.setReturnValue(Stream.empty());
    }

    @Inject(method = "getNearbyRepellingMagnets", at = @At("HEAD"), cancellable = true, require = 0)
    private static void cancelRepellingMagnetScan(BlockPos pos, ServerLevel level, int range, CallbackInfoReturnable<Stream<BlockPos>> cir)
    {
        cir.setReturnValue(Stream.empty());
    }

    @Inject(method = "tickMagnetism", at = @At("HEAD"), cancellable = true, require = 0)
    private static void disableMagnetismProcessing(Entity entity, CallbackInfo ci)
    {
        ci.cancel();
    }

    @Inject(method = "isPulledByMagnets", at = @At("HEAD"), cancellable = true, require = 0)
    private static void disableIsPulledByMagnets(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);
    }

    @Inject(method = "attachesToMagnets", at = @At("HEAD"), cancellable = true, require = 0)
    private static void disableAttachesToMagnets(Entity entity, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);
    }
}