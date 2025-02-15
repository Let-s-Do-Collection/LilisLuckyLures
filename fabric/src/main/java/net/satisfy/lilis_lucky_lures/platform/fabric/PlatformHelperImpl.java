package net.satisfy.lilis_lucky_lures.platform.fabric;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.nio.file.Path;

public class PlatformHelperImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> T fromJson(ResourceLocation recipeId, JsonObject json) {
        if (!"conditional".equals(recipeId.getNamespace())) {
            throw new UnsupportedOperationException(
                    "All Lili's Lucky Lures conditional recipes must use the 'conditional' namespace. Invalid recipe: " + recipeId
            );
        }
        return (T) RecipeManager.fromJson(recipeId, GsonHelper.getAsJsonObject(json, "recipe"));
    }
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
