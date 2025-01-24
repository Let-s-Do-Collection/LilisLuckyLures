package net.satisfy.lilis_lucky_lures.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import org.joml.Quaternionf;

import java.util.Objects;

public class FishTrapBlockEntityRenderer implements BlockEntityRenderer<FishTrapBlockEntity> {

    public FishTrapBlockEntityRenderer() {
    }

    @Override
    public void render(FishTrapBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
            ItemStack itemStack = blockEntity.getItem(i);
            if (!itemStack.isEmpty()) {
                poseStack.pushPose();

                double offset = Math.sin((Objects.requireNonNull(blockEntity.getLevel()).getGameTime() + partialTick) / 8.0) * 0.1;
                double x = 0.5;
                double y = 0.6 + offset * 0.05;
                double z = 0.5;

                if (i == 0) {
                    x = 0.3;
                    z = 0.3;
                } else if (i == 1) {
                    x = 0.7;
                    z = 0.7;
                }

                poseStack.translate(x, y, z);
                poseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(45), 0, 0));
                float scale = 0.75f;
                poseStack.scale(scale, scale, scale);

                Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);

                poseStack.popPose();
            }
        }
    }
}
