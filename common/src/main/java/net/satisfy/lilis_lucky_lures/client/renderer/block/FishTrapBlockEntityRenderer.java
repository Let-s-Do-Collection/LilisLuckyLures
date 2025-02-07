package net.satisfy.lilis_lucky_lures.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.lilis_lucky_lures.core.block.FishTrapBlock;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import org.joml.Quaternionf;

public class FishTrapBlockEntityRenderer implements BlockEntityRenderer<FishTrapBlockEntity> {
    private long lastRenderTime = 0;
    private float rotationAngle = 0.0F;

    public FishTrapBlockEntityRenderer() {
    }

    private float updateRotationAngle(FishTrapBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null) return rotationAngle;

        long currentTime = System.currentTimeMillis();
        if (lastRenderTime == 0) {
            lastRenderTime = currentTime;
        }
        float deltaTime = (currentTime - lastRenderTime) / 1000.0F;
        lastRenderTime = currentTime;

        rotationAngle += deltaTime * 40.0F;
        rotationAngle %= 360;

        return rotationAngle;
    }

    @Override
    public void render(FishTrapBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if (blockEntity.getLevel() == null) return;

        BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        if (!(state.getBlock() instanceof FishTrapBlock)) return;

        boolean isFull = state.getValue(FishTrapBlock.FULL);
        boolean hasBait = state.getValue(FishTrapBlock.HAS_BAIT);
        boolean isHanging = state.getValue(FishTrapBlock.HANGING);

        ItemStack itemToRender = isFull ? blockEntity.getItem(1) : (hasBait ? blockEntity.getItem(0) : ItemStack.EMPTY);
        if (!itemToRender.isEmpty()) {
            poseStack.pushPose();

            final var angle = updateRotationAngle(blockEntity);
            double yOffset = isHanging ? 0.125 : 0.0;

            poseStack.translate(0.5, 0.3 + yOffset, 0.5);
            poseStack.mulPose(new Quaternionf().rotationY((float) Math.toRadians(angle)));
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(90)));

            float scale = 0.85f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(itemToRender, ItemDisplayContext.GROUND, LightTexture.FULL_SKY, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
