package net.satisfy.lilis_lucky_lures.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.CREATIVE_MODE_TAB);

    @SuppressWarnings("unused")
    public static final RegistrySupplier<CreativeModeTab> LILIS_LUCKY_LURES_TAB = CREATIVE_MODE_TABS.register("lilis_lucky_lures", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.RIVER_FISH_POOL.get()))
            .title(Component.translatable("creativetab.lilis_lucky_lures.tab").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC))))
            .displayItems((parameters, output) -> {
                output.accept(ObjectRegistry.REDSTONE_COIL.get());
                output.accept(ObjectRegistry.FISH_BAG.get());
                output.accept(ObjectRegistry.DYNAMITE.get());
                output.accept(ObjectRegistry.SPEAR.get());
                output.accept(ObjectRegistry.FISHING_NET.get());
                output.accept(ObjectRegistry.BAMBOO_FISHING_ROD.get());
                output.accept(ObjectRegistry.FLOATING_DEBRIS.get());
                output.accept(ObjectRegistry.FLOATING_BOOKS.get());
                output.accept(ObjectRegistry.RIVER_FISH_POOL.get());
                output.accept(ObjectRegistry.OCEAN_FISH_POOL.get());
                output.accept(ObjectRegistry.FISH_TRAP.get());
                output.accept(ObjectRegistry.HANGING_FRAME.get());
                output.accept(ObjectRegistry.FISH_TROPHY_FRAME.get());
                output.accept(ObjectRegistry.FISH_NET.get());
                output.accept(ObjectRegistry.SOAKED_BAG.get());
                output.accept(ObjectRegistry.ANGLERS_HAT.get());
                output.accept(ObjectRegistry.COOKED_COD_MEAL.get());
                output.accept(ObjectRegistry.SALMON_ROLLS.get());
                output.accept(ObjectRegistry.GRILLED_TROPICAL_FISH.get());
                output.accept(ObjectRegistry.PUFFER_PLATER.get());
                output.accept(ObjectRegistry.LILIS_LUCKY_LURES_BANNER.get());
            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
