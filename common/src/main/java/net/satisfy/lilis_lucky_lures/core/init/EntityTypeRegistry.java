package net.satisfy.lilis_lucky_lures.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.block.entity.FishTrapBlockEntity;
import net.satisfy.lilis_lucky_lures.core.block.entity.HangingFrameBlockEntity;
import net.satisfy.lilis_lucky_lures.core.entity.DynamiteEntity;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingBooksEntity;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.entity.RiverFishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.entity.projectile.ThrownSpearEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

import java.util.function.Supplier;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DynamiteEntity>> DYNAMITE = registerEntityType("dynamite", () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new LilisLuckyLuresIdentifier("dynamite").toString()));
    public static final RegistrySupplier<EntityType<ThrownSpearEntity>> THROWN_SPEAR = registerEntityType("thrown_spear", () -> EntityType.Builder.<ThrownSpearEntity>of(ThrownSpearEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new LilisLuckyLuresIdentifier("thrown_spear").toString()));

    public static final RegistrySupplier<EntityType<FloatingDebrisEntity>> FLOATING_DEBRIS = registerEntityType("floating_debris", () -> EntityType.Builder.of(FloatingDebrisEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(new LilisLuckyLuresIdentifier("floating_debris").toString()));
    public static final RegistrySupplier<EntityType<FloatingBooksEntity>> FLOATING_BOOKS = registerEntityType("floating_books", () -> EntityType.Builder.of(FloatingBooksEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(new LilisLuckyLuresIdentifier("floating_books").toString()));
    public static final RegistrySupplier<EntityType<RiverFishPoolEntity>> RIVER_FISH_POOL = registerEntityType("river_fish_pool", () -> EntityType.Builder.of(RiverFishPoolEntity::new, MobCategory.CREATURE).sized(2f, 2.5f).build(new LilisLuckyLuresIdentifier("river_fish_pool").toString()));

    public static final RegistrySupplier<BlockEntityType<FishTrapBlockEntity>> FISH_TRAP = registerBlockEntity("fish_trap", () -> BlockEntityType.Builder.of(FishTrapBlockEntity::new, ObjectRegistry.FISH_TRAP.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<HangingFrameBlockEntity>> HANGING_FRAME = registerBlockEntity("hanging_frame", () -> BlockEntityType.Builder.of(HangingFrameBlockEntity::new, ObjectRegistry.HANGING_FRAME.get()).build(null));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new LilisLuckyLuresIdentifier(name), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(new LilisLuckyLuresIdentifier(path), type);
    }

    public static void init() {
        ENTITY_TYPES.register();
        BLOCK_ENTITY_TYPES.register();
    }
}
