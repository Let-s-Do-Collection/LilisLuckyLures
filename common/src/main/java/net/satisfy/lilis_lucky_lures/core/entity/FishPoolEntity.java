package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class FishPoolEntity extends Entity {
    private final float randomRotation;
    private int interactions = 0;
    private static final int MAX_INTERACTIONS = 3;
    private static final float DESTRUCTION_SPEED = 0.05F;

    private static final EntityDataAccessor<Boolean> IS_DESTROYING = SynchedEntityData.defineId(FishPoolEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DESTRUCTION_PROGRESS = SynchedEntityData.defineId(FishPoolEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> HURT_TIME = SynchedEntityData.defineId(FishPoolEntity.class, EntityDataSerializers.INT);

    public final float planksFrequency;
    public final float planksPhase;
    public final float planksAmplitude;

    public FishPoolEntity(EntityType<? extends FishPoolEntity> type, Level level) {
        super(type, level);
        Random random = new Random();
        this.setBoundingBox(this.getBoundingBox().move(0, 1, 0));
        this.randomRotation = random.nextFloat() * 360.0F;
        this.planksFrequency = 0.15F + random.nextFloat() * 0.2F;
        this.planksPhase = random.nextFloat() * (float) Math.PI * 2;
        this.planksAmplitude = 0.3F + random.nextFloat() * 0.4F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_DESTROYING, false);
        this.entityData.define(DESTRUCTION_PROGRESS, 0.0F);
        this.entityData.define(HURT_TIME, 0);
    }

    @Override
    public void tick() {
        super.tick();

        int currentHurtTime = this.entityData.get(HURT_TIME);
        if (currentHurtTime > 0) {
            this.entityData.set(HURT_TIME, currentHurtTime - 1);
        }

        if (!level().isClientSide && !isAboveWater()) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        if (this.entityData.get(IS_DESTROYING)) {
            float currentProgress = this.entityData.get(DESTRUCTION_PROGRESS);
            currentProgress += DESTRUCTION_SPEED;
            this.entityData.set(DESTRUCTION_PROGRESS, currentProgress);
            if (currentProgress >= 1.0F) {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    public float getRandomRotation() {
        return randomRotation;
    }

    private boolean isAboveWater() {
        return level().getBlockState(this.blockPosition().below()).getFluidState().isSource();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        interactions = compound.getInt("Interactions");
        boolean destroying = compound.getBoolean("IsDestroying");
        float progress = compound.getFloat("DestructionProgress");
        this.entityData.set(IS_DESTROYING, destroying);
        this.entityData.set(DESTRUCTION_PROGRESS, progress);
        this.entityData.set(HURT_TIME, compound.getInt("HurtTime"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Interactions", interactions);
        compound.putBoolean("IsDestroying", this.entityData.get(IS_DESTROYING));
        compound.putFloat("DestructionProgress", this.entityData.get(DESTRUCTION_PROGRESS));
        compound.putInt("HurtTime", this.entityData.get(HURT_TIME));
    }

    public void triggerHurt() {
        this.entityData.set(HURT_TIME, 10);
    }

    protected LootTable getLootTable(ServerLevel serverLevel) {
        return serverLevel.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("fishing_pools/floating_debris"));
    }

    public void onFishHookInteract(Player player) {
        if (!level().isClientSide && level() instanceof ServerLevel serverLevel) {
            LootTable lootTable = getLootTable(serverLevel);
            LootParams lootParams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, position())
                    .withParameter(LootContextParams.KILLER_ENTITY, player)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, serverLevel.damageSources().generic())
                    .create(LootContextParamSets.ENTITY);
            List<ItemStack> loot = lootTable.getRandomItems(lootParams);
            loot.forEach(player::addItem);
            triggerHurt();
            interactions++;
            if (interactions >= MAX_INTERACTIONS) {
                removeWithEffects(serverLevel);
            }
        }
    }


    public void removeWithEffects(ServerLevel serverLevel) {
        serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GOAT_SCREAMING_HORN_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);

        for (int i = 0; i < 500; i++) {
            double yOffset = serverLevel.random.nextDouble() * 15.0;
            double xOffset = 0.25 * (serverLevel.random.nextDouble() - 0.5);
            double zOffset = 0.25 * (serverLevel.random.nextDouble() - 0.5);
            double velocityY = 0.1 + serverLevel.random.nextDouble() * 0.2;

            serverLevel.sendParticles(ParticleTypes.SPLASH, this.getX() + xOffset + 0.5, this.getY() + yOffset, this.getZ() + zOffset + 0.5, 1, 0.0, velocityY, 0.0, 0.0);
        }

        for (int i = 0; i < 125; i++) {
            double xOffset = serverLevel.random.nextGaussian() * 0.2;
            double yOffset = serverLevel.random.nextGaussian() * 0.2;
            double zOffset = serverLevel.random.nextGaussian() * 0.2;

            serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 20, xOffset, yOffset, zOffset, 0.1);
            serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY() + 1, this.getZ(), 10, xOffset, yOffset, zOffset, 0.1);
            serverLevel.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 5, xOffset, yOffset, zOffset, 0.1);
        }

        this.entityData.set(IS_DESTROYING, true);
    }

    public int getHurtTime() {
        return this.entityData.get(HURT_TIME);
    }

    @Override
    public @NotNull AABB getBoundingBoxForCulling() {
        return new AABB(this.getX() - 1, this.getY() - 1, this.getZ() - 1, this.getX() + 1, this.getY() + 1, this.getZ() + 1);
    }
}
