package net.satisfy.lilis_lucky_lures.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.CREATIVE_MODE_TAB);

    @SuppressWarnings("unused")
    public static final RegistrySupplier<CreativeModeTab> LILIS_LUCKY_LURES_TAB = CREATIVE_MODE_TABS.register("lilis_lucky_lures", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.BAMBOO_FISHING_ROD.get()))
            .title(Component.translatable("creativetab.lilis_lucky_lures.tab"))
            .displayItems((parameters, output) -> {
                output.accept(ObjectRegistry.DYNAMITE.get());
                output.accept(ObjectRegistry.SPEAR.get());
                output.accept(ObjectRegistry.FISHING_NET.get());
                output.accept(ObjectRegistry.BAMBOO_FISHING_ROD.get());
            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
