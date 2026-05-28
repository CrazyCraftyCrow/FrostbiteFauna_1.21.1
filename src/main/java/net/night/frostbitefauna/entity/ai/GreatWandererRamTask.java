package net.night.frostbitefauna.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class GreatWandererRamTask<E extends MobEntity> extends MultiTickTask<E> {
    private final int cooldownTicks;
    private int ramTicks = 0;

    public GreatWandererRamTask(int cooldownTicks) {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_PRESENT,
                MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryModuleState.VALUE_ABSENT
        ));
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    protected boolean shouldRun(ServerWorld world, E entity) {
        return true;
    }

    @Override
    protected void run(ServerWorld world, E entity, long time) {
        this.ramTicks = 0;
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ENTITY_GOAT_RAM_IMPACT, SoundCategory.NEUTRAL, 1.0F, 0.5F);
    }

    @Override
    protected void keepRunning(ServerWorld world, E entity, long time) {
        this.ramTicks++;
        LivingEntity target = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        if (target == null) {
            this.stop(world, entity, time);
            return;
        }

        entity.getLookControl().lookAt(target, 30.0F, 30.0F);

        if (this.ramTicks > 15) {
            Vec3d direction = new Vec3d(target.getX() - entity.getX(), 0, target.getZ() - entity.getZ()).normalize();
            entity.setVelocity(direction.x * 1.4, entity.getVelocity().y, direction.z * 1.4);

            if (entity.getBoundingBox().expand(0.3).intersects(target.getBoundingBox())) {
                target.damage(world.getDamageSources().mobAttack(entity), 8.0F);

                double kb = entity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK) + 1.5;
                target.takeKnockback(kb, -direction.x, -direction.z);
                this.stop(world, entity, time);
            }

            if (entity.horizontalCollision) {
                entity.damage(world.getDamageSources().flyIntoWall(), 1.0F);
                entity.getBrain().remember(MemoryModuleType.RAM_COOLDOWN_TICKS, this.cooldownTicks * 2);
                this.stop(world, entity, time);
            }
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, E entity, long time) {
        entity.getBrain().remember(MemoryModuleType.RAM_COOLDOWN_TICKS, this.cooldownTicks);
    }
}
