package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class FishTrapBlock extends BaseEntityBlock {
    public static final BooleanProperty FULL = BooleanProperty.create("full");
    public static final BooleanProperty HAS_BAIT = BooleanProperty.create("has_bait");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty HANGING = BooleanProperty.create("hanging");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final TagKey<Block> ROPES = TagKey.create(Registries.BLOCK, new LilisLuckyLuresIdentifier("ropes"));

    private static final VoxelShape SHAPE_NORMAL = Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.625, 0.9375);
    private static final VoxelShape SHAPE_HANGING = Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.75, 0.9375);


    public FishTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FULL, false)
                .setValue(HAS_BAIT, false)
                .setValue(WATERLOGGED, false)
                .setValue(HANGING, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FULL, HAS_BAIT, WATERLOGGED, HANGING, FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean hanging = isHanging(level, pos.above());

        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
                .setValue(HANGING, hanging);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP) {
            boolean hanging = isHanging(level, pos.above());
            return state.setValue(HANGING, hanging);
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (fromPos.equals(pos.above())) {
            boolean hanging = isHanging(level, pos.above());
            level.setBlock(pos, state.setValue(HANGING, hanging), 3);
        }
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private boolean isHanging(LevelAccessor level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos);
        return aboveState.is(ROPES) || aboveState.getBlock() instanceof FishNetFenceBlock;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(HANGING) ? SHAPE_HANGING : SHAPE_NORMAL;
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTrapBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FishTrapBlockEntity fishTrap) {
            if (!level.isClientSide) {
                if (player.isShiftKeyDown()) {
                    ItemStack slot0 = fishTrap.getItem(0);
                    if (!slot0.isEmpty()) {
                        boolean added = player.getInventory().add(slot0.copy());
                        if (added) {
                            fishTrap.removeItem(0, slot0.getCount());
                        } else {
                            popResource(level, pos, slot0.copy());
                            fishTrap.removeItem(0, slot0.getCount());
                        }
                    }
                } else {
                    ItemStack heldItem = player.getItemInHand(hand);
                    if (!heldItem.isEmpty() && state.getValue(WATERLOGGED)) {
                        if (fishTrap.getItem(0).isEmpty()) {
                            ItemStack toInsert = heldItem.copy();
                            if (!player.isCreative()) {
                                heldItem.shrink(1);
                            }
                            fishTrap.setItem(0, toInsert);
                        }
                    } else if (heldItem.isEmpty()) {
                        ItemStack output = fishTrap.getItem(1);
                        if (!output.isEmpty()) {
                            boolean added = player.getInventory().add(output.copy());
                            if (added) {
                                fishTrap.removeItem(1, output.getCount());
                            } else {
                                popResource(level, pos, output.copy());
                                fishTrap.removeItem(1, output.getCount());
                            }
                        }
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, EntityTypeRegistry.FISH_TRAP.get(), (lvl, pos, blkState, blockEntity) -> blockEntity.tick());
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);

        if (state.getValue(FULL)) {
            if (random.nextFloat() < 0.1f) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.5;
                double y = pos.getY() + 0.7 + random.nextDouble() * 0.3;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.5;

                level.addParticle(ParticleTypes.BUBBLE_COLUMN_UP, x, y, z, 0, 0.02, 0);
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.isClientSide && state.getValue(WATERLOGGED)) {
            int particleCount = 10;
            double centerX = pos.getX() + 0.5;
            double centerZ = pos.getZ() + 0.5;
            double centerY = pos.getY() + 0.1;
            double velocity = 0.05;

            for (int i = 0; i < particleCount; i++) {
                double angle = (2 * Math.PI / particleCount) * i;
                double radius = 0.15;

                for (int j = 0; j < 3; j++) {
                    double x = centerX + radius * Math.cos(angle);
                    double z = centerZ + radius * Math.sin(angle);
                    double y = centerY + j * 0.1;

                    double velX = Math.cos(angle) * velocity;
                    double velZ = Math.sin(angle) * velocity;
                    double velY = 0.02 + (j * 0.01);

                    level.addParticle(ParticleTypes.BUBBLE, x, y, z, velX, velY, velZ);
                    radius += 0.4;
                }
            }
        }
    }

    public void updateBlockState(Level level, BlockPos pos, boolean full, boolean hasBait) {
        BlockState state = level.getBlockState(pos);
        BlockState newState = state.setValue(FULL, full).setValue(HAS_BAIT, hasBait);
        if (state != newState) {
            level.setBlock(pos, newState, 3);
        }
    }
}
