package net.satisfy.lilis_lucky_lures.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.block.FishTrapBlock;
import net.satisfy.lilis_lucky_lures.core.item.*;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> REDSTONE_COIL = registerWithItem("redstone_coil", () -> new Block(BlockBehaviour.Properties.copy(Blocks.REDSTONE_BLOCK)));
    public static final RegistrySupplier<Block> FISH_TRAP = registerWithItem("fish_trap", () -> new FishTrapBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> FISH_BARREL = registerWithItem("fish_barrel", () -> new BarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
    public static final RegistrySupplier<Item> SOAKED_BAG = registerItem("soaked_bag", () -> new SoakedBagItem(getSettings()));


    public static final RegistrySupplier<Item> SPEAR = registerItem("spear", () -> new SpearItem(getSettings().durability(124)));
    public static final RegistrySupplier<Item> DYNAMITE = registerItem("dynamite", () -> new DynamiteItem(getSettings()));
    public static final RegistrySupplier<Item> BAMBOO_FISHING_ROD = registerItem("bamboo_fishing_rod", () -> new FishingRodItem(getSettings()));
    public static final RegistrySupplier<Item> FISHING_NET = registerItem("fishing_net", () -> new FishingNetItem(getSettings()));
    public static final RegistrySupplier<Item> FLOATING_DEBRIS = registerItem("floating_debris", () -> new FloatingDebrisItem(getSettings()));

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
