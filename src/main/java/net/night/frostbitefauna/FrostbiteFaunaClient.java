package net.night.frostbitefauna;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BedBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.night.frostbitefauna.block.ModBlocks;
import net.night.frostbitefauna.entity.ModEntities;
import net.night.frostbitefauna.entity.client.GreatModel;
import net.night.frostbitefauna.entity.client.GreatRenderer;

public class FrostbiteFaunaClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityModelLayerRegistry.registerModelLayer(GreatModel.THEGREATWANDERER, GreatModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.THEGREATWANDERER, GreatRenderer::new);

        // --- NEW: Custom Bed Client Registration Infrastructure ---

        // --- Custom Bed Client Setup ---
        // 1. Force transparency layers so the bed legs look normal
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREAT_BED, RenderLayer.getCutout());

        // 2. Bind the block to use the vanilla bed rendering geometry models
        BlockEntityRendererFactories.register(BlockEntityType.BED, BedBlockEntityRenderer::new);
    }
}
