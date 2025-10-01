package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.lilis_lucky_lures.core.block.FishTrapBlock;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FishTrapBlockEntity extends BlockEntity implements WorldlyContainer, Clearable {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    private int timer = 0;
    private int duration = 0;
    private boolean processing = false;
    private FishTrapRecipe recipe;

    public FishTrapBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.FISH_TRAP.get(), pos, state);
    }

    public void tick() {
        if (level != null && !level.isClientSide) {
            ItemStack inputItem = inventory.get(0);
            if (recipe == null && !inputItem.isEmpty()) {
                getRecipe(level, inputItem).ifPresent(currentRecipe -> recipe = currentRecipe);
            }
            if (recipe == null || inputItem.isEmpty()) {
                processing = false;
                timer = 0;
                return;
            }
            if (processing) {
                timer++;
                if (timer >= duration) {
                    processing = false;
                    timer = 0;
                    ItemStack stack = inventory.get(0);
                    if (!stack.isEmpty()) {
                        stack.shrink(1);
                        if (stack.getCount() <= 0) {
                            inventory.set(0, ItemStack.EMPTY);
                        }
                    }
                    addCatchToOutput();
                    recipe = null;
                    setChanged();
                }
            } else if (recipe.getBaitItem().test(inputItem)) {
                processing = true;
                duration = recipe.getRandomDuration();
            }
            updateBlockState();
        }
    }

    private void addCatchToOutput() {
        ItemStack output = recipe.getCatchItem().copy();
        output.setCount(recipe.getCatchCount());
        ItemStack existingOutput = inventory.get(1);
        if (existingOutput.isEmpty()) {
            inventory.set(1, output);
        } else if (ItemStack.isSameItemSameComponents(existingOutput, output)) {
            existingOutput.grow(output.getCount());
            inventory.set(1, existingOutput);
        }
        updateBlockState();
    }

    private void updateBlockState() {
        if (level != null) {
            boolean isFull = !inventory.get(1).isEmpty();
            boolean hasBait = !inventory.get(0).isEmpty();
            BlockState state = level.getBlockState(worldPosition);
            if (state.getBlock() instanceof FishTrapBlock fishTrapBlock) {
                fishTrapBlock.updateBlockState(level, worldPosition, isFull, hasBait);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        ContainerHelper.loadAllItems(compoundTag, inventory, provider);
        timer = compoundTag.getInt("Timer");
        duration = compoundTag.getInt("Duration");
        processing = compoundTag.getBoolean("Processing");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, inventory, provider);
        compoundTag.putInt("Timer", timer);
        compoundTag.putInt("Duration", duration);
        compoundTag.putBoolean("Processing", processing);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        ContainerHelper.saveAllItems(tag, inventory, provider);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack removed = ContainerHelper.removeItem(inventory, slot, amount);
        setChanged();
        updateBlockState();
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = ContainerHelper.takeItem(inventory, slot);
        setChanged();
        updateBlockState();
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
        updateBlockState();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        recipe = null;
        setChanged();
        updateBlockState();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 0 && canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 1;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == 0) {
            return level != null && getRecipe(level, stack).isPresent();
        }
        return false;
    }

    public static Optional<FishTrapRecipe> getRecipe(Level level, ItemStack itemStack) {
        return level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.FISH_TRAP_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).filter(r -> r.getBaitItem().test(itemStack)).findFirst();
    }
}
