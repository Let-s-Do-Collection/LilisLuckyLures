package net.satisfy.lilis_lucky_lures.core.util;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class LilisLuckyLuresUtil {

    public static <T extends Block> RegistrySupplier<T> registerWithItem(DeferredRegister<Block> registerB, Registrar<Block> registrarB, DeferredRegister<Item> registerI, Registrar<Item> registrarI, ResourceLocation name, Supplier<T> block) {
        RegistrySupplier<T> toReturn = registerWithoutItem(registerB, registrarB, name, block);
        registerItem(registerI, registrarI, name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        return toReturn;
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(DeferredRegister<Block> register, Registrar<Block> registrar, ResourceLocation path, Supplier<T> block) {
        return Platform.isForge() ? register.register(path.getPath(), block) : registrar.register(path, block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(DeferredRegister<Item> register, Registrar<Item> registrar, ResourceLocation path, Supplier<T> itemSupplier) {
        return Platform.isForge() ? register.register(path.getPath(), itemSupplier) : registrar.register(path, itemSupplier);
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for(int i = 0; i < times; ++i) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.joinUnoptimized(buffer[1], Shapes.box(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX), BooleanOp.OR));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    public static Collection<ServerPlayer> getTrackingPlayers(ServerLevel world, ChunkPos chunkPos) {
        Objects.requireNonNull(world);
        Objects.requireNonNull(chunkPos);
        return world.getChunkSource().chunkMap.getPlayers(chunkPos, false);
    }

    public static Collection<ServerPlayer> getTrackingPlayers(ServerLevel world, BlockPos blockPos) {
        Objects.requireNonNull(blockPos);
        return getTrackingPlayers(world, new ChunkPos(blockPos));
    }

    public static Optional<Tuple<Float, Float>> getRelativeHitCoordinatesForBlockFace(
            BlockHitResult blockHitResult,
            Direction direction,
            Direction[] unAllowedDirections) {

        Direction hitDirection = blockHitResult.getDirection();

        for (Direction unAllowed : unAllowedDirections) {
            if (unAllowed == hitDirection) {
                return Optional.empty();
            }
        }

        if (hitDirection != direction && hitDirection != Direction.UP && hitDirection != Direction.DOWN) {
            return Optional.empty();
        }

        BlockPos adjacentPos = blockHitResult.getBlockPos().relative(hitDirection);
        Vec3 hitLocation = blockHitResult.getLocation().subtract(
                adjacentPos.getX(),
                adjacentPos.getY(),
                adjacentPos.getZ()
        );

        float x = (float) hitLocation.x();
        float z = (float) hitLocation.z();
        float y = (float) hitLocation.y();

        Direction effectiveDirection = (hitDirection == Direction.UP || hitDirection == Direction.DOWN)
                ? direction
                : hitDirection;

        return switch (effectiveDirection) {
            case NORTH -> Optional.of(new Tuple<>(1.0f - x, y));
            case SOUTH -> Optional.of(new Tuple<>(x, y));
            case WEST -> Optional.of(new Tuple<>(z, y));
            case EAST -> Optional.of(new Tuple<>(1.0f - z, y));
            default -> Optional.empty();
        };
    }
}
