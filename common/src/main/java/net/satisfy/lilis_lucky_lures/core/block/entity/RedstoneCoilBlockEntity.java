package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.satisfy.lilis_lucky_lures.core.block.RedstoneCoilBlock;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;

import java.util.List;
import java.util.Random;

public class RedstoneCoilBlockEntity extends BlockEntity {
    private static final Random RANDOM = new Random();
    private BlockPos targetPos = null;
    private int beamProgress = 0;
    private int tickCounter = 0;
    private int phase = 0;
    private static final int TICK_THRESHOLD = 45;

    public RedstoneCoilBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.REDSTONE_COIL.get(), pos, state);
    }

    public void setActive(boolean active) {
        if (level != null) {
            level.setBlock(worldPosition, getBlockState().setValue(RedstoneCoilBlock.ACTIVE, active), 3);
        }
    }

    @SuppressWarnings("unused")
    public void stopTicking() {
        tickCounter = 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RedstoneCoilBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel) || !state.getValue(RedstoneCoilBlock.ACTIVE)) return;
        be.tickCounter++;

        if (be.tickCounter >= TICK_THRESHOLD) {
            be.tickCounter = 0;
            int range = Math.min(32, Math.max(3, serverLevel.getBestNeighborSignal(pos)));
            AABB box = new AABB(pos).inflate(range);
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive);
            if (!entities.isEmpty()) {
                LivingEntity target = entities.get(0);
                be.targetPos = target.blockPosition();
                be.beamProgress = 0;
                be.phase = 1;
            }
        }

        if (be.phase == 1 && be.beamProgress < 30) {
            double radius = (double) be.beamProgress / 30.0 * 2.0;
            for (int i = 0; i < 5; i++) {
                double angle = serverLevel.random.nextDouble() * Math.PI * 2;
                double x = pos.getX() + 0.5 + radius * Math.cos(angle);
                double y = pos.getY() + 0.5 + serverLevel.random.nextDouble();
                double z = pos.getZ() + 0.5 + radius * Math.sin(angle);
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0, 0, 0);
            }
            be.beamProgress++;
            if (be.beamProgress >= 30) {
                be.beamProgress = 0;
                be.phase = 2;
            }
        }

        if (be.phase == 2 && be.beamProgress < 20 && be.targetPos != null) {
            be.beamProgress++;
            double progress = (double) be.beamProgress / 20.0;
            double startX = pos.getX() + 0.5;
            double startY = pos.getY() + 0.5;
            double startZ = pos.getZ() + 0.5;
            double endX = be.targetPos.getX() + 0.5;
            double endY = be.targetPos.getY() + 0.5;
            double endZ = be.targetPos.getZ() + 0.5;
            double currentX = startX + (endX - startX) * progress;
            double currentY = startY + (endY - startY) * progress;
            double currentZ = startZ + (endZ - startZ) * progress;
            serverLevel.sendParticles(ParticleTypes.END_ROD, currentX, currentY, currentZ, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, currentX, currentY, currentZ, 1, 0, 0, 0, 0);

            BlockPos currentPos = new BlockPos((int) currentX, (int) currentY, (int) currentZ);
            List<Player> shields = serverLevel.getEntitiesOfClass(Player.class, new AABB(currentPos).inflate(0.5), LivingEntity::isBlocking);

            if (!shields.isEmpty()) {
                serverLevel.sendParticles(ParticleTypes.SMOKE, currentX, currentY, currentZ, 10, 0.5, 0.5, 0.5, 0);
                serverLevel.sendParticles(ParticleTypes.FLASH, currentX, currentY, currentZ, 5, 0.3, 0.3, 0.3, 0);
                Player shield = shields.get(0);
                shield.hurt(serverLevel.damageSources().magic(), 1.0F);
                be.phase = 0;
                be.targetPos = null;
                be.beamProgress = 0;
                return;
            }

            if (be.beamProgress >= 20) {
                List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(be.targetPos).inflate(1.0), LivingEntity::isAlive);
                if (!targets.isEmpty()) {
                    LivingEntity livingTarget = targets.get(0);
                    float damage = 10.0F;
                    boolean hasIronArmor = false;
                    for (ItemStack armorStack : livingTarget.getArmorSlots()) {
                        if (armorStack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.IRON) {
                            hasIronArmor = true;
                            break;
                        }
                    }
                    if (hasIronArmor) {
                        damage *= 1.25F;
                    }
                    livingTarget.hurt(serverLevel.damageSources().magic(), damage);
                    if (hasIronArmor) {
                        for (ItemStack armorStack : livingTarget.getArmorSlots()) {
                            if (armorStack.getItem() instanceof ArmorItem armorItem && armorItem.getMaterial() == ArmorMaterials.IRON) {
                                int damageAmount = Math.max(1, (int)(armorStack.getMaxDamage() * 0.1));
                                armorStack.hurt(damageAmount, level.getRandom(), null);
                            }
                        }
                    }
                    livingTarget.setSecondsOnFire(5);
                    setFireAround(serverLevel, be.targetPos);
                    spawnImpactParticles(serverLevel, be.targetPos);
                }
                be.phase = 0;
                be.targetPos = null;
                be.beamProgress = 0;
            }
        }
    }

    private static void setFireAround(ServerLevel level, BlockPos center) {
        for (int x = center.getX() - 3; x <= center.getX() + 3; x++) {
            for (int y = center.getY() - 3; y <= center.getY() + 3; y++) {
                for (int z = center.getZ() - 3; z <= center.getZ() + 3; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    if (level.isEmptyBlock(currentPos.above()) && RANDOM.nextDouble() < 0.05) {
                        level.setBlock(currentPos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private static void spawnImpactParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            double offsetX = level.random.nextDouble() - 0.5;
            double offsetY = level.random.nextDouble() * 0.5;
            double offsetZ = level.random.nextDouble() - 0.5;
            double particleX = pos.getX() + 0.5 + offsetX;
            double particleY = pos.getY() + 0.5 + offsetY;
            double particleZ = pos.getZ() + 0.5 + offsetZ;
            level.sendParticles(ParticleTypes.SMOKE, particleX, particleY, particleZ, 1, 0, 0, 0, 0);
            level.sendParticles(ParticleTypes.FLAME, particleX, particleY, particleZ, 1, 0, 0, 0, 0);
        }
    }
}
