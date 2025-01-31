package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FishNetFenceBlock extends CrossCollisionBlock {
    private static final VoxelShape POST_SHAPE = Block.box(7, 0, 7, 9, 16, 9);
    private static final VoxelShape NORTH_SHAPE = Block.box(7, 0, 0, 9, 16, 7);
    private static final VoxelShape SOUTH_SHAPE = Block.box(7, 0, 9, 9, 16, 16);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 0, 7, 7, 16, 9);
    private static final VoxelShape EAST_SHAPE = Block.box(9, 0, 7, 16, 16, 9);

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public FishNetFenceBlock(BlockBehaviour.Properties properties) {
        super(2.0F, 2.0F, 16.0F, 16.0F, 16.0F, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = POST_SHAPE;
        if (state.getValue(NORTH)) shape = net.minecraft.world.phys.shapes.Shapes.or(shape, NORTH_SHAPE);
        if (state.getValue(SOUTH)) shape = net.minecraft.world.phys.shapes.Shapes.or(shape, SOUTH_SHAPE);
        if (state.getValue(WEST)) shape = net.minecraft.world.phys.shapes.Shapes.or(shape, WEST_SHAPE);
        if (state.getValue(EAST)) shape = net.minecraft.world.phys.shapes.Shapes.or(shape, EAST_SHAPE);
        return shape;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    private boolean connectsTo(BlockState state, Direction direction) {
        return state.getBlock() instanceof FishNetFenceBlock;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            ItemStack itemStack = player.getItemInHand(hand);
            return itemStack.is(Items.LEAD) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            return LeadItem.bindPlayerMobs(player, level, pos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = world.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(NORTH, this.connectsTo(world.getBlockState(pos.north()), Direction.SOUTH))
                .setValue(EAST, this.connectsTo(world.getBlockState(pos.east()), Direction.WEST))
                .setValue(SOUTH, this.connectsTo(world.getBlockState(pos.south()), Direction.NORTH))
                .setValue(WEST, this.connectsTo(world.getBlockState(pos.west()), Direction.EAST))
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (direction.getAxis().getPlane() == Plane.HORIZONTAL) {
            return state.setValue((BooleanProperty) PROPERTY_BY_DIRECTION.get(direction), this.connectsTo(newState, direction.getOpposite()));
        }
        return super.updateShape(state, direction, newState, world, pos, neighborPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }
}
