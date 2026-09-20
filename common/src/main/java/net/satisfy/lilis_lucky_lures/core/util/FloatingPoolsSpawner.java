package net.satisfy.lilis_lucky_lures.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
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

    private static void attemptSpawn(ServerLevel level) {
        if (level.players().isEmpty()) return;
        RandomSource random = level.random;
        var player = level.players().get(random.nextInt(level.players().size()));

        BlockPos validSpawn = findValidSpawnPos(level, player.blockPosition());
        if (validSpawn == null) return;

        int spawnX = validSpawn.getX();
        int spawnY = validSpawn.getY();
        int spawnZ = validSpawn.getZ();

        // All floating pool entities share this common superclass, so this single check covers
        // every variant. Without it, two independently rolled spawn points could end up right on
        // top of (or inside) each other since only entity COUNTS in a wide radius were checked,
        // never the actual clearance at the chosen point.
        AABB clearance = new AABB(spawnX, spawnY, spawnZ, spawnX, spawnY, spawnZ).inflate(6.0);
        if (!level.getEntitiesOfClass(FloatingDebrisEntity.class, clearance).isEmpty()) {
            return;
        }

        AABB area = new AABB(spawnX - 128, spawnY - 128, spawnZ - 128, spawnX + 128, spawnY + 128, spawnZ + 128);
        int totalCount = level.getEntitiesOfClass(FloatingDebrisEntity.class, area).size();
        totalCount += level.getEntitiesOfClass(FloatingBooksEntity.class, area).size();

        var biome = level.getBiome(validSpawn);
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
            // Any real body of water counts, not just ones tagged as river/ocean biome - a
            // desert oasis, a plains pond etc. are all perfectly fishable but were previously
            // rejected outright, which could leave debris never spawning near the player at all.
            int waterTopY = findWaterSurfaceY(level, newPos.getX(), newPos.getZ());
            if (waterTopY != Integer.MIN_VALUE) {
                return new BlockPos(newPos.getX(), waterTopY - 2, newPos.getZ());
            }
        }
        return null;
    }

    // WORLD_SURFACE only reports the topmost non-air block, which also matches lily pads,
    // flowers and other thin plants that some water/plant mods place on top of the water
    // (and may prevent from breaking). Trusting that heightmap alone spawns debris on top
    // of those plants instead of on the actual water, floating well above the surface.
    // Scanning down for the real water fluid keeps the spawn height correct regardless.
    private static int findWaterSurfaceY(ServerLevel level, int x, int z) {
        int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        int bottom = level.getMinBuildHeight();
        for (int y = top; y >= bottom; y--) {
            if (level.getFluidState(new BlockPos(x, y, z)).is(FluidTags.WATER)) {
                return y;
            }
        }
        return Integer.MIN_VALUE;
    }

    public static void tick(ServerLevel level) {
        if (level.getGameTime() % SPAWN_INTERVAL == 0) attemptSpawn(level);
    }
}
