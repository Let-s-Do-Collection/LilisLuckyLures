package net.satisfy.lilis_lucky_lures.core.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.satisfy.lilis_lucky_lures.core.compat.jei.category.FishTrapCategory;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import net.satisfy.lilis_lucky_lures.core.registry.RecipeTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


@JeiPlugin
public class LilisLuckyLuresJEIPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FishTrapCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<FishTrapRecipe> siloRecipes = rm.getAllRecipesFor(RecipeTypeRegistry.FISH_TRAP_RECIPE_TYPE.get());
        registration.addRecipes(FishTrapCategory.BAITING_TYPE, siloRecipes);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new LilisLuckyLuresIdentifier("jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ObjectRegistry.FISH_TRAP.get().asItem().getDefaultInstance(), FishTrapCategory.BAITING_TYPE);
    }
}
