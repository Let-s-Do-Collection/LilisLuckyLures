package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.satisfy.lilis_lucky_lures.core.entity.SpearEntity;
import org.jetbrains.annotations.NotNull;

public class SpearItem extends Item {
    public SpearItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            SpearEntity spear = new SpearEntity(level, player);
            spear.setItem(itemStack);
            spear.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);
            level.addFreshEntity(spear);
            if (!player.isCreative()) {
                player.getInventory().removeItem(itemStack);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
    }
}
