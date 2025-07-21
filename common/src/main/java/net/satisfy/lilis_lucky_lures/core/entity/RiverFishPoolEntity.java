package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class RiverFishPoolEntity extends FloatingDebrisEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public RiverFishPoolEntity(EntityType<? extends RiverFishPoolEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LootTable getLootTable(ServerLevel serverLevel) {
        return serverLevel.getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, LilisLuckyLuresIdentifier.identifier("gameplay/fishing_pools/river_fish_pool")));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            updateAnimations();
        }
    }

    private void updateAnimations() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 200;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
    }
}
