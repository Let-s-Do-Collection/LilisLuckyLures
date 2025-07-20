package net.satisfy.lilis_lucky_lures.platform.neoforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import net.neoforged.neoforge.common.crafting.ConditionalRecipeOutput;
import org.jetbrains.annotations.Nullable;

public class PlatformHelperImpl {

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> T fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonObject recipe = GsonHelper.getAsJsonObject(json, "recipe");
        JsonArray conditions = GsonHelper.getAsJsonArray(json, "conditions");
        JsonObject forgeRecipe = new JsonObject();
        forgeRecipe.addProperty("type", "forge:conditional");
        JsonArray recipes = new JsonArray();
        JsonObject newRecipe = new JsonObject();
        newRecipe.add("conditions", conditions);
        newRecipe.add("recipe", recipe);
        recipes.add(newRecipe);
        forgeRecipe.add("recipes", recipes);
        return (T) ConditionalRecipeOutput.SERIALZIER.fromJson(recipeId, forgeRecipe);
    }

    public static boolean isModLoaded(String modid) {
        ModList modList = ModList.get();
        if(modList != null){
            return modList.isLoaded(modid);
        }
        return isModPreLoaded(modid);
    }

    public static boolean isModPreLoaded(String modid) {
        return getPreLoadedModInfo(modid) != null;
    }

    public static @Nullable ModInfo getPreLoadedModInfo(String modId){
        for(ModInfo info : LoadingModList.get().getMods()){
            if(info.getModId().equals(modId)) {
                return info;
            }
        }
        return null;
    }
}
