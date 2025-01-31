package net.satisfy.lilis_lucky_lures.core.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
    }

    public ThrownSpearEntity(Level level, LivingEntity livingEntity, ItemStack itemStack) {
        super(EntityTypeRegistry.THROWN_SPEAR.get(), livingEntity, level);
        this.spearItem = itemStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(itemStack));
        this.entityData.set(ID_FOIL, itemStack.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_LOYALTY, (byte) 0);
        this.entityData.define(ID_FOIL, false);
        this.setBoundingBox(new AABB(-0.25, -0.25, -0.25, 0.25, 0.25, 0.25));
    }

    @Override
    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(0.5F, 0.5F);
    }

    public void tick() {
        if (this.inGroundTime > 4) this.dealtDamage = true;

        Entity entity = this.getOwner();
        int loyaltyLevel = this.entityData.get(ID_LOYALTY);
        if (loyaltyLevel > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 ownerPos = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + ownerPos.y * 0.015 * loyaltyLevel, this.getZ());
                if (this.level().isClientSide) this.yOld = this.getY();

                double speed = 0.05 * loyaltyLevel;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(ownerPos.normalize().scale(speed)));
                if (this.clientSideReturnSpearTickCount == 0) this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                this.clientSideReturnSpearTickCount++;
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity owner = this.getOwner();
        return owner != null && owner.isAlive() && (!(owner instanceof ServerPlayer) || !owner.isSpectator());
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        float damage = 8.0F;
        if (target instanceof LivingEntity livingTarget) {
            damage += EnchantmentHelper.getDamageBonus(this.spearItem, livingTarget.getMobType());

            if (livingTarget.isInWater()) {
                damage *= 1.35F;
            }
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, owner == null ? this : owner);
        this.dealtDamage = true;
        SoundEvent sound = SoundEvents.TRIDENT_HIT;

        if (target.hurt(damageSource, damage) && target instanceof LivingEntity livingTarget) {
            if (target.getType() == EntityType.ENDERMAN) return;
            if (owner instanceof LivingEntity livingOwner) {
                EnchantmentHelper.doPostHurtEffects(livingTarget, livingOwner);
                EnchantmentHelper.doPostDamageEffects(livingOwner, livingTarget);
            }
            this.doPostHurtEffects(livingTarget);
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        float pitch = 1.0F;

        this.playSound(sound, pitch, 1.0F);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || (this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem()));
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) super.playerTouch(player);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Spear", 10)) this.spearItem = ItemStack.of(tag.getCompound("Spear"));
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.entityData.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.spearItem));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Spear", this.spearItem.save(new CompoundTag()));
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
}