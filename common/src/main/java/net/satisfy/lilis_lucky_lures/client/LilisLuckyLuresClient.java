package net.satisfy.lilis_lucky_lures.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.lilis_lucky_lures.client.model.armor.AnglersHatModel;
import net.satisfy.lilis_lucky_lures.client.model.entity.*;
import net.satisfy.lilis_lucky_lures.client.renderer.block.CompletionistBannerRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.block.FishTrapBlockEntityRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.block.FishTrophyFrameRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.block.HangingFrameRenderer;
import net.satisfy.lilis_lucky_lures.client.renderer.entity.*;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;


@Environment(EnvType.CLIENT)
public class LilisLuckyLuresClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(),
                ObjectRegistry.FISH_TRAP.get(), ObjectRegistry.HANGING_FRAME.get(), ObjectRegistry.ELASTIC_FISHING_NET.get(), ObjectRegistry.FISH_NET_FENCE.get()
        );

        BlockEntityRendererRegistry.register(EntityTypeRegistry.FISH_TRAP.get(), context -> new FishTrapBlockEntityRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.HANGING_FRAME.get(), context -> new HangingFrameRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.FISH_TROPHY_FRAME.get(), context -> new FishTrophyFrameRenderer());
        BlockEntityRendererRegistry.register(EntityTypeRegistry.LILIS_LUCKY_LURES_BANNER.get(), CompletionistBannerRenderer::new);
    }

    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayer();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.FLOATING_DEBRIS, FloatingDebrisRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.FLOATING_BOOKS, FloatingBooksRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.RIVER_FISH_POOL, RiverFishPoolRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.OCEAN_FISH_POOL, OceanFishPoolRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.DYNAMITE, RotatingThrownItemRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.THROWN_SPEAR, ThrownSpearRenderer::new);
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(FloatingDebrisModel.LAYER_LOCATION, FloatingDebrisModel::getTexturedModelData);
        EntityModelLayerRegistry.register(FloatingBooksModel.LAYER_LOCATION, FloatingBooksModel::getTexturedModelData);
        EntityModelLayerRegistry.register(RiverFishPoolModel.LAYER_LOCATION, RiverFishPoolModel::getTexturedModelData);
        EntityModelLayerRegistry.register(OceanFishPoolModel.LAYER_LOCATION, OceanFishPoolModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SpearModel.LAYER_LOCATION, SpearModel::getTexturedModelData);
        EntityModelLayerRegistry.register(CompletionistBannerRenderer.LAYER_LOCATION, CompletionistBannerRenderer::createBodyLayer);
        EntityModelLayerRegistry.register(AnglersHatModel.LAYER_LOCATION, AnglersHatModel::createBodyLayer);
    }
}
