package io.github.ktraw2.antigrieftweaks.mixin;

import io.github.ktraw2.antigrieftweaks.AntiGriefTweaks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(TntEntity.class)
public abstract class DisableTNTMixin extends Entity {
    public DisableTNTMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "explode()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injected(
            final CallbackInfo callbackInfo
    ) {
        if (
                Arrays.stream(AntiGriefTweaks.CONFIG.disableTNTDimensions())
                        .map(Identifier::new)
                        .anyMatch(e -> ((EntityAccessorMixin) (Object) this).getWorld().getDimensionEntry().matchesId(e))
        ) {
            callbackInfo.cancel();
        }
    }
}
