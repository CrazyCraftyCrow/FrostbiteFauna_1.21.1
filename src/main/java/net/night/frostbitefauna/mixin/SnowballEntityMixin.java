package net.night.frostbitefauna.mixin;

import net.night.frostbitefauna.entity.custom.FrozenZombieEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public class SnowballEntityMixin {

    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void applyFrozenZombieSnowballEffect(EntityHitResult entityHitResult, CallbackInfo ci) {
        SnowballEntity snowball = (SnowballEntity) (Object) this;
        Entity owner = snowball.getOwner();

        // 1. Check if the entity that threw this snowball is your custom Frozen Zombie
        if (owner instanceof FrozenZombieEntity && entityHitResult.getEntity() instanceof LivingEntity target) {

            // 2. Inflict custom damage (2.0F equals 1 full heart of health)
            target.damage(snowball.getDamageSources().thrown(snowball, owner), 2.0F);

            // 3. Apply Slowness I for 4 seconds (80 ticks)
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0));

            // 4. Client-side cosmetic flare: Spawn frosty snowflake bursts right on impact
            if (!snowball.getWorld().isClient()) {
                snowball.getWorld().getPlayers().forEach(player -> {
                    // Send visual breaking cues to nearby players
                    for (int i = 0; i < 6; i++) {
                        snowball.getWorld().addParticle(
                                ParticleTypes.SNOWFLAKE,
                                target.getX(), target.getEyeY(), target.getZ(),
                                0.0D, 0.0D, 0.0D
                        );
                    }
                });
            }
        }
    }
}
