package net.night.frostbitefauna.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.night.frostbitefauna.entity.custom.GreatEntity;

public class IceGrassBlock extends TallPlantBlock {

    public IceGrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);

        // 1. Only run logic on the server side, and only target Living Entities
        if (!world.isClient() && world instanceof ServerWorld serverWorld && entity instanceof LivingEntity livingEntity) {

            // 2. IMMUNITY CHECK: If the entity is your Great Wanderer, do absolutely nothing
            if (livingEntity instanceof GreatEntity) {
                return;
            }

            // 3. DOUBLE-DAMAGE PROTECTION: Only deal damage if the player hits the LOWER half of the block.
            // This prevents the game from processing collisions on both halves simultaneously.
            if (state.get(HALF) == DoubleBlockHalf.UPPER) {
                return;
            }

            // 4. Create a freezing damage source
            DamageSource frostDamage = serverWorld.getDamageSources().create(DamageTypes.FREEZE);

            // 5. Inflict damage (1.0F is half a heart)
            livingEntity.damage(frostDamage, 1.0F);
        }
    }
}
