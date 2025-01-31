package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.satisfy.lilis_lucky_lures.core.block.entity.RedstoneCoilBlockEntity;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

@SuppressWarnings("deprecation")
public class RedstoneCoilBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty UPPER = BooleanProperty.create("upper");

    private static final Random RANDOM = new Random();

    public RedstoneCoilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(FACING, Direction.NORTH)
                .setValue(UPPER, false));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, net.minecraft.world.level.block.Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean powered = level.hasNeighborSignal(pos);
            if (state.getValue(ACTIVE) != powered) {
                level.setBlock(pos, state.setValue(ACTIVE, powered), 3);
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof RedstoneCoilBlockEntity coil) {
                    coil.setActive(powered);
                }
            }
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos posAbove = ctx.getClickedPos().above();
        Level level = ctx.getLevel();

        if (!level.getBlockState(posAbove).canBeReplaced(ctx)) {
            return null;
        }

        Direction facing = ctx.getHorizontalDirection().getOpposite();

        BlockState baseState = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(UPPER, false);

        BlockState upperState = defaultBlockState()
                .setValue(FACING, facing)
                .setValue(UPPER, true);

        level.setBlock(posAbove, upperState, 3);

        return baseState;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!state.getValue(UPPER)) {
                level.removeBlock(pos.above(), false);
            } else {
                level.removeBlock(pos.below(), false);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneCoilBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, EntityTypeRegistry.REDSTONE_COIL.get(), RedstoneCoilBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING, UPPER);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE) && !state.getValue(UPPER)) {
            double centerX = pos.getX() + 0.5;
            double centerY = pos.getY() + 1.0;
            double centerZ = pos.getZ() + 0.5;

            for (int i = 0; i < 2; i++) {
                double offsetX = (RANDOM.nextDouble() - 0.5) * 0.6;
                double offsetY = RANDOM.nextDouble() * 0.3;
                double offsetZ = (RANDOM.nextDouble() - 0.5) * 0.6;
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 0, 0, 0);
            }
        }
    }
}
