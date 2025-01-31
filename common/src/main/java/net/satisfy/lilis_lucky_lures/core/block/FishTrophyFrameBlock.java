package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrophyFrameBlockEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class FishTrophyFrameBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.9375, 0.9375, 0.8125, 1), BooleanOp.OR);        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    public FishTrophyFrameBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        Level worldView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        Direction[] placementDirections = ctx.getNearestLookingDirections();

        for (Direction direction : placementDirections) {
            if (direction.getAxis().isHorizontal() && (blockState = blockState.setValue(FACING, direction.getOpposite())).canSurvive(worldView, blockPos)) {
                return blockState;
            }
        }
        return null;
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof FishTrophyFrameBlockEntity displayBlockEntity)) return InteractionResult.PASS;

        ItemStack stackInHand = player.getItemInHand(hand);

        if (!stackInHand.isEmpty()) {
            if (!level.isClientSide && displayBlockEntity.getDisplayedItem().isEmpty()) {
                ItemStack singleItemStack = stackInHand.copy();
                singleItemStack.setCount(1);
                if (displayBlockEntity.setDisplayedItem(singleItemStack)) {
                    if (!player.getAbilities().instabuild) {
                        stackInHand.shrink(1);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }

        if (!displayBlockEntity.getDisplayedItem().isEmpty()) {
            if (!level.isClientSide) {
                ItemStack displayedItem = displayBlockEntity.getDisplayedItem().copy();
                if (!player.addItem(displayedItem)) {
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), displayedItem);
                    level.addFreshEntity(itemEntity);
                }
                displayBlockEntity.removeDisplayedItem(1);
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FishTrophyFrameBlockEntity displayBlockEntity) {
                displayBlockEntity.dropContents();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTrophyFrameBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
