package net.satisfy.lilis_lucky_lures.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.lilis_lucky_lures.LilisLuckyLures;
import net.satisfy.lilis_lucky_lures.core.entity.DynamiteEntity;
import net.satisfy.lilis_lucky_lures.core.entity.SpearEntity;
import net.satisfy.lilis_lucky_lures.core.util.LilisLuckyLuresIdentifier;

import java.util.function.Supplier;

public class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(LilisLuckyLures.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DynamiteEntity>> DYNAMITE = registerEntityType("dynamite", () -> EntityType.Builder.<DynamiteEntity>of(DynamiteEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new LilisLuckyLuresIdentifier("dynamite").toString()));
    public static final RegistrySupplier<EntityType<SpearEntity>> SPEAR = registerEntityType("spear", () -> EntityType.Builder.<SpearEntity>of(SpearEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).build(new LilisLuckyLuresIdentifier("spear").toString()));


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
