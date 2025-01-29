package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.BlockPos;
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
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            List<FloatingDebrisEntity> nearbyDebris = level.getEntitiesOfClass(
                    FloatingDebrisEntity.class,
                    player.getBoundingBox().inflate(5)
            );

            if (!nearbyDebris.isEmpty()) {
                FloatingDebrisEntity targetDebris = nearbyDebris.get(0);
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

                if (level instanceof ServerLevel serverLevel) {
                    spawnParticles(serverLevel, targetDebris.blockPosition());
                }
            }
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }
        return stack;
    }

    private void spawnParticles(ServerLevel serverLevel, BlockPos blockPos) {
        RandomSource random = serverLevel.getRandom();
        for (int i = 0; i < 10; i++) {
            double x = blockPos.getX() + 0.5 + (random.nextDouble() - 0.5);
            double y = blockPos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double z = blockPos.getZ() + 0.5 + (random.nextDouble() - 0.5);
            serverLevel.sendParticles(ParticleTypes.BUBBLE, x, y, z, 1, 0.0, 0.0, 0.0, 0.01);
        }
        for (int i = 0; i < 5; i++) {
            double x = blockPos.getX() + 0.5 + (random.nextDouble() - 0.5);
            double y = blockPos.getY() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double z = blockPos.getZ() + 0.5 + (random.nextDouble() - 0.5);
            serverLevel.sendParticles(ParticleTypes.BUBBLE_POP, x, y, z, 1, 0.0, 0.0, 0.0, 0.02);
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
