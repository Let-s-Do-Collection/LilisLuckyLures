package net.satisfy.lilis_lucky_lures.core.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

public class DynamiteEntity extends ThrowableItemProjectile {
    private int lifeAfterImpact = 0;
    private static final int TIME_BEFORE_EXPLOSION = 60;
    private boolean hasImpacted = false;

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
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (!hasImpacted) {
                this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            } else {
                lifeAfterImpact++;
                if (lifeAfterImpact >= TIME_BEFORE_EXPLOSION) {
                    explode();
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void onHit(net.minecraft.world.phys.HitResult result) {
        super.onHit(result);
        if (!hasImpacted) {
            hasImpacted = true;
        }
    }

    private void explode() {
        boolean mobGriefing = this.level().getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING);
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, mobGriefing ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE);
        this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        this.level().playSound(null, new BlockPos((int) this.getX(), (int) this.getY(), (int) this.getZ()), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
