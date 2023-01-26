package io.github.ktraw2.disablesculkcatalyst.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.block.entity.SculkCatalystBlockEntity")
public abstract class DisableSculkCatalystSpreadMixin extends BlockEntity implements GameEventListener {
	public DisableSculkCatalystSpreadMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Shadow
	abstract void triggerCriteria(LivingEntity deadEntity);

	public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
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
			return true;
		}
		return false;
	}
}