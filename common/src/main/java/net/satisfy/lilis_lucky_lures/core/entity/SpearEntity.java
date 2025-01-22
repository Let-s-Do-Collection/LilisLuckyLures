package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;

public class SpearEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SpearEntity.class, EntityDataSerializers.ITEM_STACK);
    private boolean stuck = false;
    private BlockPos stuckPosition;
    private float stuckYaw;
    private float stuckPitch;

    public SpearEntity(EntityType<? extends SpearEntity> type, Level level) {
        super(type, level);
    }

    public SpearEntity(Level level, LivingEntity owner) {
        super(EntityTypeRegistry.SPEAR.get(), owner, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void setItem(ItemStack stack) {
        this.entityData.set(DATA_ITEM_STACK, stack.copy());
    }

    public ItemStack getItemStack() {
        return this.entityData.get(DATA_ITEM_STACK);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Item", getItemStack().save(new CompoundTag()));
        compound.putBoolean("Stuck", stuck);
        if (stuck && stuckPosition != null) {
            compound.putInt("StuckX", stuckPosition.getX());
            compound.putInt("StuckY", stuckPosition.getY());
            compound.putInt("StuckZ", stuckPosition.getZ());
            compound.putFloat("StuckYaw", stuckYaw);
            compound.putFloat("StuckPitch", stuckPitch);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Item")) {
            setItem(ItemStack.of(compound.getCompound("Item")));
        }
        if (compound.getBoolean("Stuck")) {
            int x = compound.getInt("StuckX");
            int y = compound.getInt("StuckY");
            int z = compound.getInt("StuckZ");
            stuckPosition = new BlockPos(x, y, z);
            stuckYaw = compound.getFloat("StuckYaw");
            stuckPitch = compound.getFloat("StuckPitch");
            stuck = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (stuck && stuckPosition != null) {
            this.setPos(stuckPosition.getX() + 0.5, stuckPosition.getY() + 0.5, stuckPosition.getZ() + 0.5);
            this.setYRot(stuckYaw);
            this.setXRot(stuckPitch);
            this.setDeltaMovement(0, 0, 0);
        } else if (this.isInWater() || this.stuck) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (!stuck) {
            if (hit.getEntity() instanceof LivingEntity target) {
                target.hurt(this.damageSources().thrown(this, this.getOwner()), 16.0F);
                this.discard();
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide && !stuck) {
            if (result.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockResult = (BlockHitResult) result;
                BlockPos pos = blockResult.getBlockPos();
                this.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                stuck = true;
                stuckPosition = pos;
                stuckYaw = this.getYRot();
                stuckPitch = this.getXRot();
            }
        }
    }
}
