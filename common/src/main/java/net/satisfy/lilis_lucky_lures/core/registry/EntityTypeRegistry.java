package net.satisfy.lilis_lucky_lures.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.block.entity.*;
import net.satisfy.lilis_lucky_lures.core.entity.*;
import net.satisfy.lilis_lucky_lures.core.entity.projectile.ThrownSpearEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

import java.util.function.Supplier;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DynamiteEntity>> DYNAMITE = registerEntityType("dynamite", () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(LilisLuckyLuresIdentifier.identifier("dynamite").toString()));
    public static final RegistrySupplier<EntityType<ThrownSpearEntity>> THROWN_SPEAR = registerEntityType("thrown_spear", () -> EntityType.Builder.<ThrownSpearEntity>of(ThrownSpearEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(LilisLuckyLuresIdentifier.identifier("thrown_spear").toString()));

    public static final RegistrySupplier<EntityType<FloatingDebrisEntity>> FLOATING_DEBRIS = registerEntityType("floating_debris", () -> EntityType.Builder.of(FloatingDebrisEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(LilisLuckyLuresIdentifier.identifier("floating_debris").toString()));
    public static final RegistrySupplier<EntityType<FloatingBooksEntity>> FLOATING_BOOKS = registerEntityType("floating_books", () -> EntityType.Builder.of(FloatingBooksEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(LilisLuckyLuresIdentifier.identifier("floating_books").toString()));
    public static final RegistrySupplier<EntityType<RiverFishPoolEntity>> RIVER_FISH_POOL = registerEntityType("river_fish_pool", () -> EntityType.Builder.of(RiverFishPoolEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(LilisLuckyLuresIdentifier.identifier("river_fish_pool").toString()));
    public static final RegistrySupplier<EntityType<OceanFishPoolEntity>> OCEAN_FISH_POOL = registerEntityType("ocean_fish_pool", () -> EntityType.Builder.of(OceanFishPoolEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(LilisLuckyLuresIdentifier.identifier("ocean_fish_pool").toString()));

    public static final RegistrySupplier<BlockEntityType<FishTrapBlockEntity>> FISH_TRAP = registerBlockEntity("fish_trap", () -> BlockEntityType.Builder.of(FishTrapBlockEntity::new, ObjectRegistry.FISH_TRAP.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<HangingFrameBlockEntity>> HANGING_FRAME = registerBlockEntity("hanging_frame", () -> BlockEntityType.Builder.of(HangingFrameBlockEntity::new, ObjectRegistry.HANGING_FRAME.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RedstoneCoilBlockEntity>> REDSTONE_COIL = registerBlockEntity("redstone_coil", () -> BlockEntityType.Builder.of(RedstoneCoilBlockEntity::new, ObjectRegistry.REDSTONE_COIL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<FishTrophyFrameBlockEntity>> FISH_TROPHY_FRAME = registerBlockEntity("fishing_trophy_frame", () -> BlockEntityType.Builder.of(FishTrophyFrameBlockEntity::new, ObjectRegistry.FISH_TROPHY_FRAME.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CompletionistBannerEntity>> LILIS_LUCKY_LURES_BANNER = registerBlockEntity("lilis_lucky_lures_banner", () -> BlockEntityType.Builder.of(CompletionistBannerEntity::new, ObjectRegistry.LILIS_LUCKY_LURES_BANNER.get(), ObjectRegistry.LILIS_LUCKY_LURES_WALL_BANNER.get()).build(null));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(LilisLuckyLuresIdentifier.identifier(name), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(LilisLuckyLuresIdentifier.identifier(path), type);
    }

    public static void init() {
        ENTITY_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
    }
}
