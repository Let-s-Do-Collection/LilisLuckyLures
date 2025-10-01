package net.satisfy.lilis_lucky_lures.core.compat.jei.category;


import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

public class FishTrapCategory implements IRecipeCategory<FishTrapRecipe> {
    public static final RecipeType<FishTrapRecipe> BAITING_TYPE = RecipeType.create(LilisLuckyLures.MOD_ID, "baiting", FishTrapRecipe.class);
    public final static ResourceLocation TEXTURE = LilisLuckyLuresIdentifier.identifier("textures/gui/fish_trap.png");

    private final IDrawable background;
    private final IDrawable icon;

    public FishTrapCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ObjectRegistry.FISH_TRAP.get()));
    }

    @Override
    public @NotNull RecipeType<FishTrapRecipe> getRecipeType() {
        return FishTrapCategory.BAITING_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return ObjectRegistry.FISH_TRAP.get().getName();
    }

    @Override
    @SuppressWarnings("removal")
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FishTrapRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 35).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 35).addItemStack(recipe.getResultItem(null));
    }
}