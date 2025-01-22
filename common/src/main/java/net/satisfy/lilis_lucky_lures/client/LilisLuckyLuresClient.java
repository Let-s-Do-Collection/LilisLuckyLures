package net.satisfy.lilis_lucky_lures.client;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.satisfy.lilis_lucky_lures.client.renderer.RotatingThrownItemRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.SpearEntityRenderer;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;


@Environment(EnvType.CLIENT)
public class LilisLuckyLuresClient {

    public static void onInitializeClient() {
    }


    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayer();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.DYNAMITE, RotatingThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.SPEAR, SpearEntityRenderer::new);
    }


    public static void registerEntityModelLayer() {

    }
}
