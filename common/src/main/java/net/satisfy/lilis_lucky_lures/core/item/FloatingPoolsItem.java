package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FloatingPoolsItem extends Item {

    public FloatingPoolsItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemStack);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            AABB checkArea = new AABB(hitResult.getLocation().x - 4, hitResult.getLocation().y - 4, hitResult.getLocation().z - 4, hitResult.getLocation().x + 4, hitResult.getLocation().y + 4, hitResult.getLocation().z + 4);
            List<FloatingDebrisEntity> nearbyDebris = level.getEntitiesOfClass(FloatingDebrisEntity.class, checkArea);
            if (!nearbyDebris.isEmpty()) return InteractionResultHolder.fail(itemStack);

            if (!level.isClientSide) {
                FloatingDebrisEntity debris;

                if (itemStack.is(ObjectRegistry.FLOATING_BOOKS.get())) {
                    debris = EntityTypeRegistry.FLOATING_BOOKS.get().create(level);
                } else if (itemStack.is(ObjectRegistry.RIVER_FISH_POOL.get())) {
                    debris = EntityTypeRegistry.RIVER_FISH_POOL.get().create(level);
                } else if (itemStack.is(ObjectRegistry.OCEAN_FISH_POOL.get())) {
                    debris = EntityTypeRegistry.OCEAN_FISH_POOL.get().create(level);
                } else {
                    debris = EntityTypeRegistry.FLOATING_DEBRIS.get().create(level);
                }

                if (debris != null) {
                    debris.setPos(hitResult.getLocation().x, hitResult.getLocation().y - 1.85, hitResult.getLocation().z);
                    debris.setYRot(player.getYRot());

                    if (level.noCollision(debris, debris.getBoundingBox())) {
                        level.addFreshEntity(debris);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                        if (!player.getAbilities().instabuild) itemStack.shrink(1);

                        showParticles(level, hitResult);

                        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                    }
                }
            }
            return InteractionResultHolder.fail(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    private void showParticles(Level level, HitResult hitResult) {
        if (level.isClientSide) {
            for (int i = 0; i < 20; i++) {
                double xOffset = (level.random.nextDouble() - 0.5) * 2.0;
                double yOffset = (level.random.nextDouble() - 0.5) * 2.0;
                double zOffset = (level.random.nextDouble() - 0.5) * 2.0;
                level.addParticle(ParticleTypes.BUBBLE_POP, hitResult.getLocation().x + xOffset, hitResult.getLocation().y + yOffset, hitResult.getLocation().z + zOffset, 0, 0, 0);
                level.addParticle(ParticleTypes.SPLASH, hitResult.getLocation().x + xOffset, hitResult.getLocation().y + yOffset, hitResult.getLocation().z + zOffset, 0, 0, 0);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.not_obtainable").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD27D46)).withItalic(true)));
    }
}

