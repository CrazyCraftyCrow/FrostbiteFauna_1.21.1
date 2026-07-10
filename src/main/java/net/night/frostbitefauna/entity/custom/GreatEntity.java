package net.night.frostbitefauna.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.item.ModItems;
import org.jetbrains.annotations.Nullable;


public class GreatEntity extends AnimalEntity {

    private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(GreatEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SHEARED, false); // Default state: Has wool/fur
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // If the player has shears, let the Shearable interface take over
        if (itemStack.isOf(Items.SHEARS)) {
            if (!this.getWorld().isClient() && this.isShearable()) {
                this.sheared(SoundCategory.PLAYERS);
                this.emitGameEvent(net.minecraft.world.event.GameEvent.SHEAR, player);
                itemStack.damage(1, player, getSlotForHand(hand));
                return ActionResult.SUCCESS;
            }
            return ActionResult.CONSUME;
        }

        return super.interactMob(player, hand);
    }

    public void sheared(SoundCategory shearedSoundCategory) {
        this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, shearedSoundCategory, 1.0F, 1.0F);
        this.setSheared(true);

        // Drop your items (e.g., 1 to 3 blocks of white wool, or replace with a custom mod item!)
        int count = 1 + this.random.nextInt(3);
        for (int i = 0; i < count; ++i) {
            this.dropItem(ModBlocks.GREAT_WOOL);
        }
    }

    public boolean isShearable() {
        // Only shearable if alive and not already sheared
        return this.isAlive() && !this.isSheared();
    }

    public boolean isSheared() {
        return this.dataTracker.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.dataTracker.set(SHEARED, sheared);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSheared(nbt.getBoolean("Sheared"));
    }

    // 7. Make the fur grow back when it eats grass (Optional, like a sheep)
    /*@Override
    public void onEatingGrass() {
        super.onEatingGrass();
        this.setSheared(false); // Fur grows back
    }*/

    public GreatEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.HAY_BLOCK), false));

        this.goalSelector.add(3, new FollowParentGoal(this, 1.1D));

        this.goalSelector.add(4, new MeleeAttackGoal(this, 2.0, true));

        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));

        this.targetSelector.add(1, new RevengeGoal(this).setGroupRevenge());
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.5)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,15)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,10);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 60;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        } else {
            // Server-side logic: 1 in 12000 chance per tick (~10 minutes) to grow fur back naturally
            if (this.isSheared() && this.random.nextInt(6000) == 0) {
                this.setSheared(false);
            }
        }
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(damageSource, causedByPlayer);

        // Ensure this only runs on the server side to prevent client-side ghost item drops
        if (!this.getWorld().isClient()) {

            // 1. Handle Oak Planks (0 to 1)
            int plankCount = this.random.nextBetween(0, 1);
            if (plankCount > 0) {
                this.dropItem(ModItems.GREAT_TUSK_FRAGMENT, plankCount);
            }

            // 2. Handle White Wool (2 to 3)
            int woolCount = this.random.nextBetween(2, 3);
            this.dropItem(ModItems.GREAT_FUR, woolCount);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.HAY_BLOCK);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.THEGREATWANDERER.create(world);
    }
}
