package net.satisfy.lilis_lucky_lures.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.recipe.FishTrapRecipe;
import net.satisfy.lilis_lucky_lures.core.recipe.SimpleConditionalRecipe;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

import java.util.function.Supplier;

public class RecipeTypeRegistry {
    private static final Registrar<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.RECIPE_TYPE).getRegistrar();
    private static final Registrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.RECIPE_SERIALIZER).getRegistrar();

    public static final RegistrySupplier<RecipeType<FishTrapRecipe>> FISH_TRAP_RECIPE_TYPE = create("fish_trap");
    public static final RegistrySupplier<RecipeSerializer<FishTrapRecipe>> FISH_TRAP_RECIPE_SERIALIZER = create("fish_trap", FishTrapRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<Recipe<?>>> CONDITIONAL_RECIPE_SERIALIZER = create("conditional", SimpleConditionalRecipe.Serializer::new);

    private static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> create(String name, Supplier<RecipeSerializer<T>> serializer) {
        return RECIPE_SERIALIZERS.register(new LilisLuckyLuresIdentifier(name), serializer);
    }

    private static <T extends Recipe<?>> RegistrySupplier<RecipeType<T>> create(String name) {
        Supplier<RecipeType<T>> type = () -> new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        return RECIPE_TYPES.register(new LilisLuckyLuresIdentifier(name), type);
    }

    public static void init() {
    }
}