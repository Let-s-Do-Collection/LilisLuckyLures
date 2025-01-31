package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CookableSuspiciousStewItem extends SuspiciousStewItem {
    private final boolean cooked;

    public CookableSuspiciousStewItem(Item.Properties properties, boolean cooked) {
        super(properties);
        this.cooked = cooked;
    }

    @SuppressWarnings("unused")
    public boolean isCooked() {
        return cooked;
    }

    private static void extractPotionEffects(ItemStack itemStack, Consumer<MobEffectInstance> consumer) {
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains("Effects", 9)) {
            ListTag listTag = compoundTag.getList("Effects", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag effectTag = listTag.getCompound(i);
                int duration = effectTag.contains("EffectDuration", 99) ? effectTag.getInt("EffectDuration") : 160;
                MobEffect effect = MobEffect.byId(effectTag.getInt("EffectId"));
                if (effect != null) {
                    consumer.accept(new MobEffectInstance(effect, duration));
                }
            }
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(itemStack, level, livingEntity);
        extractPotionEffects(itemStack, livingEntity::addEffect);
        return livingEntity instanceof Player && ((Player) livingEntity).getAbilities().instabuild ? result : new ItemStack(Items.BOWL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!cooked) {
            tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.uncooked").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xD27D46))));
        }
    }
}
