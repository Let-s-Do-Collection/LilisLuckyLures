package net.satisfy.lilis_lucky_lures.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void fixFishingLinePosition(FishingHook fishingHook, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        Player player = fishingHook.getPlayerOwner();
        if (player != null && player.getMainHandItem().is(ObjectRegistry.BAMBOO_FISHING_ROD.get())) {
            float magnitude = -0.95f;
            float yawRad = (float) Math.toRadians(player.getYRot());
            float sin = (float) Math.sin(yawRad);
            float cos = (float) Math.cos(yawRad);
            float dx = (player.getMainArm() == HumanoidArm.RIGHT ? magnitude : -magnitude);
            float offsetX = cos * dx;
            float offsetZ = sin * dx;
            poseStack.translate(offsetX, 0.0f, offsetZ);
        }
    }
}




