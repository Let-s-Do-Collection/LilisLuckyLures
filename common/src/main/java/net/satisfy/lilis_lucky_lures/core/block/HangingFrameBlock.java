package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
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
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);

    private static final VoxelShape TOP_SHAPE = Shapes.join(
            Shapes.join(
                    Shapes.join(
                            Shapes.join(Shapes.empty(), Shapes.box(0.875, 0, 0.4375, 1, 0.875, 0.5625), BooleanOp.OR),
                            Shapes.box(0, 0, 0.4375, 0.125, 0.875, 0.5625), BooleanOp.OR),
                    Shapes.box(0, 0.875, 0.4375, 1, 1, 0.5625), BooleanOp.OR),
            Shapes.box(0.125, 0.625, 0.4375, 0.875, 0.875, 0.5625), BooleanOp.OR);

    private static final VoxelShape BOTTOM_SHAPE = Shapes.join(
            Shapes.join(Shapes.join(Shapes.join(
                                    Shapes.join(Shapes.empty(), Shapes.box(0, 0.125, 0.4375, 0.125, 0.625, 0.5625), BooleanOp.OR),
                                    Shapes.box(0.875, 0.125, 0.4375, 1, 0.625, 0.5625), BooleanOp.OR),
                            Shapes.box(0, 0, 0.3125, 0.125, 0.125, 0.6875), BooleanOp.OR),
                    Shapes.box(0.875, 0, 0.3125, 1, 0.125, 0.6875), BooleanOp.OR),
            Shapes.box(0, 0.625, 0.4375, 1, 1, 0.5625), BooleanOp.OR);

    private static final Map<Direction, VoxelShape> TOP_SHAPES = new HashMap<>();
    private static final Map<Direction, VoxelShape> BOTTOM_SHAPES = new HashMap<>();

    static {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            TOP_SHAPES.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, TOP_SHAPE));
            BOTTOM_SHAPES.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, BOTTOM_SHAPE));
        }
    }

    public HangingFrameBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos posAbove = ctx.getClickedPos().above();
        Level level = ctx.getLevel();

        if (!level.getBlockState(posAbove).canBeReplaced(ctx)) {
            return null;
        }

        BlockState baseState = defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(HALF, DoubleBlockHalf.LOWER);

        level.setBlock(posAbove, baseState.setValue(HALF, DoubleBlockHalf.UPPER), 3);

        return baseState;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
                level.removeBlock(pos.above(), false);
            } else {
                level.removeBlock(pos.below(), false);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);

        if (direction == Direction.DOWN && half == DoubleBlockHalf.UPPER && neighborState.getBlock() != this) {
            return Blocks.AIR.defaultBlockState();
        }

        if (direction == Direction.UP && half == DoubleBlockHalf.LOWER && neighborState.getBlock() != this) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? TOP_SHAPES.get(state.getValue(FACING)) : BOTTOM_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HangingFrameBlockEntity(pos, state, this.size());
    }

    public int size() {
        return 3;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof HangingFrameBlockEntity shelfBlockEntity)) {
            return InteractionResult.PASS;
        }

        Optional<Tuple<Float, Float>> optional = LilisLuckyLuresUtil.getRelativeHitCoordinatesForBlockFace(hit, state.getValue(FACING), new Direction[]{Direction.DOWN, Direction.UP});
        if (optional.isEmpty()) {
            return InteractionResult.PASS;
        }

        int i = 2 - (int) (optional.get().getA() * 3);
        if (i < 0 || i >= shelfBlockEntity.getInventory().size()) {
            return InteractionResult.PASS;
        }

        if (!shelfBlockEntity.getInventory().get(i).isEmpty()) {
            if (!world.isClientSide) {
                ItemStack itemStack = shelfBlockEntity.removeStack(i);
                world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.getInventory().add(itemStack)) {
                    player.drop(itemStack, false);
                }
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.is(ItemTags.FISHES)) {
            if (!world.isClientSide) {
                shelfBlockEntity.setStack(i, stack.split(1));
                world.playSound(null, pos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (player.isCreative()) {
                    stack.grow(1);
                }
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.CONSUME;
    }
}
