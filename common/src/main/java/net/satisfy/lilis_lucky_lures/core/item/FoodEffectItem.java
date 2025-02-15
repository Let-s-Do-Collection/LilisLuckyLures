package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FoodEffectItem extends Item {
    private final int effectDuration;
    private final boolean cooked;

    public FoodEffectItem(Item.Properties properties, int effectDuration, boolean cooked) {
        super(properties);
        this.effectDuration = effectDuration;
        this.cooked = cooked;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(cooked ? MobEffects.LUCK : MobEffects.HUNGER, effectDuration));

            if (entity instanceof Player player) {
                int nutrition = cooked ? 6 : 2;
                float saturation = cooked ? 0.6F : 0.2F;

                player.getFoodData().eat(nutrition, saturation);
            }
        }

        if (entity instanceof ServerPlayer player) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
            player.getInventory().add(new ItemStack(Items.BOWL));
        }

        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.8F, 1.0F);
        level.gameEvent(entity, GameEvent.EAT, entity.position());

        return stack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (cooked) {
            MobEffectInstance effectInstance = new MobEffectInstance(MobEffects.LUCK, effectDuration);
            int effectLevel = effectInstance.getAmplifier();
            MutableComponent effectName = Component.translatable(effectInstance.getEffect().getDescriptionId());

            if (effectLevel > 0) {
                effectName.append(" ").append(Component.translatable("potion.potency." + effectLevel));
            }

            String durationText = MobEffectUtil.formatDuration(effectInstance, 1.0f).getString();
            MutableComponent effectDuration = Component.translatable(" (").append(Component.literal(durationText)).append(Component.literal(")"));

            tooltip.add(effectName.append(effectDuration).withStyle(effectInstance.getEffect().getCategory().getTooltipFormatting()));
        } else {
            tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.uncooked").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD27D46))));

        }
    }
}
