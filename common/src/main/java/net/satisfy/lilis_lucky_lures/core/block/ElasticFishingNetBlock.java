package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ElasticFishingNetBlock extends Block {
    private static final VoxelShape SHAPE = Shapes.box(0.0, 3.0 / 16.0, 0.0, 1.0, (3.0 + 1.0) / 16.0, 1.0);
    private static final EntityDataAccessor<Integer> BOUNCE_COUNT = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);

    public ElasticFishingNetBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float fallDistance) {
        if (!level.isClientSide) {
            int bounceCount = getBounceCount(entity);

            if (bounceCount < 2) {
                double fallVelocity = Math.abs(entity.getDeltaMovement().y);
                if (fallVelocity > 0.5) {
                    bounceUp(entity, fallVelocity);
                    setBounceCount(entity, bounceCount + 1);
                } else {
                    resetBounceCount(entity);
                }
            } else {
                resetBounceCount(entity);
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
            }
            entity.fallDistance = 0;
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        if (!entity.isSuppressingBounce()) {
            int bounceCount = getBounceCount(entity);
            if (bounceCount < 2) {
                double fallVelocity = Math.abs(entity.getDeltaMovement().y);
                if (fallVelocity > 0.3) {
                    bounceUp(entity, fallVelocity);
                    setBounceCount(entity, bounceCount + 1);
                } else {
                    resetBounceCount(entity);
                }
            } else {
                resetBounceCount(entity);
                entity.setDeltaMovement(entity.getDeltaMovement().x, 0, entity.getDeltaMovement().z);
            }
        }
    }

    private void bounceUp(Entity entity, double fallVelocity) {
        double bounceFactor = Mth.clamp(fallVelocity * 0.85, 0.5, 2.0);
        entity.setDeltaMovement(entity.getDeltaMovement().x, bounceFactor, entity.getDeltaMovement().z);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (!(player.getInventory().getArmor(0).getItem() instanceof ArmorItem)) {
                Vec3 motion = player.getDeltaMovement();
                player.setDeltaMovement(motion.multiply(0.9, 1, 0.9));
            }
        }
    }

    private int getBounceCount(Entity entity) {
        return entity.getEntityData().get(BOUNCE_COUNT);
    }

    private void setBounceCount(Entity entity, int count) {
        entity.getEntityData().set(BOUNCE_COUNT, count);
    }

    private void resetBounceCount(Entity entity) {
        entity.getEntityData().set(BOUNCE_COUNT, 0);
    }
}
