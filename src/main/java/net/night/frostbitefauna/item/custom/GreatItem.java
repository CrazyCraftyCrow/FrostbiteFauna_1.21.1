package net.night.frostbitefauna.item.custom;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.List;


public class GreatItem extends PickaxeItem {

    public GreatItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Run purely server-side to guarantee sync and avoid ghost knocks
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d playerPos = user.getPos();
            double shockwaveRadius = 6.0;

            // 1. Define bounds to catch entities surrounding the player
            Box bounds = new Box(
                    playerPos.x - shockwaveRadius, playerPos.y - 2.0, playerPos.z - shockwaveRadius,
                    playerPos.x + shockwaveRadius, playerPos.y + 4.0, playerPos.z + shockwaveRadius
            );

            // 2. Fetch every living entity within that bounding box (excluding the player)
            List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, bounds, entity -> entity != user);

            for (LivingEntity target : targets) {
                Vec3d targetPos = target.getPos();
                // Determine horizontal direction away from the player
                double dx = targetPos.x - playerPos.x;
                double dz = targetPos.z - playerPos.z;
                double distance = Math.sqrt(dx * dx + dz * dz);

                if (distance > 0) {
                    // Normalize vector components
                    dx /= distance;
                    dz /= distance;

                    // Falloff scaling: Enemies closer to the player fly further
                    double distanceScale = 1.0 - (distance / shockwaveRadius);
                    if (distanceScale < 0.1) distanceScale = 0.1;

                    // Apply horizontal knockback velocity and upward pop lift
                    double horizontalForce = 1.8 * distanceScale;
                    double verticalLift = 1.5 * distanceScale;

                    // Set target motion safely
                    target.setVelocity(dx * horizontalForce, verticalLift, dz * horizontalForce);

                    // CRITICAL 1.21.1: Forces the server to tell clients to animate the knockback immediately
                    target.velocityModified = true;

                    // Deal minor explosive splash damage (2.0F = 1 Full Heart)
                    target.damage(serverWorld.getDamageSources().explosion(user, user), 2.0F);
                }
            }

            // 3. Spawn server-authoritative explosion and frost particles
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, playerPos.x, playerPos.y + 0.5, playerPos.z, 1, 0.0, 0.0, 0.0, 0.0);
            serverWorld.spawnParticles(
                    ParticleTypes.SNOWFLAKE,
                    playerPos.x, playerPos.y + 0.5, playerPos.z, // Center coordinates
                    120,    // Number of particles to spawn
                    4.0,    // X spread radius (covers up to 4 blocks wide left/right)
                    1.5,    // Y spread radius (covers up to 1.5 blocks up/down)
                    4.0,    // Z spread radius (covers up to 4 blocks deep forward/back)
                    0.25    // Speed/Velocity of the particles shooting outward
            );
            serverWorld.spawnParticles(ParticleTypes.CLOUD, playerPos.x, playerPos.y + 0.5, playerPos.z, 50, 3.5, 0.5, 3.5, 0.02);

            // 4. Play overlapping block shatter and blast sounds
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.9F, 1.3F);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 0.6F);

            // 5. Trigger an 8-second (160 ticks) action cooldown on the pickaxe
            user.getItemCooldownManager().set(this, 160);

            // 6. 1.21.1 Item break damage formatting using EquipmentSlot enum
            EquipmentSlot slot = (hand == Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            itemStack.damage(5, user, slot);

            return TypedActionResult.success(itemStack, false);
        }

        return TypedActionResult.consume(itemStack);
    }
}
