package net.satisfy.lilis_lucky_lures.fabric.core.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {

    @Inject(method = "shouldStopFishing", at = @At("HEAD"), cancellable = true)
    private void injectedShouldStopFishing(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack mainHandStack = player.getMainHandItem();
        ItemStack offHandStack = player.getOffhandItem();
        boolean isUsingMainHand = mainHandStack.is(ObjectRegistry.BAMBOO_FISHING_ROD.get());
        boolean isUsingOffHand = offHandStack.is(ObjectRegistry.BAMBOO_FISHING_ROD.get());

        if (isUsingMainHand || isUsingOffHand) {
            FishingHook fishingHook = (FishingHook) (Object) this;
            if (!player.isRemoved() && player.isAlive() && fishingHook.distanceToSqr(player) <= 1024.0) {
                cir.setReturnValue(false);
            } else {
                fishingHook.discard();
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "retrieve", at = @At("TAIL"))
    private void onRetrieve(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        FishingHook fishingHook = (FishingHook) (Object) this;
        if (!fishingHook.level().isClientSide) {
            Player owner = fishingHook.getPlayerOwner();
            if (owner != null) {
                int returnValue = cir.getReturnValue();
                if (returnValue > 0) {
                    Vec3 hookPosition = fishingHook.position();
                    AABB hookBoundingBox = new AABB(
                            hookPosition.x - 1.0, hookPosition.y - 1.0, hookPosition.z - 1.0,
                            hookPosition.x + 1.0, hookPosition.y + 1.0, hookPosition.z + 1.0
                    );
                    List<FloatingDebrisEntity> debrisEntities = fishingHook.level().getEntitiesOfClass(FloatingDebrisEntity.class, hookBoundingBox);
                    if (!debrisEntities.isEmpty()) {
                        FloatingDebrisEntity debrisEntity = debrisEntities.get(0);
                        debrisEntity.onFishHookInteract(owner);
                    }
                }
            }
        }
    }
}
