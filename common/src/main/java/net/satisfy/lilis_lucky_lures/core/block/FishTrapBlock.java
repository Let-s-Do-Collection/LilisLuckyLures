package net.satisfy.lilis_lucky_lures.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FishTrapBlock extends BaseEntityBlock {
    public FishTrapBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTrapBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof FishTrapBlockEntity fishTrap) {
            if (!level.isClientSide) {
                ItemStack heldItem = player.getItemInHand(hand);
                if (!heldItem.isEmpty()) {
                    boolean inserted = fishTrap.insertInput(heldItem);
                    if (inserted && !player.isCreative()) {
                        heldItem.shrink(1);
                    }
                } else {
                    ItemStack output = fishTrap.extractOutput();
                    if (!output.isEmpty()) {
                        boolean added = player.getInventory().add(output.copy());
                        if (!added) {
                            popResource(level, pos, output.copy());
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
        return RenderShape.INVISIBLE;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, EntityTypeRegistry.FISH_TRAP.get(), (lvl, pos, blkState, blockEntity) -> blockEntity.tick());
    }
}
