package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

public class FloatingBooksEntity extends FloatingDebrisEntity {
    public FloatingBooksEntity(EntityType<? extends FloatingBooksEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public LootTable getLootTable(ServerLevel serverLevel) {
        return serverLevel.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("gameplay/fishing_pools/floating_books"));
    }
}
