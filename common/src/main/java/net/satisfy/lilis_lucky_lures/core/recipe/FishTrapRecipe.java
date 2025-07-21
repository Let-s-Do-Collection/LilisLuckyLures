package net.satisfy.lilis_lucky_lures.core.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.satisfy.lilis_lucky_lures.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("unused")
public class FishTrapRecipe implements Recipe<RecipeInput> {
    private final Ingredient baitItem;
    private final ItemStack catchItem;
    private final int catchCount;
    private final int minDuration;
    private final int maxDuration;
    private final Random random = new Random();

    public FishTrapRecipe(Ingredient baitItem, ItemStack catchItem, int catchCount, int minDuration, int maxDuration) {
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
    public boolean matches(RecipeInput recipeInput, Level level) {
        return baitItem.test(recipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return catchItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return catchItem.copy();
    }

    public @NotNull ResourceLocation getId() {
        return RecipeTypeRegistry.FISH_TRAP_RECIPE_TYPE.getId();
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

        public static final StreamCodec<RegistryFriendlyByteBuf, FishTrapRecipe> STREAM_CODEC =
                StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static final MapCodec<FishTrapRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(Ingredient.CODEC.fieldOf("bait_item").forGetter(FishTrapRecipe::getBaitItem),
                    ItemStack.STRICT_CODEC.fieldOf("catch_result").forGetter(FishTrapRecipe::getCatchItem),
                    Codec.INT.fieldOf("catch_count").forGetter(FishTrapRecipe::getCatchCount),
                    Codec.INT.fieldOf("catch_duration_min").forGetter(FishTrapRecipe::getMinDuration),
                    Codec.INT.fieldOf("catch_duration_max").forGetter(FishTrapRecipe::getMinDuration))
                    .apply(instance, FishTrapRecipe::new);
        });

        public static @NotNull FishTrapRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            Ingredient baitItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            ItemStack catchItem = ItemStack.STREAM_CODEC.decode(buf);
            int catchCount = buf.readInt();
            int minDuration = buf.readInt();
            int maxDuration = buf.readInt();
            return new FishTrapRecipe(baitItem, catchItem, catchCount, minDuration, maxDuration);
        }

        public static void toNetwork(RegistryFriendlyByteBuf buf, FishTrapRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.baitItem);
            ItemStack.STREAM_CODEC.encode(buf, recipe.catchItem);
            buf.writeInt(recipe.catchCount);
            buf.writeInt(recipe.minDuration);
            buf.writeInt(recipe.maxDuration);
        }

        @Override
        public MapCodec<FishTrapRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FishTrapRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
