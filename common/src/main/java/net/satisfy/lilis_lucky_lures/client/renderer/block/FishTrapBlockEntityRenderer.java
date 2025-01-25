package net.satisfy.lilis_lucky_lures.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.lilis_lucky_lures.core.block.FishTrapBlock;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import org.joml.Quaternionf;

public class FishTrapBlockEntityRenderer implements BlockEntityRenderer<FishTrapBlockEntity> {

    @Override
    public void render(FishTrapBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (blockEntity.getLevel() == null) return;

        BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        if (!(state.getBlock() instanceof FishTrapBlock)) return;

        boolean isFull = state.getValue(FishTrapBlock.FULL);
        boolean hasBait = state.getValue(FishTrapBlock.HAS_BAIT);

        ItemStack itemToRender = isFull ? blockEntity.getItem(1) : (hasBait ? blockEntity.getItem(0) : ItemStack.EMPTY);

        if (!itemToRender.isEmpty()) {
            poseStack.pushPose();

            double gameTime = blockEntity.getLevel().getGameTime() + partialTick;
            double offsetY = Math.sin(gameTime / 8.0) * 0.05; 
            double offsetX = Math.cos(gameTime / 10.0) * 0.05;
            double offsetZ = Math.sin(gameTime / 12.0) * 0.05;

            double x = 0.5 + offsetX;
            double y = 0.3 + offsetY;
            double z = 0.5 + offsetZ;

            poseStack.translate(x, y, z);
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90 + Math.sin(gameTime / 20.0) * 15)));
            float scale = 0.85f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(itemToRender, ItemDisplayContext.GROUND, LightTexture.FULL_SKY, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
