package net.satisfy.lilis_lucky_lures.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.satisfy.lilis_lucky_lures.core.block.FishTrophyFrameBlock;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrophyFrameBlockEntity;
import org.joml.Quaternionf;

public class FishTrophyFrameRenderer implements BlockEntityRenderer<FishTrophyFrameBlockEntity> {
    @Override
    public void render(FishTrophyFrameBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ItemStack itemStack = blockEntity.getDisplayedItem();
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            poseStack.translate(0.5, 0.5, 0.5);

            Direction facing = blockEntity.getBlockState().getValue(FishTrophyFrameBlock.FACING);
            switch (facing) {
                case NORTH -> poseStack.mulPose(new Quaternionf().rotateY(0));
                case SOUTH -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                case WEST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
                case EAST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));
            }

            poseStack.translate(0, 0, 0.425);

            if (itemStack.is(ItemTags.FISHES)) {
                poseStack.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(-45)));
                poseStack.translate(-0.05, -0.05, 0);
            }

            poseStack.scale(0.8f, 0.8f, 0.8f);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }

}
