package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.satisfy.lilis_lucky_lures.core.entity.FishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.init.EntityTypeRegistry;
import net.satisfy.lilis_lucky_lures.core.init.ObjectRegistry;
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
        if (hitResult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemStack);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            AABB checkArea = new AABB(
                    hitResult.getLocation().x - 4, hitResult.getLocation().y - 4, hitResult.getLocation().z - 4,
                    hitResult.getLocation().x + 4, hitResult.getLocation().y + 4, hitResult.getLocation().z + 4
            );

            if (hitResult instanceof BlockHitResult blockHitResult && level.getFluidState(blockHitResult.getBlockPos()).isSource()) {
                if (itemStack.is(ObjectRegistry.FISH_POOL.get().asItem())) {
                    List<FishPoolEntity> nearbyPools = level.getEntitiesOfClass(FishPoolEntity.class, checkArea);
                    if (!nearbyPools.isEmpty()) return InteractionResultHolder.fail(itemStack);
                    if (!level.isClientSide) {
                        FishPoolEntity pool = EntityTypeRegistry.FISH_POOL.get().create(level);
                        if (pool != null) {
                            pool.setPos(hitResult.getLocation().x, hitResult.getLocation().y - 1.85, hitResult.getLocation().z);
                            pool.setYRot(player.getYRot());
                            if (level.noCollision(pool, pool.getBoundingBox())) {
                                level.addFreshEntity(pool);
                                level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                                if (!player.getAbilities().instabuild) itemStack.shrink(1);
                                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                            }
                        }
                    } else {
                        spawnParticles(level, hitResult);
                    }
                    return InteractionResultHolder.fail(itemStack);
                } else if (itemStack.is(ObjectRegistry.FLOATING_DEBRIS.get().asItem())) {
                    List<FloatingDebrisEntity> nearbyDebris = level.getEntitiesOfClass(FloatingDebrisEntity.class, checkArea);
                    if (!nearbyDebris.isEmpty()) return InteractionResultHolder.fail(itemStack);
                    if (!level.isClientSide) {
                        FloatingDebrisEntity debris = EntityTypeRegistry.FLOATING_DEBRIS.get().create(level);
                        if (debris != null) {
                            debris.setPos(hitResult.getLocation().x, hitResult.getLocation().y - 1.85, hitResult.getLocation().z);
                            debris.setYRot(player.getYRot());
                            if (level.noCollision(debris, debris.getBoundingBox())) {
                                level.addFreshEntity(debris);
                                level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
                                if (!player.getAbilities().instabuild) itemStack.shrink(1);
                                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
                            }
                        }
                    } else {
                        spawnParticles(level, hitResult);
                    }
                    return InteractionResultHolder.fail(itemStack);
                }
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    private void spawnParticles(Level level, HitResult hitResult) {
        for (int i = 0; i < 20; i++) {
            double xOffset = (level.random.nextDouble() - 0.5) * 2.0;
            double yOffset = (level.random.nextDouble() - 0.5) * 2.0;
            double zOffset = (level.random.nextDouble() - 0.5) * 2.0;
            level.addParticle(ParticleTypes.BUBBLE_POP, hitResult.getLocation().x + xOffset, hitResult.getLocation().y + yOffset, hitResult.getLocation().z + zOffset, 0, 0, 0);
            level.addParticle(ParticleTypes.SPLASH, hitResult.getLocation().x + xOffset, hitResult.getLocation().y + yOffset, hitResult.getLocation().z + zOffset, 0, 0, 0);
        }
    }
}

