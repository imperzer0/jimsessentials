package com.github.jimsessentials.mixins;

import com.github.jimsessentials.dimensions.wastelands.DIM_Wastelands;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin
{
    private static final ThreadLocal<Boolean> jimsessentials$TARGETED_ATTACK =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    private boolean jimsessentials$isInWastelands()
    {
        WitherBoss self = (WitherBoss) (Object) this;
        return !self.level().isClientSide()
                && self.level().dimension().equals(DIM_Wastelands.Instance().LEVEL_KEY);
    }

    @Inject(
            method = "setAlternativeTarget(II)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void filterNonPlayerAlternativeTarget(int targetOffset, int newId, CallbackInfo ci)
    {
        if (newId <= 0 || !jimsessentials$isInWastelands())
            return;

        WitherBoss self = (WitherBoss) (Object) this;
        Entity entity = self.level().getEntity(newId);
        if (!(entity instanceof Player))
        {
            self.setAlternativeTarget(targetOffset, 0);
            ci.cancel();
        }
    }

    @Inject(
            method = "performRangedAttack(ILnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void beforeTargetedAttack(int head, LivingEntity target, CallbackInfo ci)
    {
        if (!jimsessentials$isInWastelands())
            return;

        if (target == null || !target.isAlive() || !(target instanceof Player))
        {
            ci.cancel();
            return;
        }

        jimsessentials$TARGETED_ATTACK.set(Boolean.TRUE);
    }

    @Inject(
            method = "performRangedAttack(ILnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("RETURN")
    )
    private void afterTargetedAttack(int head, LivingEntity target, CallbackInfo ci)
    {
        jimsessentials$TARGETED_ATTACK.set(Boolean.FALSE);
    }

    @Inject(
            method = "performRangedAttack(IDDDZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preventRandomCoordinateSkull(int head, double x, double y, double z, boolean isDangerous, CallbackInfo ci)
    {
        if (jimsessentials$isInWastelands() && !Boolean.TRUE.equals(jimsessentials$TARGETED_ATTACK.get()))
            ci.cancel();
    }
}