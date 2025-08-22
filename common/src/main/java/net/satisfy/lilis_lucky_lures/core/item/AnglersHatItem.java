package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnglersHatItem extends ArmorItem {
    private final ResourceLocation hatTexture;

    public AnglersHatItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties, ResourceLocation hatTexture) {
        super(armorMaterial, type, properties);
        this.hatTexture = hatTexture;
    }

    public ResourceLocation getHatTexture() {
        return hatTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.lilis_lucky_lures.item.anglers_hat_worn").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xDAFFFF))).append(Component.literal(" ")).append(Component.translatable("tooltip.lilis_lucky_lures.item.anglers_hat_effect").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC)))));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            applyLuckBonus(player);
        }
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return true;
    }

    public static void applyLuckBonus(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        if (helmet.getItem() instanceof AnglersHatItem) {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 0, 0, true, false, true));
        }
    }
}

