package net.satisfy.lilis_lucky_lures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.lilis_lucky_lures.client.model.entity.FloatingDebrisModel;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class FloatingDebrisRenderer extends EntityRenderer<FloatingDebrisEntity> {
    private static final ResourceLocation TEXTURE = new LilisLuckyLuresIdentifier("textures/entity/floating_debris.png");
    private final FloatingDebrisModel<FloatingDebrisEntity> model;

    public FloatingDebrisRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FloatingDebrisModel<>(context.bakeLayer(FloatingDebrisModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(FloatingDebrisEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FloatingDebrisEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        Quaternionf rotation = new Quaternionf().rotateY((float) Math.toRadians(entity.getRandomRotation()));
        poseStack.last().pose().rotate(rotation);
        float ageInTicks = entity.tickCount + partialTicks;
        model.setupAnim(entity, 0.0f, 0.0f, ageInTicks, entityYaw, 0.0f);
        int overlay = entity.getHurtTime() > 0 ? OverlayTexture.RED_OVERLAY_V : OverlayTexture.NO_OVERLAY;
        model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), packedLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
