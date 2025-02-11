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
    private float movementTime = 0.0F;

    public FishTrapBlockEntityRenderer() {
    }

    private void updateMovement(FishTrapBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null) return;

        long currentTime = System.currentTimeMillis();
        if (lastRenderTime == 0) {
            lastRenderTime = currentTime;
        }
        float deltaTime = (currentTime - lastRenderTime) / 1000.0F;
        lastRenderTime = currentTime;

        rotationAngle += deltaTime * 40.0F;
        rotationAngle %= 360;

        movementTime += deltaTime * 1.5F;
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
            updateMovement(blockEntity);

            poseStack.pushPose();

            final float angle = (float) Math.toRadians(rotationAngle);
            final float radius = 0.12f; 
            final float baseYOffset = isHanging ? 0.125f : 0.0f;
            
            float xOffset = (float) Math.sin(movementTime) * radius;
            float zOffset = (float) Math.cos(movementTime) * radius;

            float yWave = (float) Math.sin(movementTime * 1.5f) * 0.05f;
            
            float tiltAngleX = (float) Math.sin(movementTime * 1.2f) * 5.0f; 
            float tiltAngleZ = (float) Math.cos(movementTime * 1.4f) * 5.0f; 

            poseStack.translate(0.5 + xOffset, 0.1 + baseYOffset + yWave, 0.5 + zOffset);
            poseStack.mulPose(new Quaternionf().rotationY(angle)); 
            poseStack.mulPose(new Quaternionf().rotationX((float) Math.toRadians(tiltAngleX))); 
            poseStack.mulPose(new Quaternionf().rotationZ((float) Math.toRadians(tiltAngleZ))); 

            float scale = 0.85f;
            poseStack.scale(scale, scale, scale);

            Minecraft.getInstance().getItemRenderer().renderStatic(itemToRender, ItemDisplayContext.GROUND, LightTexture.FULL_SKY, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}

