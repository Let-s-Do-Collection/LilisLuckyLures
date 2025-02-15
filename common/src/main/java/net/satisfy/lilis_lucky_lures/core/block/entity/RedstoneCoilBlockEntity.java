package net.satisfy.lilis_lucky_lures.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.satisfy.lilis_lucky_lures.core.block.RedstoneCoilBlock;
import net.satisfy.lilis_lucky_lures.core.registry.EntityTypeRegistry;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RedstoneCoilBlockEntity extends BlockEntity {
    private static final Random RANDOM = new Random();
    private BlockPos targetPos = null;
    private int beamProgress = 0;
    private int tickCounter = 0;
    private int phase = 0;
    private UUID owner = null;

    public RedstoneCoilBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.REDSTONE_COIL.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (owner != null) {
            tag.putUUID("Owner", owner);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("Owner")) {
            owner = tag.getUUID("Owner");
        }
    }


    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setActive(boolean active) {
        if (level != null) {
            level.setBlock(worldPosition, getBlockState().setValue(RedstoneCoilBlock.ACTIVE, active), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, RedstoneCoilBlockEntity be) {
        if (!(level instanceof ServerLevel serverLevel) || !state.getValue(RedstoneCoilBlock.ACTIVE)) return;
        be.tickCounter++;
        if (be.tickCounter >= 200) {
            be.tickCounter = 0;
            int range = Math.min(32, Math.max(3, serverLevel.getBestNeighborSignal(pos)));
            AABB box = new AABB(pos).inflate(range);
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive);
            RedstoneCoilBlock.RedstoneCoilTarget targetMode = state.getValue(RedstoneCoilBlock.TARGET);
            List<LivingEntity> filtered = new ArrayList<>();
            switch (targetMode) {
                case NONE -> {
                }
                case FISHES -> {
                    for (LivingEntity entity : entities) {
                        if (entity.isInWater()) filtered.add(entity);
                    }
                }
                case PLAYER -> {
                    Player ownerPlayer = be.owner != null ? serverLevel.getPlayerByUUID(be.owner) : null;
                    for (LivingEntity entity : entities) {
                        if (entity instanceof Player p && !p.isCreative()) {
                            if (ownerPlayer != null) {
                                if (p.getUUID().equals(ownerPlayer.getUUID())) continue;
                                if (ownerPlayer.getTeam() != null && p.getTeam() != null && ownerPlayer.getTeam().isAlliedTo(p.getTeam()))
                                    continue;
                            }
                            filtered.add(entity);
                        }
                    }
                }
                case MONSTER -> {
                    for (LivingEntity entity : entities) {
                        if (entity.getType().getCategory() == MobCategory.MONSTER) filtered.add(entity);
                    }
                }
            }
            if (!filtered.isEmpty()) {
                LivingEntity target = filtered.get(0);
                be.targetPos = target.blockPosition();
                be.beamProgress = 0;
                be.phase = 1;
                serverLevel.playSound(null, pos, net.minecraft.sounds.SoundEvents.EVOKER_PREPARE_ATTACK, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
        if (be.phase == 1 && be.beamProgress < 30) {
            double radius = (1.0 - (double) be.beamProgress / 30.0) * 2.0;
            for (int i = 0; i < 5; i++) {
                double angle = serverLevel.random.nextDouble() * Math.PI * 2;
                double x = pos.getX() + 0.5 + radius * Math.cos(angle);
                double y = pos.getY() + 1.0 + serverLevel.random.nextDouble();
                double z = pos.getZ() + 0.5 + radius * Math.sin(angle);
                serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0, 0, 0, 0);
            }
            be.beamProgress++;
            if (be.beamProgress >= 30) {
                be.beamProgress = 0;
                be.phase = 2;
            }
        }
        if (be.phase == 2 && be.beamProgress < 20 && be.targetPos != null) {
            if (be.beamProgress == 0) {
                serverLevel.playSound(null, pos, SoundEvents.GHAST_SHOOT, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            be.beamProgress++;
            double progress = (double) be.beamProgress / 20.0;
            Vector3d start = new Vector3d(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
            Vector3d end = new Vector3d(be.targetPos.getX() + 0.5, be.targetPos.getY() + 0.5, be.targetPos.getZ() + 0.5);
            Vector3d dir = new Vector3d(end).sub(start);
            Vector3d current = new Vector3d(dir).mul(progress).add(start);
            Vector3d arbitrary = new Vector3d(0, 1, 0);
            if (Math.abs(dir.dot(arbitrary)) > 0.99) arbitrary.set(1, 0, 0);
            Vector3d perp = new Vector3d();
            dir.cross(arbitrary, perp).normalize();
            double wave = Math.sin(progress * Math.PI * 4) * 0.1;
            perp.mul(wave);
            current.add(perp);
            serverLevel.sendParticles(ParticleTypes.END_ROD, current.x, current.y, current.z, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, current.x, current.y, current.z, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(ParticleTypes.SMOKE, current.x, current.y, current.z, 1, 0, 0, 0, 0);
            if (be.beamProgress >= 20) {
                List<LivingEntity> targets = serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(be.targetPos).inflate(1.0), LivingEntity::isAlive);
                if (!targets.isEmpty()) {
                    LivingEntity livingTarget = targets.get(0);
                    if (livingTarget instanceof Player player && player.isBlocking()) {
                        boolean mainShield = !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ShieldItem;
                        boolean offShield = !player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof ShieldItem;
                        if (mainShield || offShield) {
                            net.minecraft.world.item.ItemStack shieldStack;
                            InteractionHand hand;
                            if (mainShield) {
                                shieldStack = player.getMainHandItem();
                                hand = InteractionHand.MAIN_HAND;
                            } else {
                                shieldStack = player.getOffhandItem();
                                hand = InteractionHand.OFF_HAND;
                            }
                            int shieldDamage = serverLevel.random.nextInt(10) + 4;
                            shieldStack.hurtAndBreak(shieldDamage, player, p -> p.broadcastBreakEvent(hand));
                            Vector3d shieldPos;
                            Vector3d look = new Vector3d(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
                            Vector3d right = new Vector3d();
                            new Vector3d(0, 1, 0).cross(look, right).normalize();
                            if (hand == InteractionHand.MAIN_HAND) {
                                shieldPos = new Vector3d(player.getX(), player.getY() + player.getEyeHeight() - 0.5, player.getZ());
                                shieldPos.add(look.mul(0.3)).add(right.mul(0.4));
                            } else {
                                shieldPos = new Vector3d(player.getX(), player.getY() + player.getEyeHeight() - 0.5, player.getZ());
                                shieldPos.add(look.mul(0.3)).sub(right.mul(0.4));
                            }
                            int flashCount = serverLevel.random.nextInt(2) + 2;
                            for (int i = 0; i < flashCount; i++) {
                                double flashX = shieldPos.x + (serverLevel.random.nextDouble() - 0.5) * 0.2;
                                double flashY = shieldPos.y + (serverLevel.random.nextDouble() - 0.5) * 0.2;
                                double flashZ = shieldPos.z + (serverLevel.random.nextDouble() - 0.5) * 0.2;
                                serverLevel.sendParticles(ParticleTypes.FLASH, flashX, flashY, flashZ, 1, 0, 0, 0, 0);
                            }
                            for (LivingEntity entity : serverLevel.getEntitiesOfClass(LivingEntity.class, new AABB(shieldPos.x, shieldPos.y, shieldPos.z, shieldPos.x, shieldPos.y, shieldPos.z).inflate(10.0), LivingEntity::isAlive)) {
                                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                            }
                        } else {
                            applyDamage(livingTarget, serverLevel, be);
                        }
                    } else {
                        applyDamage(livingTarget, serverLevel, be);
                    }
                }
                be.phase = 0;
                be.targetPos = null;
                be.beamProgress = 0;
            }
        }
    }

    private static void applyDamage(LivingEntity livingTarget, ServerLevel serverLevel, RedstoneCoilBlockEntity be) {
        float damage = 10.0F;
        if (livingTarget.isInWater()) {
            damage *= 1.65F;
        }
        boolean hasIronArmor = false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                net.minecraft.world.item.ItemStack armorStack = livingTarget.getItemBySlot(slot);
                if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getMaterial() == ArmorMaterials.IRON) {
                        hasIronArmor = true;
                        int armorDamage = serverLevel.random.nextInt(5) + 3;
                        armorStack.hurtAndBreak(armorDamage, livingTarget, e -> e.broadcastBreakEvent(slot));
                    }
                }
            }
        }
        if (hasIronArmor) {
            damage *= 1.12F;
        }
        livingTarget.hurt(serverLevel.damageSources().magic(), damage);
        livingTarget.setSecondsOnFire(5);
        setFireAround(serverLevel, be.targetPos);
        spawnImpactParticles(serverLevel, be.targetPos);
    }

    private static void setFireAround(ServerLevel level, BlockPos center) {
        for (int x = center.getX() - 3; x <= center.getX() + 3; x++) {
            for (int y = center.getY() - 3; y <= center.getY() + 3; y++) {
                for (int z = center.getZ() - 3; z <= center.getZ() + 3; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockPos abovePos = currentPos.above();
                    if (!level.getBlockState(currentPos).isAir() && level.isEmptyBlock(abovePos) && RANDOM.nextDouble() < 0.05) {
                        level.setBlock(abovePos, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    private static void spawnImpactParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 40; i++) {
            Vector3d vec = new Vector3d(level.random.nextDouble() * 2 - 1, level.random.nextDouble() * 2 - 1, level.random.nextDouble() * 2 - 1);
            if (vec.lengthSquared() == 0)
                continue;
            vec.normalize().mul(level.random.nextDouble() * 1.5 + 0.5);
            double particleX = pos.getX() + 0.5;
            double particleY = pos.getY() + 0.5;
            double particleZ = pos.getZ() + 0.5;
            level.sendParticles(ParticleTypes.SMOKE, particleX, particleY, particleZ, 0, vec.x, vec.y, vec.z, 0.2);
            level.sendParticles(ParticleTypes.FLAME, particleX, particleY, particleZ, 0, vec.x * 1.2, vec.y * 1.2, vec.z * 1.2, 0.1);
        }
    }
}
