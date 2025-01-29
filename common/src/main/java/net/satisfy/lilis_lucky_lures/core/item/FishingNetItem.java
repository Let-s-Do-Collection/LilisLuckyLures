package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Comparator;

public class FishingNetItem extends Item {
    private static final float SUCCESS_CHANCE = 0.8F;
    private static final int USE_DURATION = 100;
    private static final int COOLDOWN_TICKS = 40;

    public FishingNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide && entity instanceof Player player) {
            FloatingDebrisEntity targetDebris = getTargetDebris(level, player);
            if (targetDebris != null) {
                ServerLevel serverLevel = (ServerLevel) level;
                spawnParticles(serverLevel, targetDebris.getX(), targetDebris.getY() + 0.5, targetDebris.getZ());
            }
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            FloatingDebrisEntity targetDebris = getTargetDebris(level, player);
            if (targetDebris != null) {
                if (level.getRandom().nextFloat() <= SUCCESS_CHANCE) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    LootTable lootTable = serverLevel.getServer().getLootData()
                            .getLootTable(new LilisLuckyLuresIdentifier("gameplay/fishing_net"));

                    LootParams lootParams = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.THIS_ENTITY, player)
                            .withParameter(LootContextParams.ORIGIN, targetDebris.position())
                            .create(LootContextParamSets.GIFT);

                    lootTable.getRandomItems(lootParams).forEach(player::addItem);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                    targetDebris.triggerInteraction();
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BUCKET_EMPTY_FISH, SoundSource.PLAYERS, 0.5F, 0.8F);
                }
            }
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }
        return stack;
    }

    private FloatingDebrisEntity getTargetDebris(Level level, Player player) {
        List<FloatingDebrisEntity> debris = level.getEntitiesOfClass(FloatingDebrisEntity.class,
                player.getBoundingBox().inflate(1));
        return debris.stream()
                .filter(entity -> isInSight(player, entity))
                .min(Comparator.comparingDouble(player::distanceTo))
                .orElse(null);
    }

    private boolean isInSight(Player player, FloatingDebrisEntity entity) {
        var lookVec = player.getLookAngle();
        var toEntity = entity.position().subtract(player.getEyePosition()).normalize();
        double dot = lookVec.dot(toEntity);
        return dot > 0.7;
    }

    private void spawnParticles(ServerLevel serverLevel, double x, double y, double z) {
        RandomSource random = serverLevel.getRandom();
        for (int i = 0; i < 15; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 1.5;
            double offsetY = random.nextDouble() + 0.5;
            double offsetZ = (random.nextDouble() - 0.5) * 1.5;

            serverLevel.sendParticles(ParticleTypes.BUBBLE, x + offsetX, y + offsetY, z + offsetZ, 2, 0, 0.05, 0, 0.01);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }
}
