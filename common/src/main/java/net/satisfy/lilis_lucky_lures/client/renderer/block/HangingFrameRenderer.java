package net.satisfy.lilis_lucky_lures.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.satisfy.lilis_lucky_lures.core.block.HangingFrameBlock;
import net.satisfy.lilis_lucky_lures.core.block.entity.HangingFrameBlockEntity;
import org.joml.Matrix4f;

public class HangingFrameRenderer implements BlockEntityRenderer<HangingFrameBlockEntity> {
    @Override
    public void render(HangingFrameBlockEntity entity, float f, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Direction dir = entity.getBlockState().getValue(HangingFrameBlock.FACING);
        Level level = entity.getLevel();
        if (level == null) return;

        NonNullList<ItemStack> inventory = entity.getInventory();
        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.get(slot);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(new Matrix4f().rotateY((float) Math.toRadians(-dir.toYRot())));
                poseStack.mulPose(new Matrix4f().rotateZ((float) Math.toRadians(45)));

                switch (slot) {
                    case 0 -> poseStack.translate(0.21, -0.1, 0);
                    case 1 -> poseStack.translate(0.0, -0.041, 0);
                    case 2 -> poseStack.translate(-0.11, 0.2, 0);
                }

                poseStack.scale(0.35f, 0.35f, 0.35f);
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, getLightLevel(level, entity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, buffer, level, 1);
                poseStack.popPose();
            }
        }
    }

    public static int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getBrightness(LightLayer.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
