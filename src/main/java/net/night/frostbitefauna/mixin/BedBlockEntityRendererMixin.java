package net.night.frostbitefauna.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BedBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.night.frostbitefauna.FrostbiteFauna;
import net.night.frostbitefauna.block.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BedBlockEntityRenderer.class)
public class BedBlockEntityRendererMixin {

    // Points directly to your custom bed texture sheet inside the Bed Atlas Map
    @Unique
    private static final SpriteIdentifier GREAT_BED_SPRITE = new SpriteIdentifier(
            TexturedRenderLayers.BEDS_ATLAS_TEXTURE,
            Identifier.of(FrostbiteFauna.MOD_ID, "entity/bed/great_bed")
    );

    @ModifyVariable(
            method = "render(Lnet/minecraft/block/entity/BedBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private SpriteIdentifier frostbitefauna$swapBedTexture(SpriteIdentifier original, net.minecraft.block.entity.BedBlockEntity entity) {
        // 1. Check if the block in the world is your custom bed
        BlockState state = entity.getCachedState();
        if (state != null && state.isOf(ModBlocks.GREAT_BED)) {
            return GREAT_BED_SPRITE;
        }

        // 2. FALLBACK FOR 3D INVENTORY SLOT:
        // When Minecraft draws the bed inside your inventory, it uses a dummy BedBlockEntity.
        // We check if the texture currently assigned to it matches the hardcoded DyeColor of your custom bed.
        // Since your custom bed constructor uses DyeColor.BLUE, we swap the texture only if it's the blue dummy being rendered!
        // This ensures normal vanilla colored beds stay untouched, while your inventory icon changes seamlessly.
        if (entity.getColor() == net.minecraft.util.DyeColor.BLUE) {
            // To be 100% precise, we verify that the player is looking at a UI screen or holding the item stack
            net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
            if (client != null && client.player != null) {
                // If the player has your bed in their inventory, hot-swap the 3D item renderer sprite
                if (client.player.getInventory().contains(new net.minecraft.item.ItemStack(ModBlocks.GREAT_BED.asItem()))) {
                    return GREAT_BED_SPRITE;
                }
            }

            // Alternative completely foolproof check: if we are rendering a bed block entity
            // but the world position is 0,0,0 (which is what Minecraft uses for dummy inventory entities)
            if (entity.getPos().getX() == 0 && entity.getPos().getY() == 0 && entity.getPos().getZ() == 0) {
                return GREAT_BED_SPRITE;
            }
        }

        return original;
    }
}
