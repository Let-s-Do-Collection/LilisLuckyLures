package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoakedBagItem extends Item {
    public SoakedBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.swing(hand);

        if (!world.isClientSide) {
            final MinecraftServer minecraftServer = world.getServer();
            if (minecraftServer != null && world instanceof ServerLevel server) {
                LootParams lootContext = new LootParams.Builder(server)
                        .withParameter(LootContextParams.THIS_ENTITY, player)
                        .withParameter(LootContextParams.ORIGIN, player.position())
                        .create(LootContextParamSets.GIFT);
                LootTable treasure = minecraftServer.getLootData().getLootTable(new ResourceLocation(LilisLuckyLures.MOD_ID, "gameplay/soaked_bag"));

                List<ItemStack> lootItems = treasure.getRandomItems(lootContext);

                boolean hasSpace = lootItems.stream().allMatch(player.getInventory()::add);

                if (!hasSpace) {
                    lootItems.forEach(itemStack -> player.drop(itemStack, false));
                }

                world.playSound(player, player.blockPosition().above(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS, 1, 1);
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.success(stack);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        Style actionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xffecb3));
        Style descriptionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC));

        Component combinedText = Component.literal("[").setStyle(actionStyle).append(Component.translatable("tooltip.lilis_lucky_lures.item.soaked_bag.action").setStyle(actionStyle)).append(Component.literal("] ").setStyle(actionStyle)).append(Component.translatable("tooltip.lilis_lucky_lures.item.soaked_bag.description").setStyle(descriptionStyle));

        tooltip.add(combinedText);
    }
}
