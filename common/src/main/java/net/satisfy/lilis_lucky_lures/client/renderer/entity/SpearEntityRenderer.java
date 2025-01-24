package net.satisfy.lilis_lucky_lures.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import net.minecraft.world.item.ItemDisplayContext;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.entity.SpearEntity;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpearEntityRenderer extends EntityRenderer<SpearEntity> {
    private final ItemRenderer itemRenderer;
    private static final ResourceLocation TRANSPARENT = new ResourceLocation(LilisLuckyLures.MOD_ID, "textures/entity/transparent.png");

    public SpearEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SpearEntity spear, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();

        float yRot = Mth.lerp(partialTicks, spear.yRotO, spear.getYRot()) - 90.0F;
        float xRot = Mth.lerp(partialTicks, spear.xRotO, spear.getXRot());

        float yRotRad = (float) Math.toRadians(yRot);
        float xRotRad = (float) Math.toRadians(xRot);

        Quaternionf rotationY = new Quaternionf().rotateY(yRotRad);
        Quaternionf rotationZ = new Quaternionf().rotateZ(xRotRad);

        matrixStack.mulPose(rotationY);
        matrixStack.mulPose(rotationZ);

        ItemStack itemStack = spear.getItemStack();
        this.itemRenderer.render(itemStack, ItemDisplayContext.GROUND, false, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                this.itemRenderer.getModel(itemStack, spear.getCommandSenderWorld(), null, 0)
        );

        matrixStack.popPose();
        super.render(spear, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SpearEntity entity) {
        return TRANSPARENT;
    }
}
