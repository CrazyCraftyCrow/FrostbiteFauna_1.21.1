package net.night.frostbitefauna.item.custom;

import net.minecraft.item.Item;

import net.night.frostbitefauna.entity.ModEntities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DecoyDummyItem extends Item {

    public DecoyDummyItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();

        // Only run logic on the Server side to prevent ghost entities
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        ItemStack itemStack = context.getStack();
        BlockPos clickedPos = context.getBlockPos();
        Direction clickedFace = context.getSide();

        // Calculate the position above the clicked block face
        BlockPos spawnPos = clickedPos.offset(clickedFace);

        if (world instanceof ServerWorld serverWorld) {
            // Spawn the custom entity using your ModEntities registry type
            var dummy = ModEntities.DECOYDUMMY.create(
                    serverWorld
            );

            if (dummy != null) {
                // Rotates the dummy to face away from the player when deployed
                float yaw = context.getPlayer() != null ? context.getPlayer().getYaw() + 180.0F : 0.0F;
                dummy.refreshPositionAndAngles(
                        spawnPos.getX() + 0.5,
                        spawnPos.getY(),
                        spawnPos.getZ() + 0.5,
                        yaw,
                        0.0F
                );

                serverWorld.spawnEntityAndPassengers(dummy);

                // Play a fluffy placement sound effect
                world.playSound(
                        null,
                        dummy.getX(),
                        dummy.getY(),
                        dummy.getZ(),
                        SoundEvents.BLOCK_WOOL_PLACE,
                        SoundCategory.BLOCKS,
                        1.0F,
                        0.8F
                );
                world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, spawnPos);

                // Consume 1 item from the stack if not in Creative mode
                if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                return ActionResult.CONSUME;
            }
        }

        return ActionResult.FAIL;
    }
}
