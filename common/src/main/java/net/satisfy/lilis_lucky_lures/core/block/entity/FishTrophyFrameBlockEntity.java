package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.lilis_lucky_lures.core.block.FishTrophyFrameBlock;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FishTrophyFrameBlockEntity extends BlockEntity implements Clearable {
    private ItemStack displayedItem = ItemStack.EMPTY;

    public FishTrophyFrameBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.FISH_TROPHY_FRAME.get(), pos, state);
    }

    public ItemStack getDisplayedItem() {
        return this.displayedItem;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.displayedItem = tag.contains("DisplayedItem", 10) ? ItemStack.parseOptional(provider, tag.getCompound("DisplayedItem")) : ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (!this.displayedItem.isEmpty()) {
            tag.put("DisplayedItem", this.displayedItem.save(provider, new CompoundTag()));
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        if (!this.displayedItem.isEmpty()) {
            tag.put("DisplayedItem", this.displayedItem.save(provider, new CompoundTag()));
        }
        return tag;
    }

    public boolean setDisplayedItem(ItemStack stack) {
        if (!this.displayedItem.isEmpty()) return false;
        this.displayedItem = stack.copyWithCount(1);
        this.updateBlockState(true);
        return true;
    }

    public void removeDisplayedItem(int count) {
        if (!this.displayedItem.isEmpty()) {
            this.displayedItem.shrink(count);
            if (this.displayedItem.isEmpty()) {
                this.displayedItem = ItemStack.EMPTY;
                this.updateBlockState(false);
            } else {
                this.markUpdated();
            }
        }
    }

    public void dropContents() {
        if (!this.displayedItem.isEmpty()) {
            assert this.level != null;
            ItemEntity e = new ItemEntity(this.level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), this.displayedItem);
            this.level.addFreshEntity(e);
            this.displayedItem = ItemStack.EMPTY;
            this.updateBlockState(false);
        } else {
            this.markUpdated();
        }
    }

    private void updateBlockState(boolean hasItem) {
        if (level != null) {
            BlockState s = level.getBlockState(worldPosition);
            if (s.hasProperty(FishTrophyFrameBlock.HAS_ITEM)) {
                level.setBlock(worldPosition, s.setValue(FishTrophyFrameBlock.HAS_ITEM, hasItem), 3);
            }
            this.markUpdated();
        }
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public void clearContent() {
        this.displayedItem = ItemStack.EMPTY;
        this.updateBlockState(false);
    }
}
