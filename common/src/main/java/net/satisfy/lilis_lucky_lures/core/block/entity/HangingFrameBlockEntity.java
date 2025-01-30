package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;
import org.jetbrains.annotations.NotNull;

public class HangingFrameBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    public HangingFrameBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.HANGING_FRAME.get(), pos, state);
    }

    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level instanceof ServerLevel serverLevel && !level.isClientSide) {
            ClientboundBlockEntityDataPacket updatePacket = getUpdatePacket();
            for (ServerPlayer player : LilisLuckyLuresUtil.getTrackingPlayers(serverLevel, getBlockPos())) {
                player.connection.send(updatePacket);
            }
            serverLevel.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
        super.setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, items);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public NonNullList<ItemStack> getInventory() {
        return items;
    }

    public ItemStack getStack(int slot) {
        return items.get(slot);
    }
}
