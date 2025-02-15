package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

import java.util.Comparator;
import java.util.List;

public class FishingNetItem extends Item {
    private static final float SUCCESS_CHANCE = 0.8F;
    private static final int USE_DURATION = 100;
    private static final int COOLDOWN_TICKS = 40;
    private static final String TAG_STATE = "State";
    private static final String TAG_ENTITY_LOOT = "EntityLoot";
    private static final int STATE_EMPTY = 0;
    private static final int STATE_FULL = 1;

    public FishingNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int state = getState(stack);
        if (state == STATE_EMPTY) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        } else {
            if (!level.isClientSide) {
                retrieveLoot(level, player, stack);
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!level.isClientSide && entity instanceof Player player && getState(stack) == STATE_EMPTY) {
            FloatingDebrisEntity targetDebris = getTargetDebris(level, player);
            if (targetDebris != null) {
                ServerLevel serverLevel = (ServerLevel) level;
                spawnParticles(serverLevel, targetDebris.getX(), targetDebris.getY() + 0.5, targetDebris.getZ());

                if (remainingUseTicks % 20 == 0) {
                    level.playSound(null, targetDebris.getX(), targetDebris.getY(), targetDebris.getZ(),
                            SoundEvents.BOAT_PADDLE_WATER, SoundSource.NEUTRAL, 1.25F, 1.0F);
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player && getState(stack) == STATE_EMPTY) {
            FloatingDebrisEntity targetDebris = getTargetDebris(level, player);
            if (targetDebris != null) {
                if (level.getRandom().nextFloat() <= SUCCESS_CHANCE) {
                    setState(stack, STATE_FULL);

                    LootTable entityLootTable = targetDebris.getLootTable((ServerLevel) level);
                    LootParams lootParams = new LootParams.Builder((ServerLevel) level)
                            .withParameter(LootContextParams.THIS_ENTITY, player)
                            .withParameter(LootContextParams.ORIGIN, player.position())
                            .create(LootContextParamSets.GIFT);

                    List<ItemStack> entityLoot = entityLootTable.getRandomItems(lootParams);

                    CompoundTag tag = stack.getOrCreateTag();
                    ListTag lootListTag = new ListTag();
                    for (ItemStack item : entityLoot) {
                        CompoundTag itemTag = new CompoundTag();
                        item.save(itemTag);
                        lootListTag.add(itemTag);
                    }
                    tag.put(TAG_ENTITY_LOOT, lootListTag);
                    stack.setTag(tag);

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.GENERIC_SWIM, SoundSource.PLAYERS, 1.0F, 1.0F);
                    targetDebris.triggerInteraction();
                } else {
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 0.5F, 0.8F);
                }
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            }
        }
        return stack;
    }

    private void retrieveLoot(Level level, Player player, ItemStack stack) {
        ServerLevel serverLevel = (ServerLevel) level;

        LootTable fishingNetLootTable = serverLevel.getServer().getLootData()
                .getLootTable(new LilisLuckyLuresIdentifier("gameplay/fishing_net"));

        LootParams fishingNetLootParams = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .create(LootContextParamSets.GIFT);

        List<ItemStack> fishingNetLoot = fishingNetLootTable.getRandomItems(fishingNetLootParams);
        fishingNetLoot.forEach(player::addItem);

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_ENTITY_LOOT, 9)) {
            ListTag lootListTag = tag.getList(TAG_ENTITY_LOOT, 10);
            for (int i = 0; i < lootListTag.size(); i++) {
                CompoundTag itemTag = lootListTag.getCompound(i);
                ItemStack entityLootItem = ItemStack.of(itemTag);
                player.addItem(entityLootItem);
            }
            tag.remove(TAG_ENTITY_LOOT);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
        setState(stack, STATE_EMPTY);
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

    private int getState(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_STATE)) {
            return tag.getInt(TAG_STATE);
        }
        return STATE_EMPTY;
    }

    private void setState(ItemStack stack, int state) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_STATE, state);
        if (state == STATE_FULL) {
            tag.putInt("CustomModelData", 1);
        } else {
            tag.remove("CustomModelData");
        }
        stack.setTag(tag);
    }
}
