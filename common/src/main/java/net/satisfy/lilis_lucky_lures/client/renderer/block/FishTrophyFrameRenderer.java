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
    public void render(FishTrophyFrameBlockEntity be, float partialTick, PoseStack ps, MultiBufferSource buf, int light, int overlay) {
        if (!be.getBlockState().getValue(FishTrophyFrameBlock.HAS_ITEM)) return;
        ItemStack stack = be.getDisplayedItem();
        if (stack.isEmpty()) return;

        ps.pushPose();
        ps.translate(0.5, 0.5, 0.5);

        Direction f = be.getBlockState().getValue(FishTrophyFrameBlock.FACING);
        switch (f) {
            case NORTH -> ps.mulPose(new Quaternionf().rotateY(0));
            case SOUTH -> ps.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
            case WEST -> ps.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
            case EAST -> ps.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));
        }

        ps.translate(0, 0, 0.425);

        if (stack.is(ItemTags.FISHES)) {
            ps.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(-45)));
            ps.translate(-0.05, -0.05, 0);
        }

        ps.scale(0.8f, 0.8f, 0.8f);
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ps, buf, be.getLevel(), 0);
        ps.popPose();
    }
}
