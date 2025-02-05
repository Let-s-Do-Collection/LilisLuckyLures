package net.satisfy.lilis_lucky_lures.platform;

import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.nio.file.Path;

public class PlatformHelper {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Recipe<?>> T fromJson(ResourceLocation recipeId, JsonObject json) {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static boolean isModLoaded(String modid){
        throw new AssertionError();
    }
}
