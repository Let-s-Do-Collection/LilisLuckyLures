package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class RiverFishPoolEntity extends FloatingDebrisEntity {
    public RiverFishPoolEntity(EntityType<? extends RiverFishPoolEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected LootTable getLootTable(ServerLevel serverLevel) {
        return serverLevel.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("gameplay/fishing_pools/river_fish_pool"));
    }
}
