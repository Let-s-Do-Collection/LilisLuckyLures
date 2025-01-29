package net.satisfy.lilis_lucky_lures.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.lilis_lucky_lures.client.model.entity.FloatingBooksModel;
import net.satisfy.lilis_lucky_lures.client.model.entity.FloatingDebrisModel;
import net.satisfy.lilis_lucky_lures.client.model.entity.RiverFishPoolModel;
import net.satisfy.lilis_lucky_lures.client.model.entity.SpearModel;
import net.satisfy.lilis_lucky_lures.client.renderer.block.FishTrapBlockEntityRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.entity.*;
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
        EntityRendererRegistry.register(EntityTypeRegistry.FLOATING_BOOKS, FloatingBooksRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.RIVER_FISH_POOL, RiverFishPoolRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DYNAMITE, RotatingThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.THROWN_SPEAR, ThrownSpearRenderer::new);

    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(FloatingDebrisModel.LAYER_LOCATION, FloatingDebrisModel::getTexturedModelData);
        EntityModelLayerRegistry.register(FloatingBooksModel.LAYER_LOCATION, FloatingBooksModel::getTexturedModelData);
        EntityModelLayerRegistry.register(RiverFishPoolModel.LAYER_LOCATION, RiverFishPoolModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SpearModel.LAYER_LOCATION, SpearModel::getTexturedModelData);

    }
}
