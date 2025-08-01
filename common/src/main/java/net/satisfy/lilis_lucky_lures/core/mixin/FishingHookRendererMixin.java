package net.satisfy.lilis_lucky_lures.core.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public class FishingHookRendererMixin {

    @Inject(method = "render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void fixFishingLinePosition(FishingHook fishingHook, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        Player player = fishingHook.getPlayerOwner();
        if (player != null && player.getMainHandItem().is(ObjectRegistry.BAMBOO_FISHING_ROD.get())) {
            float magnitude = -0.95f;
            float yawRad = Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                    ? (float) Math.toRadians(player.getYRot())
                    : (float) Math.toRadians(((LivingEntityAccessor) player).getYBodyRot());
            float sin = (float) Math.sin(yawRad);
            float cos = (float) Math.cos(yawRad);
            float dx = player.getMainArm() == HumanoidArm.RIGHT ? magnitude : -magnitude;
            Vector3f offset = new Vector3f(cos * dx, 0.0f, sin * dx);
            poseStack.translate(offset.x(), offset.y(), offset.z());
        }
    }
}
