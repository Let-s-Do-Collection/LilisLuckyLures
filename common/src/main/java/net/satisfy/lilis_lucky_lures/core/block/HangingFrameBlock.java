package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Tuple;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.lilis_lucky_lures.core.block.entity.HangingFrameBlockEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class HangingFrameBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape BASE_SHAPE = Shapes.join(
            Shapes.join(
                    Shapes.join(
                            Shapes.join(
                                    Shapes.join(
                                            Shapes.empty(),
                                            Shapes.box(0, 0.125, 0.4375, 0.125, 0.625, 0.5625), BooleanOp.OR),
                                    Shapes.box(0.875, 0.125, 0.4375, 1, 0.625, 0.5625), BooleanOp.OR),
                            Shapes.box(0, 0, 0.3125, 0.125, 0.125, 0.6875), BooleanOp.OR),
                    Shapes.box(0.875, 0, 0.3125, 1, 0.125, 0.6875), BooleanOp.OR),
            Shapes.box(0, 0.625, 0.4375, 1, 1, 0.5625), BooleanOp.OR
    );
    private static final Map<Direction, VoxelShape> SHAPE = new HashMap<>();

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPE.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, BASE_SHAPE));
        }
    }

    public HangingFrameBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HangingFrameBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof HangingFrameBlockEntity entity)) return InteractionResult.PASS;

        Optional<Tuple<Float, Float>> relative = LilisLuckyLuresUtil.getRelativeHitCoordinatesForBlockFace(hit, state.getValue(FACING), new Direction[]{});
        if (relative.isEmpty()) return InteractionResult.PASS;

        float x = relative.get().getA();
        int slot = x < 0.33f ? 0 : x < 0.66f ? 1 : 2;
        ItemStack existing = entity.getStack(slot);

        if (existing.isEmpty()) {
            ItemStack held = player.getItemInHand(hand);
            if (!held.isEmpty()) {
                ItemStack copy = held.copy();
                copy.setCount(1);
                entity.setStack(slot, copy);
                held.shrink(1);
                entity.setChanged();
                return InteractionResult.CONSUME;
            }
        } else {
            player.addItem(existing);
            entity.setStack(slot, ItemStack.EMPTY);
            entity.setChanged();
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HangingFrameBlockEntity entity) {
                if (world instanceof ServerLevel serverLevel) {
                    Containers.dropContents(serverLevel, pos, entity.getInventory());
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

}
