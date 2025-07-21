package net.satisfy.lilis_lucky_lures.core.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThrownSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack spearItem;
    private boolean dealtDamage;
    public int clientSideReturnSpearTickCount;

    public ThrownSpearEntity(EntityType<? extends ThrownSpearEntity> entityType, Level level) {
        super(entityType, level);
        this.spearItem = new ItemStack(ObjectRegistry.SPEAR.get());
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(spearItem));
        this.entityData.set(ID_FOIL, spearItem.hasFoil());
    }

    public ThrownSpearEntity(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        super(EntityTypeRegistry.THROWN_SPEAR.get(), livingEntity, level, itemStack, null);
        this.spearItem = itemStack.copy();
        this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(itemStack));
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }

    public ThrownSpearEntity(Level level, double x, double y, double z, ItemStack itemStack) {
        super(EntityTypeRegistry.THROWN_SPEAR.get(), x, y, z, level, itemStack, itemStack);
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(itemStack));
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte)0);
        builder.define(ID_FOIL, false);
        this.setBoundingBox(new AABB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25));
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(0.5F, 0.5F);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = this.entityData.get(ID_LOYALTY);
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d = 0.05 * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ObjectRegistry.SPEAR.get());
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 8.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, (Entity)(entity2 == null ? this : entity2));
        Level var7 = this.level();
        if (var7 instanceof ServerLevel serverLevel) {
            f = EnchantmentHelper.modifyDamage(serverLevel, this.spearItem, entity, damageSource, f);
        }

        this.dealtDamage = true;
        if (entity.hurt(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, this.spearItem);
            }

            if (entity instanceof LivingEntity livingEntity) {
                this.doKnockback(livingEntity, damageSource);
                this.doPostHurtEffects(livingEntity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.spearItem;
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Spear", 10)) this.spearItem = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Spear"));
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (getLoyaltyFromItem(this.spearItem)));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Spear", this.spearItem.save(this.level().registryAccess()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != Pickup.ALLOWED || this.entityData.get(ID_LOYALTY) <= 0) super.tickDespawn();
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    private byte getLoyaltyFromItem(ItemStack itemStack) {
        if (this.level() instanceof ServerLevel serverLevel) {
            return (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, itemStack, this), 0, 127);
        } else {
            return 0;
        }
    }
}