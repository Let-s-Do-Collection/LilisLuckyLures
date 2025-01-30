package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class OceanFishPoolEntity extends FloatingDebrisEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public OceanFishPoolEntity(EntityType<? extends OceanFishPoolEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected LootTable getLootTable(ServerLevel serverLevel) {
        return serverLevel.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("gameplay/fishing_pools/ocean_fish_pool"));
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
