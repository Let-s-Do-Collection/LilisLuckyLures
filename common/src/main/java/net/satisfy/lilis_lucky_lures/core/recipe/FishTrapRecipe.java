package net.satisfy.lilis_lucky_lures.core.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.lilis_lucky_lures.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("unused")
public class FishTrapRecipe implements Recipe<Container> {
    final ResourceLocation id;
    private final Ingredient baitItem;
    private final ItemStack catchItem;
    private final int catchCount;
    private final int minDuration;
    private final int maxDuration;
    private final Random random = new Random();

    public FishTrapRecipe(ResourceLocation id, Ingredient baitItem, ItemStack catchItem, int catchCount, int minDuration, int maxDuration) {
        this.id = id;
        this.baitItem = baitItem;
        this.catchItem = catchItem;
        this.catchCount = catchCount;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    public Ingredient getBaitItem() {
        return this.baitItem;
    }

    public ItemStack getCatchItem() {
        return this.catchItem.copy();
    }

    public int getCatchCount() {
        return this.catchCount;
    }

    public int getRandomDuration() {
        return minDuration + random.nextInt(maxDuration - minDuration + 1);
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNullList = NonNullList.create();
        nonNullList.add(this.baitItem);
        return nonNullList;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        return baitItem.test(inventory.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return catchItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return catchItem.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.FISH_TRAP_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.FISH_TRAP_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<FishTrapRecipe> {

        @Override
        public @NotNull FishTrapRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient baitItem = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "bait_item"));
            JsonObject result = GsonHelper.getAsJsonObject(json, "catch").getAsJsonObject("result");
            ItemStack catchItem = ShapedRecipe.itemStackFromJson(result);
            int catchCount = GsonHelper.getAsInt(result, "count", 1);
            JsonObject duration = GsonHelper.getAsJsonObject(json, "catch_duration");
            int minDuration = GsonHelper.getAsInt(duration, "min");
            int maxDuration = GsonHelper.getAsInt(duration, "max");
            return new FishTrapRecipe(id, baitItem, catchItem, catchCount, minDuration, maxDuration);
        }

        @Override
        public @NotNull FishTrapRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient baitItem = Ingredient.fromNetwork(buf);
            ItemStack catchItem = buf.readItem();
            int catchCount = buf.readInt();
            int minDuration = buf.readInt();
            int maxDuration = buf.readInt();
            return new FishTrapRecipe(id, baitItem, catchItem, catchCount, minDuration, maxDuration);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, FishTrapRecipe recipe) {
            recipe.baitItem.toNetwork(buf);
            buf.writeItem(recipe.catchItem);
            buf.writeInt(recipe.catchCount);
            buf.writeInt(recipe.minDuration);
            buf.writeInt(recipe.maxDuration);
        }
    }
}
