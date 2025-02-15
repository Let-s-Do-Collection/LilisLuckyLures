package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FishNetBlockItem extends Item {
    private static final String MODE_KEY = "FishingNetMode";
    private final Block netBlock;
    private final Block fenceBlock;

    public FishNetBlockItem(Properties properties, Block netBlock, Block fenceBlock) {
        super(properties);
        this.netBlock = netBlock;
        this.fenceBlock = fenceBlock;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                toggleMode(stack, (ServerLevel) level, player);
            }
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        String mode = getMode(stack);
        Block targetBlock = mode.equals("fence") ? fenceBlock : netBlock;

        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockState state = targetBlock.getStateForPlacement(placeContext);
        if (state == null || !state.canSurvive(level, pos)) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            level.setBlock(pos, state, Block.UPDATE_ALL);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                toggleMode(stack, (ServerLevel) level, player);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    private void toggleMode(ItemStack stack, ServerLevel level, Player player) {
        String mode = getMode(stack);
        String newMode = mode.equals("net") ? "fence" : "net";
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(MODE_KEY, newMode);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (newMode.equals("net")) {
            tag.putInt("CustomModelData", 0);
        } else {
            tag.putInt("CustomModelData", 1);
        }
        stack.setTag(tag);
    }

    private String getMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return (tag != null && tag.contains(MODE_KEY)) ? tag.getString(MODE_KEY) : "net";
    }


@Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        Style actionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xffecb3));
        Style descriptionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC));

        Component combinedText = Component.literal("[").setStyle(actionStyle).append(Component.translatable("tooltip.lilis_lucky_lures.item.fish_net.action").setStyle(actionStyle)).append(Component.literal("] ").setStyle(actionStyle)).append(Component.translatable("tooltip.lilis_lucky_lures.item.fish_net.description").setStyle(descriptionStyle));

        tooltip.add(combinedText);
    }
}
