package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class CookableSuspiciousStewItem extends Item {
    private final boolean cooked;

    public CookableSuspiciousStewItem(Item.Properties properties, boolean cooked) {
        super(properties);
        this.cooked = cooked;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            if (cooked) {
                MobEffectInstance randomEffect = getRandomEffect();
                entity.addEffect(randomEffect);
            } else {
                entity.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
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

    private static MobEffectInstance getRandomEffect() {
        MobEffect[] possibleEffects = {
                MobEffects.REGENERATION.value(),
                MobEffects.MOVEMENT_SPEED.value(),
                MobEffects.DAMAGE_BOOST.value(),
                MobEffects.ABSORPTION.value(),
                MobEffects.LUCK.value(),
                MobEffects.DAMAGE_RESISTANCE.value(),
                MobEffects.JUMP.value(),
                MobEffects.FIRE_RESISTANCE.value(),
                MobEffects.WATER_BREATHING.value(),
                MobEffects.NIGHT_VISION.value(),
                MobEffects.SLOW_FALLING.value(),
                MobEffects.CONDUIT_POWER.value(),
                MobEffects.DOLPHINS_GRACE.value(),
                MobEffects.MOVEMENT_SLOWDOWN.value(),
                MobEffects.DIG_SLOWDOWN.value(),
                MobEffects.CONFUSION.value(),
                MobEffects.BLINDNESS.value(),
                MobEffects.HUNGER.value(),
                MobEffects.WEAKNESS.value(),
                MobEffects.POISON.value()
        };

        Random random = new Random();
        MobEffect effect = possibleEffects[random.nextInt(possibleEffects.length)];
        int duration = 100 + random.nextInt(201);

        return new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), duration);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (!cooked) {
            tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.uncooked").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD27D46))));
        }
    }
}
