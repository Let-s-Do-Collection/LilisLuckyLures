package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.lilis_lucky_lures.core.block.entity.RedstoneCoilBlockEntity;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class RedstoneCoilBlock extends BaseEntityBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final EnumProperty<DoubleBlockHalf> HALF = EnumProperty.create("half", DoubleBlockHalf.class);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<RedstoneCoilTarget> TARGET = EnumProperty.create("target", RedstoneCoilTarget.class);

    public enum RedstoneCoilTarget implements StringRepresentable {
        NONE("none"), FISHES("fishes"), PLAYER("player"), MONSTER("monster");
        private final String name;

        RedstoneCoilTarget(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    private static final VoxelShape TOP_SHAPE;
    private static final VoxelShape BOTTOM_SHAPE;
    private static final Map<Direction, VoxelShape> TOP_SHAPES = new HashMap<>();
    private static final Map<Direction, VoxelShape> BOTTOM_SHAPES = new HashMap<>();
    private static final java.util.Random RANDOM = new java.util.Random();

    public RedstoneCoilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false).setValue(FACING, Direction.NORTH).setValue(HALF, DoubleBlockHalf.LOWER).setValue(TARGET, RedstoneCoilTarget.NONE));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
                boolean powered = level.hasNeighborSignal(pos);
                if (state.getValue(ACTIVE) != powered) {
                    level.setBlock(pos, state.setValue(ACTIVE, powered), 3);
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof RedstoneCoilBlockEntity coil) {
                        coil.setActive(powered);
                    }
                }
                BlockPos posAbove = pos.above();
                BlockState stateAbove = level.getBlockState(posAbove);
                if (stateAbove.getBlock() instanceof RedstoneCoilBlock && stateAbove.getValue(HALF) == DoubleBlockHalf.UPPER && stateAbove.getValue(ACTIVE) != powered) {
                    level.setBlock(posAbove, stateAbove.setValue(ACTIVE, powered), 3);
                    BlockEntity beAbove = level.getBlockEntity(posAbove);
                    if (beAbove instanceof RedstoneCoilBlockEntity coilAbove) {
                        coilAbove.setActive(powered);
                    }
                }
            } else {
                BlockPos posBelow = pos.below();
                BlockState stateBelow = level.getBlockState(posBelow);
                if (stateBelow.getBlock() instanceof RedstoneCoilBlock && stateBelow.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    boolean bottomActive = stateBelow.getValue(ACTIVE);
                    if (state.getValue(ACTIVE) != bottomActive) {
                        level.setBlock(pos, state.setValue(ACTIVE, bottomActive), 3);
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof RedstoneCoilBlockEntity coil) {
                            coil.setActive(bottomActive);
                        }
                    }
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
        BlockState baseState = defaultBlockState().setValue(FACING, facing).setValue(HALF, DoubleBlockHalf.LOWER).setValue(TARGET, RedstoneCoilTarget.NONE);
        BlockState upperState = defaultBlockState().setValue(FACING, facing).setValue(HALF, DoubleBlockHalf.UPPER).setValue(TARGET, RedstoneCoilTarget.NONE);
        level.setBlock(posAbove, upperState, 3);
        return baseState;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos otherPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);

            if (otherState.is(this)) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
                    Block.dropResources(otherState, level, pos.below());
                }
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
        builder.add(ACTIVE, FACING, HALF, TARGET);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVE) && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            double centerX = pos.getX() + 0.5;
            double centerY = pos.getY() + 1.5;
            double centerZ = pos.getZ() + 0.5;
            DustParticleOptions redDust = new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
            for (int i = 0; i < 2; i++) {
                double offsetX = (RANDOM.nextDouble() - 0.5) * 0.6;
                double offsetY = RANDOM.nextDouble() * 0.3;
                double offsetZ = (RANDOM.nextDouble() - 0.5) * 0.6;
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 0, 0, 0);
                level.addParticle(redDust, centerX + offsetX, centerY + offsetY, centerZ + offsetZ, 0, 0, 0);
            }
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        BlockPos basePos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        BlockState baseState = level.getBlockState(basePos);
        if (!(baseState.getBlock() instanceof RedstoneCoilBlock)) return InteractionResult.PASS;
        RedstoneCoilTarget current = baseState.getValue(TARGET);
        RedstoneCoilTarget next = switch (current) {
            case NONE -> RedstoneCoilTarget.FISHES;
            case FISHES -> RedstoneCoilTarget.PLAYER;
            case PLAYER -> RedstoneCoilTarget.MONSTER;
            default -> RedstoneCoilTarget.NONE;
        };
        level.setBlock(basePos, baseState.setValue(TARGET, next), 3);
        return InteractionResult.CONSUME;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (placer instanceof Player player) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RedstoneCoilBlockEntity coil) {
                coil.setOwner(player.getUUID());
            }
        }
    }

    static {
        VoxelShape shape = Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.875, 0.5625);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.5, 0.1875, 0.8125, 0.75, 0.8125), BooleanOp.OR);
        TOP_SHAPE = shape;
        shape = Shapes.box(0.4375, 0.875, 0.4375, 0.5625, 1, 0.5625);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.875, 0.875, 0.875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.875, 0.875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.25, 0.0625, 0.75, 0.75, 0.125), BooleanOp.OR);
        BOTTOM_SHAPE = shape;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            TOP_SHAPES.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, TOP_SHAPE));
            BOTTOM_SHAPES.put(direction, LilisLuckyLuresUtil.rotateShape(Direction.NORTH, direction, BOTTOM_SHAPE));
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? TOP_SHAPES.get(state.getValue(FACING)) : BOTTOM_SHAPES.get(state.getValue(FACING));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        Style defaultStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC));
        Style actionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xffecb3));
        Style noteStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xDAFFFF));

        if (!Screen.hasShiftDown()) {
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.shift_to_show_more").setStyle(actionStyle)
                    .append(Component.translatable("tooltip.lilis_lucky_lures.block.information").setStyle(defaultStyle)));
        } else {
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.description.defense").setStyle(defaultStyle));
            list.add(Component.empty());
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.right_click_to_switch").setStyle(actionStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.modes").setStyle(defaultStyle)));
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.mode.none").setStyle(noteStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.mode.none.description").setStyle(defaultStyle)));
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.mode.player").setStyle(noteStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.mode.player.description").setStyle(defaultStyle)));
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.mode.fishes").setStyle(noteStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.mode.fishes.description").setStyle(defaultStyle)));
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.mode.monster").setStyle(noteStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.mode.monster.description").setStyle(defaultStyle)));
            list.add(Component.empty());
            list.add(Component.translatable("tooltip.lilis_lucky_lures.block.note").setStyle(noteStyle).append(Component.translatable("tooltip.lilis_lucky_lures.block.note.description").setStyle(defaultStyle)));
        }
    }
}
