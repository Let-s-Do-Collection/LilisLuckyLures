package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FishNetItem extends Item {
    private static final String TAG_FULL = "Full";
    private static final String TAG_LOOT = "Loot";

    public FishNetItem(Properties properties) {
        super(properties.durability(64));
    }

    public InteractionResult interactWithDebris(ItemStack stack, Player player, FloatingDebrisEntity debris, InteractionHand hand) {
        if (isFull(stack)) return InteractionResult.PASS;

        if (player.level().isClientSide()) return InteractionResult.SUCCESS;

        int luck = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FISHING_LUCK, stack);
        float successChance = 0.6F + (luck * 0.1F);
        boolean success = RandomSource.create().nextFloat() < successChance;

        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        debris.triggerHurt();

        if (success) {
            ServerLevel level = (ServerLevel) player.level();
            LootTable lootTable = level.getServer().getLootData().getLootTable(new LilisLuckyLuresIdentifier("gameplay/fish_net"));
            LootParams lootParams = new LootParams.Builder(level)
                    .withParameter(LootContextParams.ORIGIN, debris.position())
                    .withParameter(LootContextParams.TOOL, stack)
                    .create(LootContextParamSets.FISHING);

            List<ItemStack> lootItems = lootTable.getRandomItems(lootParams);
            if (!lootItems.isEmpty()) {
                CompoundTag tag = stack.getOrCreateTag();
                tag.putBoolean(TAG_FULL, true);
                tag.put(TAG_LOOT, lootItems.get(0).save(new CompoundTag()));
                setFullTexture(stack);
                player.level().playSound(null, debris.blockPosition(), SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            player.level().playSound(null, debris.blockPosition(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isFull(stack)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player player) || !isFull(stack)) return stack;

        CompoundTag tag = stack.getOrCreateTag();
        ItemStack loot = ItemStack.of(tag.getCompound(TAG_LOOT));
        if (!player.getInventory().add(loot)) player.drop(loot, false);
        tag.remove(TAG_FULL);
        tag.remove(TAG_LOOT);
        setEmptyTexture(stack);

        level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F);
        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return isFull(stack) ? 16 : 0;
    }

    public static boolean isFull(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(TAG_FULL);
    }

    private void setFullTexture(ItemStack stack) {
        stack.getOrCreateTag().putInt("CustomModelData", 1);
    }

    private void setEmptyTexture(ItemStack stack) {
        stack.getOrCreateTag().putInt("CustomModelData", 0);
    }
}
