package net.satisfy.lilis_lucky_lures.core.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.satisfy.lilis_lucky_lures.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

public class SimpleConditionalRecipe {
    public static class Serializer<T extends Recipe<?>> implements RecipeSerializer<T> {

        @Override
        public @NotNull T fromJson(ResourceLocation recipeId, JsonObject json) {
            return PlatformHelper.fromJson(recipeId, json);
        }

        @Override
        public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return null;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        }
    }


    public static boolean checkCondition(JsonObject c) {
        String type = GsonHelper.getAsString(c, "type");

        if (type.equals("neoforge:mod_loaded")) {
            String modId = c.get("modid").getAsString();
            return PlatformHelper.isModLoaded(modId);
        }
        return false;
    }
}