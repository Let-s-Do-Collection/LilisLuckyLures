package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class FloatingDebrisEntity extends Entity {
    private int interactions = 0;
    private static final int MAX_INTERACTIONS = 3;

    public final float planksFrequency;
    public final float planksPhase;
    public final float planksAmplitude;

    public FloatingDebrisEntity(EntityType<? extends FloatingDebrisEntity> type, Level level) {
        super(type, level);
        Random random = new Random();
        this.planksFrequency = 0.15F + random.nextFloat() * 0.2F;
        this.planksPhase = random.nextFloat() * (float) Math.PI * 2;
        this.planksAmplitude = 0.3F + random.nextFloat() * 0.4F;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        interactions = compound.getInt("Interactions");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Interactions", interactions);
    }

    public void onFishHookInteract(Player player) {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("entities/floating_debris"));
            LootParams lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, position())
                    .withParameter(LootContextParams.KILLER_ENTITY, player)
                    .create(LootContextParamSets.ENTITY);
            List<ItemStack> loot = lootTable.getRandomItems(lootParams);
            loot.forEach(player::addItem);
            interactions++;
            if (interactions >= MAX_INTERACTIONS) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return new AABB(this.getX() - 1, this.getY() - 1, this.getZ() - 1, this.getX() + 1, this.getY() + 1, this.getZ() + 1);
    }
}
