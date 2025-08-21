package net.satisfy.lilis_lucky_lures.core.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

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
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String mode = tag.contains(MODE_KEY) ? tag.getString(MODE_KEY) : "net";
        String newMode = mode.equals("net") ? "fence" : "net";
        tag.putString(MODE_KEY, newMode);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new net.minecraft.world.item.component.CustomModelData(newMode.equals("net") ? 0 : 1));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private String getMode(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(MODE_KEY) ? tag.getString(MODE_KEY) : "net";
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        Style actionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xffecb3));
        Style descriptionStyle = Style.EMPTY.withColor(TextColor.fromRgb(0x52A3CC));

        Component combinedText = Component.literal("[").setStyle(actionStyle).append(Component.translatable("tooltip.lilis_lucky_lures.item.fish_net.action").setStyle(actionStyle)).append(Component.literal("] ").setStyle(actionStyle)).append(Component.translatable("tooltip.lilis_lucky_lures.item.fish_net.description").setStyle(descriptionStyle));

        tooltip.add(combinedText);
    }
}
