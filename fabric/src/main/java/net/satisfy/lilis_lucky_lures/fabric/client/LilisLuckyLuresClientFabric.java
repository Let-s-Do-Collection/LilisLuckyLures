package net.satisfy.lilis_lucky_lures.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.satisfy.lilis_lucky_lures.client.LilisLuckyLuresClient;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import net.satisfy.lilis_lucky_lures.fabric.client.renderer.HatArmorRenderer;

public class LilisLuckyLuresClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LilisLuckyLuresClient.preInitClient();
        LilisLuckyLuresClient.onInitializeClient();
        registerItemProperties();

        ArmorRenderer.register(new HatArmorRenderer(), ObjectRegistry.ANGLERS_HAT.get());
    }

    private static void registerItemProperties() {
        ItemProperties.register(ObjectRegistry.BAMBOO_FISHING_ROD.get(), new ResourceLocation("cast"),
                (itemStack, clientWorld, livingEntity, seed) -> {
                    if (livingEntity instanceof Player player) {
                        return (player.getMainHandItem() == itemStack || player.getOffhandItem() == itemStack)
                                && player.fishing != null ? 1.0F : 0.0F;
                    }
                    return 0.0F;
                });
    }
}
