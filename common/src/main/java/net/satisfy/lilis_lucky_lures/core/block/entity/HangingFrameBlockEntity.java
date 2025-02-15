package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.satisfy.lilis_lucky_lures.core.block.HangingFrameBlock;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;
import org.jetbrains.annotations.NotNull;

public class HangingFrameBlockEntity extends BlockEntity {
    private int size;
    private NonNullList<ItemStack> inventory;

    public HangingFrameBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.HANGING_FRAME.get(), pos, state);
        this.size = 3;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public HangingFrameBlockEntity(BlockPos pos, BlockState state, int size) {
        super(EntityTypeRegistry.HANGING_FRAME.get(), pos, state);
        this.size = size;
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
    }

    public ItemStack removeStack(int slot) {
        ItemStack stack = this.inventory.set(slot, ItemStack.EMPTY);
        this.setChanged();
        return stack;
    }

    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        Level var2 = this.level;
        if (var2 instanceof ServerLevel serverLevel) {
            if (!this.level.isClientSide()) {
                Packet<ClientGamePacketListener> updatePacket = this.getUpdatePacket();

                for (ServerPlayer player : LilisLuckyLuresUtil.getTrackingPlayers(serverLevel, this.getBlockPos())) {
                    player.connection.send(updatePacket);
                }
            }
        }

        super.setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.size = nbt.getInt("size");
        this.inventory = NonNullList.withSize(this.size, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putInt("size", this.size);
        super.saveAdditional(nbt);
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
        return this.inventory;
    }

    public boolean isTop() {
        return getBlockState().getValue(HangingFrameBlock.HALF) == DoubleBlockHalf.UPPER;
    }
}
