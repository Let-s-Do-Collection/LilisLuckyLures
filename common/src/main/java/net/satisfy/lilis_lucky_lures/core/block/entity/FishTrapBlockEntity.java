package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.NonNullList;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.RecipeTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.Block.popResource;

public class FishTrapBlockEntity extends BlockEntity implements Container, Clearable {
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
                SimpleContainer container = new SimpleContainer(1);
                container.setItem(0, inputItem);
                RecipeManager recipeManager = level.getRecipeManager();
                Recipe<?> currentRecipe = recipeManager.getRecipeFor(RecipeTypeRegistry.FISH_TRAP_RECIPE_TYPE.get(), container, level).orElse(null);
                if (currentRecipe instanceof FishTrapRecipe fishTrapRecipe) {
                    recipe = fishTrapRecipe;
                }
            }
            if (recipe == null) {
                return;
            }
            if (processing) {
                if (++timer >= duration) {
                    processing = false;
                    timer = 0;
                    inputItem.shrink(1);
                    if (inputItem.isEmpty()) {
                        inventory.set(0, ItemStack.EMPTY);
                    }
                    addCatchToOutput();
                    recipe = null;
                }
            } else if (!inputItem.isEmpty() && recipe.getBaitItem().test(inputItem)) {
                processing = true;
                duration = recipe.getRandomDuration();
            }
        }
    }

    private void addCatchToOutput() {
        ItemStack output = recipe.getCatchItem().copy();
        output.setCount(recipe.getCatchCount());
        ItemStack existingOutput = inventory.get(1);
        if (existingOutput.isEmpty()) {
            inventory.set(1, output);
        } else if (ItemStack.isSameItemSameTags(existingOutput, output)) {
            existingOutput.grow(output.getCount());
            inventory.set(1, existingOutput);
        } else {
            assert level != null;
            popResource(level, worldPosition, output);
        }
        markUpdated();
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
        markUpdated();
        return removed;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = ContainerHelper.takeItem(inventory, slot);
        markUpdated();
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (slot == 0) {
            recipe = null;
        }
        markUpdated();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        inventory.clear();
        recipe = null;
        markUpdated();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, inventory);
        timer = tag.getInt("Timer");
        duration = tag.getInt("Duration");
        processing = tag.getBoolean("Processing");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, inventory);
        tag.putInt("Timer", timer);
        tag.putInt("Duration", duration);
        tag.putBoolean("Processing", processing);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, inventory);
        compoundTag.putInt("Timer", timer);
        compoundTag.putInt("Duration", duration);
        compoundTag.putBoolean("Processing", processing);
        return compoundTag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean insertInput(ItemStack stack) {
        if (inventory.get(0).isEmpty()) {
            inventory.set(0, stack.copy());
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
            markUpdated();
            return true;
        }
        return false;
    }

    public ItemStack extractOutput() {
        ItemStack output = inventory.get(1).copy();
        if (!output.isEmpty()) {
            inventory.set(1, ItemStack.EMPTY);
            markUpdated();
        }
        return output;
    }
}
