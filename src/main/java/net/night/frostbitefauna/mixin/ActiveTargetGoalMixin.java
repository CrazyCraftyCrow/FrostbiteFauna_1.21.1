package net.night.frostbitefauna.mixin;

import net.night.frostbitefauna.entity.custom.DecoyDummyEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ActiveTargetGoal.class)
public class ActiveTargetGoalMixin {

    @Shadow protected LivingEntity targetEntity;

    @Inject(method = "findClosestTarget", at = @At("RETURN"))
    private void distractHostileMobs(CallbackInfo ci) {
        ActiveTargetGoal<?> goal = (ActiveTargetGoal<?>) (Object) this;

        // Use the accessor to cleanly read the protected 'mob' field
        MobEntity mob = ((TrackTargetGoalAccessor) goal).getMob();

        if (mob != null && mob.isAlive() && mob.getWorld() != null) {
            Box searchRadius = mob.getBoundingBox().expand(16.0);

            List<DecoyDummyEntity> nearbyDummies = mob.getWorld().getEntitiesByClass(
                    DecoyDummyEntity.class,
                    searchRadius,
                    LivingEntity::isAlive
            );

            if (!nearbyDummies.isEmpty()) {
                this.targetEntity = nearbyDummies.get(0);
            }
        }
    }
}
