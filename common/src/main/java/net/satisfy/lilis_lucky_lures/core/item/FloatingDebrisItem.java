package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FloatingDebrisItem extends Item {

    public FloatingDebrisItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemStack);
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            List<Entity> nearbyEntities = level.getEntities(player, player.getBoundingBox().expandTowards(player.getViewVector(1.0F).scale(5.0D)).inflate(1.0D), EntitySelector.NO_SPECTATORS.and(Entity::isPickable));
            for (Entity entity : nearbyEntities) {
                if (entity.getBoundingBox().inflate(entity.getPickRadius()).contains(player.getEyePosition())) {
                    return InteractionResultHolder.pass(itemStack);
                }
            }

            if (!level.isClientSide) {
                FloatingDebrisEntity debris = EntityTypeRegistry.FLOATING_DEBRIS.get().create(level);
                if (debris != null) {
                    debris.setPos(hitResult.getLocation().x, hitResult.getLocation().y - 1.85, hitResult.getLocation().z);
                    debris.setYRot(player.getYRot());

                    if (level.noCollision(debris, debris.getBoundingBox())) {
                        level.addFreshEntity(debris);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }
                        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                    }
                }
            }
            return InteractionResultHolder.fail(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }

}
