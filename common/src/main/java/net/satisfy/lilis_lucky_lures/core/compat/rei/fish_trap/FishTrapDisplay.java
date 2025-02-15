package net.satisfy.lilis_lucky_lures.core.compat.rei.fish_trap;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;

import java.util.Collections;
import java.util.stream.Collectors;

public class FishTrapDisplay extends BasicDisplay {
    public static final CategoryIdentifier<FishTrapDisplay> FISH_TRAP_DISPLAY = CategoryIdentifier.of(LilisLuckyLures.MOD_ID, "fish_trap_display");

    public FishTrapDisplay(FishTrapRecipe recipe) {
        super(
                recipe.getIngredients().stream()
                        .map(EntryIngredients::ofIngredient)
                        .collect(Collectors.toList()),
                Collections.singletonList(EntryIngredients.of(recipe.getResultItem(null)))
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FishTrapCategory.FISH_TRAP_DISPLAY;
    }
}


