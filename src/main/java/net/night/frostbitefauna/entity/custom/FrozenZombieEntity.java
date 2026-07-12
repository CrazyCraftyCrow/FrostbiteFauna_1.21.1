package net.night.frostbitefauna.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.night.frostbitefauna.item.ModItems;

public class FrozenZombieEntity extends ZombieEntity implements RangedAttackMob {

    private static final TrackedData<Boolean> IS_RANGED_MODE = DataTracker.registerData(FrozenZombieEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    // 🌟 RENAME AND ADD ANIMATION STATE HOOKS TO MATCH MELEE vs RANGED TRACKING
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState meleeAttackAnimationState = new AnimationState();
    public final AnimationState rangedAttackAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private int combatModeCooldown = 100;

    public FrozenZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_RANGED_MODE, false);
    }

    @Override
    protected void initCustomGoals() {
        this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.25D, 20, 15.0F) {
            @Override
            public boolean canStart() {
                return isRangedMode() && super.canStart();
            }
        });

        this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0D, false) {
            @Override
            public boolean canStart() {
                return !isRangedMode() && super.canStart();
            }
        });

        this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.add(6, new MoveThroughVillageGoal(this, 1.0, true, 4, this::canBreakDoors));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge(ZombifiedPiglinEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal(this, MerchantEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal(this, IronGolemEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));

        this.targetSelector.add(3, new ActiveTargetGoal<>(this, DecoyDummyEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createFrozenZombieAttributes() {
        return ZombieEntity.createZombieAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        } else {
            if (--this.combatModeCooldown <= 0) {
                this.setRangedMode(this.random.nextBoolean());
                this.combatModeCooldown = 100 + this.random.nextInt(60);
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (success && target instanceof LivingEntity livingTarget) {
            livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0));
            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.5F);

            // 🌟 TRIGGER MELEE SWING PACKET (Byte status code 5) FOR CLIENTS
            if (!this.getWorld().isClient()) {
                this.getWorld().sendEntityStatus(this, (byte) 5);
            }
        }
        return success;
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        SnowballEntity snowball = new SnowballEntity(this.getWorld(), this);
        double d = target.getEyeY() - 1.100000023841858D;
        double e = target.getX() - this.getX();
        double f = d - snowball.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(e * e + g * g) * 0.2F;
        snowball.setVelocity(e, f + h, g, 1.6F, 12.0F);

        this.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(snowball);

        // 🌟 TRIGGER RANGED THROW PACKET (Byte status code 4) FOR CLIENTS
        if (!this.getWorld().isClient()) {
            this.getWorld().sendEntityStatus(this, (byte) 4);
        }
    }

    @Override
    public void handleStatus(byte status) {
        // 🌟 ROUTE INCOMING DATA PACKETS TO THE SEPARATE ANIMATION TIMELINES
        if (status == 4) {
            this.rangedAttackAnimationState.start(this.age);
        } else if (status == 5) {
            this.meleeAttackAnimationState.start(this.age);
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    protected boolean canConvertInWater() {
        return false;
    }

    public boolean isRangedMode() { return this.dataTracker.get(IS_RANGED_MODE); }
    public void setRangedMode(boolean ranged) { this.dataTracker.set(IS_RANGED_MODE, ranged); }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 60;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsRangedMode", this.isRangedMode());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setRangedMode(nbt.getBoolean("IsRangedMode"));
    }

    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);

        if (!this.getWorld().isClient()) {

            int fleshCount = this.random.nextBetween(1, 2);
            if (fleshCount > 0) {
                this.dropItem(Items.ROTTEN_FLESH, fleshCount);
            }
        }
    }
}
