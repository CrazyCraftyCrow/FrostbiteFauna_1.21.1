package net.night.frostbitefauna.block.custom;

import net.minecraft.block.BedBlock;
import net.minecraft.util.DyeColor;
import net.minecraft.block.BlockState;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class GreatBedBlock extends BedBlock {

    public GreatBedBlock(DyeColor color, Settings settings) {
        super(color, settings);
    }

    // 1. Keeps the placement logic completely safe from standard vanilla color crashes
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    // 2. Triggers when a sleeping player is ejected from the bed (waking up naturally or being forced out)
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        // Run this logic exclusively on the Server-side thread to prevent potion syncing desyncs
        if (!world.isClient) {
            // Find any player that was sleeping at this exact coordinate block position
            for (PlayerEntity player : world.getPlayers()) {
                if (player.getSleepingPosition().isPresent() && player.getSleepingPosition().get().equals(pos)) {

                    // Check if the current biome is considered a cold/freezing biome
                    if (isColdBiome(world, pos)) {
                        // Apply Speed II (value 1) for 3 minutes (3600 ticks)
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 3600, 1, false, true));
                        // Apply Haste II (value 1) for 3 minutes (3600 ticks)
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 3600, 1, false, true));
                    }
                }
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    /**
     * Helper method to determine if the bed is placed in a freezing/cold environment
     */
    private boolean isColdBiome(World world, BlockPos pos) {
        var biomeEntry = world.getBiome(pos);
        if (biomeEntry != null && biomeEntry.value() != null) {
            // Vanilla considers anything below 0.15f as cold/snowy (e.g., Snowy Tundra, Ice Peaks, Taiga)
            float temperature = biomeEntry.value().getTemperature();
            return temperature < 0.15f;
        }
        return false;
    }

}
