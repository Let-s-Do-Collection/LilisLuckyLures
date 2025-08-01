package net.satisfy.lilis_lucky_lures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.satisfy.lilis_lucky_lures.client.model.entity.SpearModel;
import net.satisfy.lilis_lucky_lures.core.entity.projectile.ThrownSpearEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ThrownSpearRenderer extends EntityRenderer<ThrownSpearEntity> {
    private static final ResourceLocation TEXTURE = LilisLuckyLuresIdentifier.identifier("textures/entity/spear.png");
    private final SpearModel model;

    public ThrownSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpearModel(context.bakeLayer(SpearModel.LAYER_LOCATION));
    }

    public void render(ThrownSpearEntity thrownSpear, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, thrownSpear.yRotO, thrownSpear.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, thrownSpear.xRotO, thrownSpear.getXRot()) + 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));

        poseStack.translate(-0.7, -0.75, 0);

        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(thrownSpear)));
        this.model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(thrownSpear, f, g, poseStack, multiBufferSource, i);
    }

    public @NotNull ResourceLocation getTextureLocation(ThrownSpearEntity thrownSpear) {
        return TEXTURE;
    }
}
