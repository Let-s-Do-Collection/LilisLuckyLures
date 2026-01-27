package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
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
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos basePos = context.getClickedPos();
        BlockPos upperPos = basePos.above();

        if (basePos.getY() >= level.getMaxBuildHeight() - 1) {
            return null;
        }

        if (!level.getWorldBorder().isWithinBounds(basePos) || !level.getWorldBorder().isWithinBounds(upperPos)) {
            return null;
        }

        if (!level.getBlockState(basePos).canBeReplaced(context)) {
            return null;
        }

        if (!level.getBlockState(upperPos).canBeReplaced(context)) {
            return null;
        }

        BlockState baseState = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(HALF, DoubleBlockHalf.LOWER);

        BlockState upperState = baseState.setValue(HALF, DoubleBlockHalf.UPPER);

        Player player = context.getPlayer();
        CollisionContext collisionContext = player == null ? CollisionContext.empty() : CollisionContext.of(player);

        if (!level.isUnobstructed(baseState, basePos, collisionContext)) {
            return null;
        }

        if (!level.isUnobstructed(upperState, upperPos, collisionContext)) {
            return null;
        }

        return baseState;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide) {
            BlockPos upperPos = pos.above();
            BlockState upperState = state.setValue(HALF, DoubleBlockHalf.UPPER);
            level.setBlock(upperPos, upperState, 3);
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (half == DoubleBlockHalf.LOWER) {
            return super.canSurvive(state, level, pos);
        }
        BlockState belowState = level.getBlockState(pos.below());
        return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos basePos = half == DoubleBlockHalf.UPPER ? pos.below() : pos;

            BlockEntity baseEntity = level.getBlockEntity(basePos);
            if (baseEntity instanceof HangingFrameBlockEntity frameBlockEntity) {
                for (ItemStack stack : frameBlockEntity.getInventory()) {
                    if (!stack.isEmpty()) {
                        Block.popResource(level, basePos, stack);
                    }
                }
                level.removeBlockEntity(basePos);
            }

            BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this) && otherState.getValue(HALF) != half) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.getValue(HALF);

        if (direction == Direction.DOWN && half == DoubleBlockHalf.UPPER && !neighborState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (direction == Direction.UP && half == DoubleBlockHalf.LOWER && !neighborState.is(this)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? TOP_SHAPES.get(state.getValue(FACING)) : BOTTOM_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return null;
        }
        return new HangingFrameBlockEntity(pos, state, this.size());
    }

    public int size() {
        return 3;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack item, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos basePos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        BlockState baseState = world.getBlockState(basePos);
        if (!baseState.is(this)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity blockEntity = world.getBlockEntity(basePos);
        if (!(blockEntity instanceof HangingFrameBlockEntity frameBlockEntity)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        Optional<Tuple<Float, Float>> optional = LilisLuckyLuresUtil.getRelativeHitCoordinatesForBlockFace(hit, baseState.getValue(FACING), new Direction[]{Direction.DOWN, Direction.UP});
        if (optional.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int slotIndex = 2 - (int) (optional.get().getA() * 3);
        if (slotIndex < 0 || slotIndex >= frameBlockEntity.getInventory().size()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!frameBlockEntity.getInventory().get(slotIndex).isEmpty()) {
            if (!world.isClientSide) {
                ItemStack removed = frameBlockEntity.removeStack(slotIndex);
                world.playSound(null, basePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (!player.getInventory().add(removed)) {
                    player.drop(removed, false);
                }
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, basePos);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }

        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty() && held.is(ItemTags.FISHES)) {
            if (!world.isClientSide) {
                frameBlockEntity.setStack(slotIndex, held.split(1));
                world.playSound(null, basePos, SoundEvents.WOOL_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (player.isCreative()) {
                    held.grow(1);
                }
                world.gameEvent(player, GameEvent.BLOCK_CHANGE, basePos);
            }
            return ItemInteractionResult.sidedSuccess(world.isClientSide);
        }

        return ItemInteractionResult.CONSUME;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }
}