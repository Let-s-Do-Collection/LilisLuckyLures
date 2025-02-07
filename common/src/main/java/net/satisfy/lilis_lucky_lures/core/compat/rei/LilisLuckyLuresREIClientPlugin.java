package net.satisfy.lilis_lucky_lures.core.compat.rei;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.satisfy.lilis_lucky_lures.core.compat.rei.fish_trap.FishTrapCategory;
import net.satisfy.lilis_lucky_lures.core.compat.rei.fish_trap.FishTrapDisplay;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;

import java.util.ArrayList;
import java.util.List;

public class LilisLuckyLuresREIClientPlugin {
    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new FishTrapCategory());
        registry.addWorkstations(FishTrapDisplay.FISH_TRAP_DISPLAY, EntryStacks.of(ObjectRegistry.FISH_TRAP.get()));
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(FishTrapRecipe.class, FishTrapDisplay::new);

    }
}
