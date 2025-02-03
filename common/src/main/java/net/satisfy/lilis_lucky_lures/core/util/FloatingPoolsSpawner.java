package net.satisfy.lilis_lucky_lures.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingBooksEntity;
import net.satisfy.lilis_lucky_lures.core.entity.FloatingDebrisEntity;
import net.satisfy.lilis_lucky_lures.core.entity.OceanFishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.entity.RiverFishPoolEntity;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import org.joml.Vector3d;

public class FloatingPoolsSpawner {
    private static final int MAX_COUNT = 3;
    private static final int SPAWN_INTERVAL = 6000;

    private static void attemptSpawn(ServerLevel level) {
        if (level.players().isEmpty()) return;
        RandomSource random = level.random;
        var player = level.players().get(random.nextInt(level.players().size()));
        int spawnX = (int) player.getX() + random.nextInt(100) - 50;
        int spawnZ = (int) player.getZ() + random.nextInt(100) - 50;
        int spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ) - 2;
        BlockPos pos = new BlockPos(spawnX, spawnY, spawnZ);
        var biome = level.getBiome(pos);
        if (!biome.is(BiomeTags.IS_RIVER) && !biome.is(BiomeTags.IS_OCEAN)) return;
        AABB area = new AABB(spawnX - 128, spawnY - 128, spawnZ - 128, spawnX + 128, spawnY + 128, spawnZ + 128);
        int totalCount = level.getEntitiesOfClass(FloatingDebrisEntity.class, area).size();
        totalCount += level.getEntitiesOfClass(FloatingBooksEntity.class, area).size();
        if (biome.is(BiomeTags.IS_OCEAN)) {
            totalCount += level.getEntitiesOfClass(OceanFishPoolEntity.class, area).size();
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            totalCount += level.getEntitiesOfClass(RiverFishPoolEntity.class, area).size();
        }
        if (totalCount >= 3) return;
        java.util.List<java.lang.Runnable> actions = new java.util.ArrayList<>();
        AABB smallArea = new AABB(spawnX - 50, spawnY - 50, spawnZ - 50, spawnX + 50, spawnY + 50, spawnZ + 50);
        if (level.getEntitiesOfClass(FloatingDebrisEntity.class, smallArea).size() < MAX_COUNT) {
            actions.add(() -> {
                FloatingDebrisEntity debris = new FloatingDebrisEntity(EntityTypeRegistry.FLOATING_DEBRIS.get(), level);
                Vector3d spawnPos = new Vector3d(spawnX + 0.5, spawnY, spawnZ + 0.5);
                debris.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                level.addFreshEntity(debris);
            });
        }
        if (biome.is(BiomeTags.IS_OCEAN)) {
            actions.add(() -> {
                OceanFishPoolEntity ocean = new OceanFishPoolEntity(EntityTypeRegistry.OCEAN_FISH_POOL.get(), level);
                Vector3d spawnPos = new Vector3d(spawnX + 0.5, spawnY, spawnZ + 0.5);
                ocean.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                level.addFreshEntity(ocean);
            });
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            actions.add(() -> {
                RiverFishPoolEntity river = new RiverFishPoolEntity(EntityTypeRegistry.RIVER_FISH_POOL.get(), level);
                Vector3d spawnPos = new Vector3d(spawnX + 0.5, spawnY, spawnZ + 0.5);
                river.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                level.addFreshEntity(river);
            });
        }
        actions.add(() -> {
            FloatingBooksEntity books = new FloatingBooksEntity(EntityTypeRegistry.FLOATING_BOOKS.get(), level);
            Vector3d spawnPos = new Vector3d(spawnX + 0.5, spawnY, spawnZ + 0.5);
            books.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            level.addFreshEntity(books);
        });
        actions.get(random.nextInt(actions.size())).run();
    }

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % SPAWN_INTERVAL == 0) attemptSpawn(level);
    }
}