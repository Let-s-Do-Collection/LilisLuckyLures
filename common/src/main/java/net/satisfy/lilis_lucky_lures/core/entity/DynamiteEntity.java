package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class DynamiteEntity extends ThrowableItemProjectile {
    private static final float EXPLOSION_RADIUS = 1.5F;
    private static final float DAMPING_FACTOR = 0.2F;
    private static final double MIN_VELOCITY = 0.2D;
    private int bounceCount;

    public DynamiteEntity(Level world, LivingEntity owner) {
        super(EntityTypeRegistry.DYNAMITE.get(), owner, world);
    }

    public DynamiteEntity(EntityType<? extends DynamiteEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ObjectRegistry.DYNAMITE.get();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    private ParticleOptions getParticle() {
        ItemStack stack = getItemRaw();
        return stack.isEmpty() ? ParticleTypes.SMOKE : new ItemParticleOption(ParticleTypes.ITEM, stack);
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 3) {
            ParticleOptions p = getParticle();
            for (int i = 0; i < 8; i++) {
                level().addParticle(p, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) {
            level().addParticle(ParticleTypes.SMOKE, getX(), getY() + 0.5D, getZ(), 0.0D, 0.0D, 0.0D);
        }
        if (getDeltaMovement().length() < MIN_VELOCITY && bounceCount > 0) {
            if (!level().isClientSide) {
                explode();
                level().broadcastEntityEvent(this, (byte)3);
                discard();
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (bounceCount >= 1 || tickCount >= 160) {
            if (!level().isClientSide) {
                explode();
                level().broadcastEntityEvent(this, (byte)3);
                discard();
            }
        } else {
            bounceCount++;
            super.onHit(result);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onHitBlock(BlockHitResult hit) {
        double vx = getDeltaMovement().x;
        double vy = getDeltaMovement().y;
        double vz = getDeltaMovement().z;
        Direction d = hit.getDirection();
        BlockPos pos = hit.getBlockPos();
        if (level().getBlockState(pos).blocksMotion()) {
            if (!level().isClientSide && bounceCount < 2) {
                level().playSound(null, getX(), getY(), getZ(), SoundEvents.METAL_HIT, SoundSource.NEUTRAL, 1.0F, 4.0F);
            }
            if (d == Direction.EAST || d == Direction.WEST) {
                vx = -vx * DAMPING_FACTOR;
            }
            if (d == Direction.DOWN || d == Direction.UP) {
                vy = -vy * DAMPING_FACTOR;
            }
            if (d == Direction.NORTH || d == Direction.SOUTH) {
                vz = -vz * DAMPING_FACTOR;
            }
            setDeltaMovement(vx, vy, vz);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        double vx = getDeltaMovement().x;
        double vy = getDeltaMovement().y;
        double vz = getDeltaMovement().z;
        double ax = Math.abs(vx);
        double ay = Math.abs(vy);
        double az = Math.abs(vz);
        if (ax >= ay && ax >= az) {
            vx = -vx * DAMPING_FACTOR;
        } else if (ay >= ax && ay >= az) {
            vy = -vy * DAMPING_FACTOR;
        } else {
            vz = -vz * DAMPING_FACTOR;
        }
        setDeltaMovement(vx, vy, vz);
    }

    @Override
    public void shootFromRotation(Entity entity, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        shoot(f, f1, f2, velocity, inaccuracy);
        setDeltaMovement(getDeltaMovement().multiply(0.7F, 0.5F, 0.7F));
        Vec3 v = entity.getDeltaMovement();
        setDeltaMovement(getDeltaMovement().add(v.x, entity.onGround() ? 0.0D : v.y, v.z));
    }

    protected void explode() {
        boolean underwater = isInWater();
        float radius = underwater ? EXPLOSION_RADIUS * 3.0F : EXPLOSION_RADIUS;
        Level.ExplosionInteraction interaction = underwater ? Level.ExplosionInteraction.NONE : Level.ExplosionInteraction.TNT;
        level().explode(this, (float)getX(), (float)(getY(0.0625D) + 0.5F), (float)getZ(), radius, interaction);
    }
}