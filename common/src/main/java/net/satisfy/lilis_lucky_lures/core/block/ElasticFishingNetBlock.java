package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
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
            double fallVelocity = Math.abs(entity.getDeltaMovement().y);
            double bounceFactor = Mth.clamp(fallVelocity * 0.9, 0.5, 3.5);
            if (bounceFactor > 0.6) { 
                entity.setDeltaMovement(entity.getDeltaMovement().x, bounceFactor, entity.getDeltaMovement().z);
            }
            entity.fallDistance = 0;
        }
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

    @Override
    public void updateEntityAfterFallOn(BlockGetter world, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(world, entity);
        } else {
            bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 motion = entity.getDeltaMovement();
        double minVelocity = 0.5; 

        if (motion.y < -minVelocity) {
            double fallVelocity = Math.abs(motion.y);
            double bounceFactor = Mth.clamp(fallVelocity * 0.85, 0.5, 3.0);
            entity.setDeltaMovement(motion.x, bounceFactor, motion.z);
        }
    }
}