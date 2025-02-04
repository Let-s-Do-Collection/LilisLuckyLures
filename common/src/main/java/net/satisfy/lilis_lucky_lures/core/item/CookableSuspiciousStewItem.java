package net.satisfy.lilis_lucky_lures.core.item;

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
import org.jetbrains.annotations.Nullable;

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
                MobEffects.REGENERATION,
                MobEffects.MOVEMENT_SPEED,
                MobEffects.DAMAGE_BOOST,
                MobEffects.ABSORPTION,
                MobEffects.LUCK,
                MobEffects.DAMAGE_RESISTANCE,
                MobEffects.JUMP,
                MobEffects.FIRE_RESISTANCE,
                MobEffects.WATER_BREATHING,
                MobEffects.NIGHT_VISION,
                MobEffects.SLOW_FALLING,
                MobEffects.CONDUIT_POWER,
                MobEffects.DOLPHINS_GRACE,
                MobEffects.MOVEMENT_SLOWDOWN,
                MobEffects.DIG_SLOWDOWN,
                MobEffects.CONFUSION,
                MobEffects.BLINDNESS,
                MobEffects.HUNGER,
                MobEffects.WEAKNESS,
                MobEffects.POISON
        };

        Random random = new Random();
        MobEffect effect = possibleEffects[random.nextInt(possibleEffects.length)];
        int duration = 100 + random.nextInt(201);

        return new MobEffectInstance(effect, duration);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!cooked) {
            tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.uncooked").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD27D46))));
        }
    }
}
