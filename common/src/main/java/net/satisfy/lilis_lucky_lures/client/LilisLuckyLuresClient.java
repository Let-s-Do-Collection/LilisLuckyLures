package net.satisfy.lilis_lucky_lures.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.lilis_lucky_lures.client.model.entity.FloatingDebrisModel;
import net.satisfy.lilis_lucky_lures.client.renderer.block.FishTrapBlockEntityRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.entity.FloatingDebrisRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.entity.RotatingThrownItemRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.entity.SpearEntityRenderer;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.ObjectRegistry;


@Environment(EnvType.CLIENT)
public class LilisLuckyLuresClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(),
                ObjectRegistry.FISH_TRAP.get()
        );
        BlockEntityRendererRegistry.register(EntityTypeRegistry.FISH_TRAP.get(), context -> new FishTrapBlockEntityRenderer());
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayer();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.FLOATING_DEBRIS, FloatingDebrisRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DYNAMITE, RotatingThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.SPEAR, SpearEntityRenderer::new);
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(FloatingDebrisModel.LAYER_LOCATION, FloatingDebrisModel::getTexturedModelData);
    }
}
