package io.github.ktraw2.antigrieftweaks.mixin;

import io.github.ktraw2.antigrieftweaks.AntiGriefTweaks;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SculkCatalystBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkCatalystBlockEntity.class)
public abstract class DisableSculkCatalystSpreadMixin extends BlockEntity implements GameEventListener {
	public DisableSculkCatalystSpreadMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Shadow
	abstract void triggerCriteria(LivingEntity deadEntity);

	@Inject(
			method = "listen(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/world/event/GameEvent$Emitter;Lnet/minecraft/util/math/Vec3d;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	public void injected(
			final ServerWorld world,
			final GameEvent event,
			final GameEvent.Emitter emitter,
			final Vec3d emitterPos,
			final CallbackInfoReturnable<Boolean> callbackInfo
	) {
		final boolean gameruleEnabled = AntiGriefTweaks.CONFIG.disableSculkCatalyst.enableGamerule();
		if (gameruleEnabled &&
				!world.getGameRules().getBoolean(AntiGriefTweaks.DISABLE_SCULK_CATALYST)) {
			return;
		}
		else if (!gameruleEnabled && !AntiGriefTweaks.CONFIG.disableSculkCatalyst.defaultSetting()) {
			return;
		}

		final Entity entity = emitter.sourceEntity();
		if (event == GameEvent.ENTITY_DIE && entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)entity;
			if (!livingEntity.isExperienceDroppingDisabled()) {
				if (livingEntity.shouldDropXp() && livingEntity.getXpToDrop() > 0) {
					this.triggerCriteria(livingEntity);
				}
				livingEntity.disableExperienceDropping();
				SculkCatalystBlock.bloom(world, this.pos, this.getCachedState(), world.getRandom());
			}
			callbackInfo.setReturnValue(true);
		}

		callbackInfo.setReturnValue(false);
	}
}