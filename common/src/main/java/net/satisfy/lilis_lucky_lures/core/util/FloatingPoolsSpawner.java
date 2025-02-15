package net.satisfy.lilis_lucky_lures.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
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
    private static BlockPos lastValidSpawn = null;
    private static long lastSpawnTime = 0;

    private static void attemptSpawn(ServerLevel level) {
        if (level.players().isEmpty()) return;
        RandomSource random = level.random;
        var player = level.players().get(random.nextInt(level.players().size()));

        if (lastValidSpawn == null || (level.getGameTime() - lastSpawnTime) > 12000) {
            lastValidSpawn = findValidSpawnPos(level, player.blockPosition());
            lastSpawnTime = level.getGameTime();
        }

        if (lastValidSpawn == null) return;

        int spawnX = lastValidSpawn.getX();
        int spawnY = lastValidSpawn.getY();
        int spawnZ = lastValidSpawn.getZ();

        AABB area = new AABB(spawnX - 128, spawnY - 128, spawnZ - 128, spawnX + 128, spawnY + 128, spawnZ + 128);
        int totalCount = level.getEntitiesOfClass(FloatingDebrisEntity.class, area).size();
        totalCount += level.getEntitiesOfClass(FloatingBooksEntity.class, area).size();

        var biome = level.getBiome(lastValidSpawn);
        if (biome.is(BiomeTags.IS_OCEAN)) {
            totalCount += level.getEntitiesOfClass(OceanFishPoolEntity.class, area).size();
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            totalCount += level.getEntitiesOfClass(RiverFishPoolEntity.class, area).size();
        }

        if (totalCount >= MAX_COUNT) return;

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

    private static BlockPos findValidSpawnPos(ServerLevel level, BlockPos center) {
        RandomSource random = level.random;
        for (int i = 0; i < 10; i++) {
            int offsetX = random.nextInt(100 * 2) - 100;
            int offsetZ = random.nextInt(100 * 2) - 100;
            BlockPos newPos = center.offset(offsetX, 0, offsetZ);
            var biome = level.getBiome(newPos);
            if (biome.is(BiomeTags.IS_RIVER) || biome.is(BiomeTags.IS_OCEAN)) {
                int spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, newPos.getX(), newPos.getZ()) - 2;
                return new BlockPos(newPos.getX(), spawnY, newPos.getZ());
            }
        }
        return null;
    }

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % SPAWN_INTERVAL == 0) attemptSpawn(level);
    }
}
