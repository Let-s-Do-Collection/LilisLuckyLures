package net.satisfy.lilis_lucky_lures.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.block.*;
import net.satisfy.lilis_lucky_lures.core.item.*;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> REDSTONE_COIL = registerWithItem("redstone_coil", () -> new RedstoneCoilBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK).lightLevel(state -> state.getValue(RedstoneCoilBlock.ACTIVE) ? 8 : 0).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> FISH_TRAP = registerWithItem("fish_trap", () -> new FishTrapBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> FISH_BAG = registerWithItem("fish_bag", () -> new FishBagBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Item> SOAKED_BAG = registerItem("soaked_bag", () -> new SoakedBagItem(getSettings()));
    public static final RegistrySupplier<Block> HANGING_FRAME = registerWithItem("hanging_frame", () -> new HangingFrameBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Item> SPEAR = registerItem("spear", () -> new SpearItem(getSettings().durability(124)));
    public static final RegistrySupplier<Item> DYNAMITE = registerItem("dynamite", () -> new DynamiteItem(getSettings()));
    public static final RegistrySupplier<Item> BAMBOO_FISHING_ROD = registerItem("bamboo_fishing_rod", () -> new FishingRodItem(getSettings()));
    public static final RegistrySupplier<Item> FISHING_NET = registerItem("fishing_net", () -> new FishingNetItem(getSettings()));
    public static final RegistrySupplier<Item> FLOATING_DEBRIS = registerItem("floating_debris", () -> new FloatingPoolsItem(getSettings()));
    public static final RegistrySupplier<Item> FLOATING_BOOKS = registerItem("floating_books", () -> new FloatingPoolsItem(getSettings()));
    public static final RegistrySupplier<Item> RIVER_FISH_POOL = registerItem("river_fish_pool", () -> new FloatingPoolsItem(getSettings()));
    public static final RegistrySupplier<Item> OCEAN_FISH_POOL = registerItem("ocean_fish_pool", () -> new FloatingPoolsItem(getSettings()));
    public static final RegistrySupplier<Item> COOKED_COD_MEAL = registerItem("cooked_cod_meal", () -> new FoodEffectItem(getFoodSettings(8, 0.8f), 3600, true));
    public static final RegistrySupplier<Item> SALMON_ROLLS = registerItem("salmon_rolls", () -> new FoodEffectItem(getFoodSettings(10, 0.9f), 4800, true));
    public static final RegistrySupplier<Item> GRILLED_TROPICAL_FISH = registerItem("grilled_tropical_fish", () -> new FoodEffectItem(getFoodSettings(6, 0.7f), 3600, true));
    public static final RegistrySupplier<Item> UNCOOKED_COD_MEAL = registerItem("uncooked_cod_meal", () -> new FoodEffectItem(getFoodSettings(6, 0.7f), 200, false));
    public static final RegistrySupplier<Item> UNCOOKED_SALMON_ROLLS = registerItem("uncooked_salmon_rolls", () -> new FoodEffectItem(getFoodSettings(4, 0.4f), 200, false));
    public static final RegistrySupplier<Item> UNCOOKED_TROPICAL_FISH = registerItem("uncooked_tropical_fish", () -> new FoodEffectItem(getFoodSettings(4, 0.5f), 200, false));
    public static final RegistrySupplier<Block> ELASTIC_FISHING_NET = registerWithoutItem("elastic_fishing_net", () -> new ElasticFishingNetBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Block> FISH_NET_FENCE = registerWithoutItem("fish_net_fence", () -> new FishNetFenceBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL)));
    public static final RegistrySupplier<Item> FISH_NET = registerItem("fish_net", () -> new FishNetBlockItem(getSettings(), ELASTIC_FISHING_NET.get(), FISH_NET_FENCE.get()));
    public static final RegistrySupplier<Block> FISH_TROPHY_FRAME = registerWithItem("fish_trophy_frame", () -> new FishTrophyFrameBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Item> PUFFER_PLATER = registerItem("puffer_plater", () -> new CookableSuspiciousStewItem(getFoodSettings(5, 0.6f), true));
    public static final RegistrySupplier<Item> UNCOOKED_PUFFER_PLATER = registerItem("uncooked_puffer_plater", () -> new CookableSuspiciousStewItem(getFoodSettings(5, 0.4f), false));


    public static void init() {
        ITEMS.register();
        BLOCKS.register();
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    private static Item.Properties getFoodSettings(int nutrition, float saturation) {
        return new Item.Properties().food(new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).alwaysEat().build());
    }


    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return LilisLuckyLuresUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new LilisLuckyLuresIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return LilisLuckyLuresUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new LilisLuckyLuresIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return LilisLuckyLuresUtil.registerItem(ITEMS, ITEM_REGISTRAR, new LilisLuckyLuresIdentifier(path), itemSupplier);
    }
}
