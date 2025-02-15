package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.satisfy.lilis_lucky_lures.core.entity.DynamiteEntity;
import org.jetbrains.annotations.NotNull;

public class DynamiteItem extends Item {
    private static final SoundEvent SOUND_EVENT = SoundEvents.FLINTANDSTEEL_USE;
    private static final int COOLDOWN = 40;
    private static final float Z = 0.0F;
    private static final float VELOCITY = 1.5F;
    private static final float INACCURACY = 1.125F;

    public DynamiteItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SOUND_EVENT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, COOLDOWN);
        if (!level.isClientSide) {
            DynamiteEntity dynamite = new DynamiteEntity(level, player);
            dynamite.setPosRaw(player.getX(), player.getEyeY() - 0.1F, player.getZ());
            dynamite.setItem(itemstack);
            dynamite.setOwner(player);
            dynamite.shootFromRotation(player, player.getXRot(), player.getYRot(), Z, VELOCITY, INACCURACY);
            level.addFreshEntity(dynamite);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
